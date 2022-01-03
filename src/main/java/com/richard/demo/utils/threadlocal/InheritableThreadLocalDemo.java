package com.richard.demo.utils.threadlocal;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/7/15 3:48 PM richard.xu Exp $
 */
public class InheritableThreadLocalDemo {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    private static InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        testThreadLocal();

        testInheritableThreadLocal();

        // 通过Callable和FutureTask创建线程
        System.out.println("[Main]Main thread is " + Thread.currentThread().getName());
        testCallable();
        System.out.println("[Main] end");
    }



    /**
     * 如果创建了新线程，inheriable thread local 的值 可以 向子线程传递
     *
     * 需要注意的是
     * 一旦子线程被创建以后，再操作父线程中的ThreadLocal变量，那么子线程是不能感知的。
     * 因为父线程和子线程还是拥有各自的ThreadLocalMap,只是在创建子线程的“一刹那”将父线程的ThreadLocalMap复制给子线程，
     * 后续两者就没啥关系了。
     */
    private static void testInheritableThreadLocal() {
        inheritableThreadLocal.set("mainThread");
        System.out.println("[testInheritableThreadLocal]value:" + inheritableThreadLocal.get());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String value = inheritableThreadLocal.get();
                System.out.println("[testInheritableThreadLocal]value:" + value);
            }
        });
        thread.start();
    }

    /**
     * 如果创建了新线程，thread local 的值不能向子线程传递
     */
    private static void testThreadLocal() {
        threadLocal.set("mainThread");
        System.out.println("[testThreadLocal]value:" + threadLocal.get());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String value = threadLocal.get();
                System.out.println("[testThreadLocal]value:" + value);
            }
        });
        thread.start();
    }


    /**
     * 通过Callable和FutureTask创建线程
     *
     * a:创建Callable接口的实现类 ，并实现Call方法
     *
     * b:创建Callable实现类的实现，使用FutureTask类包装Callable对象，该FutureTask对象封装了Callable对象的Call方法的返回值
     *
     * c:使用FutureTask对象作为Thread对象的target创建并启动线程
     *
     * d:调用FutureTask对象的get()来获取子线程执行结束的返回值
     * 
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void testCallable() throws ExecutionException, InterruptedException {
        Callable<String> oneCallable = new Tickets<>();
        FutureTask<String> oneTask = new FutureTask<>(oneCallable);
        Thread t = new Thread(oneTask);
        System.out.println("[testCallable]" + Thread.currentThread().getName());
        t.start();
        System.out.println("[testCallable]" + oneTask.get().toString());

    }

}


class Tickets<String> implements Callable<String> {

    // 重写call方法
    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName() + "-->我是通过实现Callable接口通过FutureTask包装器来实现的线程");
        return (String) "tst01";
    }
}
