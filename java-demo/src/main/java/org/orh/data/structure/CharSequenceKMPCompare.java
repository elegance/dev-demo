package org.orh.data.structure;

public class CharSequenceKMPCompare {

    public static void main(String[] args) {
        char[] mainS = {'g', 'o', 'o', 'd', 'g', 'o', 'o', 'g', 'l', 'e'}; // 模拟主字符串 ：goodgoogle
        char[] targetS = {'g', 'o', 'o', 'g', 'l', 'e'}; // 模拟目标字符串: google

        System.out.println(indexOf(mainS, targetS));

        char[] targetS2 = {'g', 'o'};
        System.out.println(indexOf(mainS, targetS2));

        char[] targetS3 = {'g', 'l', 'e'};
        System.out.println(indexOf(mainS, targetS3));
        
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            indexOf(mainS, targetS3);
        }
        System.out.println("elapsed:" + (System.currentTimeMillis() - start));
    }

    private static void getNext(char[] target, int[] next) {
        int i = 1, j = 0;
        next[0] = 0;

        while (i < target.length) {
            if (j == 0 || target[i] == target[j]) {
                ++i;
                ++j;
                next[i] = j;
            } else {
                j = next[j]; // 若字符不相同，则j值回溯
            }
        }
    }

    public static int indexOf(char[] source, char[] target) {
        int i = 0; // 主串当前位置下标
        int j = 0; // 子串当前位置下标

        int next[] = new int[255]; // 定义next 数组

        getNext(target, next); // 对串T进行分析，得到next数组

        while (i < source.length && j < target.length) { /* 若 i 小于 source的长度，且j小于target的长度： 循环继续 */
            if (j == 0 || source[i] == target[j]) { /* 两位相等则继续，与朴素算法相比增加了 j==0 的条件 */
                i++;
                j++;
            } else {
                j = next[j]; /* j 退回合适的位置，i值不变 */
            }
        }
//        System.out.println("i:" + i + "   j:" + j);

        if (j != 0 && target[0] == source[i - target.length]) {
            return i - target.length;
        }
        return -1;
    }
}
