package com.martindengler.proj.FIXSimple.spec;

import java.util.Map;
import java.util.HashMap;
import java.util.EnumSet;


public enum TimeInForce {
    // comments are from the FIX spec and sometimes unhelpfully
    // cryptic
        AT_THE_OPENING("2"), //OTG
        DAY("0"),
        FILL_OR_KILL("4"),  // FOK
        GOOD_TILL_CANCEL("1"), // GTC
        GOOD_TILL_CROSSING("5"), // GTX
        GOOD_TILL_DATE("6"), // GTD
        IMMEDIATE_OR_CANCEL("3"), //IOC


        ;

    private static final Map<String, TimeInForce> lookup
        = new HashMap<String, TimeInForce>();

    static {
        for(TimeInForce t : EnumSet.allOf(TimeInForce.class))
            lookup.put(t.getCode(), t);
    }

    private String code;

    public static TimeInForce fromCode(String code) {
        return lookup.get(code);
    }

    private TimeInForce(String code) {
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



