package com.passenger.financial.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static String getDateStr(Date date,String partten){
        SimpleDateFormat sdf = new SimpleDateFormat(partten);
        return sdf.format(date);
    }
}
