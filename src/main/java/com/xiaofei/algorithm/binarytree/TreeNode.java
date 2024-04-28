package com.xiaofei.algorithm.binarytree;

public class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    /**
     * 创建二叉树
     * @param num
     * @return
     */
    public static TreeNode creatBalancedBS(int num) {
        if (num <= 0) {
            return null;
        }
        return generateBalancedBSTHelper(1, num);
    }

    /**
     * 递归创建平衡二叉树;
     * @param start 递归结束条件,通过间隔来判断结束
     * @param end
     * @return 根节点
     */
    private static TreeNode generateBalancedBSTHelper(int start, int end) {
        if (start > end) {
            return null;
        }

        // 计算根节点的值
        int mid = (start + end) / 2;
        // 创建根节点
        TreeNode root = new TreeNode(mid);

        // 递归构建左子树
        root.left = generateBalancedBSTHelper(start, mid - 1);
        // 递归构建右子树
        root.right = generateBalancedBSTHelper(mid + 1, end);

        return root;
    }
}