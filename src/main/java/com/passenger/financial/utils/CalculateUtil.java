package com.passenger.financial.utils;

import java.math.BigDecimal;

public class CalculateUtil {


    /**
     * 计算两个数相减
     * @param income 被减数
     * @param spending 减数
     * @return 差
     */
    public static String sub(String income, String spending) {
        return String.valueOf(new BigDecimal(income).subtract(new BigDecimal(spending)));
    }

    public static String add(String d1, String d2) {
        return String.valueOf(new BigDecimal(d1).add(new BigDecimal(d2)));
    }

    public static String div(String d1, String d2) {
        return  String.valueOf(new BigDecimal(d1).divide(new BigDecimal(d2),2,BigDecimal.ROUND_DOWN));
    }

    public static void main(String[] args) {
        System.out.println(div("960.35", "3.5"));
    }
}
