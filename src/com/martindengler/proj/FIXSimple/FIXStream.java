package com.martindengler.proj.FIXSimple;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import com.martindengler.proj.FIXSimple.spec.MsgType;
import com.martindengler.proj.FIXSimple.spec.Tag;

import static com.martindengler.proj.FIXSimple.FIXMessage.FIX_PREAMBLE;
import static com.martindengler.proj.FIXSimple.FIXMessage.SOH_byte;


public class FIXStream {

    private BlockingQueue<FIXMessage> incomingQueue;
    private BlockingQueue<FIXMessage> outgoingQueue;

    private Executor executor;

    public static FIXStream connect(Socket conversation) throws IOException {
        return new FIXStream(new InputStreamReader(conversation.getInputStream()),
                             new OutputStreamWriter(conversation.getOutputStream()));
    }

    public static FIXStream connect(Reader inputReader, Writer outputWriter) {
        return new FIXStream(inputReader, outputWriter);
    }

    public static FIXStream connect(DatagramSocket ds) {
        throw new IllegalStateException("unimplemented");
    }


    private FIXStream() {
        this.incomingQueue = new ArrayBlockingQueue<FIXMessage>(10);
        this.outgoingQueue = new ArrayBlockingQueue<FIXMessage>(10);

        this.executor = new ThreadPoolExecutor(5,     // corePoolSize
                                               1000,  // maximumPoolSize
                                               100,   // keepAliveTime in...
                                               TimeUnit.SECONDS,
                                               new ArrayBlockingQueue<Runnable>(10));
    }


    private FIXStream(final Reader inputReader, final Writer outputWriter) {
        this();

        Runnable inputHandler = new Runnable() {
                public void run() {
                    FIXMessage readMessage;
                    for (;;) {
                        try {
                            readMessage = readOneMessage(inputReader);
                        } catch (IOException ioe) {
                            readMessage = null;
                            System.err.println("FIXStream.inputHandler.run: readOneMessage threw IOException; stopping.");
                            System.err.println(ioe.getMessage());
                            System.err.println(ioe.toString());
                        }
                        if (readMessage == null)
                            break;
                        incomingQueue.add(readMessage);
                    }
                }
            };

        Runnable outputHandler = new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            FIXMessage outgoing = outgoingQueue.take();
                            System.err.format("FIXStream.outputHandler.run(): writing message to wire: %s%n", outgoing.toString(false));
                            outputWriter.write(outgoing.toWire());
                            outputWriter.flush();
                            System.err.format("FIXStream.outputHandler.run(): wrote message to wire: %s%n", outgoing.toString(false));
                        } catch (InterruptedException inte) {
                            System.err.println("FIXStream.outputHandler.run(): InterruptedException in outgoingQueue.take(); will retry");
                            System.err.println(inte.getMessage());
                            System.err.println(inte.toString());
                        } catch (IOException ioe) {
                            System.err.println("FIXStream.outputHandler.run(): IOException in outputWriter.write(); stopping");
                            System.err.println(ioe.getMessage());
                            System.err.println(ioe.toString());
                            break;
                        }
                    }
                }
            };

        this.executor.execute(inputHandler);
        this.executor.execute(outputHandler);
    }


    private FIXMessage readOneMessage(Reader inputReader) throws IOException {

        System.err.println("readOneMessage() starting");

        int c;
        String lineSoFar = "";
        while (!(lineSoFar.equals(FIX_PREAMBLE))
               &&
               (c = inputReader.read()) != -1) {
            lineSoFar += (char) c;
            System.err.format("readOneMessage() [PRE] read %s (total: %s)%n", c, lineSoFar.replace(FIXMessage.SOH, "<SOH>"));
        }

        if (!lineSoFar.equals(FIX_PREAMBLE))
            throw new IllegalStateException(String.format("problem reading FIX stream; read %s but no FIX Preamble (%s) found.",
                                                          lineSoFar,
                                                          FIX_PREAMBLE.replace(FIXMessage.SOH, "<SOH>")));

        String messageLengthAsString = "";
        while ((c = inputReader.read()) != -1 && (char) c != (char) SOH_byte) {
            messageLengthAsString += (char) c;
            System.err.format("readOneMessage() [LEN] read %s (total: %s)%n", c, messageLengthAsString.replace(FIXMessage.SOH, "<SOH>"));
        }

        lineSoFar += messageLengthAsString;
        lineSoFar += (char) SOH_byte;

        Integer messageLength = Integer.parseInt(messageLengthAsString);
        Integer restOfMessageLength = messageLength - FIX_PREAMBLE.length();
        char[] restOfMessage = new char[restOfMessageLength];
        Integer charsRead = inputReader.read(restOfMessage, 0, restOfMessageLength);
        if (charsRead != restOfMessageLength)
            throw new IllegalStateException(String.format("problem reading FIX stream; was supposed to read %s chars (per fix message prefix %s) but actually read %s chars)",
                                                     restOfMessageLength,
                                                     lineSoFar.replace(FIXMessage.SOH, "<SOH>"),
                                                     charsRead));
        lineSoFar += String.valueOf(restOfMessage);
        System.err.format("readOneMessage() [RST] read %s (total: %s)%n", String.valueOf(restOfMessage).replace(FIXMessage.SOH, "<SOH>"), lineSoFar.replace(FIXMessage.SOH, "<SOH>"));

        FIXMessage message = FIXMessage.factory(lineSoFar + restOfMessage, FIXMessage.SOH);
        System.err.format("readOneMessage() [END] read %s message%n", message);
        return message;
    }


    public BlockingQueue<FIXMessage> inputStream() {
        return this.incomingQueue;
    }

    public BlockingQueue<FIXMessage> outputStream() {  //TODO: rename outputQueue?
        return this.outgoingQueue;
    }

}