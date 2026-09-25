package rip.vantage.commons.util.string;

import java.util.Arrays;

public class StringSimilarityUtil {
    public StringSimilarityUtil() {
    }

    public static double getSimilarity(String var0, String var1) {
        int i = var0.length();
        int j = var1.length();
        int[][] aint = new int[i + 1][j + 1];

        for (int k = 0; k <= i; k++) {
            for (int l = 0; l <= j; l++) {
                if (k == 0) {
                    aint[k][l] = l;
                } else if (l == 0) {
                    aint[k][l] = k;
                } else {
                    aint[k][l] = min(aint[k - 1][l - 1] + costOfSubstitution(var0.charAt(k - 1), var1.charAt(l - 1)), aint[k - 1][l] + 1, aint[k][l - 1] + 1);
                }
            }
        }

        int i1 = Math.max(i, j);
        return 1.0 - (double)aint[i][j] / i1;
    }

    private static int costOfSubstitution(char var0, char var1) {
        return var0 == var1 ? 0 : 1;
    }

    private static int min(int... var0) {
        return Arrays.stream(var0).min().orElse(Integer.MAX_VALUE);
    }
}
