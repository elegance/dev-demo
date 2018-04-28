package org.orh.leetcode;

import java.util.Arrays;

/**
 * @author orh
 * @description <pre>
 * 给定数组: nums = [1,1,2],
 * 你的函数应该返回新长度 2, 并且原数组nums的前两个元素必须是1和2
 * 不需要理会新的数组长度后面的元素
 * </pre>
 * @date 2018/4/13 9:42
 */
public class Array21 {
    public static void main(String[] args) {
        int[] nums = {0, 1, 1, 2, 2, 2, 3, 4, 4, 5, 5, 5};
        System.out.println(Arrays.toString(nums));
        System.out.println(removeDuplicates(nums));
        System.out.println(Arrays.toString(nums));
    }

    public static int removeDuplicates(int[] nums) {
        // 表示 不重复元素区间的结束索引值
        int uniqeEnd = 0;

        // 处理 uniqEnd ..i , 每次只要 i 与  uniqEnd 比较

        // 从 1 开始往前匹配
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[uniqeEnd]) {
                // 交换保留了重复的元素
                swap(nums, ++uniqeEnd, i);
            }
        }
        return uniqeEnd + 1;
    }

    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
