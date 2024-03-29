package com.xiaofei.algorithm.linkedlist;

import java.util.*;

public class MergeKLists {

    /**
     * 队列里面的内容是Status
     */
    PriorityQueue<Status> queue = new PriorityQueue<Status>();

    public static void main(String[] args) {
        ListNode[] lists = new ListNode[2];
        lists[0] = null; // 空链表
        lists[1] = new ListNode(1); // 包含一个节点值为 1 的链表
        MergeKLists mergeKLists = new MergeKLists();
        ListNode listNode = mergeKLists.mergeKLists(lists);
        ListNode.printAllList(listNode);

    }

    class Status implements Comparable<Status> {
        //节点的值
        int val;
        //节点本身;
        ListNode ptr;

        Status(int val, ListNode ptr) {
            this.val = val;
            this.ptr = ptr;
        }

        //对比方法是按照数值比较;
        public int compareTo(Status status2) {
            return this.val - status2.val;
        }
    }



    public ListNode mergeKLists(ListNode[] lists) {
        for (ListNode node: lists) {
            if (node != null) {
                //把节点和节点本身的数据打包进去
                queue.offer(new Status(node.val, node));
            }
        }
        ListNode head = new ListNode(0);//哑节点
        ListNode tail = head;
        while (!queue.isEmpty()) {
            Status f = queue.poll();
            tail.next = f.ptr;
            tail = tail.next;
            if (f.ptr.next != null) {
                queue.offer(new Status(f.ptr.next.val, f.ptr.next));
            }
        }
        return head.next;
    }





//    public static ListNode mergeKLists(ListNode[] lists) {
//        try {
//            //暴力求解
//            if (lists.length == 0) {
//                return null;
//            }
//            List<Integer> intList = new ArrayList<Integer>();
//            for (ListNode list : lists) {
//                while (true) {
//                    if (list == null){
//                        break;
//                    }
//                    intList.add(list.val);
//                    if (list.next == null) {
//                        break;
//                    }
//                    list = list.next;
//                }
//            }
//
//
//            Collections.sort(intList, Comparator.naturalOrder());
//            ListNode dummy = new ListNode(0);
//            ListNode tail = dummy;
//            for (Integer integer : intList) {
//                ListNode t = new ListNode(integer);
//                tail.next = t;
//                tail = t;
//            }
//            return dummy.next;
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
}
