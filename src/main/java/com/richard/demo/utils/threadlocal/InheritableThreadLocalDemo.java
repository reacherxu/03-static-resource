package com.richard.demo.utils.threadlocal;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/7/15 3:48 PM richard.xu Exp $
 */
public class InheritableThreadLocalDemo {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    private static InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) {
        testThreadLocal();

        testInheritableThreadLocal();
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


}


