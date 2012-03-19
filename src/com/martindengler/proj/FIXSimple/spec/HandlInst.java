/*
 * Copyright (C) 2012 Martin Dengler <martin@martindengler.com>
 *
 * Licensed under the GNU GPL v3+; see LICENSE file
 *
 */

package com.martindengler.proj.FIXSimple.spec;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public enum HandlInst {
    // comments are from the FIX spec and sometimes unhelpfully
    // cryptic
        AUTOMATED_PRIVATE("1"),
        AUTOMATED_PUBLIC("2"),
        MANUAL("3"),

        ;

    private static final Map<String, HandlInst> lookup
        = new HashMap<String, HandlInst>();

    static {
        for(HandlInst t : EnumSet.allOf(HandlInst.class))
            lookup.put(t.getCode(), t);
    }

    private String code;

    public static HandlInst fromCode(String code) {
        return lookup.get(code);
    }

    private HandlInst(String code) {
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



