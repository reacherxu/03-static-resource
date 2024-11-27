package com.richard.demo.utils.leetcode;

import java.util.LinkedList;

import org.junit.Test;

import com.richard.demo.utils.leetcode.model.TreeNode;

public class BinaryTree {
    /**
     * 前序遍历：1 2 4 5 7 8 3 6
     * 中序遍历：4 2 7 5 8 1 3 6
     * 后序遍历：4 7 8 5 2 6 3 1
     * 层次遍历：1 2 3 4 5 6 7 8
     *
     * 1
     * / \
     * 2 3
     * / \ \
     * 4 5 6
     * / \
     * 7 8
     *
     * 
     * @return
     */
    public TreeNode createDefaultTree() {
        TreeNode root = new TreeNode(1);
        TreeNode left = new TreeNode(2);
        TreeNode right = new TreeNode(3);
        root.left = left;
        root.right = right;
        TreeNode left2 = new TreeNode(4);
        TreeNode right2 = new TreeNode(5);
        left.left = left2;
        left.right = right2;

        TreeNode left5 = new TreeNode(7);
        TreeNode right5 = new TreeNode(8);
        right2.left = left5;
        right2.right = right5;
        TreeNode right3 = new TreeNode(6);
        right.right = right3;

        return root;
    }

    @Test
    public void testBinaryTree() {
        // 二叉树的遍历
        TreeNode root = createDefaultTree();

        // 1. 前序遍历
        System.out.println("前序遍历：");
        preOrder(root);
        System.out.println();
        // 2. 中序遍历
        System.out.println("中序遍历：");
        inOrder(root);
        System.out.println();

        // 3. 后序遍历
        System.out.println("后序遍历：");
        postOrder(root);
        System.out.println();

        // 4. 层次遍历
        // 只需要一个队列即可，先在队列中加入根结点
        System.out.println("层次遍历：");
        levelTraversal(root);
    }

    private void levelTraversal(TreeNode root) {
        if (root == null) {
            return;
        }

        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            System.out.print(node.val + "  ");
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }

    private void postOrder(TreeNode root) {
        if (root != null) {
            postOrder(root.left);
            postOrder(root.right);
            System.out.print(root.val + "  ");
        }
    }

    private void preOrder(TreeNode root) {
        if (root != null) {
            System.out.print(root.val + "  ");
            preOrder(root.left);
            preOrder(root.right);
        }
    }

    private void inOrder(TreeNode root) {
        if (root != null) {
            inOrder(root.left);
            System.out.print(root.val + "  ");
            inOrder(root.right);
        }
    }
}
