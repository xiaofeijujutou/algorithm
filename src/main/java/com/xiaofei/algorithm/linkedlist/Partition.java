package com.xiaofei.algorithm.linkedlist;

public class Partition {
    public static void main(String[] args) {
        Partition p = new Partition();
        ListNode listNode = ListNode.creatNodeByReverse(50);
        ListNode partition = p.partition(listNode, 25);

        ListNode.printAllList(partition);
    }
    public ListNode partition(ListNode head, int x) {
        ListNode bigDummy = new ListNode(-1);
        ListNode bigDummyTail = bigDummy;
        ListNode smallDummy = new ListNode(-2);
        ListNode smallDummyTail = smallDummy;
        //初始化哑节点;
        ListNode index = new ListNode();
        index.next = head;
        //哑节点的下一个节点;
        ListNode next = index.next;
        while (next != null){
            index = next;//不会报错;
            next = next.next;
            if (index.val < x){
                //丢入小队列
                smallDummyTail.next = index;
                smallDummyTail = index;
                index.next = null;
            }else {
                //丢入大队列
                bigDummyTail.next = index;
                bigDummyTail = index;
                index.next = null;
            }
        }
        smallDummyTail.next = bigDummy.next;
        return smallDummy.next;
    }
}
