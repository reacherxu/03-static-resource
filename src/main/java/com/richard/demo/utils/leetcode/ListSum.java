package com.richard.demo.utils.leetcode;

import java.util.LinkedList;

import org.junit.Test;

import com.richard.demo.utils.leetcode.model.ListNode;

public class ListSum {
    /**
     * 给定两个非空链表来表示两个非负整数。
     * <li>
     * 位数按照逆序方式存储，
     * <li/>
     * 它们的每个节点只存储单个数字.将两数相加返回一个新的链表。
     * <p>
     * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
     * 输出：7 -> 0 -> 8
     * 原因：342 + 465 = 807
     *
     * 考点 ： 遍历列表
     */
    @Test
    public void testAdd() {
        ListNode n1_0 = new ListNode(2);
        ListNode n1_1 = new ListNode(4);
        ListNode n1_2 = new ListNode(3);
        n1_0.next = n1_1;
        n1_1.next = n1_2;
        print(n1_0);

        ListNode n2_0 = new ListNode(5);
        ListNode n2_1 = new ListNode(6);
        ListNode n2_2 = new ListNode(4);
        n2_0.next = n2_1;
        n2_1.next = n2_2;
        print(n2_0);
        ListNode result = add(n1_0, n2_0);
        print(result);
    }

    /**
     * 空间复杂度o(1) 和o（n）的算法
     */
    @Test
    public void testReverse() {

        ListNode node = createDefaultList();
        // ListNode result = reverse1(a);
        // print(result);
        ListNode result = reverse2(node);
        print(result);
    }

    private ListNode createDefaultList() {
        ListNode a = new ListNode(1);
        ListNode b = new ListNode(2);
        ListNode c = new ListNode(3);
        ListNode d = new ListNode(4);
        ListNode e = new ListNode(5);
        a.next = b;
        b.next = c;
        c.next = d;
        d.next = e;
        return a;
    }

    // merge 2个有序列表， 使用递归
    @Test
    public void testMerge2List() {
        ListNode node1 = createDefaultList();
        ListNode a = new ListNode(1);
        ListNode b = new ListNode(8);
        a.next = b;
        ListNode node2 = a;

        ListNode newNode = merge(node1, node2);
        print(newNode);
    }

    private ListNode merge(ListNode node1, ListNode node2) {
        // node1 与node2 长度可能不一致，当一个为空的时候，剩下的就是node直接拼接上去
        if (node1 == null) {
            return node2;
        }
        if (node2 == null) {
            return node1;
        }

        // 使用递归
        if (node1.val < node2.val) {
            node1.next = merge(node1.next, node2);
            return node1;
        } else {
            node2.next = merge(node1, node2.next);
            return node2;
        }
    }

    /**
     * 方法1 ： 得到长度，n-k+1 就是倒数第k个
     * 方法2 ： 不需要事先遍历一遍 => 2个指针同时开始跑
     */
    @Test
    public void testFindLastKthToTail() {
        ListNode node = createDefaultList();
        int k = 4;
        ListNode n = findLastKthToTail1(node, k - 1);
        print(n);
    }

    // 删除链表的倒数第 N 个节点
    @Test
    public void testDeleteLastKthToTail() {
        ListNode node = createDefaultList();
        int k = 4;
        ListNode toDelete = findLastKthToTail(node, k + 1);
        toDelete.next = toDelete.next.next;
        print(node);
    }

    private ListNode findLastKthToTail1(ListNode node, int k) {
        ListNode n1 = node, n2 = node;
        int count = 0;
        while (n1 != null) {
            n1 = n1.next;
            count++;
        }
        if (k > count) {
            return null;
        }

        // 直接计算倒数第k个
        int lastk = count - k + 1;
        for (int i = 1; i < lastk; i++) {
            n2 = n2.next;
        }
        return n2;
    }

    private ListNode findLastKthToTail(ListNode node, int k) {
        ListNode l1 = node, l2 = node;
        int count = 0, index = k;
        // l1 需要一直跑，跑到k的时候，l2 开始跑
        while (l1 != null) {
            l1 = l1.next;
            count++;
            if (index < 1) {
                l2 = l2.next;
            }
            index--;
        }

        // corner case
        if (count < k) {
            return null;
        }
        return l2;
    }

    // use stack 空间复杂度O(n)
    public ListNode reverse1(ListNode head) {
        LinkedList<ListNode> stack = new LinkedList<>();
        ListNode cur = head;
        while (cur != null) {
            stack.push(new ListNode(cur.val));
            cur = cur.next;
        }
        ListNode dummyHead = new ListNode(0);
        ListNode head1 = dummyHead;
        while (!stack.isEmpty()) {
            dummyHead.next = stack.pop();
            dummyHead = dummyHead.next;
        }
        return head1.next;
    }

    public ListNode reverse2(ListNode head) {
        ListNode pre = null;
        ListNode cur = head;
        while (cur != null) {
            // 存储下一个值, 防止链表断裂
            ListNode temp = cur.next;

            // 反转
            cur.next = pre;
            pre = cur;
            cur = temp;
        }
        return pre;
    }

    public void print(ListNode head) {
        ListNode temp = head;
        while (temp != null) {
            System.out.print(temp.val + " ");
            temp = temp.next;
        }
        System.out.println();
    }

    /**
     * 思路：逆序则是从个位数开始相加
     * 按位相加，记录进位 => (要有虚拟表头) 取值->相加求进位当位置上的值->走下一个节点
     * 注意 ： 最后还有一个进位
     *
     * @param l1
     * @param l2
     * @return
     */
    public ListNode add(ListNode l1, ListNode l2) {
        ListNode dummyHead1 = new ListNode(0);
        ListNode head = dummyHead1;
        int carry = 0;
        while (l1 != null || l2 != null) {
            // get value
            int value1 = (l1 == null) ? 0 : l1.val;
            int value2 = (l2 == null) ? 0 : l2.val;

            // calculate sum and remainder
            int sum = value1 + value2 + carry;
            carry = sum / 10;
            int remainder = sum % 10;
            ListNode temp = new ListNode(remainder);
            dummyHead1.next = temp;
            dummyHead1 = dummyHead1.next;

            // move to next node
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        // do not forget last carry
        if (carry > 0) {
            dummyHead1.next = new ListNode(carry);
        }
        return head.next;
    }
}
