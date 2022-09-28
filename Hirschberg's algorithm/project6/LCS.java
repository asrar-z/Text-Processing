package project6;

public class LCS {

    public String find(String A, String B) {
        // Implement the Hirschberg LCS algorithm
        return ALG_C(A.length(), B.length(), A, B);
    }

    private void ALG_B(int m, int n, String A, String B, int[] LL) {
        int[][] K = new int[2][n + 1];
        /*Initialization: */ for (int j = 0; j <= n; j++) K[1][j] = 0;

        for (int i = 1; i <= m; i++) {
            for (int j = 0; j <= n; j++) K[0][j] = K[1][j];
            for (int j = 1; j <= n; j++) {
                if (A.charAt(i - 1) == B.charAt(j - 1)) {
                    K[1][j] = K[0][j - 1] + 1;
                } else {
                    K[1][j] = Integer.max(K[1][j - 1], K[0][j]);
                }
            }
        }
        // LL(j) <-- K(1, j) [j=0...n]
        for (int j = 0; j < K[1].length; j++) LL[j] = K[1][j];
    }

    public String ALG_C(int m, int n, String A, String B) {
        String e = ""; // e is the empty string
        String C;

        if (n == 0) {
            C = e;
        } else if (m == 1) {
            C = e;
            for (int j = 0; j < n; j++) {
                if (A.charAt(0) == B.charAt(j)) {
                    C = String.valueOf(A.charAt(0));
                    break;
                }
            }
        } else { // split problem
            int i = (int) Math.floor(m / 2.0);

            int[] L1 = new int[n + 1];
            int[] L2 = new int[n + 1];
            String Ahat = new StringBuilder(A.substring(i)).reverse().toString();
            String Bhat = new StringBuilder(B).reverse().toString();

            ALG_B(i, n, A.substring(0, i), B, L1);
            ALG_B(m - i, n, Ahat, Bhat, L2);

            int M, k;
            M = k = 0;
            for (int j = 0; j <= n; j++) {
                if (L1[j] + L2[n - j] > M) {
                    M = L1[j] + L2[n - j]; // M <-- max {L1(j) + L2(n-j)}
                    k = j;
                }
            }

            String C1 = ALG_C(i, k, A.substring(0, i), B.substring(0, k));
            String C2 = ALG_C(M - i, n - k, A.substring(i), B.substring(k));

            C = C1 + C2; // C <-- C1 || C2
        }
        return C;
    }
}
