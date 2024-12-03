package queensolution;

import queensolution.model.HillClimbingLog;
import queensolution.model.SimulatedAnnealing;
import queensolution.model.State;
import queensolution.model.TabuSearch;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import static queensolution.model.QueenConstants.AUTO_RUN_times;
import static queensolution.model.QueenConstants.MAX_ITERATIONS;
import static queensolution.model.QueenConstants.MAX_STAGNATION;
import static queensolution.model.QueenConstants.OPEN_AUTO_RUN;
import static queensolution.model.QueenConstants.OPEN_AUTO_RUN_SIZE;
import static queensolution.model.QueenConstants.QUEEN_COUNT;
import static queensolution.model.QueenConstants.QUEEN_POSITIONS;
import static queensolution.model.QueenConstants.TABU_SIZE;

public class QueensMethods {


    public static void main(String[] args) {
//        solution();
        solutionOfTabuSearch();
//        solutionOfSimulatedAnnealing();
    }

    private static Map<String, Object> inputScanner() {
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

        result.put(QUEEN_COUNT, k);
        result.put(QUEEN_POSITIONS, positions);
        return result;
    }

    /**
     * 隨機重啟
     */
    public static void solution(){
        Map<String,Object> input = inputScanner();
        int k = (int)input.get(QUEEN_COUNT);
        int [] initPositions = (int[]) input.get(QUEEN_POSITIONS);
        State iniState = new State(initPositions);
        HillClimbingLog hillClimbingLog = new HillClimbingLog(10000,10000);
        hillClimbingLog.execute(iniState);
    }


    /**
     * TabuSearch
     */
    public static void solutionOfTabuSearch() {
        State initState = null;

        if(OPEN_AUTO_RUN){
            int currentRunTimes = 0;
            int successTimes = 0;
            int failTimes = 0;
            for(int i =0 ; i < AUTO_RUN_times;i++){
                int[] Queen =  randomlyGeneratedQueen(OPEN_AUTO_RUN_SIZE);
                initState = new State(Queen);
                TabuSearch tabuSearch = new TabuSearch(MAX_ITERATIONS, TABU_SIZE, MAX_STAGNATION);
                State solution = tabuSearch.execute(initState);
                if(solution.getConflicts() == 0){
                    successTimes++;
                }else {
                    failTimes++;
                }
                currentRunTimes++;
            }
            System.out.println("成功次數:"+successTimes);
            System.out.println("失敗次數"+failTimes);
        }else {
            Map<String,Object> input = inputScanner();
            int k = (int)input.get(QUEEN_COUNT);
            int[] initPositions = (int[]) input.get(QUEEN_POSITIONS);
            initState = new State(initPositions);
            TabuSearch tabuSearch = new TabuSearch(MAX_ITERATIONS, TABU_SIZE, MAX_STAGNATION);
            State solution = tabuSearch.execute(initState);
        }


    }

    /**
     * SimulatedAnnealing
     */
    public static void solutionOfSimulatedAnnealing() {
        Map<String,Object> input = inputScanner();
        int k = (int)input.get(QUEEN_COUNT);
        int[] initPositions = (int[]) input.get(QUEEN_POSITIONS);
        State iniState = new State(initPositions);

        State initialState = new State(initPositions);
        SimulatedAnnealing sa = new SimulatedAnnealing(100.0, 0.995, 10000);
        State solution = sa.execute(initialState);
    }

    public static int[] randomlyGeneratedQueen(int size) {
        int[] positions = new int[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            positions[i] = random.nextInt(size);
        }

        return positions;
    }
}

