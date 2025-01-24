package com.deeplayTest;

import java.util.Random;

public class Solution3 {

    public static void main(String[] args) {
        int[] seq1 = {4, 2, 4};
        int[] seq2 = {4, 4, 4};
        int rollsNumber = 1000;
        long trials = 100000000;

        double[] result = computeProbabilities(seq1, seq2, rollsNumber);
        double[] resultMonteCarlo = monteCarloProbabilities(seq1, seq2, rollsNumber, trials);
        System.out.println("Результаты динамического программирования:");
        System.out.printf("P(Игрок 1 > Игрок 2) = %.6f%n", result[0]);
        System.out.printf("P(Ничья)             = %.6f%n", result[1]);
        System.out.printf("P(Игрок 2 > Игрок 1) = %.6f%n", result[2]);
        System.out.println("Результаты метода Монте-Карло:");
        System.out.printf("P(Игрок 1 > Игрок 2) = %.6f%n", resultMonteCarlo[0]);
        System.out.printf("P(Ничья)             = %.6f%n", resultMonteCarlo[1]);
        System.out.printf("P(Игрок 2 > Игрок 1) = %.6f%n", resultMonteCarlo[2]);
    }

    public static double[] getSequenceProbabilities(int[] seq, int rollsNumber) {
        int seqLength = seq.length;
        double sequenceProbability = 1.0 / Math.pow(Config.DICE_SIDES, seqLength);
        double notSequenceProbability = 1 - sequenceProbability;

        double[][] dp = new double[rollsNumber + 1][rollsNumber / seqLength + 1];
        dp[0][0] = 1.0;
        for (int i = 0; i <= rollsNumber; i++) {
            for (int j = 0; j <= rollsNumber / seqLength; j++) {
                double current = dp[i][j];
                if (current == 0) continue;
                if ( i + seqLength <= rollsNumber) {
                    dp[i + seqLength][j + 1] += current * sequenceProbability;
                    dp[i + 1][j] += current * notSequenceProbability;
                } else if (i < rollsNumber) {
                    dp[i + 1][j] += current;
                }
            }
        }

        double[] distribution = new double[rollsNumber/seqLength + 1];
        for (int i = 0; i < distribution.length; i++) {
            distribution[i] = dp[rollsNumber][i];
        }
        return distribution;
    }

    public static double[] computeProbabilities(int[] seq1, int[] seq2, int rollsNumber) {
        double p1Win = 0, p2Win = 0, tie = 0;

        double[] seq1Dist = getSequenceProbabilities(seq1, rollsNumber);
        double[] seq2Dist = getSequenceProbabilities(seq2, rollsNumber);

        for (int i = 0; i < seq1Dist.length; i++) {
            for (int j = 0; j < seq2Dist.length; j++) {
                double probability = seq1Dist[i] * seq2Dist[j];
                if (i > j) {
                    p1Win += probability;
                } else if (i == j) {
                    tie += probability;
                } else {
                    p2Win += probability;
                }
            }
        }
        return new double[]{p1Win, tie, p2Win};
    }


    public static double[] monteCarloProbabilities(int[] seq1, int[] seq2,
                                                   int rollsNumber, long trials) {
        Random rand = new Random();
        int countP1Wins = 0, countTie = 0, countP2Wins = 0;

        for (int t = 0; t < trials; t++) {
            int[] allRolls = new int[rollsNumber];
            for (int i = 0; i < rollsNumber; i++) {
                allRolls[i] = 1 + rand.nextInt(Config.DICE_SIDES);
            }

            int score1 = countNonOverlapping(allRolls, seq1);
            int score2 = countNonOverlapping(allRolls, seq2);

            if (score1 > score2) {
                countP1Wins++;
            } else if (score1 == score2) {
                countTie++;
            } else {
                countP2Wins++;
            }
        }

        double p1Wins = (double) countP1Wins / trials;
        double tie = (double) countTie / trials;
        double p2Wins = (double) countP2Wins / trials;

        return new double[]{p1Wins, tie, p2Wins};
    }

    public static int countNonOverlapping(int[] rollsNumber, int[] seq) {
        int count = 0;
        int i = 0;
        while (i <= rollsNumber.length - seq.length) {
            if (matches(rollsNumber, i, seq)) {
                count++;
                i += seq.length;
            } else {
                i++;
            }
        }
        return count;
    }

    private static boolean matches(int[] rollsNumber, int start, int[] seq) {
        for (int k = 0; k < seq.length; k++) {
            if (rollsNumber.length - start < seq.length || rollsNumber[start + k] != seq[k]) {
                return false;
            }
        }
        return true;
    }
}
