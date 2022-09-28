package project3;

public class RK {
    public int match(String T, String P) {
        int n = T.length(), m = P.length();
        if (m > n) return -1;

        int H = 0, f = 0;
        for (int i = 1; i <= m; i++) {
            // hash of pattern
            H ^= s(h(P.charAt(i - 1)), m - i);
        }

        for (int i = 0; i <= n - m; i++) {
            if (i == 0) {
                // initial hash -> H = h(P)
                for (int j = 1; j <= m; j++) {
                    f ^= s(h(T.charAt(j - 1)), m - j);
                }
            } else {
                // Cyclic Polynomial Rolling Hash
                f = shiftHash(f, T, i, m);
            }
            if (f == H) {
                // check P against T[i : i + m − 1]
                int j = 0;
                while (j < m && T.charAt(i + j) == P.charAt(j)) {
                    j++;
                    if (j == m) return i;
                }
            }
        }
        return -1;
    }

    private int shiftHash(int f, String T, int i, int k) {
        return s(f, 1) ^ s(h(T.charAt(i - 1)), k) ^ h(T.charAt(i + k - 1));
    }

    private int s(int bits, int k) {
        return (bits >>> k) | (bits << (Integer.SIZE - k));
    }

    private int h(char c) {
        return c - 'A';
    }
}