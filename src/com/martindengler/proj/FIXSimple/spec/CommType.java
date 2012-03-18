package com.martindengler.proj.FIXSimple.spec;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public enum CommType {
        ABSOLUTE("2"),
        PERCENTAGE("2"),
        PER_SHARE("1"),
        ;

    private static final Map<String, CommType> lookup
        = new HashMap<String, CommType>();

    static {
        for(CommType t : EnumSet.allOf(CommType.class))
            lookup.put(t.getCode(), t);
    }

    private String code;

    public static CommType fromCode(String code) {
        return lookup.get(code);
    }

    private CommType(String code) {
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



