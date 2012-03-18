package com.martindengler.proj.FIXSimple;

import java.util.Collection;
import java.util.Map;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.io.IOException;
import java.net.Socket;

import com.martindengler.proj.FIXSimple.spec.MsgType;
import com.martindengler.proj.FIXSimple.spec.OrdStatus;
import com.martindengler.proj.FIXSimple.spec.Tag;

public class Initiator {

    public Initiator() {
    }

    public void run() throws IOException {
        FIXEndpointConnector server = new FIXEndpointConnector("MARTIN", "SERVER");
        FIXMessage message;
        FIXMessage response;

        System.err.println("Connecting...");
        if (!server.connect("localhost", 16180)) {
            System.err.println("Couldn't connect; exiting.");
            System.exit(1);
        }
        System.err.println("Challenge accepted.");


        message = FIXMessage.factory(MsgType.LOGON);
        server.deliver(message);

        response = server.receive();
        if (response.getMsgType() != MsgType.LOGON)
            throw new IllegalStateException("didn't get LOGON response; got " + response.toString());

        message = FIXMessage.factory(MsgType.NEW_ORDER_SINGLE)
            .putM(Tag.ORDSTATUS, OrdStatus.NEW)
            .putM(Tag.ORDERQTY,  "10000")
            ;
        server.deliver(message);

        response = server.receive();
        if (response.getMsgType() != MsgType.EXECUTION_REPORT)
            throw new IllegalStateException("didn't get EXECUTION_REPORT response; got " + response.toString());

        FIXMessagePrinter.prettyPrint(response);

        message = FIXMessage.factory(MsgType.LOGOUT);
        server.deliver(message);

        response = server.receive();
        if (response.getMsgType() != MsgType.LOGOUT)
            throw new IllegalStateException("didn't get LOGOUT response; got " + response.toString());

        System.err.println("Challenge completed.");
        System.exit(0);

    }


    public static void main(String[] args) throws IOException {
        Initiator client = new Initiator();
        client.run();
    }

}