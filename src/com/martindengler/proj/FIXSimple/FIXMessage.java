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


//TODO: put in spec/ as FIXMessage42?
public class FIXMessage extends TreeMap<Tag, String> {

    private static final long serialVersionUID = 16180339887L;

    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final byte SOH_byte = 0x01;
    public static final String SOH = String.format("%c", SOH_byte); //FUTURE: make enum?
    public static final byte[] FIX_PREAMBLE = ("8=FIX.4.2" + SOH + "9=")
        .getBytes(ISO_8859_1);



    private int checksum = 0;

    private boolean dontRecalcValues = false; // when we read messages from the wire, we don't want to recalc values
    private byte[] wireBytes; //when this instance was made from the wire rather than built up programmatically

    private FIXMessage() {};  //immutable; TODO: actually do this properly



    public static FIXMessage factory(MsgType messageType) {
        FIXMessage message = new FIXMessage()
            .putM(Tag.BEGINSTRING,     "FIX.4.2")
            //    Tag.BODYLENGTH       calculated later
            .putM(Tag.MSGTYPE,         messageType.getCode().toString())
            //    Tag.SENDERCOMPID     set later
            //    Tag.TARGETCOMPID     set later
            //    Tag.MSGSEQNUM,       calculated later
            //    Tag.SENDINGTIME      calculated later
            ;
        return message;
    }


    public static Integer validate(FIXMessage message) {
        return 0;
    }


    /**
     * Parses the message from a string using a custom delimiter
     */
    public static FIXMessage factory(String wireBytesAsString, String delimiter, Boolean recalculateValues) {

        FIXMessage message = new FIXMessage();

        // 1) split into Tag, Data pairs
        // 2) add pairs to message
        // 3) call validate() (might throw)
        // 4) return message

        for (String rawPair : wireBytesAsString.split(delimiter)) {
            String[] pair = rawPair.split("=");

            if (pair.length != 2)
                throw new IllegalStateException(String.format("FIXMessage.factory():" +
                                " bad pair %s in message %s",
                                rawPair, wireBytesAsString));

            Tag tag = Tag.fromCode(Integer.parseInt(pair[0]));
            String data = pair[1];

            message = message.putM(tag, data, recalculateValues);
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
    public static FIXMessage fromWire(byte[] wireBytes) {
        FIXMessage message = factory(wireBytes, SOH_byte, false);
        message.wireBytes = wireBytes;
        return message;
    }


    public static FIXMessage factory(byte[] wireBytes, byte delimiter, Boolean recalculateValues) {
        return factory(new String(wireBytes, ISO_8859_1),
                new String(new byte[] {delimiter}),
                       recalculateValues);
    }


    public static FIXMessage factory(String fromString) {
        return factory(fromString, SOH);
    }


    public static FIXMessage factory(String fromString, String delimiter) {
        return factory(fromString, delimiter, true);
    }



    public FIXMessage putM(Tag key, String value, Boolean recalculateValues) {
        FIXMessage changed = new FIXMessage();
        changed.putAll(this);
        changed.put(key, value);
        if (recalculateValues) {
            changed.put(Tag.BODYLENGTH, String.format("%d", changed.calculateFIXBodyLength()));
            changed.put(Tag.CHECKSUM, String.format("%03d", changed.calculateFIXChecksum()));
        }
        return changed;
    }

    public FIXMessage putM(Tag key, String value) {
        return putM(key, value, true);
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
        return putM(key, String.format("%1$tY%1$tm%1$td-%1$tH:%1$tM:%1$tS", value));
    }


    public MsgType getMsgType() {
        if (!this.containsKey(Tag.MSGTYPE))
            throw new IllegalStateException("FIXMessage: no Tag.MSGTYPE tag present");
        return MsgType.fromCode(this.get(Tag.MSGTYPE));
    }


    @Override
    public String toString() {
        return this.toString(true);
    }

    public String toString(Boolean verbose) {
        return this.toString(",", verbose);
    }

    public String toString(String delimiter, Boolean verbose) {
        StringBuilder sb = new StringBuilder();

        String msgTypeString = "";
        if (this.containsKey(Tag.MSGTYPE)) {
            MsgType msgType = MsgType.fromCode(this.get(Tag.MSGTYPE));
            msgTypeString = String.format("%s\t(%s)",
                    msgType.name(), msgType.getCode());
        } else {
            msgTypeString = "INVALID: no Tag.MSGTYPE key";
        }
        sb.append(msgTypeString);

        sb.append(" -> ");

        if (!this.containsKey(Tag.TARGETCOMPID)) {
            sb.append(" <target unset>");
        } else {
            sb.append(this.get(Tag.TARGETCOMPID));
        }

        sb.append(" ");

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


    /**
     * See also fromWire(...) static method
     */
    public String toWire() {
        return this.toWire(false);
    }


    public String toWire(Boolean excludeChecksumPair) {
        StringBuilder sb = new StringBuilder();
        String delimiter = SOH;

        int idx = 0;
        Set<Map.Entry<Tag, String>> entrySet = this.entrySet();
        int size = entrySet.size();
        for (Map.Entry<Tag, String> pair : entrySet) {
            if (excludeChecksumPair && pair.getKey() == Tag.CHECKSUM)
                continue;
            sb.append(String.format("%s=%s",
                            pair.getKey().getCode(),
                            pair.getValue()));
            sb.append(delimiter);
        }

        return sb.toString();

    }


    protected byte[] getWireBytes() {
        String wireMessageString = this.toWire();
        return wireMessageString.getBytes(ISO_8859_1);
    }


    protected byte[] getWireBytesWithoutChecksumTag() {
        String wireMessageString = this.toWire(true);
        return wireMessageString.getBytes(ISO_8859_1);
    }


    public Integer calculateFIXBodyLength() {
        byte[] toCount = this.getWireBytesWithoutChecksumTag();
        return toCount.length;
    }


    public Integer calculateFIXChecksum() {
        byte[] wireMessageBytes = this.getWireBytesWithoutChecksumTag();

        int checksum = 0;

        for (byte b : wireMessageBytes)
            checksum += (int) b;

        checksum = checksum % 256;

        return checksum;
    }

}
