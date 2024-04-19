package com.xiaofei.algorithm.array;

public class RemoveElement {
    public static void main(String[] args) {
        RemoveElement r = new RemoveElement();

    }
    public int removeElement(int[] nums, int val) {

        int leftIndex = 0, rightIndex = 0, resultLength = nums.length;

        for (int i = 0; i < nums.length; i++) {
            if (val == nums[rightIndex]){
                rightIndex++;
                resultLength--;
            }else {
                nums[leftIndex] = nums[rightIndex];
                leftIndex++;
                rightIndex++;
            }
        }
        return resultLength;
    }
}
