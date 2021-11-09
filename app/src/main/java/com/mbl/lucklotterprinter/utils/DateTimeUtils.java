package com.mbl.lucklotterprinter.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
    public static final String DEFAULT_DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String SIMPLE_DATE_FORMAT = "dd/MM/yyyy";
    public static final String SIMPLE_TIME_FORMAT = "HH:mm:ss";

    public static Date convertStringToDate(String dateStr, String format) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            date = formatter.parse(dateStr);
        } catch (Exception e) {
        }
        return date;
    }

    public static Date convertStringToDateDefault(String dateStr) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
        try {
            date = formatter.parse(dateStr);
        } catch (Exception e) {

        }
        return date;
    }

    public static String convertDateToString(Date date, String format) {
        String dateStr = "";
        SimpleDateFormat formatter;
        if (TextUtils.isEmpty(format))
            formatter = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
        else
            formatter = new SimpleDateFormat(format);
        try {
            dateStr = formatter.format(date);
        } catch (Exception e) {

        }
        return dateStr;
    }

    public static String getCurrentDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        Date date = new Date();
        return formatter.format(date);
    }

    public static int compareToDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
        return sdf.format(date1).compareTo(sdf.format(date2));
    }
}
