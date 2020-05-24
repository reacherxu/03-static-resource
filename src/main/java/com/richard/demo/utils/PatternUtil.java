/**
 * SAP Inc.
 * Copyright (c) 1972-2018 All Rights Reserved.
 */
package com.richard.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;

/**
 * Reference: https://www.cnblogs.com/SQP51312/p/6134324.html
 * API: http://tool.oschina.net/apidocs/apidoc?api=jdk_7u4
 * 
 * @author richard.xu03@sap.com
 * @version $Id: PatternUtil.java, v 0.1 May 16, 2018 4:22:08 PM richard.xu Exp $
 */
public class PatternUtil {

    /**
     * Two implementations
     * 1.Pattern.matches is a static method
     * 2.matcher.matches
     * 
     * What's the difference between String.matches and Matcher.matches?
     * A Matcher is created on on a precompiled regexp, 
     * while String.matches must recompile the regexp every time it executes, 
     * so it becomes more wasteful the more often you run that line of code.
     */
    @Test
    public void matches() {
        // 1.matches 只有完全匹配时才会返回true
        Pattern p = Pattern.compile("(\\w+)%(\\d+)");
        Matcher m = p.matcher("%ab%12");
        System.out.println(m.matches());// false
        m = p.matcher("ab%12%");
        System.out.println(m.matches());// false
        m = p.matcher("ab%12");
        System.out.println(m.matches());// true

        // 2. lookingAt 从目标字符串开始位置进行匹配,必须开始就匹配成功
        Pattern p1 = Pattern.compile("(\\w+)%(\\d+)");
        Matcher m1 = p.matcher("ab%12-cd%34");
        System.out.println(m.lookingAt());// true
        m1 = p1.matcher("%ab%12-cd%34");
        System.out.println(m1.lookingAt());// false

    }

    /**
     * 
     */
    @Test
    public void test() {
        String regex = "\\['[a-zA-Z0-9\\.]*'\\]";
        String srcA = "['Exp.aa']*fdsfdsf['bb']";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(srcA);

        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

    /**
     * reset method
     * start method
     * end   method
     * 
     * 其实group(i),start(i),end(i)所带的参数i就是正则表达式中的子表达式索引（第几个子表达式）
     * 将“组”的概念与“子表达式”对应起来之后，理解matcher的group,start,end就完全没有障碍了
     */
    @Test
    public void testReset() {
        Pattern p = Pattern.compile("(\\w+)%(\\d+)");
        Matcher m = p.matcher("ab%12-cd%345+");
        if (m.find()) {
            System.out.println("开始索引：" + m.start());// 开始索引：0
            System.out.println("group():" + m.group());// group():ab%12
            System.out.println("结束索引：" + m.end());// 结束索引：5
        }
        //        m.reset();
        if (m.find()) {
            System.out.println("开始索引：" + m.start());// 开始索引：0
            System.out.println("group():" + m.group());// group():ab%12
            System.out.println("结束索引：" + m.end());// 结束索引12
        }
    }

    /**
     * <b>group是针对（）来说的，group（0）就是指的整个串，group（1） 指的是第一个括号里的东西，group（2）指的第二个括号里的东西<\b>
     */
    @Test
    public void testGroupCount() {
        Pattern p = Pattern.compile("(\\w+)%(\\d+)/([0-9])");
        Matcher m = p.matcher("ab%12/1-cd%34/2-cd%4/3");

        System.out.println("groupCount :" + m.groupCount());

        while (m.find()) {
            System.out.print("group 1 :" + m.group(1) + "\t");
            System.out.print("group 2 :" + m.group(2) + "\t");
            System.out.println("group 3 :" + m.group(3));
        }
    }

    /**
     * appendReplacement: 将当前匹配子串替换为指定字符串
     * 注意：对于最后一次匹配，其后的字符串并没有添加入StringBuffer对象中，若需要这部分的内容需要使用appendTail方法。
     */
    @Test
    public void testAppend() {
        Pattern p = Pattern.compile("(\\w+)%(\\d+)");
        Matcher m = p.matcher("前ab%12中cd%34后");
        StringBuffer s = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(s, "app");
        }
        System.out.println(s);// 前app中app
        m.appendTail(s);
        System.out.println(s);// 前app中app后
    }
}
