package com.richard.demo.utils.leetcode;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringAlgorithm {

    /**
     * 给定一个字符串 s ，找到 它的第一个不重复的字符，并返回它的索引 。如果不存在，则返回 -1 。
     */
    @Test
    public void testFindFirstUniqueChar() {
        log.info("first char index :" + firstUniqChar("leetcode"));
        log.info("first char index :" + firstUniqChar("loveleetcode"));
        log.info("first char index :" + firstUniqChar("aabb"));
    }

    // linked hashmap 2次遍历，第一次计数，第二次找个数为1的
    public int firstUniqChar(String s) {

        LinkedHashMap<Character, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        Character find = null;
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                find = entry.getKey();
                break;
            }
        }
        return find == null ? -1 : s.indexOf(find);
    }
}
