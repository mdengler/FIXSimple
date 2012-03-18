package com.martindengler.proj.FIXSimple;

import java.util.Collection;
import java.util.Map;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.io.IOException;
import java.net.Socket;

import com.martindengler.proj.FIXSimple.spec.CommType;
import com.martindengler.proj.FIXSimple.spec.ExecInst;
import com.martindengler.proj.FIXSimple.spec.ExecTransType;
import com.martindengler.proj.FIXSimple.spec.ExecType;
import com.martindengler.proj.FIXSimple.spec.HandlInst;
import com.martindengler.proj.FIXSimple.spec.MsgType;
import com.martindengler.proj.FIXSimple.spec.OrdStatus;
import com.martindengler.proj.FIXSimple.spec.OrdType;
import com.martindengler.proj.FIXSimple.spec.SettlementType;
import com.martindengler.proj.FIXSimple.spec.Side;
import com.martindengler.proj.FIXSimple.spec.Tag;
import com.martindengler.proj.FIXSimple.spec.TimeInForce;
import com.martindengler.proj.util.UTCCalendar;

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
            .putM(Tag.CLORDID,    "ORD27181828")
            .putM(Tag.ORDSTATUS,  OrdStatus.NEW)
            .putM(Tag.ORDERQTY,   "10000")
            .putM(Tag.HANDLINST,  HandlInst.AUTOMATED_PRIVATE)
            .putM(Tag.EXECINST,   ExecInst.ALL_OR_NONE)
            .putM(Tag.SYMBOL,     "MS.N")
            .putM(Tag.SIDE,       Side.BUY)
            .putM(Tag.TRANSACTTIME, UTCCalendar.getInstance())
            .putM(Tag.ORDTYPE,    OrdType.MARKET)
            .putM(Tag.ACCOUNT,    "88888")
            .putM(Tag.COMMTYPE,   CommType.ABSOLUTE)
            .putM(Tag.COMMISSION, "0.02")
            .putM(Tag.SETTLEMENTTYPE, SettlementType.REGULAR)
            .putM(Tag.PRICE,      "19.51")
            .putM(Tag.TIMEINFORCE, TimeInForce.FILL_OR_KILL)
            .putM(Tag.TEXT,       "FIXSimple is simple")
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