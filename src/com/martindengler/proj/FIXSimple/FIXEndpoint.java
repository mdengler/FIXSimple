package com.martindengler.proj.FIXSimple;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;


import com.martindengler.proj.FIXSimple.spec.Tag;


/**
 * Convenience class for Initiators
 */

public class FIXEndpoint {

    private String senderCompId;
    private String targetCompId;

    private FIXStream bidirectionalStream;
    private BlockingQueue<FIXMessage> outgoingMessageQueue;
    private BlockingQueue<FIXMessage> incomingMessageQueue;
    private Iterator<FIXMessage> incomingMessageIterator;

    private Boolean connected = false; //TODO: make thread-safe


    private FIXEndpoint() {
    }


    public FIXEndpoint(String senderCompId, String targetCompId) {
        this.senderCompId = senderCompId;
        this.targetCompId = targetCompId;
    }


    public Boolean connect(String hostname, int port) {
        return this.connect(hostname, port, 10);
    }


    public Boolean connect(String hostname, int port, int timeout) {
        InetSocketAddress endpointAddress = new InetSocketAddress(hostname, port);
        try {
            Socket endpoint = new Socket();
            endpoint.connect(endpointAddress, timeout);

            return this.connect(endpoint);

        } catch (IOException ioe) {
            System.err.println(String.format("FIXEndpoint.connect(...): couldn't connect to %s:%s (timeout: %s)",
                            hostname, port, timeout));
            System.err.println(ioe.getMessage());
            System.err.println(ioe.toString());

            this.connected = false;

            return false;
        }
    }


    public Boolean connect(Socket s) {
        try {
            this.bidirectionalStream = FIXStream.connect(s);

            this.incomingMessageQueue = this.bidirectionalStream.inputStream();
            this.outgoingMessageQueue = this.bidirectionalStream.outputStream();

            this.incomingMessageIterator = this.incomingMessageQueue.iterator();

            this.connected = true;
        } catch (IOException unrecoverable) {
            this.connected = false;
            return false;
        }

        return this.connected;
    }


    public Boolean deliver(FIXMessage message) {
        if (!this.connected)
            throw new RuntimeException("not connected");

        try {
            this.outgoingMessageQueue.put(message
                    .putM(Tag.SENDERCOMPID, this.senderCompId)
                    .putM(Tag.TARGETCOMPID, this.targetCompId)
                    .putM(Tag.SENDINGTIME, Calendar.getInstance()));
        } catch (InterruptedException inte) { //TODO: this is probably not right
            System.err.println("FIXEndpoint.deliver(): interrupted; will NOT retry");
            System.err.println(inte.getMessage());
            System.err.println(inte.toString());
            return false;
        }

        return true;

    }


    public FIXMessage receive() {
        if (!this.connected)
            throw new RuntimeException("not connected");

        if (this.incomingMessageIterator.hasNext()) {
            return this.incomingMessageIterator.next();
        } else {
            return null;
        }
    }

}