package com.richard.demo.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.MutablePair;
import org.junit.Test;

import com.google.gson.Gson;

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
        ImmutablePair<Integer, String> pair2 = ImmutablePair.of(2, "guojing");

        List<ImmutablePair<Integer, String>> pairs = new ArrayList<>();
        pairs.add(pair);
        pairs.add(pair2);

        // 输出 1,yideng
        Gson gson = new Gson();
        System.out.println(gson.toJson(pairs));

        // 返回三个字段
        ImmutableTriple<Integer, String, Date> triple = ImmutableTriple.of(1, "yideng", new Date());
        // 输出 1,yideng,Wed Apr 07 23:30:00 CST 2021
        System.out.println(triple.getLeft() + "," + triple.getMiddle() + "," + triple.getRight());

    }

    @Test
    public void testReturnTempResponse() {

        MutablePair<Integer, String> pair1 = MutablePair.of(1, "yideng");
        System.out.println(pair1.getLeft() + "," + "," + pair1.getRight());
        pair1.setLeft(2);
        pair1.setRight("yideng2");
        System.out.println(pair1.getLeft() + "," + "," + pair1.getRight());
    }
}
