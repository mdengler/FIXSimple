package com.martindengler.proj.FIXSimple;


import com.martindengler.proj.FIXSimple.spec.MsgType;
import com.martindengler.proj.FIXSimple.spec.Tag;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Set;


public abstract class FIXMessagePrinter {


    public static void prettyPrint(FIXMessage m) throws IOException {
        prettyPrint(m, System.out);
    }

    public static void prettyPrint(FIXMessage m, OutputStream ps) throws IOException {
        prettyPrint(m, new OutputStreamWriter(ps));
    }

    public static void prettyPrint(FIXMessage m, Writer w) throws IOException {
        w.write(String.format("%s%n", prettyString(m)));
        w.flush();
    }

    public static String prettyString(FIXMessage m) {
        StringBuilder sb = new StringBuilder();
        String nl = String.format("%n");

        sb.append("---------------------" + nl);

        String msgTypeString = "Message type: ";
        if (m.containsKey(Tag.MSGTYPE)) {
            MsgType msgType = MsgType.fromCode(m.get(Tag.MSGTYPE));
            msgTypeString += String.format("%s\t(%s)",
                    msgType.name(), msgType.getCode());
        } else {
            msgTypeString += "INVALID: no Tag.MSGTYPE key";
        }
        sb.append(msgTypeString);

        sb.append(nl + "TargetCompID: ");

        if (!m.containsKey(Tag.TARGETCOMPID)) {
            sb.append(" <target unset>");
        } else {
            sb.append(m.get(Tag.TARGETCOMPID));
        }

        sb.append(nl);

        int idx = 0;
        Set<Map.Entry<Tag, String>> entrySet = m.entrySet();
        int size = entrySet.size();
        for (Map.Entry<Tag, String> pair : entrySet) {
            Tag t = pair.getKey();
            String v = pair.getValue();
            sb.append(String.format("    %03d - %15s = %s",
                            t.getCode(),
                            t.name(),
                            v));
            sb.append(nl);
        }

        sb.append("---------------------" + nl);

        return sb.toString();

    }


    private FIXMessagePrinter() {}

}