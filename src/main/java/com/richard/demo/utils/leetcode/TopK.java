package com.richard.demo.utils.leetcode;

import java.util.*;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.richard.demo.utils.JacksonUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * topk 则需要降序， 重点不管是Arrays.sort Or Collections.sort
 * 第二个参数 new Comparator o2-o1 表示降序
 */
@Slf4j
@Data
public class TopK {
    private List<Order> orderList = new ArrayList<>();

    public void addCart(Order order) {
        this.orderList.add(order);
    }

    public void addCartList(List<Order> orderList) {
        this.orderList.addAll(orderList);
    }

    /**
     * 给定一个单词列表 words 和一个整数 k ，返回前 k 个出现次数最多的单词。
     * <p>
     * 返回的答案应该按单词出现频率
     * <li>由高到低排序
     * <li/>
     * 如果不同的单词有相同出现频率，
     * <li>按字典顺序 排序
     * <li/>
     */
    @Test
    public void testTopKWords() {
        String words[] = new String[] {"i", "love", "leetcode", "i", "love", "coding"};
        int k = 2;
        List<String> top = topkWords(words, k);
        System.out.println(top);


        String words2[] = new String[] {"the", "day", "is", "sunny", "the", "the", "the", "sunny", "is", "is"};
        top = topkWords(words2, 4);
        System.out.println(top);

    }

    // 考点1 map 计数map.getOrDefault
    // 考点1 List的compare 方法 ： Collections.sort(list,new Comparator<String>)
    private List<String> topkWords(String[] words, int k) {
        HashMap<String, Integer> cnt = new HashMap<>();
        for (String word : words) {
            cnt.put(word, cnt.getOrDefault(word, 0) + 1);
        }
        List<String> keys = new ArrayList<>(cnt.keySet());
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return cnt.get(o1) == cnt.get(o2) ? o1.compareTo(o2) : cnt.get(o2) - cnt.get(o1);
            }
        });
        return keys.subList(0, k);
    }


    /**
     * 给你一个整数数组 nums 和一个整数 k ，
     * 请你返回其中出现频率前 k 高的元素。你可以按 任意顺序 返回答案。
     */
    @Test
    public void testTopKNumFrequent() {
        int num[] = new int[] {1, 1, 1, 2, 2, 3};
        int k = 2;
        int[] result = topKNumFrequent(num, k);
        JacksonUtil.writeStr(result);
    }

    public int[] topKNumFrequent(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();
        // 计数
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], map.getOrDefault(nums[i], 0) + 1);
        }
        // 比较
        List<Integer> list = new ArrayList<>(map.keySet());
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return map.get(o2) - map.get(o1);
            }
        });

        int result[] = new int[k];
        List<Integer> sub = list.subList(0, k);
        for (int i = 0; i < sub.size(); i++) {
            result[i] = sub.get(i);
        }
        return result;
    }


    public static void main(String[] args) {
        Order o1 = new Order("phone", new Date(1609459200000L));
        Order o2 = new Order("mouse", new Date(1609459200000L));
        Order o3 = new Order("phone", new Date(1609459200000L));
        Order o4 = new Order("phone", new Date(1609459200000L));
        Order o5 = new Order("water", new Date(1609459200000L));
        Order o6 = new Order("water", new Date(1609459200000L));
        TopK solution = new TopK();
        solution.addCartList(Lists.newArrayList(o1, o2, o3, o4, o5, o6));
        List<String> topk = solution.topkOrder(solution.getOrderList(), 2);
        System.out.println(topk);
    }

    @Test
    public void testOrder1() {
        Order o1 = new Order("phone", new Date(1609459200000L));
        Order o2 = new Order("mouse", new Date(1609459200000L));
        Order o3 = new Order("phone", new Date(1609459200000L));
        Order o4 = new Order("phone", new Date(1609459200000L));
        Order o5 = new Order("water", new Date(1609459200000L));
        Order o6 = new Order("water", new Date(1609459200000L));

        List<String> os = topkOrder(Lists.newArrayList(o1, o2, o3, o4, o5, o6), 2);
        System.out.println(JacksonUtil.writeStr(os));
    }

    private List<String> topkOrder(List<Order> orders, int k) {
        HashMap<String, Integer> map = new HashMap<>();
        // 计数
        for (int i = 0; i < orders.size(); i++) {
            map.put(orders.get(i).getItem(), map.getOrDefault(orders.get(i).getItem(), 0) + 1);
        }
        // 排序
        // get product list
        List<String> list = new ArrayList<>(map.keySet());
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return map.get(o2) - map.get(o1);
            }
        });
        return list.subList(0, k);
    }

    /**
     * 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
     *
     * 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
     *
     * 你必须设计并实现时间复杂度为 O(n) 的算法解决此问题。
     */
    @Test
    public void testFindKthLargest() {
        int nums[] = {3, 2, 1, 5, 6, 4};
        log.info(" kth largest is {}", findKthLargest(nums, 2));
        int nums2[] = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        log.info(" kth largest is {}", findKthLargest(nums2, 4));

    }

    // 题目分析：本题希望我们返回数组排序之后的倒数第 k 个位置, 即数组n-k位置的数
    public int findKthLargest(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }


    /**
     * 给你一个字符串数组 nums 和一个整数 k 。nums 中的每个字符串都表示一个不含前导零的整数。
     *
     * 返回 nums 中表示第 k 大整数的字符串。
     */
    @Test
    public void testFindKthLargestNumber() {

        String nums4[] = {"623986800", "3", "887298", "695", "794", "6888794705", "269409", "59930972", "723091307", "726368", "8028385786",
                "378585"};
        log.info("kth largest is {}", kthLargestNumber2(nums4, 11));

        String nums[] = {"3", "7", "6", "10"};
        log.info("kth largest is {}", kthLargestNumber(nums, 4));

        String nums2[] = {"2", "21", "12", "1"};
        log.info("kth largest is {}", kthLargestNumber(nums2, 3));

        String nums3[] = {"0", "0"};
        log.info("kth largest is {}", kthLargestNumber(nums3, 2));


    }

    public String kthLargestNumber(String[] nums, int k) {
        // string 排序，实现自定义comparator
        Arrays.sort(nums, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.valueOf(o2) - Integer.valueOf(o1);
            }
        });
        return nums[k - 1];
    }

    // 因为是整数，比较长度即可
    public String kthLargestNumber2(String[] nums, int k) {
        // string 排序，实现自定义comparator
        Arrays.sort(nums, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() == o2.length() ? o2.compareTo(o1) : o2.length() - o1.length();
            }
        });
        return nums[k - 1];
    }
}


@Data
@AllArgsConstructor
class Order {
    private String item;
    private Date time;
}
