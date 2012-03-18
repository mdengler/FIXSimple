package com.martindengler.proj.FIXSimple;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.martindengler.proj.FIXSimple.spec.MsgType;
import com.martindengler.proj.FIXSimple.spec.Tag;
import com.martindengler.proj.FIXSimple.spec.OrdStatus;

public class Acceptor {

    private Executor executor;

    public Acceptor() {
        this.executor = new ThreadPoolExecutor(5,     // corePoolSize
                                               1000,  // maximumPoolSize
                                               100,   // keepAliveTime in...
                                               TimeUnit.SECONDS,
                                               new ArrayBlockingQueue<Runnable>(10));
    }

    public void run() throws IOException, InterruptedException {
        Integer listenPort = 16180;
        ServerSocket listenSocket = new ServerSocket(listenPort);

        String mySenderCompId = "SERVER";

        while (true) {  // client handling start
            System.err.println("Listening on port " + listenPort);
            Socket clientSocket = listenSocket.accept();
            AcceptorSimpleClientHandler handler =
                new AcceptorSimpleClientHandler(mySenderCompId);
            handler.connect(clientSocket);
            this.executor.execute(handler);
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