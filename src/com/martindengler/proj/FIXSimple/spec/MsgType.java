package com.martindengler.proj.FIXSimple.spec;

import java.util.Map;
import java.util.HashMap;
import java.util.EnumSet;


public enum MsgType {
    HEARTBEAT("0"),
        TEST_REQUEST("1"),
        RESEND_REQUEST("2"),
        REJECT("3"),
        SEQUENCE_RESET("4"),
        LOGOUT("5"),
        IOI("6"),
        ADVERTISEMENT("7"),
        EXECUTION_REPORT("8"),
        ORDER_CANCEL_REJECT("9"),
        LOGON("A"),
        NEWS("B"),
        EMAIL("C"),
        NEW_ORDER_SINGLE("D"),
        NEW_ORDER_LIST("E"),
        ORDER_CANCEL_REQUEST("F"),
        ORDER_CANCEL_REPLACE_REQUEST("G"),
        ORDER_STATUS_REQUEST("H"),
        ALLOCATION("J"),
        LIST_CANCEL_REQUEST("K"),
        LIST_EXECUTE("L"),
        LIST_STATUS_REQUEST("M"),
        LIST_STATUS("N"),
        ALLOCATION_ACK("P"),
        DONT_KNOW_TRADE("Q"),
        QUOTE_REQUEST("R"),
        QUOTE("S"),
        SETTLEMENT_INSTRUCTIONS("T"),
        MARKET_DATA_REQUEST("V"),
        MARKET_DATA_SNAPSHOT_FULL_REFRESH("W"),
        MARKET_DATA_INCREMENTAL_REFRESH("X"),
        MARKET_DATA_REQUEST_REJECT("Y"),
        QUOTE_CANCEL("Z"),
        QUOTE_STATUS_REQUEST("a"),
        QUOTE_ACKNOWLEDGEMENT("b"),
        SECURITY_DEFINITION_REQUEST("c"),
        SECURITY_DEFINITION("d"),
        SECURITY_STATUS_REQUEST("e"),
        SECURITY_STATUS("f"),
        TRADING_SESSION_STATUS_REQUEST("g"),
        TRADING_SESSION_STATUS("h"),
        MASS_QUOTE("i"),
        BUSINESS_MESSAGE_REJECT("j"),
        BID_REQUEST("k"),
        BID_RESPONSE("l"),
        LIST_STRIKE_PRICE("m"),
        ;

    private static final Map<String, MsgType> lookup
        = new HashMap<String, MsgType>();

    static {
        for(MsgType t : EnumSet.allOf(MsgType.class))
            lookup.put(t.getCode(), t);
    }

    private String code;

    public static MsgType fromCode(String code) {
        return lookup.get(code);
    }

    private MsgType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public String toString() {
        return this.code;
    }

    public String toString(Boolean verbose) {
        if (verbose)
            return this.toString() + "_" + this.name();
        else
            return this.toString();
    }

}



