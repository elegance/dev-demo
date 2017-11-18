package org.orh.data.structure;

/**
 * 朴素的模式匹配算法-挨个对比字符 
 */
public class CharSequenceCompare {
    public static void main(String[] args) {
        char[] mainS = {'g', 'o', 'o', 'd', 'g', 'o', 'o', 'g', 'l', 'e'}; // 模拟主字符串 ：goodgoogle
        char[] targetS = {'g', 'o', 'o', 'g', 'l', 'e'}; // 模拟目标字符串: google

        System.out.println(indexOf(mainS, targetS));
        
        char[] targetS2 = {'g', 'o'};
        System.out.println(indexOf(mainS, targetS2));

        char[] targetS3 = {'t', 'o'};
        System.out.println(indexOf(mainS, targetS3));
    }

    public static int indexOf(char[] mainS, char[] targetS) {
        if (mainS == null || targetS == null || mainS.length < targetS.length) {
            throw new IllegalArgumentException("参数不合法！");
        }
        int i, j;

        for (i = 0, j = 0; i < mainS.length && j < targetS.length;) {
            if (mainS[i] == targetS[j]) {
                j++;
                i++;
            } else {
                i = i - j + 1; // 每次不等时，外部大循环 起始值往后挪动1位，即从1开始，下次从2开始，再下次3开始....
                j = 0; // j 归0，小循环从头开始
            }
        }

        if (j != 0) {
            return i - targetS.length;
        }
        return -1;
    }
}
