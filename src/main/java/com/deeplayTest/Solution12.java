package com.deeplayTest;

import java.util.*;

public class Solution12 {

    public static List<Integer> getRightOrder(List<Integer> list) {
        List<Integer> result = new ArrayList<>(Collections.nCopies(list.size(), 0));
        List<Integer> sortedList = list.stream().sorted().toList();
        int oddIndex = 0, evenIndex = list.size() - 1;
        for (Integer i : sortedList) {
            if (i != 0)
                if (i % 2 != 0) {
                    result.set(oddIndex, i);
                    oddIndex++;
                } else {
                    result.set(evenIndex, i);
                    evenIndex--;
                }
        }
        return result;
    }

    public static <T> List<T> findMostFrequentElements(List<T> list) {
        Map<T, Integer> frequencyMap = new HashMap<>();

        for (T element : list) {
            frequencyMap.put(element, frequencyMap.getOrDefault(element, 0) + 1);
        }
        int maxFrequency = Collections.max(frequencyMap.values());

        List<T> mostFrequentElements = new ArrayList<>();

        for (Map.Entry<T, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() == maxFrequency) {
                mostFrequentElements.add(entry.getKey());
            }
        }

        return mostFrequentElements;
    }

    public static void main(String[] args) {
        Random rand = new Random();

        int size = Config.DEFAULT_ARRAY_SIZE;
        List<Integer> arr = rand.ints(size, Config.RANDOM_MIN, Config.RANDOM_MAX)
                .boxed()
                .toList();

        System.out.println("Generated array: " + arr);
        System.out.println("Reordered array: " + getRightOrder(arr));
        System.out.println("Most frequent element: " + findMostFrequentElements(arr));
    }
}
