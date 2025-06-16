package longestcommonsubsequence;

public class LongestCommonSubsequence {

    /**
     * 計算兩個字串的最長公共子序列長度
     * @param text1 第一個字串
     * @param text2 第二個字串
     * @return LCS 的長度
     */
    public static int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();

        // 建立 DP 表格，dp[i][j] 代表 text1[0...i-1] 和 text2[0...j-1] 的 LCS 長度
        int[][] dp = new int[m + 1][n + 1];

        // 填充 DP 表格
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    // 字元相同，LCS 長度 +1
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    // 字元不同，取較大的 LCS 長度
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[m][n];
    }

    /**
     * 取得實際的最長公共子序列字串
     * @param text1 第一個字串
     * @param text2 第二個字串
     * @return LCS 字串
     */
    public static String getLCSString(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m + 1][n + 1];

        // 建立 DP 表格
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        // 回溯找出實際的 LCS 字串
        StringBuilder lcs = new StringBuilder();
        int i = m, j = n;

        while (i > 0 && j > 0) {
            if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                // 找到相同字元，加入結果
                lcs.append(text1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }

        // 因為是倒序建立的，需要反轉
        return lcs.reverse().toString();
    }

    /**
     * 空間優化版本 - 只需要 O(min(m,n)) 空間
     */
    public static int longestCommonSubsequenceOptimized(String text1, String text2) {
        // 確保 text1 是較短的字串以節省空間
        if (text1.length() > text2.length()) {
            return longestCommonSubsequenceOptimized(text2, text1);
        }

        int m = text1.length();
        int n = text2.length();

        // 只需要兩行的空間
        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 1; j <= n; j++) {
            for (int i = 1; i <= m; i++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    curr[i] = prev[i - 1] + 1;
                } else {
                    curr[i] = Math.max(prev[i], curr[i - 1]);
                }
            }
            // 交換陣列
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[m];
    }

    /**
     * 視覺化 DP 表格（類似黑板上的圖）
     */
    public static void visualizeLCS(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m + 1][n + 1];

        // 建立 DP 表格
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        // 打印表格標題
        System.out.println("\n=== LCS 動態規劃視覺化 ===");
        System.out.println("字串 1 (縱軸): " + text1);
        System.out.println("字串 2 (橫軸): " + text2);
        System.out.println();

        // 打印列標題
        System.out.print("    j ");
        for (int j = 0; j <= n; j++) {
            System.out.printf("%4d", j);
        }
        System.out.println();

        // 打印字串2字元
        System.out.print("      ");
        for (int j = 0; j <= n; j++) {
            if (j == 0) {
                System.out.print("   ε");
            } else {
                System.out.printf("%4c", text2.charAt(j - 1));
            }
        }
        System.out.println();

        // 打印分隔線
        System.out.print("i     ");
        for (int j = 0; j <= n; j++) {
            System.out.print("----");
        }
        System.out.println();

        // 打印DP表格內容
        for (int i = 0; i <= m; i++) {
            // 打印行號和字元
            if (i == 0) {
                System.out.printf("%d ε |", i);
            } else {
                System.out.printf("%d %c |", i, text1.charAt(i - 1));
            }

            // 打印該行的值
            for (int j = 0; j <= n; j++) {
                System.out.printf("%4d", dp[i][j]);
            }
            System.out.println();
        }

        System.out.println("\n最長公共子序列長度: " + dp[m][n]);
        System.out.println("最長公共子序列: " + getLCSString(text1, text2));
    }

    /**
     * 打印帶箭頭的 DP 表格（模擬黑板上的箭頭方向）
     */
    public static void visualizeLCSWithArrows(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        char[][] direction = new char[m + 1][n + 1];

        // 建立 DP 表格並記錄方向
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    direction[i][j] = '↖'; // 對角線箭頭
                } else {
                    if (dp[i - 1][j] > dp[i][j - 1]) {
                        dp[i][j] = dp[i - 1][j];
                        direction[i][j] = '↑'; // 向上箭頭
                    } else {
                        dp[i][j] = dp[i][j - 1];
                        direction[i][j] = '←'; // 向左箭頭
                    }
                }
            }
        }

        System.out.println("\n=== 帶箭頭的 DP 表格 ===");
        System.out.println("↖: 字元相同，來自對角線");
        System.out.println("↑: 來自上方");
        System.out.println("←: 來自左方");
        System.out.println();

        // 打印表格標題
        System.out.print("     ");
        for (int j = 0; j <= n; j++) {
            if (j == 0) {
                System.out.print("  ε ");
            } else {
                System.out.printf("  %c ", text2.charAt(j - 1));
            }
        }
        System.out.println();

        // 打印DP表格內容
        for (int i = 0; i <= m; i++) {
            // 打印行標籤
            if (i == 0) {
                System.out.print("ε  ");
            } else {
                System.out.printf("%c  ", text1.charAt(i - 1));
            }

            // 打印該行的值和箭頭
            for (int j = 0; j <= n; j++) {
                if (i == 0 || j == 0) {
                    System.out.printf("%2d  ", dp[i][j]);
                } else {
                    System.out.printf("%d%c ", dp[i][j], direction[i][j]);
                }
            }
            System.out.println();
        }

        // 打印回溯路徑
        System.out.println("\n回溯路徑（找出實際的LCS）:");
        printBacktrackPath(text1, text2, dp, direction);
    }

    /**
     * 打印回溯路徑
     */
    private static void printBacktrackPath(String text1, String text2, int[][] dp, char[][] direction) {
        int i = text1.length(), j = text2.length();
        StringBuilder path = new StringBuilder();
        StringBuilder lcs = new StringBuilder();

        while (i > 0 && j > 0) {
            path.append(String.format("(%d,%d)", i, j));

            if (direction[i][j] == '↖') {
                lcs.append(text1.charAt(i - 1));
                path.append(" ↖ ");
                i--;
                j--;
            } else if (direction[i][j] == '↑') {
                path.append(" ↑ ");
                i--;
            } else {
                path.append(" ← ");
                j--;
            }
        }

        System.out.println("路徑: " + path.toString());
        System.out.println("LCS: " + lcs.reverse().toString());
    }

    /**
     * 測試方法
     */
    public static void main(String[] args) {
        // 黑板上的例子
        String text1 = "ABCBDAB";  // 對應黑板上的縱軸
        String text2 = "BDCABA";  // 對應黑板上的橫軸

        System.out.println("=== 黑板範例 ===");
        visualizeLCS(text1, text2);
        visualizeLCSWithArrows(text1, text2);

        System.out.println("\n" + "=".repeat(50));

        // 其他測試案例
        String test1 = "ABCDGH";
        String test2 = "AEDFHR";

        System.out.println("\n=== 其他範例 ===");
        visualizeLCS(test1, test2);
        visualizeLCSWithArrows(test1, test2);
    }

    private static void testLCS(String s1, String s2) {
        System.out.printf("'%s' 和 '%s' 的 LCS: '%s' (長度: %d)\n",
                s1, s2, getLCSString(s1, s2), longestCommonSubsequence(s1, s2));
    }
}