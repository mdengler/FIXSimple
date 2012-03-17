package com.martindengler.proj.fixms;

import java.util.Comparator;
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import com.martindengler.proj.fixms.spec.MsgType;
import com.martindengler.proj.fixms.spec.Tag;

public class FIXMessage extends TreeMap<Tag, String> {

    private static final long serialVersionUID = 16180339887L;

    public static String SOH = String.format("%c", 0x08); //FUTURE: make enum?

    private FIXMessage() {};  //immutable; TODO: actually do this properly

    public static FIXMessage factory(String messageType) {
        FIXMessage message = new FIXMessage()
            .putM(Tag.BEGINSTRING, "FIXT.1.1")
            .putM(Tag.BODYLENGTH, "42");
        return message;
    }

    public FIXMessage putM(Tag key, String value) {
        FIXMessage changed = new FIXMessage();
        changed.putAll(this);
        changed.put(key, value);
        return changed;
    }

    @Override
    public String toString() {
        return this.toString(",", true);
    }

    public String toString(String delimiter, Boolean verbose) {
        StringBuilder sb = new StringBuilder();
        int idx = 0;
        Set<Map.Entry<Tag, String>> entrySet = this.entrySet();
        int size = entrySet.size();
        for (Map.Entry<Tag, String> pair : entrySet) {
            sb.append(String.format("%s=%s",
                                    pair.getKey().toString(verbose),
                                    pair.getValue()));
            if (++idx < size)
                sb.append(delimiter);
        }
        return sb.toString();
    }

    public String toWire() {
        return this.toString(SOH, false);
    }

}
