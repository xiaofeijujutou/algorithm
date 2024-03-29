package com.xiaofei.algorithm.linkedlist;


import java.util.List;

//Definition for singly-linked list.



public class AddTwoNumbers {
    public static void main(String[] args) {
        ListNode l1 = new ListNode(9);
        ListNode l2 = new ListNode(9);
        ListNode l3 = new ListNode(9);
        ListNode l4 = new ListNode(9);

        ListNode l5 = new ListNode(9);
        ListNode l6 = new ListNode(9);
        ListNode l7 = new ListNode(9);
        ListNode l8 = new ListNode(9);
        ListNode l9 = new ListNode(9);
        ListNode l10 = new ListNode(9);

        ListNode l11 = new ListNode(1);
        ListNode l12 = new ListNode(1);
        //l1
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;


        l5.next = l6;
        l6.next = l7;
        l7.next = l8;
        l8.next = l9;
        l9.next = l10;
//        l1.next = l2;
//        l2.next = l3;
//        l3.next = l4;
//        l4.next = l5;
//        l5.next = l6;
//        l6.next = l7;
//        l7.next = l8;
//        l8.next = l9;
//        l9.next = l10;
//        l10.next = l11;


        ListNode listNode = addTwoNumbers(l1, l5);
        while (true){
            System.out.println(listNode.val);
            if (listNode.next == null){
                break;
            }
            listNode = listNode.next;
        }



    }
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int carry = 0;//进位;
        //=====================================>初始化头结点
        ListNode head = new ListNode();
        ListNode tail = head;

        //初始头结点
        int sum = l1.val + l2.val + carry;
        if (sum >= 10){ //大于10
            sum = sum % 10;
            head.val = sum;
            carry = 1;
        }else { //小于10
            carry = 0;
            head.val = sum;
        }
        //=====================================>初始化头结点

        while (true){
            //先判断,然后下移一个节点;
            if (l1.next == null && l2.next == null){
                //挂载
                if (carry == 1){
                    ListNode temp = new ListNode(1);
                    tail.next = temp;
                }
                return head;
            }
            if (l1.next == null){
                if (carry == 0){
                    tail.next = l2.next;
                    return head;
                }
                copyNode(tail, l2.next);
                return head;
            }
            if (l2.next == null){
                if (carry == 0){
                    tail.next = l1.next;
                    return head;
                }
                copyNode(tail, l1.next);
                return head;
            }

            l1 = l1.next; l2 = l2.next;

            sum = l1.val + l2.val + carry;
            if (sum >= 10){ //大于10
                sum = sum % 10;
                ListNode temp = new ListNode(sum);
                tail.next = temp;
                tail = temp;
                carry = 1;
            }else { //小于10
                carry = 0;
                ListNode temp = new ListNode(sum);
                tail.next = temp;
                tail = temp;
            }
        }

    }

    private static void copyNode(ListNode tail, ListNode next) {
        tail.next = next;
        while (true){
            int val = next.val + 1;
            if (val == 10){
                next.val = 0;
                if (next.next == null){
                    next.next = new ListNode(1);
                    break;
                }
                next = next.next;
            }else {
                next.val = val;
                break;
            }

        }

    }

//    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
//        long[] an1 = analyzeNode(l1);
//        long[] an2 = analyzeNode(l2);
//        long maxLength = Math.max(an1[0], an2[0]);
//        long sum = an1[1] + an2[1];
//        return getResultList(maxLength, sum);
//    }
//
//    private static ListNode getResultList(long maxLength, long sum) { //3,4122
//        String numStr = String.valueOf(sum);
//        ListNode head;
//        ListNode tail = null;
//
//        head = new ListNode(Character.getNumericValue(numStr.charAt(numStr.length() - 1)));
//        tail  = head;
//        for (long i = numStr.length() - 2 ; i > 0; i--) {
//            ListNode tempt = new ListNode(Character.getNumericValue(numStr.charAt((int)i)));
//            tail.next = tempt;
//            tail = tempt;
//        }
//        return head;
//    }
//
//    public static long[] analyzeNode(ListNode ln){ //3->2->3  323
//        long sum = 0L;
//        long length = 0;
//        sum = ln.val;
//        if (ln.next == null){
//            return new long[]{1, sum};
//        }
//        ln = ln.next;
//        while (true){
//            sum = sum + ln.val * (long)Math.pow(10, length + 1);
//            length++;
//            if (ln.next == null){
//                break;
//            }
//            ln = ln.next;
//        }
//        return new long[]{length, sum};
//    }

}
