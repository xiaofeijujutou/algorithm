package com.xiaofei.algorithm.array;

public class IsMatch {
    public static void main(String[] args) {
        IsMatch i = new IsMatch();
        System.out.println(i.isMatch("mississippi", "mis*is*ip*."));
    }
    public boolean isMatch(String s, String p) {
        char[] string = s.toCharArray();
        char[] pattern = p.toCharArray();
        for (int i = 0; i < pattern.length; i++) {
            if (string[0] == pattern[i] || pattern[i] == '.'){
                if (doMatch(string, pattern ,i)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 执行一次匹配
     * @param string
     * @param pattern
     * @param pIndex
     * @return
     */
    private boolean doMatch(char[] string, char[] pattern, int pIndex) {
        if (pattern[pIndex] == '.' && pIndex + 1 != pattern.length && pattern[pIndex + 1] == '*'){
            return true;
        }


        return false;
    }

}
