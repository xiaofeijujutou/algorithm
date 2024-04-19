package com.xiaofei.algorithm.linkedlist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 总体思路就是一个容器存储所有数据;
 * 一个容器来存储排行榜;
 */
public class AllOne {
    public static void main(String[] args) {
        AllOne allOne = new AllOne();
        allOne.getMaxKey();
        allOne.inc("b");
        allOne.inc("a");
        allOne.inc("a");
        allOne.inc("a");
        allOne.dec("a");
        allOne.dec("a");
        System.out.println(allOne.getMaxKey());
        System.out.println(allOne.getMinKey());
    }


    Map<String, Node> nodeMap = new HashMap<>();
    Node root;

    public AllOne() {
        root = new Node("", 0);
        root.next = root;
        root.prev = root;

    }

    public void inc(String key) {
        //包含key
        if (nodeMap.containsKey(key)){
            Node origin = nodeMap.get(key);
            Node next = origin.next;
            if (origin.next == root || origin.next.count - origin.count > 1){
                //已经最大值/不需要修改排行榜,在origin后方插入
                Node newNode = origin.insert(new Node(key, origin.count + 1));
                nodeMap.put(key, newNode);
            }else {
                //不是最大值&&需要修改排行榜或者
                origin.next.addKey(key);
                nodeMap.put(key, origin.next);
            }
            //删除原来痕迹
            origin.removeKey(key);
            if (origin.isEmpty()){
                origin.removeNode();
            }
        }else {
            Node created = new Node(key, 1);
            nodeMap.put(key, created);
            //修改排行榜
            if (root.prev == root || root.prev.count > 1){
                //插入到root前面;
                root.insert(created);
            }else{
                root.prev.addKey(key);
            }
        }
    }

    public void dec(String key) {
        Node origin = nodeMap.get(key);
        if (origin.count == 1){
            nodeMap.remove(key);
        }else {
            if (origin.prev == root || origin.count - origin.prev.count > 1){
                Node newNode = origin.prev.insert(new Node(key, origin.count - 1));
                nodeMap.put(key, newNode);
            }else {
                origin.prev.addKey(key);
                nodeMap.put(key, origin.prev);
            }
        }
        origin.removeKey(key);
        if (origin.isEmpty()){
            origin.removeNode();
        }
    }

    public String getMaxKey() {
        return root.prev != null ? root.prev.keys.iterator().next() : "";
    }

    public String getMinKey() {
        return root.next != null ? root.next.keys.iterator().next() : "";
    }






}

class Node {
    Node prev;
    Node next;
    Set<String> keys;
    int count;

    public Node(String key, int count) {
        this.count = count;
        keys = new HashSet<String>();
        keys.add(key);
    }

    public void removeKey(String key) {
        keys.remove(key);
    }
    public void addKey(String key) {
        keys.add(key);
    }
    public boolean isEmpty(){
        return keys.isEmpty();
    }

    public Node insert(Node newNode){
        newNode.prev = this;
        newNode.next = this.next;
        newNode.prev.next = newNode;
        newNode.next.prev = newNode;
        return newNode;
    }

    public void removeNode(){
        this.prev.next = this.next;
        this.next.prev = this.prev;
    }
}
//    /**
//     * 双向链表,里面有一个Set<String>和int count;
//     * 这个root是哨兵节点,初始化,但是不会存入map;
//     * root.next存储的是数量最少的key;
//     * root.prev是数量最多的key;
//     */
//    Node root;
//    Map<String, Node> nodeMap;
//
//    public AllOne() {
//        nodeMap = new HashMap<String, Node>();
//        //初始化链表哨兵,下面判断节点的 next 若为 root,则表示 next 为空(prev 同理)
//        root = new Node("root", 0);
//        root.prev = root;
//        root.next = root;
//    }
//
//    /**
//     * 添加方法
//     * @param key
//     */
//    public void inc(String key) {
//        //如果map里面已经包含了key
//        if (nodeMap.containsKey(key)) {
//            //从map里面获取当前节点;
//            Node current = nodeMap.get(key);
//            //获取当前节点的下一个节点;
//            Node next = current.next;
//            //如果下一个等于哨兵节点,自己本身就是数量最多的key;或者数量差距大于1,也就是没有发生位置变化;
//            if (next == root || current.count + 1 < next.count) {
//                //那么就在自己的后面insert一个节点;
//                nodeMap.put(key, current.insert(new Node(key, current.count + 1)));
//            } else { //数量差距刚好等于1,加1之后就在同一个节点了;
//                next.keys.add(key);
//                nodeMap.put(key, next);
//            }
//            //刚才插入了一个新的链表,那么现在就要移除老链表,要么移除Set里面的key,要么整个节点都移除了;
//            //如果本来两个人住同一间屋子,那就一出那个人,然后开辟一个新的屋子;
//            current.keys.remove(key);
//            if (current.keys.isEmpty()) {
//                current.remove();
//            }
//        }
//        //map里面不包含key,也就是新来的;
//        else {
//            //如果最小的还没有,或者最小的大于1(新来的肯定是1),那么他就是最小的
//            if (root.next == root || root.next.count > 1) {
//                //key不在map中,先挂载到root节点后面,然后再put到map中;
//                nodeMap.put(key, root.insert(new Node(key, 1)));
//            } else {
//                //否则在Set里面加一个key就行了;
//                root.next.keys.add(key);
//                nodeMap.put(key, root.next);
//            }
//        }
//    }
//
//    public void dec(String key) {
//        Node current = nodeMap.get(key);
//        if (current.count == 1) {  // key 仅出现一次，将其移出 nodeMap
//            nodeMap.remove(key);
//        } else {
//            Node pre = current.prev;
//            //如果前面是root或者不需要变换位置;
//            if (pre == root || pre.count < current.count - 1) {
//                nodeMap.put(key, current.prev.insert(new Node(key, current.count - 1)));
//            } else { //需要变化位置;
//                pre.keys.add(key);
//                nodeMap.put(key, pre);
//            }
//        }
//        current.keys.remove(key);
//        if (current.keys.isEmpty()) {
//            current.remove();
//        }
//    }
//
//    public String getMaxKey() {
//        return root.prev != null ? root.prev.keys.iterator().next() : "";
//    }
//
//    public String getMinKey() {
//        return root.next != null ? root.next.keys.iterator().next() : "";
//    }
//}
//
///**
// * 环形双向链表
// */
//class Node {
//    Node prev;
//    Node next;
//    /**
//     * Set的作用:假设说最小的数量是1,但是a,b,c的数量都是1;
//     * 为了避免遍历,直接用Set存储;
//     */
//    Set<String> keys;
//    int count;
//
//    public Node() {
//        this("", 0);
//    }
//
//    public Node(String key, int count) {
//        this.count = count;
//        keys = new HashSet<String>();
//        keys.add(key);
//    }
//
//    /**
//     * 双向环形链表经典在this后插入node的方法;
//     * @param node
//     * @return
//     */
//    public Node insert(Node node) {
//        node.prev = this;
//        node.next = this.next;
//        node.prev.next = node;
//        node.next.prev = node;
//        return node;
//    }
//
//    /**
//     * 双向环形链表经典移除this的方法;
//     */
//    public void remove() {
//        this.prev.next = this.next;
//        this.next.prev = this.prev;
//    }


//}

