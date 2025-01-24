package com.deeplayTest;

import java.util.*;

public class Solution4 {

    public static void partitionConsecutiveSums(int[] arr, int K) {

        long totalSum = Arrays.stream(arr).sum();

        if (K <= 0) {
            System.out.println("невозможно");
        }

        long ksum = (long) K * (K-1) / 2;
        long adjusted = totalSum - ksum;
        if (adjusted % K != 0) {
            System.out.println("невозможно");
            return;
        }

        long L = adjusted / K;
        long[] targets = new long[K];
        for (int i = 0; i < K; i++) {
            targets[i] = L + i;
        }

        long[] remain = Arrays.copyOf(targets, K);

        Integer[] arrObj = Arrays.stream(arr).boxed().toArray(Integer[]::new);
        Arrays.sort(arrObj, (a, b) -> Integer.compare(Math.abs(b), Math.abs(a)));
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arrObj[i];
        }

        int[] groupOfElement = new int[arr.length];
        Arrays.fill(groupOfElement, -1);

        Map<String, Boolean> memo = new HashMap<>();

        boolean can = backtrack(arr, 0, remain, groupOfElement, memo);

        if (!can) {
            System.out.println("невозможно");
            return;
        }

        List<List<Integer>> groups = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            groups.add(new ArrayList<>());
        }
        for (int i = 0; i < arr.length; i++) {
            int g = groupOfElement[i];
            groups.get(g).add(arr[i]);
        }

        for (int i = 0; i < K; i++) {
            long sumGroup = 0;
            for (int val : groups.get(i)) {
                sumGroup += val;
            }
            System.out.print(groups.get(i) + "," + sumGroup + " ");
        }
        System.out.println();
    }

    private static boolean backtrack(int[] arr, int index,
                                     long[] remain,
                                     int[] groupOfElement,
                                     Map<String, Boolean> memo)
    {
        if (index == arr.length) {
            for (long r : remain) {
                if (r != 0) return false;
            }
            return true;
        }

        String key = index + "_" + Arrays.hashCode(remain);
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        int val = arr[index];

        for (int i = 0; i < remain.length; i++) {
            long oldNeed = remain[i];
            long newNeed = oldNeed - val;

            remain[i] = newNeed;
            groupOfElement[index] = i;

            if (backtrack(arr, index + 1, remain, groupOfElement, memo)) {
                memo.put(key, true);
                remain[i] = oldNeed;
                return true;
            }

            remain[i] = oldNeed;
            groupOfElement[index] = -1;
        }

        memo.put(key, false);
        return false;
    }


    public static void main(String[] args) {
        partitionConsecutiveSums(new int[]{10, 11, 7, 7, 12}, 2);
        partitionConsecutiveSums(new int[]{5, 2, 6, 4, 3, 6}, 4);
        partitionConsecutiveSums(new int[]{7, 8, 12, 1}, 3);
        partitionConsecutiveSums(new int[]{2, -1, 3, 1}, 2);
    }
}
