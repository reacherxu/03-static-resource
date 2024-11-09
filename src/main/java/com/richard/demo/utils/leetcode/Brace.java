package com.richard.demo.utils.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;


public class Brace {

    // Q1 测试括号深度 ----------------------------------------------------------

    /**
     * 爱奇艺 2018 秋招 Java
     * 一个合法的括号匹配序列有以下定义:空串""是一个合法的括号匹配序列如果"X"和"Y"都是合法的括号匹配序列,
     * "XY"也是一个合法的括号匹配序列如果"X"是一个合法的括号匹配序列,
     * 那么"(X)"也是一个合法的括号匹配序列每个合法的括号序列都可以由以上规则生成。
     * 例如: "","()","()()","((()))"都是合法的括号序列
     * 对于一个合法的括号序列我们又有以下定义它的深度:
     * 空串""的深度是 0如果字符串"X"的深度是 x,字符串"Y"的深度是 y,那么字符串"XY"的深度为 max(x,y)
     * 如果"X"的深度是 x,那么字符串"(X)"的深度是 x+1
     * 例如: "()()()"的深度是 1,"((()))"的深度是 3。牛牛现在给你一个合法的括号序列,需要你计算出其深度。
     */
    @Test
    public void testBraceDepth() {
        String s = "()((()))";
        System.out.println(braceDepth(s));
        s = "(())()()";
        System.out.println(braceDepth(s));
    }

    /**
     * 输入描述:
     * 输入包括一个合法的括号序列s,s长度length(2 ≤ length ≤ 50),序列中只包含'('和')'。
     * <p>
     * 输出描述:
     * 输出一个正整数,即这个序列的深度。
     *
     * @param s
     * @return
     */
    private int braceDepth(String s) {
        int cnt = 0, max = 0, i;
        for (i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '(') {
                cnt++;
            } else {
                cnt--;
            }
            max = Math.max(max, cnt);
        }
        return max;
    }

    // Q2 有效的括号 ----------------------------------------------------------

    /**
     * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
     *
     * 有效字符串需满足：
     *
     * 左括号必须用相同类型的右括号闭合。
     * 左括号必须以正确的顺序闭合。
     * 每个右括号都有一个对应的相同类型的左括号。
     */
    @Test
    public void testIsValidBrace() {
        String s = "()";
        System.out.println(isValidBrace(s));

        s = "()[]{}";
        System.out.println(isValidBrace(s));

        s = "(]";
        System.out.println(isValidBrace(s));
        s = "([])";
        System.out.println(isValidBrace(s));

    }

    private boolean isValidBrace(String str) {
        // corner case
        if (str.length() == 0) {
            return true;
        }
        if (str.length() % 2 != 0) {
            return false;
        }
        // 配对括号
        HashMap<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put(']', '[');
        map.put('}', '{');

        // use stack
        LinkedList<Character> stack = new LinkedList<>();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(' || str.charAt(i) == '[' || str.charAt(i) == '{') {
                stack.push(str.charAt(i));
            } else {
                Character right = map.get(str.charAt(i));
                if (stack.isEmpty() || stack.pop() != right) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    // Q3 ----------------------------------------------------------

    /**
     * 设计一种算法，打印n对括号的所有合法的（例如，开闭一一对应）组合。
     *
     * 说明：解集不能包含重复的子集。
     */
    @Test
    public void testGenerateBrace() {
        int n = 3;
        List<String> result = generateBrace(3);
        System.out.println(result);
    }

    private List<String> generateBrace(int n) {
        char[] str = new char[n * 2];
        int pos = 0;
        List<String> allResult = new ArrayList<>();
        generateAll(str, pos, allResult);
        return new ArrayList<>(allResult);
    }

    private void generateAll(char[] str, int pos, List<String> allResult) {
        if (pos == str.length) {
            if (isValidBrace(toString(str))) {
                allResult.add(new String(str));
            }
        } else {
            str[pos] = '(';
            generateAll(str, pos + 1, allResult);
            str[pos] = ')';
            generateAll(str, pos + 1, allResult);
        }
    }


    public String toString(char[] str) {
        StringBuilder sb = new StringBuilder();
        for (char c : str) {
            sb.append(c);
        }
        return sb.toString();
    }

    @Test
    public void testCharToStr() {
        char[] str = new char[] {'(', '(', '(', ')', ')', ')'};
        System.out.println(toString(str));
    }

}
