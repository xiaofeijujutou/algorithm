package com.xiaofei.algorithm.binarytree;

import java.util.ArrayList;
import java.util.List;

public class InorderTraversal {

    public static void main(String[] args) {
        InorderTraversal i = new InorderTraversal();
        TreeNode treeNode = TreeNode.creatBalancedBS(19);
        List<Integer> integers = i.inorderTraversal(treeNode);
        System.out.println(integers.toString());

    }
    public List<Integer> inorderTraversal(TreeNode root) {
        inorder(root);
        return list;
    }
    private final List<Integer> list = new ArrayList<>();
    private void inorder(TreeNode node) {

        if (node.left != null){
            inorder(node.left);
        }
        list.add(node.val);
        if (node.right != null){
            inorder(node.right);
        }

    }
}
