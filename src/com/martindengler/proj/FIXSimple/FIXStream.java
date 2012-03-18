package com.martindengler.proj.FIXSimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.martindengler.proj.FIXSimple.spec.MsgType;
import com.martindengler.proj.FIXSimple.spec.Tag;

public class FIXStream {

    public static Collection<FIXMessage> accept() {
        Collection<FIXMessage> stream = new ArrayList<FIXMessage>();
        stream.add(FIXMessage.factory(MsgType.LOGON, 1));
        stream.add(FIXMessage.factory(MsgType.NEW_ORDER_SINGLE, 2));
        stream.add(FIXMessage.factory(MsgType.LOGOUT, 3));
        return stream;
    }

}