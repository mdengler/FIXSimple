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


public enum ExecTransType {
        CANCEL("1"),
        CORRECT("2"),
        NEW("0"),
        STATUS("3"),
        ;

    private static final Map<String, ExecTransType> lookup
        = new HashMap<String, ExecTransType>();

    static {
        for(ExecTransType t : EnumSet.allOf(ExecTransType.class))
            lookup.put(t.getCode(), t);
    }

    private String code;

    public static ExecTransType fromCode(String code) {
        return lookup.get(code);
    }

    private ExecTransType(String code) {
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



