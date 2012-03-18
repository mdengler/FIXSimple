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
import java.util.Arrays;
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
import static com.martindengler.proj.FIXSimple.FIXMessage.ISO_8859_1;
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


    public BlockingQueue<FIXMessage> inputStream() {
        return this.incomingQueue;
    }


    public BlockingQueue<FIXMessage> outputStream() {  //TODO: rename outputQueue?
        return this.outgoingQueue;
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
                        } catch (RuntimeException e) {
                            System.err.println("FIXStream.inputHandler.run: readOneMessage threw IOException; stopping.");
                            System.err.println(e.getMessage());
                            System.err.println(e.toString());
                            throw e;
                        }
                        if (readMessage == null)
                            break;
                        System.err.format("FIXStream.inputHandler.run: got message from readOneMessage%n");
                        System.err.println(readMessage.toString());
                        incomingQueue.add(readMessage);
                        System.err.println("..added to incomingQueue");
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
        //tl;dr: this method is long, and very ugly
        //
        // So we have to parse a byte stream of unknown length to read
        // exactly the FIX message for FIXMessage.fromBytes().
        //
        // Here's what we do:
        //
        // 0) create the variable "buffer" to hold the FIX message
        // 1) fill it until we get the "preamble": up to the BodyLength value
        // 2) carefully read the BodyLength value (also put in buffer)
        // 3) using BodyLength, read the rest (pre-checksum) into buffer
        // 4) read the checksum tag/value pair into buffer
        // 5) use FIXMessage.fromBytes(buffer) to return the message


        //TODO: ensure we don't overflow this buffer
        int bufferLength = 10 * 1024;

        byte[] buffer = new byte[bufferLength];
        Arrays.fill(buffer, SOH_byte);  // TODO: document / consider repercussions of this guard value

        int lastReadCharacter;
        int countReadCharacters = 0;

        // inefficient but simpler
        byte[] preambleBuffer = new byte[FIX_PREAMBLE.length];

        while (!Arrays.equals(FIX_PREAMBLE, preambleBuffer)
                &&
                (lastReadCharacter = inputReader.read()) != -1) {

            preambleBuffer[countReadCharacters] = (byte) lastReadCharacter;
            buffer[countReadCharacters++] = (byte) lastReadCharacter;
        }

        if (!Arrays.equals(FIX_PREAMBLE, preambleBuffer)) {
            String msg = String.format("problem reading FIX stream;" +
                    "read %s but no FIX Preamble (%s) found.",
                    new String(preambleBuffer).replace(FIXMessage.SOH, "<SOH>"),
                    new String(FIX_PREAMBLE).replace(FIXMessage.SOH, "<SOH>"));
            throw new IllegalStateException(msg);
        }

        int bodyLengthStartIndex = countReadCharacters;
        while ((lastReadCharacter = inputReader.read()) != -1
                &&
                lastReadCharacter != (int) SOH_byte) {

            buffer[countReadCharacters++] = (byte) lastReadCharacter;
        }

        if (lastReadCharacter != (int) SOH_byte) {
            String msg = String.format("problem reading FIX stream;" +
                    "read %s but SOH character found.",
                    new String(buffer, 0, countReadCharacters)
                    .replace(FIXMessage.SOH, "<SOH>"));
            throw new IllegalStateException(msg);
        }

        /* we should have started reading message length at buffer index
         * 12, and say the length was "256", so countReadCharacters goes
         * 12 - get new lastReadCharacter, it's 2, countReadCharacters = 13
         * 13 - get new lastReadCharacter, it's 5, countReadCharacters = 14
         * 14 - get new lastReadCharacter, it's 6, countReadCharacters = 15
         * 15 - get new lastReadCharacter, it's SOH, countReadCharacters = 15
         * so the bodyLengthOffset is 15 - 12 = 3.
         */
        int bodyLengthOffset = (countReadCharacters - bodyLengthStartIndex);

        // now update the buffer with the read SOH and increase the count
        buffer[countReadCharacters++] = (byte) lastReadCharacter;  // it's SOH

        Integer bodyLength = Integer.parseInt(new String(buffer,
                        bodyLengthStartIndex,
                        bodyLengthOffset));
        Integer restOfBodyLength = bodyLength - countReadCharacters;

        // FUTURE: this seems a bit risky, consider how to make it safer
        for (int i=0; i < restOfBodyLength; i++)
            buffer[countReadCharacters++] = (byte) inputReader.read();

        if (buffer[countReadCharacters - 1] != SOH_byte) {
            String msg = String.format("problem reading FIX stream;" +
                    " was supposed to read %s chars before trailer" +
                    " (per fix message prefix %s)" +
                    " but last char read was not SOH; read [%s]",
                    restOfBodyLength,
                    new String(buffer, 0, bodyLengthStartIndex)
                    .replace(FIXMessage.SOH, "<SOH>"),
                    new String(buffer, 0, countReadCharacters)
                                       ); //this is why eclipse indenting FAILs
            throw new IllegalStateException(msg);
        }


        // we just have checksum tag/value pair + its SOH left
        Integer startOfChecksumIndex = countReadCharacters;
        byte[] checksumTagBytes = Tag.CHECKSUM.getCode()
            .toString().getBytes(ISO_8859_1);
        while ((lastReadCharacter = inputReader.read()) != -1
                &&
                lastReadCharacter != (int) SOH_byte) {

            buffer[countReadCharacters++] = (byte) lastReadCharacter;
        }

        if (lastReadCharacter != (int) SOH_byte) {
            String msg = String.format("problem reading FIX stream;" +
                    " was supposed to read trailing checksum tag/value" +
                    " pair and SOH, but actually read: [%s]",
                    new String(buffer,
                            startOfChecksumIndex,
                            countReadCharacters - startOfChecksumIndex - 1)
                    .replace(FIXMessage.SOH, "<SOH>"));
            throw new IllegalStateException(msg);
        }

        // now update the buffer with the read SOH and increase the count
        buffer[countReadCharacters++] = (byte) lastReadCharacter;  // it's SOH

        FIXMessage message = FIXMessage.fromWire(Arrays.
                copyOfRange(buffer, 0, countReadCharacters));
        return message;

    }

}