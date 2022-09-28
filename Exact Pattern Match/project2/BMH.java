package project2;

public class BMH {

    public int match(String T, String P) {
        int[] L = lastOccurrenceFunction(P);
        int n = T.length(), m = P.length();
        int i = m-1, j=m-1;
        while (i<n) {
            if(T.charAt(i) == P.charAt(j)){
                if(j==0){
                    return i;
                }else{
                    j--;i--;
                }
            }else{
                int l = L[T.charAt(i)];
                i = i+m-Math.min(j, 1+l);
                j=m-1;
            }
        }
        return -1;
    }

    private int[] lastOccurrenceFunction(String P) {
        int[] L = new int[256];
        for (int i = 0; i < 256; i++) L[i] = -1;
        for (int i = 0; i < P.length(); i++) L[P.charAt(i)] = i;
        return L;
    }
}
