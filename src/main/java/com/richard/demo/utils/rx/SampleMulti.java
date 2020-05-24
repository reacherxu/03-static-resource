package com.richard.demo.utils.rx;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import rx.Observable;
import rx.schedulers.Schedulers;


public class SampleMulti {

    public static int intenseCalculation(int i) {
        try {
            System.out.println("Calculating " + i + " on " + Thread.currentThread().getName());
            Thread.sleep(5000);
            if (i == 3) {
                throw new RuntimeException("item 3 failed");
            }
            return i * 2;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Observable<Integer> getNumberedObservable(final int value) {
        return Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() {
                System.out.println("thread id: " + Thread.currentThread().getId());
                return intenseCalculation(value);
            }
        });
    }

    public static void main(String[] args) {

        // ExecutorService executor = Executors.newFixedThreadPool(10);

        List<Integer> inputList = new ArrayList<>();
        inputList.add(1);
        inputList.add(2);
        inputList.add(3);
        inputList.add(4);
        inputList.add(5);

        // if error, the item will be replaced with dummy one , here is -1 , so 5 items returned
        // including a dummy one (-1).
        List<Integer> sucessList = Observable.from(inputList)
                .flatMap(value -> getNumberedObservable(value).subscribeOn(Schedulers.io())
                        .doOnError(e -> System.out.println("failed " + value + " " + e)).onErrorReturn(e -> -1))
                .toList().toBlocking().single();

        System.out.println(sucessList);

        // if error , the item will be ignored , so only 4 items returned.
        // List<Integer> sucessList2 = Observable.from(inputList)
        // .flatMap(
        // value -> getNumberedObservable(value).subscribeOn(Schedulers.io())
        // .doOnError(e -> System.out.println("failed " + value + " " + e))
        // .onErrorResumeNext(response -> Observable.<Integer>empty()))
        // .toList().toBlocking().single();
        //
        // System.out.println(sucessList2);
    }

}
