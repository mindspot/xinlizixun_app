package util;

import java.util.Calendar;
import java.util.TimeZone;

import common.repository.bean.OrderTimeBean;

/**
 * Created by hpzhan on 2020/2/25.
 */

public class TimeUtils {
    public static final int DOUBLE_CLICK_TIME = 1000;
    public static OrderTimeBean getDay(int addDay) {
        OrderTimeBean bean = new OrderTimeBean();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        if (addDay != 0)
            c.add(Calendar.DAY_OF_MONTH, addDay);
        bean.setYear(String.valueOf(c.get(Calendar.YEAR))); // 获取当前年份
        bean.setMonth(String.valueOf(c.get(Calendar.MONTH) + 1));// 获取当前月份
        bean.setDay(getDayStr(c.get(Calendar.DAY_OF_MONTH)));// 获取当前月份的日期号码
        bean.setWeek(getWeek(c.get(Calendar.DAY_OF_WEEK)));
        return bean;
    }

    public static String getWeek(int week) {
        String weekStr = "";
        switch (week) {
            case 1:
                weekStr = "周日";
                break;
            case 2:
                weekStr = "周一";
                break;
            case 3:
                weekStr = "周二";
                break;
            case 4:
                weekStr = "周三";
                break;
            case 5:
                weekStr = "周四";
                break;
            case 6:
                weekStr = "周五";
                break;
            case 7:
                weekStr = "周六";
                break;
        }
        return weekStr;
    }

    public static String getDayStr(int day) {
        return day > 9 ? day + "" : "0" + day;
    }

    public static String getMonth() {
        OrderTimeBean bean = new OrderTimeBean();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return (c.get(Calendar.MONTH) + 1) + "月";
    }
}
