package org.orh.leetcode;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author orh
 * @description 洗牌算法
 * @date 2018/4/11 10:45
 */
public class Shutter {

    public static void main(String[] args) {
        int[] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        char[] arr2 = {'A', 'B', 'C', 'D', 'E', 'F', 'I', 'J'};
        System.out.println(Arrays.toString(arr));
        shutter(arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void shutter(int[] arr) {
        // 每次确定 数组(未确定)最后一位的元素，确定方法
        // 第 1 轮，最后一个未确定元素为：arr.length - 1, 即最后一位，从 [0, arr.length-1] 中取随机，放置在 arr.length-i-1位置上
        // 第 2 轮，最后一个未确定元素为：arr.length - 1 - 1...
        // 第 3 轮，最后一个未确定元素为：arr.length - 1 - 1 -1...
        // ...
        for (int i = 0; i < arr.length; i++) {
            // sure pos : arr.length - i - 1
            int pos = arr.length - i - 1;
            int randomIdx = ThreadLocalRandom.current().nextInt(0, pos + 1);
            swap(arr, pos, randomIdx);
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
