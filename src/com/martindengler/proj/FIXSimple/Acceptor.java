package com.martindengler.proj.FIXSimple;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.martindengler.proj.FIXSimple.spec.MsgType;
import com.martindengler.proj.FIXSimple.spec.Tag;

public class Acceptor {

    public Acceptor() {
    }

    public void run() throws IOException {
        Integer listenPort = 16180;
        ServerSocket listenSocket = new ServerSocket(listenPort);

        while (true) {
            System.err.println("Listening on port " + listenPort);
            FIXStream bidirectionalStream = FIXStream.connect(listenSocket.accept());

            System.err.println("got connection");

            Iterator<FIXMessage> input = bidirectionalStream.inputStream().iterator();
            BlockingQueue<FIXMessage> output = bidirectionalStream.outputStream();

            while (input.hasNext()) {
                FIXMessage response = processMessage(input.next());
                if (response != null) {
                    output.add(response); //TODO: change to offer(), handle errors
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

    public static void main(String[] args) throws IOException {
        Acceptor server = new Acceptor();
        server.run();
    }

}