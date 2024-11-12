package com.richard.demo.utils.leetcode;

import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

// 2个栈实现一个队列
public class StackSolution {

    Stack<String> stack1 = new Stack<>();
    Stack<String> stack2 = new Stack<>();

    public void push(String e) {
        stack1.push(e);
    }

    public String pop() {
        // cornor case
        if (stack1.isEmpty() && stack2.isEmpty()) {
            return StringUtils.EMPTY;
        }
        // 注意，这里要看stack2
        if (stack2.isEmpty()) {
            while (!stack1.isEmpty()) {
                stack2.push(stack1.pop());
            }
        }
        return stack2.pop();
    }

    // 判断出栈顺序是否正确
    public boolean isPopOrder(String a[], String b[]) {
        if (a.length != b.length) {
            return false;
        }

        // 借助辅助stack 来判断是否正确
        Stack assist = new Stack();
        // 记录pop的顺序
        int popIndex = 0;
        for (int i = 0; i < a.length; i++) {
            assist.push(a[i]);
            while (!assist.isEmpty() && assist.peek() == b[popIndex]) {
                assist.pop();
                popIndex++;
            }
        }

        return assist.isEmpty();

    }

    public static void main(String[] args) {
        StackSolution ss = new StackSolution();
        ss.push("1");
        ss.push("2");
        ss.push("3");
        ss.push("4");
        ss.push("5");

        for (int i = 0; i < 5; i++) {
            System.out.println(ss.pop() + " ");
        }

        String a[] = {"1", "2", "3", "4", "5"};
        String b[] = {"4", "5", "3", "2", "1"};
        String c[] = {"4", "3", "5", "1", "2"};
        System.out.println(ss.isPopOrder(a, b));
        System.out.println(ss.isPopOrder(a, c));

    }
}
