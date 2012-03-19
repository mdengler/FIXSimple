/*
 * Copyright (C) 2012 Martin Dengler <martin@martindengler.com>
 *
 * Licensed under the GNU GPL v3+; see LICENSE file
 *
 */

package com.martindengler.proj.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <code>UTCCalendar</code> is a helper to avoid relying on the user-
 * and configuration-specific Calendar defaults, since FIX message
 * timestamps are always in UTC.  UTCCalendar is a drop-in replacement
 * for Calendar.getInstance().
 *
 * @author  Martin Dengler
 */
public class UTCCalendar {
    private UTCCalendar() {}

    private static TimeZone UTCTZ = java.util.TimeZone.getTimeZone("UTC");
    private static Locale ROOT = java.util.Locale.ROOT;

    public static Calendar getInstance() {
        return new GregorianCalendar(UTCTZ, ROOT);
    }

}