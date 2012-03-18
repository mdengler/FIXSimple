package com.martindengler.proj.FIXSimple;


import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;


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
        sb.append("--------------------------------------------------");
        sb.append(String.format("%n"));
        sb.append(m.toString());
        sb.append(String.format("%n"));
        sb.append("--------------------------------------------------");
        sb.append(String.format("%n"));
        return sb.toString();
    }


    private FIXMessagePrinter() {}

}