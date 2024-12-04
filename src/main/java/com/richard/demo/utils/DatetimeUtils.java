package com.richard.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/6/3 4:46 PM richard.xu Exp $
 */
@Slf4j
public class DatetimeUtils {

    /**
     * 将Long类型的时间戳转换成String 类型的时间格式，时间格式为：yyyy-MM-dd HH:mm:ss
     */
    @Test
    public void timeToString() {
        // "arrivalTimestamp": "/Date(1637064158000)/",
        Long time = System.currentTimeMillis();
        Assert.notNull(time, "time is null");
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String result = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
        log.info(result);
    }

    @Test
    public void test() {
        String fromVersion = "2102";
        String toVersion = "2108";

        List<String> versions = Lists.newArrayList("2011", "2102", "2105", "2108");
        log.info("fromVersion index is {}, toVersion index is {} ", versions.indexOf(fromVersion), versions.indexOf(toVersion));
        List<String> subVersions = versions.subList(versions.indexOf(fromVersion), versions.indexOf(toVersion) + 1);
        log.info(subVersions.toString());

        log.info("result is {}", "2024-11-14 11:13:23".compareTo("2024-11-15 09:13:23"));

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置格式
        Calendar calendar = Calendar.getInstance(); // 创建Calendar 的实例
        calendar.setTime(date);
    }

    /**
     * 日期进行加减
     */
    @Test
    public void addDate() {
        Date date = new Date();// 获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// 设置格式

        Calendar calendar = Calendar.getInstance(); // 创建Calendar 的实例
        calendar.setTime(date);
        System.out.println(simpleDateFormat.format(calendar.getTime()));

        calendar.add(Calendar.DAY_OF_MONTH, 28); // 当前时间减去一天，即一天前的时间
        System.out.println(simpleDateFormat.format(calendar.getTime()));

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MONTH, -3);// 当前时间减去一个月，即一个月前的时间
        System.out.println(simpleDateFormat.format(calendar2.getTime()));

        Calendar calendar3 = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);// 当前时间减去一年，即一年前的时间
        System.out.println(simpleDateFormat.format(calendar3.getTime()));

        System.out.println(calendar.getTimeInMillis());// 返回当前时间的毫秒数
    }


    @Test
    public void testDateCompare3() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse("2009-12-31");
        Date date2 = sdf.parse("2019-01-31");
        System.out.println("date1 : " + sdf.format(date1));
        System.out.println("date2 : " + sdf.format(date2));


        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date3 = sdf1.parse("2024-11-14 11:13:23");
        Date date4 = sdf1.parse("2024-11-15 09:13:23");
        System.out.println("date3 : " + sdf1.format(date3));
        System.out.println("date4 : " + sdf1.format(date4));
        System.out.println("result : " + date3.after(date4));

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        if (cal1.after(cal2)) {
            System.out.println("Date1 时间在 Date2 之后");
        }

        if (cal1.before(cal2)) {
            System.out.println("Date1 时间在 Date2 之前");
        }

        if (cal1.equals(cal2)) {
            System.out.println("Date1 时间与 Date2 相等");
        }
    }
}
