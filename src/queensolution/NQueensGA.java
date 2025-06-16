package queensolution;

import java.util.*;

public class NQueensGA {
    private int nQueens;
    private int populationSize;
    private int eliteSize;
    private double mutationRate;
    private Random random;
    private int[] bestSolution;
    private int bestConflicts;
    private List<Integer> conflictHistory;
    private int noImprovementCount;
    private double bestFitnessSoFar;
    private static final int GENERATION_Max = 50000;
    private static final int GENERATION_RESTART = 30;


    public NQueensGA(int nQueens) {
        this.nQueens = nQueens;
        this.populationSize = 20000;  // 增加族群大小
        this.eliteSize = 20;          // 增加精英數量
        this.mutationRate = 0.005;    // 設定合適的突變率
        this.random = new Random();
        this.conflictHistory = new ArrayList<>();
        this.bestConflicts = Integer.MAX_VALUE;
        this.noImprovementCount = 0;
        this.bestFitnessSoFar = Double.MAX_VALUE;
    }

    private int calculateConflicts(int[] positions) {
        int conflictCount = 0;
        int n = positions.length;
        StringBuilder logMessage = new StringBuilder();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (positions[i] == positions[j]) {
                    conflictCount++;
                    logMessage.append(String.format(
                            "列衝突: 第%d行和第%d行的皇后在同一列 (第%d列)\n",
                            i + 1, j + 1, positions[i]
                    ));
                } else if (Math.abs(positions[i] - positions[j]) == j - i) {
                    conflictCount++;
                    logMessage.append(String.format(
                            "對角線衝突: 第%d行的皇后(%d,%d)和第%d行的皇后(%d,%d)\n",
                            i + 1, i + 1, positions[i], j + 1, j + 1, positions[j]
                    ));
                }
            }
        }
        logMessage.append(String.format(
                "詳細計算得出的衝突總數: %d", conflictCount
        ));
        System.out.println(logMessage);
        return conflictCount;
    }

    public void printBoard(int[] queens) {
        for (int i = 0; i < queens.length; i++) {
            for (int j = 0; j < queens.length; j++) {
                System.out.print(queens[i] == j ? "Q " : ". ");
            }
            System.out.println();
        }
        System.out.print("棋盤位置"+ Arrays.toString(queens));
        System.out.println("衝突數:"+ calculateConflicts(queens));
    }

    private int[] createIndividual() {
        int[] individual = new int[nQueens];
        for (int i = 0; i < nQueens; i++) {
            individual[i] = i;
        }
        // Fisher-Yates shuffle
        for (int i = nQueens - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = individual[i];
            individual[i] = individual[j];
            individual[j] = temp;
        }
        return individual;
    }

    private double calculateFitness(int[] individual) {
        int attacks = 0;
        for (int i = 0; i < nQueens; i++) {
            for (int j = i + 1; j < nQueens; j++) {
                if (individual[i] == individual[j] ||
                        Math.abs(individual[i] - individual[j]) == Math.abs(i - j)) {
                    attacks++;
                }
            }
        }
        return attacks;  // 負號使得攻擊數越少，適應度越高
    }

    private int[] tournamentSelection(List<int[]> population) {
        int tournamentSize = 5;  // 增加tournament大小
        int[] best = null;
        double bestFitness = Double.POSITIVE_INFINITY;

        for (int i = 0; i < tournamentSize; i++) {
            int[] individual = population.get(random.nextInt(population.size()));
            double fitness = calculateFitness(individual);
            if (fitness < bestFitness) {  // 適應度越高越好
                best = individual.clone();
                bestFitness = fitness;
            }
        }
        return best;
    }

    private int[][] crossover(int[] parent1, int[] parent2) {
        int point1 = random.nextInt(nQueens);
        int point2 = random.nextInt(nQueens);
        if (point1 > point2) {
            int temp = point1;
            point1 = point2;
            point2 = temp;
        }

        int[][] children = new int[2][nQueens];
        boolean[] used1 = new boolean[nQueens];
        boolean[] used2 = new boolean[nQueens];

        // Copy crossover segment
        for (int i = point1; i < point2; i++) {
            children[0][i] = parent1[i];
            children[1][i] = parent2[i];
            used1[parent1[i]] = true;
            used2[parent2[i]] = true;
        }

        // Fill remaining positions
        int pos1 = 0, pos2 = 0;
        for (int i = 0; i < nQueens; i++) {
            if (i == point1) {
                i = point2;
                continue;
            }
            while (pos1 < nQueens && used1[parent2[pos1]]) pos1++;
            while (pos2 < nQueens && used2[parent1[pos2]]) pos2++;

            if (pos1 < nQueens) children[0][i] = parent2[pos1++];
            if (pos2 < nQueens) children[1][i] = parent1[pos2++];
        }

        return children;
    }

    private void mutate(int[] individual) {
        if (random.nextDouble() < mutationRate) {
            int i = random.nextInt(nQueens);
            int j = random.nextInt(nQueens);
            int temp = individual[i];
            individual[i] = individual[j];
            individual[j] = temp;
        }
    }

    public int[] solve() {
        List<int[]> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(createIndividual());
        }

        for (int generation = 0; generation < GENERATION_Max; generation++) {  // 增加世代數
            population.sort((a, b) -> Double.compare(calculateFitness(a), calculateFitness(b)));

            double currentBestFitness = calculateFitness(population.get(0));

            // 更新最佳解記錄
            if (currentBestFitness < bestFitnessSoFar) {
                bestFitnessSoFar = currentBestFitness;
                bestSolution = population.get(0).clone();
                noImprovementCount = 0;
            } else {
                noImprovementCount++;
            }

            // 如果連續50代沒有改善，進行重啟
            if (noImprovementCount > GENERATION_RESTART) {
                for (int i = populationSize/2; i < populationSize; i++) {
                    population.set(i, createIndividual());
                }
                noImprovementCount = 0;
                System.out.println("Generation " + generation + ": 執行重啟");
            }

            List<int[]> newPopulation = new ArrayList<>();
            // Add elites
            for (int i = 0; i < eliteSize; i++) {
                newPopulation.add(population.get(i).clone());
            }

            // Create new individuals
            while (newPopulation.size() < populationSize) {
                int[] parent1 = tournamentSelection(population);
                int[] parent2 = tournamentSelection(population);
                int[][] children = crossover(parent1, parent2);

                mutate(children[0]);
                mutate(children[1]);

                newPopulation.add(children[0]);
                if (newPopulation.size() < populationSize) {
                    newPopulation.add(children[1]);
                }
            }

            population = newPopulation;

//            if (generation % 10 == 0) {
//                System.out.printf("Generation %d: Best Fitness = %.2f%n",
//                        generation, currentBestFitness);
//            }
//            System.out.printf("Generation %d: Best Fitness = %.2f%n",
//                    generation, currentBestFitness);

            System.out.printf("Generation %d: Worst Fitness = %.2f%n",
                    generation, calculateFitness(population.get(population.size() - 1)));

            // 找到無衝突解就結束
            if (currentBestFitness == 0) {
                System.out.println("找到完美解！在第 " + generation + " 代");
                break;
            }
        }

        return bestSolution != null ? bestSolution : population.get(0);
    }

    public static void main(String[] args) {
        NQueensGA ga = new NQueensGA(12);
        System.out.println("開始求解"+ ga.nQueens + "皇后問題...");
        int[] solution = ga.solve();
        System.out.println("\n最終解: " + Arrays.toString(solution));
        System.out.println("\n最終棋盤狀態:");
        ga.printBoard(solution);
    }
}