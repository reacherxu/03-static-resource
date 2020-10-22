/**
 * SAP Inc.
 * Copyright (c) 1972-2020 All Rights Reserved.
 */
package com.richard.demo.utils.rx;

import java.util.List;
import java.util.concurrent.Executors;
import org.junit.Test;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *
 * @author richard.xu03@sap.com
 * @version $Id: Sample2.java, v 0.1 Oct 20, 2020 11:20:52 AM richard.xu Exp $
 */
@Slf4j
public class Sample2 {

    public static void main(String[] args) {
        // 创建的Observable and subscriber 的3种方式
        // test01();
        // test02();
        // test03();
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
                .toList().toBlocking().single();
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

    @Test
    public void testAction() {
        // 1.先创建 Action* 的对象，这里创建了三个 Action* 对象
        Action1<Integer> onNextAction = new Action1<Integer>() {
            // onNext()
            @Override
            public void call(Integer s) {
                System.out.println(s);
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                // Error handling
                log.info("Error handling");
            }
        };

        Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                log.info("completed");
            }
        };

        // 2. RxJava之后会自动根据重载的subscribe(action*,...)方法参数的个数创建出对应的 subscriber
        Observable integerObservable = Observable.just(1, 2, "3");
        integerObservable.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }

    @Test
    public void test04() {
        System.out.println("Main Thread -> " + Thread.currentThread().getId());

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                System.err.println("Subscriber.onNext Thread -> " + Thread.currentThread().getName());
                subscriber.onNext("message");
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("completed");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println("Subscriber.onNext Thread -> " + Thread.currentThread().getId());
            }
        });
    }
    /**
     * 
     */
    @Test
    public void test03() {
        System.out.println("Main Thread -> " + Thread.currentThread().getId());

        Integer[] array = new Integer[] {1, 2, 3};
        Observable.from(array).subscribeOn(Schedulers.from(Executors.newFixedThreadPool(10))).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("Complete!");
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(Integer value) {
                System.out.println("Thread " + Thread.currentThread().getId() + " onNext: " + value);
            }
        });
        System.out.println("==================method 3======================");
    }

    /**
     * just 本质也是调用from方法，转换成一个observable 对象
     */
    @Test
    public void test02() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).subscribeOn(Schedulers.io()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                System.out.println("Complete!");
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(Object value) {
                System.out.println("Thread " + Thread.currentThread().getId() + " onNext: " + value);
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

        // Observable(可观察对象) 通过subscribe()方法和Subscriber(订阅者)实现了订阅关系(两者建立了联系)。
        integerObservable.subscribe(integerSubscriber);
        System.out.println("==================method 1======================");

    }
}
