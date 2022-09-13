package com.richard.demo.utils.util;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.index.qual.NonNegative;
import org.junit.Test;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.google.common.collect.Lists;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * refer to
 * https://blog.csdn.net/l_dongyang/article/details/108326755
 * https://juejin.cn/post/7084474452376813605
 * 
 * @author richard.xu03@sap.com
 * @version v 0.1 2022/7/15 15:26 richard.xu Exp $
 */
@Slf4j
public class CaffeineTest {


    @Test
    public void testCache() throws ExecutionException, InterruptedException {
        String k = "manualCache";
        if (StringUtils.equals(k, "manualCache")) {
            /**
             * 手动加载：手动控制缓存的增删改处理，主动增加、获取以及依据函数式更新缓存；
             * 底层使用ConcurrentHashMap进行节点存储，因此get()方法是安全的。
             * 批量查找可以使用getAllPresent()方法或者带填充默认值的getAll()方法。
             */
            cacheTest(k);
        } else if (StringUtils.equals(k, "loadingCache")) {
            /*
             * 同步加载：LoadingCache对象进行缓存的操作，使用CacheLoader进行缓存存储管理。
             * 批量查找可以使用getAll()方法。默认情况下，getAll()将会对缓存中没有值的key分别调用CacheLoader.load方法来构建缓存的值（build中的表达式）。
             * 我们可以重写CacheLoader.loadAll方法来提高getAll()的效
             */
            loadingCacheTest(k);
        } else if (StringUtils.equals(k, "asyncLoadingCache")) {
            /*
             * AsyncLoadingCache对象进行缓存管理，get()返回一个CompletableFuture对象，默认使用ForkJoinPool.commonPool()来执行异步线程，
             * 但是我们可以通过Caffeine.executor(Executor) 方法来替换线程池
             */
            asyncLoadingCacheTest(k);
        }
    }

    /**
     * 手动填充测试
     */
    public void cacheTest(String k) {
        Cache<String, String> cache = Caffeine.newBuilder().maximumSize(100).expireAfterAccess(100L, TimeUnit.SECONDS)
                .removalListener(
                        (String key, Object value, RemovalCause cause) -> System.out.printf("Key %s was removed (%s)%n", key, cause))
                .build();

        cache.put("c1", "c1");
        // 获取缓存值，如果为空，返回null
        log.info("cacheTest present： [{}] -> [{}]", k, cache.getIfPresent(k));
        // 获取返回值，如果为空，则运行后面表达式，存入该缓存
        log.info("cacheTest default： [{}] -> [{}]", k, cache.get(k, this::buildLoader));
        log.info("cacheTest present： [{}] -> [{}]", k, cache.getAllPresent(Lists.newArrayList("c1", k)));
        // 清除缓存
        cache.invalidate(k);
        log.info("cacheTest present： [{}] -> [{}]", k, cache.getIfPresent(k));
    }

    private String buildLoader(String k) {
        return k + "+default";
    }


    /**
     * 同步填充测试
     */
    public void loadingCacheTest(String k) {
        // 同步填充在build方法指定表达式
        LoadingCache<String, String> loadingCache =
                Caffeine.newBuilder().maximumSize(100).expireAfterAccess(100L, TimeUnit.SECONDS).build(this::buildLoader);

        loadingCache.put("c1", "c1");
        log.info("loadingCacheTest get： [{}] -> [{}]", k, loadingCache.get(k));
        // 获取缓存值，如果为空，返回null
        log.info("loadingCacheTest present： [{}] -> [{}]", k, loadingCache.getIfPresent(k));
        // 获取返回值，如果为空，则运行后面表达式，存入该缓存
        log.info("loadingCacheTest default： [{}] -> [{}]", k, loadingCache.get(k, this::buildLoader));
        log.info("loadingCacheTest present： [{}] -> [{}]", k, loadingCache.getIfPresent(k));
        loadingCache.invalidate(k);
        log.info("loadingCacheTest present： [{}] -> [{}]", k, loadingCache.getIfPresent(k));
    }

    /**
     * 异步填充测试
     */
    public void asyncLoadingCacheTest(String k) throws ExecutionException, InterruptedException {
        // 异步加载使用Executor去调用方法并返回一个CompletableFuture。异步加载缓存使用了响应式编程模型。
        // 如果要以同步方式调用时，应提供CacheLoader。要以异步表示时，应该提供一个AsyncCacheLoader，并返回一个CompletableFuture。
        AsyncLoadingCache<String, String> asyncLoadingCache = Caffeine.newBuilder().maximumSize(100)
                .expireAfterAccess(100L, TimeUnit.SECONDS).buildAsync(s -> this.buildLoaderAsync("123").get());

        log.info("asyncLoadingCacheTest get： [{}] -> [{}]", k, asyncLoadingCache.get(k).get());
        // 获取返回值，如果为空，则运行后面表达式，存入该缓存
        log.info("asyncLoadingCacheTest default： [{}] -> [{}]", k, asyncLoadingCache.get(k, this::buildLoader).get());
    }

    private CompletableFuture<String> buildLoaderAsync(String k) {
        return CompletableFuture.supplyAsync(() -> k + "+buildLoaderAsync");
    }

    /**
     * 淘汰策略-size
     * Caffeine的缓存清除是惰性的，可能发生在读请求后或者写请求后，比如说有一条数据过期后，不会立即删除，
     * 可能在下一次读/写操作后触发删除（类比于redis的惰性删除）。如果读请求和写请求比较少，但想要尽快的删掉cache中过期的数据的话，
     * 可以通过增加定时器的方法，定时执行cache.cleanUp()方法（异步方法，可以等待执行），触发缓存清除操作。
     */
    @Test
    public void sizeTest() {
        // removalListener()可以监听数据驱逐事件，可以在该监听事件中输出数据淘汰的原因等等。
        LoadingCache<String, String> loadingCache = Caffeine.newBuilder().maximumSize(1)
                .removalListener(
                        (String key, Object value, RemovalCause cause) -> System.out.printf("Key %s was removed (%s)%n", key, cause))
                .build(this::buildLoader);

        List<String> list = Lists.newArrayList("c1", "c2", "c3");
        loadingCache.put(list.get(0), list.get(0));
        log.info("weightTest get： [{}] -> [{}]", list.get(0), loadingCache.get(list.get(0)));
        loadingCache.put(list.get(1), list.get(1));
        log.info("weightTest get： [{}] -> [{}]", list.get(1), loadingCache.get(list.get(1)));
        loadingCache.put(list.get(2), list.get(2));
        log.info("weightTest get： [{}] -> [{}]", list.get(2), loadingCache.get(list.get(1)));
        log.info("before clean up, weightTest cache map：{}", loadingCache.getAll(list));
        loadingCache.cleanUp();
        log.info("before clean up,weightTest cache map：{}", loadingCache.getAll(list));
    }

    /**
     * 淘汰策略-weight
     */
    @Test
    public void weightTest() {
        LoadingCache<String, String> loadingCache =
                Caffeine.newBuilder().maximumWeight(10).weigher((key, value) -> 5).build(this::buildLoader);

        List<String> list = Lists.newArrayList("c1", "c2", "c3");
        loadingCache.put(list.get(0), list.get(0));
        log.info("weightTest get： [{}] -> [{}]", list.get(0), loadingCache.get(list.get(0)));
        loadingCache.put(list.get(1), list.get(1));
        log.info("weightTest get： [{}] -> [{}]", list.get(1), loadingCache.get(list.get(1)));
        loadingCache.put(list.get(2), list.get(2));
        log.info("weightTest get： [{}] -> [{}]", list.get(2), loadingCache.get(list.get(2)));
        log.info("before clean up,weightTest cache map：{}", loadingCache.getAll(list));
        loadingCache.cleanUp();
        log.info("before clean up,weightTest cache map：{}", loadingCache.getAll(list));
    }

    /**
     * 淘汰策略-time
     */
    @Test
    public void timeTest() throws InterruptedException {
        // 1.缓存访问后，一定时间后失效
        LoadingCache<String, String> loadingCacheOne =
                Caffeine.newBuilder().expireAfterWrite(1L, TimeUnit.SECONDS).build(this::buildLoader);

        // 2.缓存写入后，一定时间后失效
        LoadingCache<String, String> loadingCacheTwo =
                Caffeine.newBuilder().expireAfterWrite(5L, TimeUnit.SECONDS).build(this::buildLoader);

        // 3.自定义过期策略
        LoadingCache<String, String> loadingCacheThree = Caffeine.newBuilder().expireAfter(new Expiry<Object, Object>() {
            @Override
            public long expireAfterCreate(@NonNull Object o, @NonNull Object o2, long l) {
                return 10 * 10 ^ 9L;
            }

            @Override
            public long expireAfterUpdate(@NonNull Object o, @NonNull Object o2, long l, @NonNegative long l1) {
                return 10 * 10 ^ 9L;
            }

            @Override
            public long expireAfterRead(@NonNull Object o, @NonNull Object o2, long l, @NonNegative long l1) {
                return 10 * 10 ^ 9L;
            }
        }).build(this::buildLoader);

        loadingCacheOne.put("c1", "c1");
        loadingCacheTwo.put("c2", "c2");
        loadingCacheThree.put("c3", "c3");
        Thread.sleep(2000L);
        log.info("after sleeping 2s,loadingCacheOne is {}", loadingCacheOne.getIfPresent("c1"));
        log.info("after sleeping 2s,loadingCacheTwo is {}", loadingCacheTwo.getIfPresent("c2"));
        log.info("after sleeping 2s,loadingCacheThree is {}", loadingCacheThree.getIfPresent("c3"));
        Thread.sleep(3000L);
        log.info("after sleeping 5s,loadingCacheTwo is {}", loadingCacheTwo.getIfPresent("c2"));
        log.info("after sleeping 5s,loadingCacheThree is {}", loadingCacheThree.getIfPresent("c3"));
        Thread.sleep(5000L);
        log.info("after sleeping 10s,loadingCacheThree is {}", loadingCacheThree.getIfPresent("c3"));

    }



}
