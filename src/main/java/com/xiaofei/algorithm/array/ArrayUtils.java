package com.xiaofei.algorithm.array;

import java.util.Scanner;

public class ArrayUtils {
    /**
     * String转int[]
     * @param numString
     * @return
     */
    public static int[] creatIntArray(String numString){
        int[] nums = new int[numString.length()];
        for (int i = 0; i < numString.length(); i++) {
            nums[i] = Integer.parseInt(numString.substring(i, i+1));
        }
        return nums;
    }

    public static int[] creatIntArrayByScanner(){
        Scanner scanner = new Scanner(System.in);
        return creatIntArray(scanner.next());
    }
}
