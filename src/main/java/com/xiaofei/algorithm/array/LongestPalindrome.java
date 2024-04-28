package com.xiaofei.algorithm.array;

public class LongestPalindrome {
    public static void main(String[] args) {
        LongestPalindrome l = new LongestPalindrome();
        String p = l.longestPalindrome("cbacdcxyzyx");
        System.out.println(p);
    }
    public String longestPalindrome(String s) {
        if (s.length() < 2) {
            return s;
        }
        //转换成char数组;
        char[] charArray = s.toCharArray();
        int max = 0;
        int beign = 0;
        for (int i = 0; i < charArray.length - 1; i++) {
            //单数找回文获得的长度
            int odd = odd(charArray, i);
            //双数找回文获得的长度
            int even = even(charArray, i, i + 1);
            //记录最长回文;
            if (odd > even) {
                even = odd;
            }
            if (even > max) {
                max = even;
                beign = i - (max - 1) / 2;
            }
        }
        return s.substring(beign, beign + max);
    }

    /**
     * 中间往两边扩散找
     * @param chars
     * @param i
     * @return 回文数长度
     */
    public static int odd(char[] chars, int i) {
        int len = 1;
        int left = i - 1;
        int right = i + 1;
        //while条件:不越界
        while (left >= 0 && right < chars.length) {
            if (chars[left] != chars[right]) {
                return len;
            } else {
                //累积并计算长度
                len += 2;
                left--;
                right++;
            }
        }
        return len;
    }

    /**
     * 从中间往两边找,edd是单数找回文,even是双数找回文;
     * @param chars
     * @param i
     * @param iPlus
     * @return
     */
    public static int even(char[] chars, int i, int iPlus) {
        if (chars[i] != chars[iPlus]) {
            return -1;
        }
        int len = 2;
        int left = i - 1;
        int right = iPlus + 1;
        while (left >= 0 && right < chars.length) {
            if (chars[left] != chars[right]) {
                return len;
            } else {
                len += 2;
                left--;
                right++;
            }
        }
        return len;
    }


    //暴力破解
//    public String longestPalindrome(String s) {
//        int leftIndex = 0, rightIndex = 0, length = 0;
//        for (int i = 0; i < s.length() - 1; i++) {
//            char left = s.charAt(i);
//            for (int j = i + 1; j < s.length(); j++) {
//                char right = s.charAt(j);
//                if (left == right){
//                    //判断是否是回文;
//                    //==>如果是=>添加记录==>继续走
//                    //==>如果不是=>继续走
//                    if(isPalindrome(s, i, j) && j - i > length){
//                        length = j - i;
//                        leftIndex = i;
//                        rightIndex = j;
//                    }
//                }
//            }
//        }
//        String result = s.substring(leftIndex, rightIndex + 1);
//        return result;
//    }
//
//    private boolean isPalindrome(String s, int left, int right) {
//        boolean flag = true;
//        left++;
//        right--;
//        while (left < right){
//            if (s.charAt(left) != s.charAt(right)){
//                flag = false;
//                break;
//            }
//            left++;
//            right--;
//        }
//        return flag;
//    }
}
