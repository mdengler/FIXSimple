package com.martindengler.proj.FIXSimple.spec;

import java.util.Map;
import java.util.HashMap;
import java.util.EnumSet;


public enum Urgency {
    // comments are from the FIX spec and sometimes unhelpfully
    // cryptic
        BACKGROUND("2"),
        FLASH("1"),
        NORMAL("0"),
        ;

    private static final Map<String, Urgency> lookup
        = new HashMap<String, Urgency>();

    static {
        for(Urgency t : EnumSet.allOf(Urgency.class))
            lookup.put(t.getCode(), t);
    }

    private String code;

    public static Urgency fromCode(String code) {
        return lookup.get(code);
    }

    private Urgency(String code) {
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



