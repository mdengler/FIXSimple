package com.martindengler.proj.FIXSimple.spec;

import java.util.Map;
import java.util.HashMap;
import java.util.EnumSet;


public enum MsgType {
        HEARTBEAT("0"),
        LOGON("A"),
        TEST("1"),
        RESEND("2"),
        REJECT("3"),
        SEQUENCE("4"),
        LOGOUT("5"),
        ADVERTISEMENT("7"),
        INDICATION("6"),
        NEWS("B"),
        EMAIL("C"),
        QUOTE_REQUEST("R"),
        QUOTE("S"),
        MASS_QUOTE("I"),
        QUOTE_CANCEL("Z"),
        QUOTE_STATUS_REQUEST("A"),
        QUOTE_ACKNOWLEDGEMENT("B"),
        MARKET_DATA_REQUEST("V"),
        MARKET_DATA_SNAPSHOT_FULL_REFRESH("W"),
        MARKET_DATA_INCREMENTAL_REFRESH("X"),
        MARKET_DATA_REQUEST_REJECT("Y"),
        SECURITY_DEFINITION_REQUEST("C"),
        SECURITY_DEFINITION("D"),
        SECURITY_STATUS_REQUEST("E"),
        SECURITY_STATUS("F"),
        TRADING_SESSION_STATUS_REQUEST("G"),
        TRADING_SESSION_STATUS("H"),
        NEW_ORDER_SINGLE("D"),
        EXECUTION_REPORT("8"),
        DONT_KNOW_TRADE_("Q"),  // aka DK
        ORDER_CANCEL_REPLACE_REQUEST("G"),  // aka order modification request
        ORDER_CANCEL_REQUEST("F"),
        ORDER_CANCEL_REJECT("9"),
        ORDER_STATUS_REQUEST("H"),
        ALLOCATION("J"),
        ALLOCATION_ACK("P"),
        SETTLEMENT_INSTRUCTIONS("T"),
        BIDREQUEST("K"),
        BIDRESPONSE("L"),
        NEW_ORDER_LIST("E"),
        LISTSTRIKEPRICE("M"),
        LIST_STATUS("N"),
        LIST_EXECUTE("L"),
        LIST_CANCEL_REQUEST("K"),
        LIST_STATUS_REQUEST("M"),
        BUSINESS_MESSAGE_REJECT("J"),
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



