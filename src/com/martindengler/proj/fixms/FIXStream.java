package com.martindengler.proj.fixms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.martindengler.proj.fixms.spec.MsgType;
import com.martindengler.proj.fixms.spec.Tag;

public class FIXStream {

    public static Collection<Map> accept() {
        Collection<Map> stream = new ArrayList<Map>();
        stream.add(newMessage("Logon"));
        stream.add(newMessage("NewOrderSingle"));
        stream.add(newMessage("Logoff"));
        return stream;
    }

    protected static Map newMessage(String messageType) {
        Map<Tag, String> message = new HashMap<Tag, String>();
        message.put(Tag.BEGINSTRING, "FIXT.1.1");
        message.put(Tag.BODYLENGTH, "42");
        return message;
    }

}