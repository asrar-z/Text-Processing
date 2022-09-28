package project2;

public class KMP {

    public int match(String T, String P) {
        int n = T.length(), m = P.length();
        int[] F = buildFailureFunction(P);
        char[] txt = T.toCharArray(), pat = P.toCharArray();
        int i = 0, j = 0;

        while (i < n) {
            if (txt[i] == pat[j]) {
                i++;
                j++;
                if (j == m) return i-m;
            } else {
                if (j != 0) {
                    j = F[j - 1];
                } else {
                    i++;
                }
            }
        }
        return -1;
    }

    private int[] buildFailureFunction(String pattern) {
        int m = pattern.length();
        int[] Failure = new int[m];
        char[] c = pattern.toCharArray();
        int j = 0, i = 1;

        while (i < m) {
            if (c[i] == c[j]) {
                Failure[i++] = ++j;
            } else {
                if (j == 0) {
                    Failure[i++] = 0;
                } else {
                    j = Failure[j - 1];
                }
            }
        }
        return Failure;
    }
}