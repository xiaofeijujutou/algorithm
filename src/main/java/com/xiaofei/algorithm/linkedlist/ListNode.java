package com.xiaofei.algorithm.linkedlist;

public class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }

    public static void printAllList(ListNode listNode){
        while (true){
            System.out.println(listNode.val);
            if (listNode.next == null){
                break;
            }
            listNode = listNode.next;
        }
    }

    public static ListNode creatNode(int length){
        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;
        for (int i = 0; i < length; i++) {
            ListNode node = new ListNode(i + 1);
            tail.next = node;
            tail = node;
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
}
