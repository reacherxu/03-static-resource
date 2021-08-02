package com.richard.demo.utils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

/**
 * 通俗的理解是当我们往一个容器添加元素的时候，不直接往当前容器添加，而是先将当前容器进行Copy，复制出一个新的容器，然后新的容器里添加元素，
 * 添加完元素之后，再将原容器的引用指向新的容器。这样做的好处是我们可以对CopyOnWrite容器进行并发的读，而不需要加锁，
 * 因为当前容器不会添加任何元素。所以CopyOnWrite容器也是一种读写分离的思想，读和写不同的容器。
 *
 * CopyOnWrite的缺点
 * 1.内存占用问题。因为CopyOnWrite的写时复制机制，所以在进行写操作的时候，内存里会同时驻扎两个对象的内存，旧的对象和新写入的对象
 * （注意:在复制的时候只是复制容器里的引用，只是在写的时候会创建新对象添加到新容器里，而旧容器的对象还在使用，所以有两份对象内存）。
 * 如果这些对象占用的内存比较大，比如说200M左右，那么再写入100M数据进去，内存就会占用300M，那么这个时候很有可能造成频繁的Yong GC和Full GC。
 *
 * 2.数据一致性问题。CopyOnWrite容器只能保证数据的最终一致性，不能保证数据的实时一致性。所以如果你希望写入的的数据，马上能读到，
 * 请不要使用CopyOnWrite容器。
 *
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/8/2 3:10 PM richard.xu Exp $
 */
@Slf4j
public class CopyOnWriteArrayListDemo {
    private static class ReadTask implements Runnable {
        List<String> list;

        public ReadTask(List<String> list) {
            this.list = list;
        }

        public void run() {
            for (String str : list) {
                log.info("[ReadTask] {} , thread id is {}", str, Thread.currentThread().getId());
                log.info("[ReadTask]读出: {} , thread id is {}", list.hashCode(), Thread.currentThread().getId());
            }
        }
    }

    public static class WriteTask implements Runnable {
        List<String> list;
        int index;

        public WriteTask(List<String> list, int index) {
            this.list = list;
            this.index = index;
        }

        public void run() {
            log.info("[WriteTask]修改{}前:{},thread id is {}", index, list.hashCode(), Thread.currentThread().getId());
            list.remove(index);
            list.add(index, "write_" + index);
            log.info("[WriteTask]修改{}后:{},thread id is {}", index, list.hashCode(), Thread.currentThread().getId());
        }
    }

    public void run() {
        final int NUM = 6;
        // List<String> list = new ArrayList<String>(); //使用普通的list
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<String>(); // 使用写时修改list
        for (int i = 0; i < NUM; i++) {
            list.add("main_" + i);
        }
        ExecutorService service = Executors.newFixedThreadPool(NUM);
        for (int i = 0; i < NUM; i++) {
            service.execute(new ReadTask(list));
            service.execute(new WriteTask(list, i));
        }
        service.shutdown();
    }

    public static void main(String[] args) {
        new CopyOnWriteArrayListDemo().run();
    }
}
