package com.xiaofei.algorithm.array;

import java.util.ArrayList;
import java.util.List;

public class Convert {
    public static void main(String[] args) {
        Convert c = new Convert();
        c.convert("fdsafdsafdsaf",5);


    }
    public String convert(String s, int numRows) {
        if (numRows == 1){
            return s;
        }
        //初始化list;
        List<StringBuilder> list = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            list.add(new StringBuilder());
        }
        int index = 0;
        int add = 1;
        for (int i = 0; i < s.length(); i++) {
            list.get(index).append(s.charAt(i));
            index += add;
            if (index == numRows - 1){
                add = -1;
            }
            if (index == 0){
                add = 1;
            }
        }
        StringBuilder b1 = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            b1.append(list.get(i).toString());
        }
        return b1.toString();
    }
}
