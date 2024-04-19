package com.xiaofei.algorithm.array;

public class PlusOne {
    public static void main(String[] args) {
        PlusOne p = new PlusOne();
        int[] ints = p.plusOne(ArrayUtils.creatIntArrayByScanner());
        for (int i = 0; i < ints.length; i++) {
            System.out.print(ints[i]);
        }
    }
    public int[] plusOne(int[] digits) {
        for (int i =  digits.length - 1; i >= 0; i--) {
            int sum = digits[i] + 1;
            if (sum != 10){
                digits[i] = sum;
                break;
            }else {
                digits[i] = 0;
                if (i == 0){
                    int[] bigDigits = new int[digits.length + 1];
                    bigDigits[0] = 1;
                    return bigDigits;
                }
            }
        }
        return digits;
    }

}
