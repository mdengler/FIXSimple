package com.martindengler.proj.fixms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.martindengler.proj.fixms.spec.MsgType;
import com.martindengler.proj.fixms.spec.Tag;

public class FIXStream {

    public static Collection<FIXMessage> accept() {
        Collection<FIXMessage> stream = new ArrayList<FIXMessage>();
        stream.add(FIXMessage.factory("Logon"));
        stream.add(FIXMessage.factory("NewOrderSingle"));
        stream.add(FIXMessage.factory("Logoff"));
        return stream;
    }

}