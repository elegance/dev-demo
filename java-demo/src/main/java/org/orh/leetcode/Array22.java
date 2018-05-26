package org.orh.leetcode;

/**
 * @author orh
 * @description <pre>
 *  假设有一个数组，它的第 i 个元素是一个给定的股票在第 i 天的价格。
 * 设计一个算法来找到最大的利润。你可以完成尽可能多的交易（多次买卖股票）。然而，你不能同时参与多个交易（你必须在再次购买前出售股票）。
 * </pre>
 * @date 2018/4/13 10:33
 */
public class Array22 {
    public static void main(String[] args) {
        int[] prices = {5, 4, 5, 4, 5, 6, 6, 4, 5, 6, 6, 7};
        System.out.println(maxProfit(prices));
    }

    public static int maxProfit(int[] prices) {
        int profit = 0;
        for (int i = 1; i < prices.length; i++) {
            // 首涨  买入 -> i > i-1
            if (prices[i] > prices[i - 1]) {
                // purchase
                profit += prices[i] - prices[i - 1];
            }
        }
        return profit;
    }
}
