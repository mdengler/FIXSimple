package com.martindengler.proj.fixms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.martindengler.proj.fixms.spec.MsgType;
import com.martindengler.proj.fixms.spec.Tag;

public class FIXMessage extends HashMap<Tag, String> {

    public static FIXMessage newMessage(String messageType) {
        FIXMessage message = new FIXMessage();
        message.put(Tag.BEGINSTRING, "FIXT.1.1");
        message.put(Tag.BODYLENGTH, "42");
        return message;
    }

}