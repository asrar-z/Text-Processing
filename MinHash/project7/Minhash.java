package project7;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Minhash {

    /**
     * Number of permutations
     */
    private static final int PI = 200;

    /*
     * Example:
     * Row | S1| S2
     * 0   | 1 | 0
     * map of Integer to int[] array
     * */
    private static final Map<Integer, int[]> characteristicMatrix = new HashMap<>();

    private static final int doc_count = 2;

    public double jaccard(String fA, String fB) {
        /**
         * fA: Name of first file
         * fB: Name of second file
         */

        Set<String> S1 = getShingles(fA);
        Set<String> S2 = getShingles(fB);

        Map<String, Integer> S1Index = new HashMap<>();

        int i = 0;
        for (String s : S1) {
            S1Index.put(s, i);
            // Set S1=1 for shingles from doc 1
            characteristicMatrix.put(i++, new int[]{1, 0});
        }

        for (String s : S2) {
            if (S1Index.containsKey(s)) {
                // Set S1=1 and S2=1 for shingles present in both doc 1 and doc2
                characteristicMatrix.get(S1Index.get(s))[1] = 1;
            } else {
                // Set S1=0 and S2=1 for shingles from doc 2
                characteristicMatrix.put(i++, new int[]{0, 1});
            }
        }

        int[] permuteColumn = new int[i];
        for (int j = 0; j < i; j++) {
            permuteColumn[j] = j;
        }

        int[][] SIG = new int[PI][2];
        computeSignature(permuteColumn, SIG);
        return estimateJaccard(SIG);
    }

    private double estimateJaccard(int[][] SIG) {
        double same = 0;
        for (int j = 0; j < PI; j++) {
            if (SIG[j][0] == SIG[j][1]) {
                same++;
            }
        }
        return same / PI;
    }

    /**
     * Construct Signature Matrix
     * @param permuteColumn : random permutation column
     * @param SIG: matrix which points to the min index
     */
    private void computeSignature(int[] permuteColumn, int[][] SIG) {
        for (int i = 0; i < PI; i++) {
            randomPermute(permuteColumn);
            int[] minIndex = new int[permuteColumn.length];
            for (int j = 0; j < permuteColumn.length; j++) {
                minIndex[permuteColumn[j]] = j;
            }

            // find index of first row with 1 in Si & make it the signature
            for (int j = 0; j < doc_count; j++) {
                for (int min : minIndex) {
                    if (characteristicMatrix.get(min)[j] == 1) {
                        SIG[i][j] = min;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Generate random permutation from 0...shingles length
     * @param arr : array 0...shingles length
     */
    private void randomPermute(int[] arr) {
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            int randomIndex = rand.nextInt(arr.length);
            int temp = arr[randomIndex];
            arr[randomIndex] = arr[i];
            arr[i] = temp;
        }
    }

    /**
     * Get Unique Shingles
     * @param f : filename
     * @return unique set
     */
    private Set<String> getShingles(String f) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(f), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<>(lines);
    }

}
