package com.martindengler.proj.FIXSimple;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.martindengler.proj.FIXSimple.spec.MsgType;
import com.martindengler.proj.FIXSimple.spec.Tag;
import com.martindengler.proj.FIXSimple.spec.OrdStatus;

public class Acceptor {

    public Acceptor() {
    }

    public void run() throws IOException, InterruptedException {
        Integer listenPort = 16180;
        ServerSocket listenSocket = new ServerSocket(listenPort);

        String mySenderCompId = "SERVER";

        while (true) {  // client handling start
            System.err.println("Listening on port " + listenPort);
            Socket clientSocket = listenSocket.accept();
            FIXStream bidirectionalStream = FIXStream.connect(clientSocket);

            System.err.println("got connection");

            BlockingQueue<FIXMessage> input  = bidirectionalStream. inputStream();
            BlockingQueue<FIXMessage> output = bidirectionalStream.outputStream();

            // TODO: break out into another thread
            // TODO: make into better state machine
            String state = "pendinglogin";
            while (true) {  // state machine start
                if (state == "pendinglogin") {
                    FIXMessage incomingMessage = input.take();
                    if (incomingMessage.getMsgType() != MsgType.LOGON) {
                        System.err.format("waiting for LOGON message" +
                                          " but got %s instead%s",
                                          incomingMessage);
                        clientSocket.close(); // TODO: improve
                        break;
                    }
                    FIXMessage responseMessage = FIXMessage
                        .factory(MsgType.LOGON)
                        .putM(Tag.SENDERCOMPID, mySenderCompId)
                        .putM(Tag.TARGETCOMPID, incomingMessage.get(Tag.SENDERCOMPID))
                        ;
                    state = "normal";
                    output.put(responseMessage);
                }
                if (state == "normal") {
                    FIXMessage incomingMessage = input.take();
                    if (incomingMessage.getMsgType() != MsgType.NEW_ORDER_SINGLE) {
                        System.err.println("Ignoring non-NOS message");
                        break;
                    }
                    System.err.println("Got NOS");
                    // TODO: handle properly
                    if (OrdStatus.valueOf(incomingMessage.get(Tag.ORDSTATUS))
                        != OrdStatus.NEW) {
                        System.err.println("Ignoring NOS with ORDSTATUS != NEW");
                        break;
                    }
                    FIXMessage responseMessage = FIXMessage
                        .factory(MsgType.EXECUTION_REPORT)
                        .putM(Tag.SENDERCOMPID, mySenderCompId)
                        .putM(Tag.TARGETCOMPID, incomingMessage.get(Tag.SENDERCOMPID))
                        .putM(Tag.LEAVESQTY,    incomingMessage.get(Tag.ORDERQTY))
                        ;
                    for (Tag t : Arrays.asList(Tag.CLORDID,
                                               Tag.ORIGCLORDID,
                                               Tag.ORDERQTY,
                                               Tag.CUMQTY,
                                               Tag.LASTSHARES)) {
                        responseMessage.putM(t, incomingMessage.get(t));
                    }
                    output.put(responseMessage);
                }
            }

            System.err.println("finished handling connection");

        }
    }


    public FIXMessage processMessage(FIXMessage message) {
        System.out.printf("processMessage(): %s%n", message);
        //TODO: fill in
        return null;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Acceptor server = new Acceptor();
        server.run();
    }

}