package com.passenger.financial.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static String getDateStr(Date date,String patten){
        SimpleDateFormat sdf = new SimpleDateFormat(patten);
        return sdf.format(date);
    }

    public static Date getDate(String startTime, String patten) throws ParseException {
        return new SimpleDateFormat(patten).parse(startTime);
    }

    public static int betweenDays(Date startDate, Date endDate) {
        int days = (int) ((endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000));
        return days;
    }
}
