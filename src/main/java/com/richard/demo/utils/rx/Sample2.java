/**
 * SAP Inc.
 * Copyright (c) 1972-2020 All Rights Reserved.
 */
package com.richard.demo.utils.rx;

import java.util.List;
import org.junit.Test;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 *
 * @author richard.xu03@sap.com
 * @version $Id: Sample2.java, v 0.1 Oct 20, 2020 11:20:52 AM richard.xu Exp $
 */
@Slf4j
public class Sample2 {

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        // 创建的Observable and subscriber 的3种方式
        test01();

        test02();
        
        test03();
    }

    /**
     * 链式操作的应用
     */
    @Test
    public void testChainedOperation() {
        Observable.just(1, 2, 3, 4, 5, 6).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer value) {
                return value % 2 == 1;
            }
        }).map(new Func1<Integer, Double>() {
            @Override
            public Double call(Integer value) {
                return Math.sqrt(value);
            }
        }).subscribe(new Subscriber<Double>() { // notice Subscriber type changed to
            @Override
            public void onCompleted() {
                System.out.println("Complete!");
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(Double value) {
                System.out.println("onNext: " + value);
            }
        });

        List<Double> list1 = Observable.from(new Integer[] {1, 2, 3, 4, 5, 6}).filter(num -> num % 2 == 1).map(num -> Math.sqrt(num))
                .toList().toBlocking()
                .single();
        System.out.println(list1);

        // stream 自定义函数的练习
        List<Double> list2 = Observable.from(new Integer[] {1, 2, 3, 4, 5, 6}).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer num) {
                return num % 2 == 1;
            }
        }).map(new Func1<Integer, Double>() {
            @Override
            public Double call(Integer num) {
                return Math.sqrt(num);
            }
        }).toList().toBlocking().single();
        System.out.println(list2);
    }

    /**
     * 
     */
    private static void test03() {
        Integer[] array = new Integer[] {1, 2, 3};
        Observable.from(array).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("Complete!");
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(Integer value) {
                System.out.println("onNext: " + value);
            }
        });
        System.out.println("==================method 3======================");
    }

    /**
     * 
     */
    private static void test02() {
        Observable.just(1, 2, 3).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("Complete!");
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(Integer value) {
                System.out.println("onNext: " + value);
            }
        });
        System.out.println("==================method 2======================");

    }

    /**
     * 一个 Observable 可以有多个 Subscribers，并且通过 Observable 发出的每一个 item，
     * 该 item 将会被发送到 Subscriber.onNext()方法来进行处理。
     * 一旦 Observable 不再发出 items，它将会调用 Subscriber.onCompleted() 方法，或如果有一个出错的话 Observable 会调用
     * Subscriber.onError() 方法。
     */
    private static void test01() {
        // Observable 发出了整数 1，2，3 然后结束了
        Observable integerObservable = Observable.create(new Observable.OnSubscribe<Subscriber>() {

            @Override
            public void call(Subscriber subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onCompleted();
            }
        });

        // 创建一个 Subscriber，那样我们就能让这些发出的流起作用
        Subscriber<Integer> integerSubscriber = new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("Complete!");
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(Integer value) {
                System.out.println("onNext: " + value);
            }
        };

        // 可以通过 Observable.subscribe() 方法将他们联系起来
        integerObservable.subscribe(integerSubscriber);
        System.out.println("==================method 1======================");

    }
}
