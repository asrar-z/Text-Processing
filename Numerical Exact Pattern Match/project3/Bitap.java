package project3;

import java.util.Arrays;

public class Bitap {

    public int match(String T, String P) {
        int n = T.length(), m = P.length();
        if (m > n) return -1;

        long X = 0, MASK = (1L << m) - 1; // MASK = 111...1 (of length m)
        long[] C = getComparisonVector(P);

        for (int j = 0; j < n; j++) {
            X = ((X << 1) & MASK) | C[T.charAt(j)];
            if (j >= m - 1 && ((X & (1L << m - 1)) == 0)) return j - m + 1;
        }
        return -1;
    }

    private long[] getComparisonVector(String P) {
        int m = P.length();
        long[] C = new long[256];
        Arrays.fill(C, (1L << m) - 1); // C[a] = 111…1 (of length m) for each a in alphabet

        for (int i = 0; i < m; i++) {
            C[P.charAt(i)] &= ~(1L << i); // C[P[i]][i] = 0
        }

        return C;
    }

}
