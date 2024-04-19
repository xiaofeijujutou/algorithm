package com.xiaofei.algorithm.array;

import java.util.ArrayList;
import java.util.List;

public class SetZeroes {
    public static void main(String[] args) {
        SetZeroes s = new SetZeroes();
    }
    public void setZeroes(int[][] matrix) {
        List<String> index = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0){
                    index.add(i + "," + j);
                }
            }
        }
        //没有0,直接返回
        if (index.size() == 0){
            return;
        }
        for (String s : index) {
            String[] split = s.split(",");
            int[] matrixI = matrix[Integer.parseInt(split[0])];
            //x轴
            for (int i = 0; i < matrixI.length; i++) {
                matrixI[i] = 0;
            }
            //y轴
            for (int[] ints : matrix) {
                ints[Integer.parseInt(split[1])] = 0;
            }
        }

    }
}
