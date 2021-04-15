package com.richard.demo.utils;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * need to import com.google.guava
 * 
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/4/15 10:52 AM richard.xu Exp $
 */
public class MapUtils {

    /**
     * Multimap 一个key可以映射多个value的HashMap
     * 省得你再创建 Map<String, List<Integer>>
     */
    @Test
    public void testMultimap() {
        Multimap<String, Integer> map = ArrayListMultimap.create();
        map.put("key", 1);
        map.put("key", 2);
        map.put("key", 2);

        Collection<Integer> values = map.get("key");
        System.out.println(map); // 输出 {"key":[1,2]}
        // 还能返回你以前使用的臃肿的Map
        Map<String, Collection<Integer>> collectionMap = map.asMap();
    }

}
