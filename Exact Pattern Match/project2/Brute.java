package project2;

public class Brute {

    public int match(String T, String P) {
        for (int i = 0; i < T.length(); i++) {
            for (int j = 0; j < P.length(); j++) {
                if (T.charAt(i+j) != P.charAt(j)) break;
                if (j == P.length() - 1) return i;
            }
        }
        return -1;
    }

}
