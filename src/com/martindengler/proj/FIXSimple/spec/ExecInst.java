package com.martindengler.proj.FIXSimple.spec;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public enum ExecInst {
        ALL_OR_NONE("G"),
        CALL_FIRST("C"),
        CROSS_FORBIDDEN("A"),
        CROSS_OK("B"),
        CUSTOMER_DISPLAY_INSTRUCTION("U"),
        DO_NOT_INCREASE("E"),
        DO_NOT_REDUCE("F"),
        GO_ALONG("3"),
        HELD("5"),
        INSTITUTIONS_ONLY("I"),
        NETTING("V"),
        NON_NEGOTIABLE("N"),
        NOT_HELD("1"),
        OVER_THE_DAY("4"),
        PARTICIPATE_DONT_INITIATE("6"),
        PEG_LAST("L"),
        PEG_LOCAL_BEST_AT_ORDER_TIME("T"),
        PEG_MARKET("P"),
        PEG_MID("M"),
        PEG_OPEN("O"),
        PEG_PRIMARY("R"),
        PEG_VWAP("W"),
        PERCENT_OF_VOLUME("D"),
        SCALE_ATTEMPT("8"),
        SCALE_STRICT("7"),
        STAY_BIDSIDE("9"),
        STAY_OFFERSIDE("0"),
        SUSPEND("S"),
        WORK("2"),
        ;

    private static final Map<String, ExecInst> lookup
        = new HashMap<String, ExecInst>();

    static {
        for(ExecInst t : EnumSet.allOf(ExecInst.class))
            lookup.put(t.getCode(), t);
    }

    private String code;

    public static ExecInst fromCode(String code) {
        return lookup.get(code);
    }

    private ExecInst(String code) {
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



