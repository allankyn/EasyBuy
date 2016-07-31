package com.akproject.easybuy.utility;

import com.akproject.easybuy.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Allan on 16/1/2016.
 */
public class DateManager {

    private final static String DAY_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static String DAY_FORMAT = "yyyy-MM-dd";
    private final static String TIMESTAMP_FORMAT = "yyyyMMddHHmmssSSS";

    public static String displayCurrentDateTime() {
        return displayCurrentDateTime(DAY_TIME_FORMAT);
    }

    public static String displayDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(DAY_FORMAT);
        return dateFormat.format(date);
    }

    public static String displayCurrentTimestamp() {
        DateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String displayCurrentDateTime(String timeFormat) {
        DateFormat dateFormat = new SimpleDateFormat(timeFormat);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getRandomTimestamp() {
        int offset = 1;
        int end = 9999;
        int rand = offset + (int)(Math.random() * (end - offset + 1));

        return displayCurrentDateTime("yyyyMMddHHmmss") + rand;
    }

    public static String displayDateTime(int year, int month, int day) {
        String yearStr = Integer.toString(year);
        String monthStr = "00" + Integer.toString(month);
        monthStr = monthStr.substring(monthStr.length() - 2);
        String dayStr = "00" + Integer.toString(day);
        dayStr = dayStr.substring(dayStr.length() - 2);

        return yearStr + "-" + monthStr + "-" + dayStr;
    }

    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getDateFromString(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(DAY_FORMAT);
        try {
            Date date = formatter.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Calendar getCurrentDateCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static Calendar getCalendarDate(int year, int month, int day) {
        String dateStr = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
        return getCalendarDateFromString(dateStr);
    }

    public static Calendar getCalendarDateFromString(String dateStr) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateFromString(dateStr));
        return cal;
    }

    public static int getNumberOfDay(int year, int month) {
        Calendar cal = new GregorianCalendar(year, month - 1, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

}
