package com.martindengler.proj.FIXSimple.spec;

import java.util.Map;
import java.util.HashMap;
import java.util.EnumSet;


public enum SettlementType {
    // comments are from the FIX spec and sometimes unhelpfully
    // cryptic
        CASH("1"),
        FUTURE("6"),
        NEXT_DAY("2"),
        REGULAR("0"),
        SELLERS_OPTION("8"),
        T_PLUS_2("3"),
        T_PLUS_3("4"),
        T_PLUS_4("5"),
        T_PLUS_5("9"),
        WHEN_ISSUED("7"),

        ;

    private static final Map<String, SettlementType> lookup
        = new HashMap<String, SettlementType>();

    static {
        for(SettlementType t : EnumSet.allOf(SettlementType.class))
            lookup.put(t.getCode(), t);
    }

    private String code;

    public static SettlementType fromCode(String code) {
        return lookup.get(code);
    }

    private SettlementType(String code) {
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



