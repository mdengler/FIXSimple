package com.martindengler.proj.FIXSimple.spec;

import java.util.Map;
import java.util.HashMap;
import java.util.EnumSet;


public enum OrdStatus {
        NEW("0"),
        PARTIALLY_FILLED("1"),
        FILLED("2"),
        DONE_FOR_DAY("3"),
        CANCELED("4"),
        REPLACED("5"),
        PENDING_CANCEL("6"), // (e.g. result of Order Cancel Request)
        STOPPED("7"),
        REJECTED("8"),
        SUSPENDED("9"),
        PENDING_NEW("A"),
        CALCULATED("B"),
        EXPIRED("C"),
        ACCEPTED_FOR_BIDDING("D"),
        PENDING_REPLACE("E"), // (e.g. result of Order Cancel/Replace Request)
        ;

    private static final Map<String, OrdStatus> lookup
        = new HashMap<String, OrdStatus>();

    static {
        for(OrdStatus t : EnumSet.allOf(OrdStatus.class))
            lookup.put(t.getCode(), t);
    }

    private String code;

    public static OrdStatus fromCode(String code) {
        return lookup.get(code);
    }

    private OrdStatus(String code) {
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



