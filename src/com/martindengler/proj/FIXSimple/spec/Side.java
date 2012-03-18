package com.martindengler.proj.FIXSimple.spec;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public enum Side {
    // comments are from the FIX spec and sometimes unhelpfully
    // cryptic
        BUY("1"),
        BUY_MINUS("3"),
        CROSS("8"), // (orders where counterparty is an exchange, valid for all messages except IOIs)
        CROSS_SHORT("9"),
        SELL("2"),
        SELL_PLUS("4"),
        SELL_SHORT("5"),
        SELL_SHORT_EXEMPT("6"),
        UNDISCLOSED("7"), // (valid for IOI and List Order messages only)

        ;

    private static final Map<String, Side> lookup
        = new HashMap<String, Side>();

    static {
        for(Side t : EnumSet.allOf(Side.class))
            lookup.put(t.getCode(), t);
    }

    private String code;

    public static Side fromCode(String code) {
        return lookup.get(code);
    }

    private Side(String code) {
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



