package com.richard.demo.utils;

import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * 1. 使用supplyAsync
 * 2. 使用runAsync
 */
public class CompletableFutureTest {

    /**
     * CompletableFuture.supplyAsync()。这个方法需要一个Supplier函数接口，通常用于执行异步计算
     * 
     * 执行结果
     * 小黑在做其他的事情...
     * 正在查询价格
     * 价格是：100元
     */
    @Test
    public void testCompletableFuture_supplyAsync() {
        // 创建一个CompletableFuture实例
        CompletableFuture<String> futurePrice = CompletableFuture.supplyAsync(() -> {
            // 模拟耗时操作，比如调用外部API
            simulateDelay();
            return "100元";
        });

        // 在这里，咱们可以做一些其他的事情，不必等待价格查询的结果
        doSomethingElse();

        // 当结果准备好后，获取它
        String price = futurePrice.join();
        System.out.println("价格是：" + price);
    }

    private void simulateDelay() {
        try {
            System.out.println("正在查询价格");
            Thread.sleep(1000); // 模拟1秒的延迟
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void doSomethingElse() {
        // 做一些其他的事情
        System.out.println("小黑在做其他的事情...");
    }

    /**
     * 如果不关心异步任务的结果，只想执行一个异步操作，那就可以用runAsync。它接受一个Runnable函数接口，不返回任何结果
     */
    @Test
    public void testCompletableFuture_runAsync() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            simulateDelay();
        });
    }

    /**
     * 有时候，咱们可能需要手动完成一个Future。比如，基于某些条件判断，决定是否提前返回结果。这时候可以用complete方法：
     * 如果checkCondition返回true，那么这个Future就会被立即完成，否则它将保持未完成状态。
     */
    @Test
    public void testCompletableFuture_manualComplete() {
        CompletableFuture<String> manualFuture = new CompletableFuture<>();
        double random = Math.random();
        // 在某些条件下手动完成Future
        boolean condition = checkCondition(random);
        if (condition) {
            doSomethingElse();
            manualFuture.complete("手动结果");
        } else {
            System.out.println("else logic...");
        }
        System.out.println("结束" + manualFuture.join());

    }

    private boolean checkCondition(double random) {
        return random > 0.3;
    }

    @Test
    public void thenCombine() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            simulateTask("user");
            return "用户小黑";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            simulateTask("config");
            return "配置信息";
        });

        // 组合两个future，等待它们都完成
        CompletableFuture<String> combinedFuture = future1.thenCombine(future2, (user, config) -> {
            return "处理结果: " + user + "，" + config;
        });
        System.out.println(combinedFuture.join());

    }

    private void simulateTask(String str) {
        try {
            if (StringUtils.equals(str, "user")) {
                Thread.sleep(3000);
                System.out.println("print user info");
            } else if (StringUtils.equals(str, "config")) {
                Thread.sleep(1000);
                System.out.println("print config info");
            } else {
                System.out.println("print nothing");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
