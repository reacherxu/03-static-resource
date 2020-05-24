/**
 * SAP Inc.
 * Copyright (c) 1972-2019 All Rights Reserved.
 */
package com.richard.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author richard.xu03@sap.com
 * @version $Id: SimpleDateFormatTest.java, v 0.1 Sep 22, 2019 10:21:34 PM richard.xu Exp $
 */
@Slf4j
public class SimpleDateFormatTest {

    /**
     * String format(Date date) 将Date格式化为日期/时间字符串
     */
    @Test
    public void formatDate() {
        Date date = new Date();
        System.out.printf("时间的一般输出格式 %s \n", date.getTime());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = sdf.format(date.getTime());
        System.out.printf("把时间戳经过处理得到期望格式的时间 %s \n", dateString);

        java.sql.Date date2 = new java.sql.Date(System.currentTimeMillis());
        System.out.printf("当前数据库时间是 %s \n", date2);
        String dateString2 = sdf.format(date.getTime());
        System.out.printf("把时间戳经过处理得到期望格式的时间 %s \n", dateString2);

    }

    /**
     * Date parse(String source) 将符合格式的指定字符串转换为Date
     * Note : 格式不对就parse 不了
     */
    @Test
    public void formatString() {

        Date date = null;
        try {
            // 2019-09-19T09:38:32.725+0000 - > yyyy-MM-dd'T'HH:mm:ss
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            date = sdf.parse("2019-09-19T09:38:32.725+0000");
            System.out.println(date);

            // 2018-08-24 12:50:20:545 - > yyyy-MM-dd HH:mm:ss
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sdf.parse("2018-08-24 12:50:20:545");
            System.out.println(date);

            // 2018-08-24 - > yyyy-MM-dd
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse("2018-08-24");
            System.out.println(date);
        } catch (Exception e) {
            log.error("parse date error");
        }
    }

}
