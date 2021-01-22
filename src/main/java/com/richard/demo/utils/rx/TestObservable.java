package com.richard.demo.utils.rx;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import org.junit.Test;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.schedulers.Schedulers;

@Slf4j
public class TestObservable {

    @Test
    public void testIOScheduler() {
        List<Integer> list = Lists.newArrayList(1,2,3);
        Observable.from(list).flatMap(num -> Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                if (num % 2 == 0) {
                    log.info("thread is {},sqrt is {}", Thread.currentThread().getId(), num * num);
                } else {
                    throw new RuntimeException("do not support odd number");
                }
                return num * num;
            }
        }).subscribeOn(Schedulers.io()).doOnError(e-> log.warn(e.getMessage())).onErrorResumeNext(response -> Observable.<Integer>empty()))
                .toList().toBlocking().single();;
    }

    @Test
    public void testScheduler() {
        SampleZip sz = new SampleZip();
        ExecutorService executor = sz.executorService();
        List<Integer> list = Lists.newArrayList(1,2,3,10,11);
        Observable.from(list).flatMap(num -> Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                if (num % 2 == 0 && num <10) {
                    log.info("thread is {},sqrt is {}", Thread.currentThread().getId(), num * num);
                } else if (num % 2 != 0 ){
                    throw new RuntimeException("do not support odd number");
                } else {
                    log.error("thread is {},sqrt is {}", Thread.currentThread().getId(), num * num);
                }
                return num * num;
            }
        }).subscribeOn(Schedulers.from(executor)).doOnError(e-> log.warn(e.getMessage())).onErrorResumeNext(response -> Observable.<Integer>empty()))
                .toList().toBlocking().single();
    }


}
