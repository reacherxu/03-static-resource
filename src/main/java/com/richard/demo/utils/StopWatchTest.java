package com.richard.demo.utils;

import java.util.stream.IntStream;

import org.junit.Test;
import org.springframework.util.StopWatch;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/4/29 4:17 PM richard.xu Exp $
 */
public class StopWatchTest {

    @Test
    public void testStopWatch() throws Exception {
        // 创建统计任务
        StopWatch stopWatch = new StopWatch("Main方法耗时");

        for (int i = 0; i < 10; i++) {
            // 开始计时
            stopWatch.start();
            Thread.sleep(1000);
            // 结束计时
            stopWatch.stop();
        }
        // 打印统计结果
        System.out.println(stopWatch.prettyPrint());
    }

    public static int calculate(int a, int b) {
        System.out.println( a * b);
        return a * b;
    }

    @Test
    public void batchStopWatch() {
        // 创建统计任务，并指定ID
        StopWatch stopWatch = new StopWatch("Main方法耗时");
        IntStream.rangeClosed(1, 10).forEach(index -> {
            // 开始计时，并指定任务名称，便于展示
            stopWatch.start("task" + index);
            // 睡眠100ms，模拟任务耗时
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
            }
            // 结束计时
            stopWatch.stop();
        });
        // 打印统计结果
        System.out.println(stopWatch.prettyPrint());
    }
}
