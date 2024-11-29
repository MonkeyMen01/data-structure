package QueenSolution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class NQueensConflictCounter {
    public static final String POSITIONS = "positions";

    public static final String K_QUEEN = "kQueen";

    public static void main(String[] args) {
        Map<String,Object> input = inputScanner();

        int k = (int)input.get(K_QUEEN);
        int [] positions = (int[]) input.get(POSITIONS);

        int conflicts = countConflicts(positions,k);
        System.out.println("衝突總數: " + conflicts);

        printConflictDetails(positions);
    }

    public static Map<String, Object> inputScanner() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("請輸入皇后數量:");
        int k = scanner.nextInt();
        scanner.nextLine();

        System.out.println("請輸入皇后位置序列(以空格分隔)");
        String[] inputArray = scanner.nextLine().split(" ");

        if (inputArray.length != k) {
            throw new IllegalArgumentException("輸入的位置數量與皇后數量不符");
        }

        Map<String, Object> result = new HashMap<>();
        int[] positions = new int[k];

        for (int i = 0; i < k; i++) {
            positions[i] = Integer.parseInt(inputArray[i]);
        }
        for(int j: positions){
            if (j >= k){
                throw new IllegalArgumentException("皇后位置與長度不符合");
            }
        }

        System.out.println("皇后數量: " + k);
        System.out.println("皇后位置: " + Arrays.toString(positions));

        result.put(K_QUEEN, k);
        result.put(POSITIONS, positions);
        return result;
    }

    private static int countConflicts(int[] positions,int kQuantity) {
        int conflicts = 0;
        int n = positions.length;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (positions[i] == positions[j] ||
                        Math.abs(positions[i] - positions[j]) == j - i) {
                    conflicts++;
                }
            }
        }

        return conflicts;
    }

    private static void printConflictDetails(int[] positions) {
        int n = positions.length;
        int conflictCount = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (positions[i] == positions[j]) {
                    if(i ==0){
                        System.out.printf("列衝突: 第%d行和第%d行的皇后在同一列 (第%d列)\n", i+1, j+1, positions[i]);
                    }
                    conflictCount++;
                } else if (Math.abs(positions[i] - positions[j]) == j - i) {
                    if(i == 0){
                        System.out.printf("對角線衝突: 第%d行的皇后(%d,%d)和第%d行的皇后(%d,%d)\n",
                                i+1, i+1, positions[i], j+1, j+1, positions[j]);
                    }
                    conflictCount++;
                }
            }
        }
        System.out.println("詳細計算得出的衝突總數: " + conflictCount);
    }
}