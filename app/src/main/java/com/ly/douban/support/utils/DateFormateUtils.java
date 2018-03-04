package com.ly.douban.support.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期格式化工具类
 *
 * @author xuding
 */
public class DateFormateUtils {

    public static final SimpleDateFormat STANDED_DF = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat STANDED_DF_MILI = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss:SSS");

    public static final SimpleDateFormat STANDED_DF_MONTH = new SimpleDateFormat("yyyy-MM");

    public static final SimpleDateFormat STANDED_DF_DATE = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat STANDED_DF_TIME = new SimpleDateFormat("HH:mm:ss");

    public static final SimpleDateFormat SIMPLE_DF_DATE = new SimpleDateFormat("yyyy-M-d");

    public static final SimpleDateFormat STANDED_DF_DATE_CHINAESE = new SimpleDateFormat("yyyy年MM月dd日");

    public static final SimpleDateFormat STANDED_DF_DATE_CHINAESE_WEEK = new SimpleDateFormat("yyyy年MM月dd日 E");

    public static final SimpleDateFormat STANDED_DF_DATE_CHINAESE_HOUR = new SimpleDateFormat("yyyy年MM月dd日HH时");

    /**
     * 转换为标准格式
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return STANDED_DF.format(date);
    }

    /**
     * 转换为标准格式
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return STANDED_DF_DATE.format(date);
    }

    /**
     * 转换为标准格式
     *
     * @param date
     * @return
     */
    public static String formatDateZn(Date date) {
        return STANDED_DF_DATE_CHINAESE.format(date);
    }

    /**
     * 转换为 yyyy-MM
     *
     * @param date
     * @return
     */
    public static String formatMonth(Date date) {
        return STANDED_DF_MONTH.format(date);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 解析日期 yyyy-MM-dd
     *
     * @param dateString
     * @return
     */
    public static Date parseDate(String dateString) {
        try {
            return STANDED_DF_DATE.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDate(long time) {
        String d = STANDED_DF.format(time);
        try {
            return STANDED_DF.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getWeek(Date date){
        String[] weeks = {"周日","周一","周二","周三","周四","周五","周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(week_index<0){
            week_index = 0;
        }
        return weeks[week_index];
    }
}
