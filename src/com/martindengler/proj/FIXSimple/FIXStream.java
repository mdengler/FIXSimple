package com.martindengler.proj.FIXSimple;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



public class FIXStream {

    public enum State {
        UNINITIALIZED,
            UNOPENED,
            ACTIVE,
            CLOSING,
            CLOSED_IN,
            CLOSED_OUT,
            CLOSED_ALL,
            UNKNOWN
            }

    private BlockingQueue<FIXMessage> incomingQueue;
    private BlockingQueue<FIXMessage> outgoingQueue;

    private ExecutorService executorService;

    private Object stateLock = new Object();
    private State state = State.UNINITIALIZED;

    private Socket socket;

    public static FIXStream connect(Socket conversation) throws IOException {
        return new FIXStream(conversation,
                new InputStreamReader(conversation.getInputStream()),
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

        this.executorService = new ThreadPoolExecutor(5,     // corePoolSize
                                               1000,  // maximumPoolSize
                                               100,   // keepAliveTime in...
                                               TimeUnit.SECONDS,
                                               new ArrayBlockingQueue<Runnable>(10));

        this.setState(State.UNOPENED);

    }


    private FIXStream(Socket conversation,
            Reader inputReader,
            Writer outputWriter) {
        this(inputReader, outputWriter);
        this.socket = conversation;
    }


    private FIXStream(Reader inputReader, Writer outputWriter) {
        this();
        this.setState(State.ACTIVE);

        // FUTURE: do something with these Futures
        this.executorService.submit(new FIXStreamInputHandler(this, inputReader));
        this.executorService.submit(new FIXStreamOutputHandler(this, outputWriter));
    }


    public BlockingQueue<FIXMessage> inputQueue() {
        return this.incomingQueue;
    }


    public BlockingQueue<FIXMessage> outputQueue() {  //TODO: rename outputQueue?
        return this.outgoingQueue;
    }


    public FIXStream.State getState() {
        synchronized(this.stateLock) {
            return this.state;
        }
    }


    public void setState(FIXStream.State newState) {
        synchronized(this.stateLock) {
            this.state = newState;
        }
    }


    public synchronized void stop() {

        // FUTURE: this method feels a bit like a lot of kowtowing to
        // APIs that will never be useful; worth a detailed review to
        // see what should be added and what can definitely be omitted
        // (add comments as to why, of course)

        this.state = State.CLOSING;  // risk winning-but-losing, avoid livelock

        Integer countdownToForcedExit = 10;
        Integer countdownByMilliseconds = 100;
        while (this.state != State.CLOSED_ALL) {

            if (!this.executorService.isTerminated()) {
                try {
                    this.executorService.shutdown();
                } catch (SecurityException ignored) {}
            }

            try {
                Thread.currentThread().sleep(countdownByMilliseconds);
            } catch (InterruptedException ignored) {}

            if (--countdownToForcedExit <= 0) {
                this.state = State.CLOSED_ALL;
            }
        }

        if (!this.executorService.isTerminated()) {
            try {
                this.executorService.shutdownNow();
            } catch (SecurityException ignored) {}
        }

        if (this.socket != null
                && !this.socket.isClosed()) {
            try {
                this.socket.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                // FUTURE: do something better here
                System.err.println("FIXStream.stop():" +
                        " IOException on socket; leaving things as they" +
                        " are :(");
            }
        }
        System.err.println("FIXStream.stop(): stopped");
    }

}