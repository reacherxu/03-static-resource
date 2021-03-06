package com.richard.demo.utils;

import java.util.Date;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.junit.Test;

/**
 * 包装临时对象
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/4/15 10:46 AM richard.xu Exp $
 */
public class Wrapper {
    /**
     * 当一个方法需要返回两个及以上字段时，我们一般会封装成一个临时对象返回，现在有了Pair和Triple就不需要了
     */
    @Test
    public void testReturn2TempResponse() {
        // 返回两个字段
        ImmutablePair<Integer, String> pair = ImmutablePair.of(1, "yideng");
        // 输出 1,yideng
        System.out.println(pair.getLeft() + "," + pair.getRight());

        // 返回三个字段
        ImmutableTriple<Integer, String, Date> triple = ImmutableTriple.of(1, "yideng", new Date());
        // 输出 1,yideng,Wed Apr 07 23:30:00 CST 2021
        System.out.println(triple.getLeft() + "," + triple.getMiddle() + "," + triple.getRight());
    }
}
