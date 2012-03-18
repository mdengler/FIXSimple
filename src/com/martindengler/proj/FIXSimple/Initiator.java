package com.martindengler.proj.FIXSimple;

import java.util.Collection;
import java.util.Map;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.io.IOException;
import java.net.Socket;

import com.martindengler.proj.FIXSimple.spec.MsgType;
import com.martindengler.proj.FIXSimple.spec.Tag;

public class Initiator {

    public Initiator() {
    }

    public void run() throws IOException {
        FIXEndpoint server = new FIXEndpoint("MARTIN", "SERVER");
        FIXMessage message;
        FIXMessage response;
        Integer nextSequenceNumber = 1;

        System.out.println("Connecting...");
        if (!server.connect("localhost", 16180)) {
            System.err.println("Couldn't connect; exiting.");
            System.exit(1);
        }
        System.out.println("Connected.");


        message = FIXMessage.factory(MsgType.LOGON, nextSequenceNumber++);
        System.out.println(message);
        server.deliver(message);

        response = server.receive();
        if (response.getMsgType() != MsgType.LOGON)
            throw new IllegalStateException("didn't get LOGON response; got " + response.toString());

        message = FIXMessage.factory(MsgType.NEW_ORDER_SINGLE, nextSequenceNumber++);
        server.deliver(message);

        response = server.receive();
        if (response.getMsgType() != MsgType.NEW_ORDER_SINGLE)
            throw new IllegalStateException("didn't get NEW_ORDER_SINGLE response; got " + response.toString());

        message = FIXMessage.factory(MsgType.LOGOUT, nextSequenceNumber++);
        server.deliver(message);

        response = server.receive();
        if (response.getMsgType() != MsgType.LOGOUT)
            throw new IllegalStateException("didn't get LOGOUT response; got " + response.toString());

        System.exit(0);

    }


    public FIXMessage processMessage(FIXMessage message) {
        System.out.printf("processMessage(): %s%n", message);
        //TODO: fill in
        return null;
    }

    public static void main(String[] args) throws IOException {
        Initiator client = new Initiator();
        client.run();
    }

}