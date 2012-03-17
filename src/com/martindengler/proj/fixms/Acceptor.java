package com.martindengler.proj.fixms;

import java.util.Collection;
import java.util.Map;
import java.util.Arrays;
import java.util.Iterator;

import com.martindengler.proj.fixms.spec.MsgType;
import com.martindengler.proj.fixms.spec.Tag;

public class Acceptor {

    public Acceptor() {
    }

    public void run() {
        Iterator<FIXMessage> stream = FIXStream.accept().iterator();
        while (stream.hasNext())
            processMessage(stream.next());
    }

    public void processMessage(FIXMessage message) {
        System.out.printf("processMessage(): %s%n", message);
    }

    public static void main(String[] args) {
        Acceptor server = new Acceptor();
        server.run();
    }

}