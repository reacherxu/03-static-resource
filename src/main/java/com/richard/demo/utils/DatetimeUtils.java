package com.richard.demo.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    public static String timeToString(Long time) {
        Assert.notNull(time, "time is null");
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
    }

    // "arrivalTimestamp": "/Date(1637064158000)/",

    public static void main(String[] args) {
        Long time = System.currentTimeMillis();
        System.out.println(time);
        System.out.println(timeToString(time));

        String fromVersion = "2102";
        String toVersion = "2108";

        List<String> versions = Lists.newArrayList("2011", "2102", "2105", "2108");
        log.info("fromVersion index is {}, toVersion index is {} ", versions.indexOf(fromVersion), versions.indexOf(toVersion));
        List<String> subVersions = versions.subList(versions.indexOf(fromVersion), versions.indexOf(toVersion) + 1);
        log.info(subVersions.toString());
    }
}
