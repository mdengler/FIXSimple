package com.martindengler.proj.FIXSimple;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Arrays;
import java.util.ArrayList;

import com.martindengler.proj.FIXSimple.spec.MsgType;
import com.martindengler.proj.FIXSimple.spec.Tag;


public class FIXMessage extends TreeMap<Tag, String> {

    private static final long serialVersionUID = 16180339887L;
    private static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

    public static byte SOH_byte = 0x01;
    public static String SOH = String.format("%c", SOH_byte); //FUTURE: make enum?

    private int checksum = 0;

    private FIXMessage() {};  //immutable; TODO: actually do this properly

    public static FIXMessage factory(String messageType, Integer sequenceNumber) {
        return factory(MsgType.valueOf(messageType), sequenceNumber);
    }

    public static FIXMessage factory(MsgType messageType, Integer sequenceNumber) {
        FIXMessage message = new FIXMessage()
            .putM(Tag.BEGINSTRING,     "FIX.4.2")
            //    Tag.BODYLENGTH       calculated later
            .putM(Tag.MSGTYPE,         messageType.getCode().toString())
            //    Tag.SENDERCOMPID     set later
            //    Tag.TARGETCOMPID     set later
            .putM(Tag.MSGSEQNUM,       sequenceNumber)
            //    Tag.SENDINGTIME      calculated later
            ;
        return message;
    }


    public static Integer validate(FIXMessage message) {
        return 0;
    }


    public static FIXMessage factory(String wireBytesAsString) {
        return factory(wireBytesAsString.getBytes(ISO_8859_1));
    }

    /**
     * Parses the message from a string using a custom delimiter
     */
    public static FIXMessage factory(String wireBytesAsString, String delimiter) {

        FIXMessage message = new FIXMessage();

        // 1) split into Tag, Data pairs
        // 2) add pairs to message
        // 3) call validate() (might throw)
        // 4) return message

        for (String rawPair : wireBytesAsString.split(delimiter)) {
            String[] pair = rawPair.split("=");
            if (pair.length != 2)
                throw new RuntimeException(String.format("FIXMessage.factory():" +
                                " bad pair %s in message %s",
                                rawPair, wireBytesAsString));
            Tag tag = Tag.fromCode(Integer.parseInt(pair[0]));
            String data = pair[1];

            System.out.println(String.format("read tag %s with data %s from rawstring %s", tag, data, wireBytesAsString));

            message = message.putM(tag, data);
        }

        return message;

    }


    /**
     * Parses the message from a bunch of bytes on the wire.
     *
     * DOES NOT VALIDATE the message.  Call
     * FIXMessage.validate(message) if you want that.
     *
     * CAN THROW.  Will throw an exception if bytes contain tags not
     * part of the FIX specification.  Behaviour is undefined if there
     * are not SOH-delimited strings of equals-delimited key/value
     * pairs.
     *
     */
    public static FIXMessage factory(byte[] wireBytes) {
        return factory(wireBytes, SOH_byte);
    }


    public static FIXMessage factory(byte[] wireBytes, byte delimiter) {
        return factory(new String(wireBytes, ISO_8859_1),
                       new String(new byte[] {delimiter}));
    }



    public FIXMessage putM(Tag key, String value) {
        FIXMessage changed = new FIXMessage();
        changed.putAll(this);
        changed.put(key, value);
        changed.put(Tag.BODYLENGTH, String.format("%d", calculateFIXBodyLength()));
        changed.put(Tag.CHECKSUM, String.format("%03d", calculateFIXChecksum()));
        return changed;
    }

    public FIXMessage putM(Tag key, Integer value) {
        return putM(key, String.format("%d", value)); //TODO: ensure signed
    }

    public FIXMessage putM(Tag key, float value) {
        return putM(key, String.format("%15f", value)); //TODO: ensure 15 sig figs
    }

    public FIXMessage putM(Tag key, Boolean value) {
        return putM(key, value ? "Y" : "N");
    }

    public FIXMessage putM(Tag key, Calendar value) {
        return putM(key, String.format("%Y%m%d-%H:%M:%S", value));
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


    protected byte[] getWireBytes() {
        String wireMessageString = this.toWire();
        return wireMessageString.getBytes(ISO_8859_1);
    }


    protected Integer calculateFIXBodyLength() {
        return getWireBytes().length;
    }


    protected int calculateFIXChecksum() {
        String wireMessageString = this.toWire();
        byte[] wireMessageBytes = wireMessageString.getBytes(ISO_8859_1);
        int checksum = 0;
        for (byte b : wireMessageBytes)
            checksum += (int) b;
        checksum = checksum % 256;
        return checksum;
    }

}
