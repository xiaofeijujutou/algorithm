package com.xiaofei.algorithm.linkedlist;

import java.util.HashMap;
import java.util.Map;

public class LFUCache {
    public static void main(String[] args) {
        LFUCache redis = new LFUCache(10);
        redis.put(2, 20);
        redis.put(1, 20);
        redis.put(5, 20);
        System.out.println(redis.get(5));

    }
    final Map<Integer, Integer> redis;
    final int capacity;
    /**
     * 构造器,初始化容量;
     * 在这里的LFU中,k-v都是int;
     * @param capacity 容量
     */
    public LFUCache(int capacity) {
        redis = new HashMap<>(capacity);
        this.capacity = capacity;
    }

    /**
     * 根据key,获取value;
     * @param key
     * @return
     */
    public int get(int key) {
        return redis.getOrDefault(key, -1);
    }

    /**
     * 存取数据;
     * @param key
     * @param value
     */
    public void put(int key, int value) {
        if (redis.size() == capacity){
            //TODO 移除使用频率最少的key,频率相同,移除时间最久没使用的;

        }
        redis.put(key, value);
    }
}
