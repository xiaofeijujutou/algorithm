package com.xiaofei.algorithm.linkedlist;


import java.util.Stack;

public class ReverseKGroup {
    public static void main(String[] args) {
        ListNode listNode = ListNode.creatNode(100);
        ListNode listNode1 = reverseKGroup(listNode, 7);
        ListNode.printAllList(listNode1);
    }



    /**
     * 分批穿针引线;
     * @param head
     * @param k
     * @return
     */
    public static ListNode reverseKGroup(ListNode head, int k) {//2
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        int nodeLength = getNodeLength(head);
        //穿针引线;
        //初始化
        ListNode sentinel = dummy;
        ListNode soldier = head;
        ListNode sentinelNext = sentinel.next;
        ListNode soldierNext = soldier.next;
        for (int i = 0; i < nodeLength / k; i++) {
            for (int j = 0; j < k - 1; j++) {
                //交换
                soldier.next = soldier.next.next;
                sentinel.next = soldierNext;
                soldierNext.next = sentinelNext;
                //重置
                sentinelNext = sentinel.next;
                soldierNext = soldier.next;
            }
            if (soldier.next == null){
                break;
            }
            sentinel = soldier;
            soldier = soldier.next;
            sentinelNext = sentinel.next;
            soldierNext = soldier.next;
        }
        return dummy.next;
    }

    public static int getNodeLength(ListNode head){
        ListNode current = head;
        int count = 0;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;

    }

//    public static ListNode reverseKGroup(ListNode head, int k) {
//        if (head == null) return null;
//        //创建栈
//        Stack<ListNode> stack = new Stack<ListNode>();
//        //初始化哑节点
//        ListNode dummy = new ListNode(-1);
//        dummy.next = head;
//        ListNode current = dummy;
//        ListNode next = dummy.next;
//        while (next != null) {
//            for (int i = 0; i < k; i++) {
//                //入栈
//                stack.push(next);
//                //往下走一个节点
//                next = next.next;
//            }
//            while (stack.size() != 0) {
//                //出栈
//                current.next = stack.pop();
//                current = current.next;
//            }
//            //移到下一个要反转的点,也就是前进一格;
//            current.next = next;
//        }
//        return dummy.next;
//    }
}
