package com.xiaofei.algorithm.linkedlist;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

public class RemoveNthFromEnd {
    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(3);
        ListNode l4 = new ListNode(4);
        ListNode l5 = new ListNode(5);
        ListNode l6 = new ListNode(6);
        ListNode l7 = new ListNode(7);
        ListNode l8 = new ListNode(8);
        ListNode l9 = new ListNode(9);
        ListNode l10 = new ListNode(10);

        l1.next = l2;
//        l2.next = l3;
//        l3.next = l4;
//        l4.next = l5;
//        l5.next = l6;
//        l6.next = l7;
//        l7.next = l8;
//        l8.next = l9;
//        l9.next = l10;

        ListNode listNode = removeNthFromEnd(l1, 2);
        ListNode.printAllList(listNode);

    }

    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        if (head.next == null){
            return null;
        }
        //是删除节点的前一个节点;
        ListNode after = dummy;  //后继节点,要删除的节点;
        ListNode before = dummy; //前驱节点,先到最后;

        for (int i = 0; i < n; i++) {
            before = before.next;
        }
        //前驱后继节点都初始化好了;
        while (true){
            if(before.next == null){
                break;
            }
            after = after.next; before = before.next;
        }
        if (after.next.next == null){
            after.next = null;
            return head;
        }
        if (after.next == head){
            return after.next.next;
        }
        after.next = after.next.next;
        return head;
    }

}
