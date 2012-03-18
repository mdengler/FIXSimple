package com.martindengler.proj.FIXSimple.spec;

import java.util.Map;
import java.util.HashMap;
import java.util.EnumSet;


public enum OrdType {
        FOREX_LIMIT("F"),
        FOREX_MARKET("C"),
        FOREX_PREVIOUSLY_QUOTED("H"),
        FOREX_SWAP("G"),
        FUNARI("I"), // (Limit Day Order with unexecuted portion handled as Market On Close. e.g. Japan)
        LIMIT("2"),
        LIMIT_ON_CLOSE("B"),
        LIMIT_OR_BETTER("7"),
        LIMIT_WITH_OR_WITHOUT("8"),
        MARKET("1"),
        MARKET_ON_CLOSE("5"),
        ON_BASIS("9"),
        ON_CLOSE("A"),
        PEGGED("P"),
        PREVIOUSLY_INDICATED("E"),
        PREVIOUSLY_QUOTED("D"),
        STOP("3"),
        STOP_LIMIT("4"),
        WITH_OR_WITHOUT("6"),

        ;

    private static final Map<String, OrdType> lookup
        = new HashMap<String, OrdType>();

    static {
        for(OrdType t : EnumSet.allOf(OrdType.class))
            lookup.put(t.getCode(), t);
    }

    private String code;

    public static OrdType fromCode(String code) {
        return lookup.get(code);
    }

    private OrdType(String code) {
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



