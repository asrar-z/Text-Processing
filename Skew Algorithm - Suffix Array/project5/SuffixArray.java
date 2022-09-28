package project5;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class SuffixArray {

    public ArrayList<Integer> construct(String S) {
        int[] suffixArray = createSuffixArray(toIntArray(S));
        ArrayList<Integer> ans = new ArrayList<>();
        for (int element : suffixArray) ans.add(element);
        return ans;
    }

    private int[] createSuffixArray(int[] S) {
        int n = S.length, group0 = (int) Math.ceil(n / 3.0), group1 = (int) Math.ceil((n - 1) / 3.0), group2 = (int) Math.ceil((n - 2) / 3.0);
        int[] s = padding(S);
        int triplesCount = group0 + group2;

        int[] S12 = get12Triples(s, triplesCount);
        S12 = sortTriples(s, triplesCount, S12);

        int[] tokensInLexOrder = getTokensForTriples(s, triplesCount, S12, group0);
        int[] suffixArray = suffixArrayUtil(s, triplesCount, S12, tokensInLexOrder);

        int[] array12 = get12Array(suffixArray, group0, triplesCount);
        int[] array0 = get0Array(s, array12, group0, triplesCount);

        return mergeArrays(s, array0, array12, triplesCount, n, group0, group1);
    }

    private int[] suffixArrayUtil(int[] s, int triplesCount, int[] S12, int[] tokensInLexOrder) {
        int[] suffixArray = new int[triplesCount];
        if (isUnique(s, triplesCount, S12)) {
            suffixArray = createSuffixArray(tokensInLexOrder);
        } else {
            for (int i = 0; i < triplesCount; i++) {
                suffixArray[tokensInLexOrder[i] - 1] = i;
            }
        }
        return suffixArray;
    }

    private int[] mergeArrays(int[] s, int[] array0, int[] array12, int triplesCount, int n, int group0, int group1) {
        int[] mergedArray = new int[group0 + triplesCount - (group0 - group1)], result12 = new int[s.length];

        int i = 0;
        int j = n % 3 == 1 ? 1 : 0;
        int k = 0;

        IntStream.range(0, triplesCount).forEach(x -> result12[array12[x]] = x + 1);

        for (int cnt = 0; cnt < triplesCount + group0; cnt++) {
            if (i == group0) {
                while (j < triplesCount) mergedArray[k++] = array12[j++];
                break;
            }

            if (j == triplesCount) {
                while (i < group0) mergedArray[k++] = array0[i++];
                break;
            }

            if (array12[j] % 3 == 1) {
                if (compareTwoSuffixes(s[array0[i]],
                        s[array12[j]],
                        result12[array0[i] + 1],
                        result12[array12[j] + 1])) {
                    mergedArray[k++] = array0[i++];
                } else {
                    mergedArray[k++] = array12[j++];
                }
            } else {
                if (compareThreeSuffixes(s[array0[i]],
                        s[array12[j]],
                        s[array0[i] + 1],
                        s[array12[j] + 1],
                        result12[array0[i] + 2],
                        result12[array12[j] + 2])) {
                    mergedArray[k++] = array0[i++];
                } else {
                    mergedArray[k++] = array12[j++];
                }
            }
        }

        return mergedArray;
    }

    private static int[] radixSort(int offset, int[] s, int[] group, int triplesCount) {
        int MAX = Arrays.stream(s).max().orElse(0);
        int[] bucket = new int[MAX + 1];
        int[] sorted = new int[triplesCount];
        IntStream.range(0, triplesCount).forEach(i -> {
            bucket[s[group[i] + offset]]++;
        });
        IntStream.rangeClosed(1, MAX).forEach(i -> bucket[i] += bucket[i - 1]);
        int i = triplesCount - 1;
        while (i >= 0) {
            sorted[--bucket[s[group[i] + offset]]] = group[i];
            i--;
        }
        return sorted;
    }

    private int[] sortTriples(int[] s, int triplesCount, int[] S12) {
        S12 = radixSort(2, s, S12, triplesCount);
        S12 = radixSort(1, s, S12, triplesCount);
        return radixSort(0, s, S12, triplesCount);
    }

    public static boolean compareTwoSuffixes(int a, int b, int c, int d) {
        return (b > a) || ((a == b) && (c < d));
    }

    public static boolean compareThreeSuffixes(int a, int b, int c, int d, int e, int f) {
        return compareTwoSuffixes(a, b, c, d) || (a == b && c == d && e < f);
    }

    private static int[] toIntArray(String S) {
        int[] s = new int[S.length()];
        for (int i = 0; i < S.length(); i++) s[i] = S.charAt(i);
        return s;
    }

    public static int[] padding(int[] S) {
        int n = S.length;
        int[] s;
        switch (n % 3) {
            case 0:
            case 2:
                s = new int[n + 2];
                s[n] = 0;
                s[n + 1] = 0;
                break;
            default:
                s = new int[n + 3];
                s[n] = 0;
                s[n + 1] = 0;
                s[n + 2] = 0;
        }
        System.arraycopy(S, 0, s, 0, n);
        return s;
    }

    private int[] get0Array(int[] s, int[] Array12, int group0, int triplesCount) {
        int[] Array0 = new int[group0];
        int i = 0, j = 0;
        while (i < triplesCount) {
            if (Array12[i] % 3 == 1) {
                Array0[j++] = Array12[i] - 1;
            }
            i++;
        }
        Array0 = radixSort(0, s, Array0, group0);
        return Array0;
    }

    private int[] get12Array(int[] suffixArray, int group0, int triplesCount) {
        int[] Array12 = new int[triplesCount];
        IntStream.range(0, triplesCount).forEach(i ->
                Array12[i] = suffixArray[i] >= group0 ? 2 + 3 * (suffixArray[i] - group0) : 1 + 3 * suffixArray[i]
        );
        return Array12;
    }

    private boolean isUnique(int[] s, int triplesCount, int[] S12) {
        for (int i = 1; i < triplesCount; i++) {
            if (containsDuplicates(s, S12, i)) return true;
        }
        return false;
    }

    private int[] getTokensForTriples(int[] s, int triplesCount, int[] S12, int group0) {
        int[] tokens = new int[triplesCount];
        tokens[0] = 1;
        int nexToken = 1;
        for (int i = 1; i < triplesCount; i++) {
            if (!containsDuplicates(s, S12, i)) nexToken++;
            tokens[i] = nexToken;
        }
        int[] tokensInLexOrder = new int[triplesCount];
        IntStream.range(0, triplesCount).forEach(j -> {
            if (S12[j] % 3 == 2) {
                tokensInLexOrder[(S12[j] - 2) / 3 + group0] = tokens[j];
            } else if (S12[j] % 3 == 1) {
                tokensInLexOrder[(S12[j] - 1) / 3] = tokens[j];
            }
        });
        return tokensInLexOrder;
    }

    private boolean containsDuplicates(int[] s, int[] S12, int i) {
        return s[S12[i]] == (s[S12[i - 1]])
                && s[S12[i] + 1] == (s[S12[i - 1] + 1])
                && s[S12[i] + 2] == (s[S12[i - 1] + 2]);
    }

    private int[] get12Triples(int[] s, int triplesCount) {
        int[] S12 = new int[triplesCount];

        int i = 0, j = 0;
        while (i < s.length - 2) {
            if (i % 3 != 0) S12[j++] = i;
            i++;
        }
        return S12;
    }

}
