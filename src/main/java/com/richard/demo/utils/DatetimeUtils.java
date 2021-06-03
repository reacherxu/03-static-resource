package com.richard.demo.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.util.Assert;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/6/3 4:46 PM richard.xu Exp $
 */
public class DatetimeUtils {

    /**
     * 将Long类型的时间戳转换成String 类型的时间格式，时间格式为：yyyy-MM-dd HH:mm:ss
     */
    public static String timeToString(Long time) {
        Assert.notNull(time, "time is null");
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
    }


    public static void main(String[] args) {
        Long time = 1622711319116L;
        System.out.println(timeToString(time));
    }
}
