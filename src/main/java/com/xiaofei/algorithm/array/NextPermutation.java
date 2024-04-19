package com.xiaofei.algorithm.array;

public class NextPermutation {
    public static void main(String[] args) {
        NextPermutation n = new NextPermutation();
        int[] ints = ArrayUtils.creatIntArray("4876555321");
        n.nextPermutation(ints);
        System.out.println("4876555321");
        for (int anInt : ints) {
            System.out.print(anInt);
        }

    }

    /**
     * 总结:从后往前遍历,保证反向单调递增,找到递减的第一个数据;
     *      然后找到大于递减数的最小数,交换之后中间的间隙反转;
     * @param nums
     */
    public void nextPermutation(int[] nums) {
        int i = nums.length - 2;
        //检查递增性,比较倒数第一个和倒数第二个,扫过的部分均为从后往前递增;
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            //如果左边的大于右边的,继续往左走一格;
            i--;
        }
        //走到这里,i就是要交换的元素的索引,后面的都是递增,这里是递减,从递增区间找到最小大于等于元素后交换再反转;保证是字典最小
        if (i >= 0) {
            int j = nums.length - 1;
            //循环往前面走,找到比索引数小的数进行交换
            while (j >= 0 && nums[i] >= nums[j]) {
                j--;
            }
            swap(nums, i, j);
        }
        reverse(nums, i + 1);
    }

    public void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    /**
     * 反转方法,已知是最大排列了,就让他最后一个和最前面一个交换顺序
     * @param nums
     * @param start
     */
    public void reverse(int[] nums, int start) {
        int left = start, right = nums.length - 1;
        while (left < right) {
            swap(nums, left, right);
            left++;
            right--;
        }
    }


}
