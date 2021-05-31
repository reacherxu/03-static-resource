package com.richard.demo.utils.rx;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.SystemUtils;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

/**
 * sample for zip , zip and flatmap
 * @author I503139
 *
 */
@Slf4j
public class SampleZip {
    // private static final Random rand = new Random();

    private static final int DEFAULT_THREADS_NUMBER = 10;
    private static final int KEEP_ALIVE_TIME = 30;
    private static final int WORK_QUEUE_SIZE = 40;

    public ExecutorService executorService() {
        int poolSize = DEFAULT_THREADS_NUMBER;
        try {
            poolSize = Integer.parseInt(
                    SystemUtils.getEnvironmentVariable("DEFAULT_FIXED_THREADPOOL_SIZE", Integer.toString(DEFAULT_THREADS_NUMBER)));
        } catch (NumberFormatException ex) {
            log.warn("'defaultThreadPoolSize' can't be parsed into integer, set to default [{}]", DEFAULT_THREADS_NUMBER);
        }
        return new ThreadPoolExecutor(poolSize, poolSize + 10, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(WORK_QUEUE_SIZE));
    }

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(10);

        Observable<Integer> one = getNumberedObservable(1).subscribeOn(Schedulers.from(executor));
        Observable<Integer> two = getNumberedObservable(2).subscribeOn(Schedulers.from(executor));
        Observable<Integer> three = getNumberedObservable(3).subscribeOn(Schedulers.from(executor));

        // zip 3 request
        Integer result = Observable.zip(one, two, three, new Func3<Integer, Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer arg0, Integer arg1, Integer arg2) {
                System.out.println("Zip0: " + arg0);
                System.out.println("Zip1: " + arg1);
                System.out.println("Zip2: " + arg2);
                return arg0 + arg1 + arg2;
            }
        }).toBlocking().single();
        System.out.println("result: " + result);

        // zip 2 request followed by one request
        Integer result3 = Observable.zip(one, two, new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer arg0, Integer arg1) {
                System.out.println("Zip0: " + arg0);
                System.out.println("Zip1: " + arg1);
                return arg0 + arg1;
            }
        })
                // .flatMap(result12 ->
                // getNumberedObservable(result12).subscribeOn(Schedulers.from(executor)))
                .flatMap(result12 -> getNumberedObservable(result12)).toBlocking().single();
        System.out.println("result3: " + result3);
    }

    public static int intenseCalculation(int i) {
        try {
            System.out.println("Calculating " + i + " on " + Thread.currentThread().getName() + "(" + Thread.currentThread().getId() + ")");
            Thread.sleep(5000);
            return i;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Observable<Integer> getNumberedObservable(final int value) {
        return Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() {
                System.out.println("call calculate value " + value + " in thread id: " + Thread.currentThread().getId());
                return intenseCalculation(value);
            }
        });
    }
}
