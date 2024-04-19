package com.xiaofei.algorithm.array;

public class RemoveDuplicates {
    public static void main(String[] args) {
        RemoveDuplicates r = new RemoveDuplicates();
        r.removeDuplicates(new int[]{1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9});
    }
    public int removeDuplicates(int[] nums) {
        int leftIndex = 0, rightIndex = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[leftIndex] == nums[rightIndex]){
                rightIndex++;
            }else {
                leftIndex++;
                nums[leftIndex] = nums[rightIndex];
                rightIndex++;
            }
        }
        return leftIndex + 1;
    }
}
