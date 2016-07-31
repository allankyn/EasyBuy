package com.akproject.easybuy.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Allan on 22/1/2016.
 */
public class FormatManager {
    
    final static String DAY_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    
    public static String displayCurrentDateTime() {
        return displayCurrentDateTime(DAY_TIME_FORMAT);
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
    
}
