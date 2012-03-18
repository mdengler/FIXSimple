package com.martindengler.proj.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class UTCCalendar {
    private UTCCalendar() {}

    private static TimeZone UTCTZ = java.util.TimeZone.getTimeZone("UTC");
    private static Locale ROOT = java.util.Locale.ROOT;

    public static Calendar getInstance() {
        return new GregorianCalendar(UTCTZ, ROOT);
    }

}