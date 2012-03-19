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


public enum OrderCapacity {
    // comments are from the FIX spec and sometimes unhelpfully
    // cryptic
        AGENCY_SINGLE_ORDER("A"),
        ALL_OTHER_ORDERS_AS_AGENT_FOR_OTHER_MEMBER("W"),
        COMPETING_DEALER_TRADES_O("O"),
        COMPETING_DEALER_TRADES_R("R"),
        COMPETING_DEALER_TRADES_T("T"),
        INDIVIDUAL_INVESTOR_SINGLE_ORDER("I"),
        PRINCIPAL("P"),
        PROGRAM_ORDER_INDEX_ARB_FOR_INDIVIDUAL_CUSTOMER("J"),
        PROGRAM_ORDER_INDEX_ARB_FOR_MEMBER_("D"),
        PROGRAM_ORDER_INDEX_ARB_FOR_OTHER_AGENCY("U"),
        PROGRAM_ORDER_INDEX_ARB_FOR_OTHER_MEMBER("M"),
        PROGRAM_ORDER_NON_INDEX_ARB_FOR_INDIVIDUAL_CUSTOMER("K"),
        PROGRAM_ORDER_NON_INDEX_ARB_FOR_MEMBER("C"),
        PROGRAM_ORDER_NON_INDEX_ARB_FOR_OTHER_AGENCY("Y"),
        PROGRAM_ORDER_NON_INDEX_ARB_FOR_OTHER_MEMBER("N"),
        REGISTERED_EQUITY_MARKET_MAKER_TRADES("E"),
        SHORT_EXEMPT_TRANSACTION_B("B"), // see also A type
        SHORT_EXEMPT_TRANSACTION_F("F"), // see also W type
        SHORT_EXEMPT_TRANSACTION_H("H"), // see also I type
        SHORT_EXEMPT_TRANSACTION_MEMBER_AFFILIATED("L"), // for member competing market-maker affiliated with the firm clearing the trade (refer to P and O types)
        SHORT_EXEMPT_TRANSACTION_MEMBER_UNAFFILIATED("X"), // for member competing market-maker not affiliated with the firm clearing the trade (refer to W and T types)
        SHORT_EXEMPT_TRANSACTION_NONMEMBER("Z"), // see also A and R types
        SPECIALIST_TRADES("S"),

        ;

    private static final Map<String, OrderCapacity> lookup
        = new HashMap<String, OrderCapacity>();

    static {
        for(OrderCapacity t : EnumSet.allOf(OrderCapacity.class))
            lookup.put(t.getCode(), t);
    }

    private String code;

    public static OrderCapacity fromCode(String code) {
        return lookup.get(code);
    }

    private OrderCapacity(String code) {
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



