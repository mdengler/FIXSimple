package com.martindengler.proj.FIXSimple.spec;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public enum IDSource {
        CTA_SYMBOL("9"), // Consolidated Tape Association (CTA) Symbol (SIAC CTS/CQS line format)
        CUSIP("1"),
        EXCHANGE_SYMBOL("8"),
        ISIN("4"),
        ISO_COUNTRY_CODE("7"),
        ISO_CURRENCY_CODE("6"),
        QUIK("3"),
        RIC("5"),
        SEDOL("2"),

            // FUTURE: handle 100+, reserved for private security identifications

        ;

    private static final Map<String, IDSource> lookup
        = new HashMap<String, IDSource>();

    static {
        for(IDSource t : EnumSet.allOf(IDSource.class))
            lookup.put(t.getCode(), t);
    }

    private String code;

    public static IDSource fromCode(String code) {
        return lookup.get(code);
    }

    private IDSource(String code) {
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



