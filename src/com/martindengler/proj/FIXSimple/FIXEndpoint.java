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

public abstract class FIXEndpoint {

    protected String senderCompId;
    protected String targetCompId;

    protected Socket socket;

    protected FIXStream bidirectionalStream;
    protected BlockingQueue<FIXMessage> outgoingMessageQueue;
    protected BlockingQueue<FIXMessage> incomingMessageQueue;
    protected Iterator<FIXMessage> incomingMessageIterator;

    protected Integer nextIncomingSequenceNumber = -1;
    protected Integer nextOutgoingSequenceNumber = -1;

    protected Boolean connected = false; //TODO: make thread-safe


    protected FIXEndpoint() {
    }


    protected FIXEndpoint(String senderCompId, String targetCompId) {
        this.senderCompId = senderCompId;
        this.targetCompId = targetCompId;
    }


    public Boolean connect(Socket s) {
        this.socket = s;
        try {
            this.bidirectionalStream = FIXStream.connect(s);

            this.incomingMessageQueue = this.bidirectionalStream.inputQueue();
            this.outgoingMessageQueue = this.bidirectionalStream.outputQueue();

            this.incomingMessageIterator = this.incomingMessageQueue.iterator();

            this.nextIncomingSequenceNumber = 1;
            this.nextOutgoingSequenceNumber = 1;

            this.connected = true;
        } catch (IOException unrecoverable) {
            this.connected = false;
            return false;
        }

        return this.connected;
    }


    public Boolean deliver(FIXMessage message) {
        if (!this.connected)
            throw new IllegalStateException("not connected");

        try {
            message = message
                .putM(Tag.MSGSEQNUM,    this.nextOutgoingSequenceNumber++)
                .putM(Tag.SENDERCOMPID, this.senderCompId)
                .putM(Tag.TARGETCOMPID, this.targetCompId)
                .putM(Tag.SENDINGTIME,  Calendar.getInstance());

            System.err.println(message);
            this.outgoingMessageQueue.put(message);
            System.err.println("put on queue");

        } catch (InterruptedException inte) { //TODO: this is probably not right
            System.err.println("FIXEndpoint.deliver(): interrupted; will NOT retry");
            System.err.println(inte.getMessage());
            System.err.println(inte.toString());
            inte.printStackTrace();
            return false;
        }

        return true;

    }


    /**
     * TODO: document that this WILL block
     */
    public FIXMessage receive() {
        if (!this.connected)
            throw new IllegalStateException("not connected");

        try {

            return this.incomingMessageQueue.take();

        } catch (InterruptedException inte) {
            throw new IllegalStateException(inte.getMessage(), inte);
        }
    }

}