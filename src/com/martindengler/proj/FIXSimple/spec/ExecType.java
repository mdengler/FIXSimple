package com.martindengler.proj.FIXSimple.spec;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public enum ExecType {
        CALCULATED("B"),
        CANCELED("4"),
        DONE_FOR_DAY("3"),
        EXPIRED("C"),
        FILL("2"),
        NEW("0"),
        PARTIAL_FILL("1"),
        PENDING_CANCEL("6"),
        PENDING_NEW("A"),
        PENDING_REPLACE("E"),
        REJECTED("8"),
        REPLACE("5"),
        RESTATED("D"),
        STOPPED("7"),
        SUSPENDED("9"),

        ;

    private static final Map<String, ExecType> lookup
        = new HashMap<String, ExecType>();

    static {
        for(ExecType t : EnumSet.allOf(ExecType.class))
            lookup.put(t.getCode(), t);
    }

    private String code;

    public static ExecType fromCode(String code) {
        return lookup.get(code);
    }

    private ExecType(String code) {
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



