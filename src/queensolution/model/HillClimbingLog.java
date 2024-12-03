package queensolution.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static queensolution.model.QueenConstants.PRINT_CONFLICTS_MESSAGE;

public class HillClimbingLog {
    private final int maxIteration;
    private final int maxRestarts;
    private int currentRestarts;
    private List<State> visitedStates;

    public HillClimbingLog(int maxIteration, int maxRestarts) {
        this.maxIteration = maxIteration;
        this.maxRestarts = maxRestarts;
        this.currentRestarts = 0;
        this.visitedStates = new ArrayList<>();
    }

    public State execute(State initialState) {
        State currentState = initialState;
        State bestState = currentState;
        int iteration = 0;
        while (currentRestarts < maxRestarts) {
            while (iteration < maxIteration) {
                if (currentState.getConflicts() == 0) {
                    System.out.println("find global optima !!");
                    System.out.println("總共重啟次數:"+currentRestarts);
                    currentState.printBoard();
                    return currentState;
                }
                List<State> nextStates = currentState.generateNextStates();
                State bestNextState = null;
                int bestConflicts = currentState.getConflicts();

                for (State nextState : nextStates) {
                    if (nextState.getConflicts() < bestConflicts && !visitedStates.contains(nextState)) {
                        bestNextState = nextState;
                        bestConflicts = nextState.getConflicts();
                    }
                }

                if (bestNextState == null || bestNextState.getConflicts() >= currentState.getConflicts()) {
                    currentRestarts++;

                    System.out.println("warning: local optima :"+currentRestarts);
                    if(PRINT_CONFLICTS_MESSAGE){
                        System.out.println(currentState.getConflictsMessage());
                        System.out.println("--------------------------------");
                    }

                    if (currentRestarts >= maxRestarts) {
                        System.out.println("reached max restarts:"+currentRestarts);
                        System.out.println("目前解的衝突數:" + bestConflicts);
                        System.out.print("棋盤位置"+ Arrays.toString(currentState.getPosition()));
                        return bestState;
                    }
                    currentState = restart(nextStates,initialState.getPosition().length);
                    iteration = 0;
                    continue;
                }

                visitedStates.add(currentState);
                currentState = bestNextState;

                if (currentState.getConflicts() < bestState.getConflicts()) {
                    bestState = new State(currentState);
                }

                iteration++;

            }

            currentRestarts++;
            List<State> currentStates = currentState.generateNextStates();
            currentState = restart(currentStates,initialState.getPosition().length);
            iteration = 0;
        }
        if(bestState.getConflicts() == 0) {
            System.out.println("find global optima !!");
            System.out.println("總共重啟次數:"+currentRestarts);
            bestState.printBoard();
        }else {
            System.out.println("\n未找到完美解，最佳解（衝突數：" + bestState.getConflicts() + "）：");
            System.out.println(bestState.getConflictsMessage());
        }
        return bestState;
    }

    private State restart(List<State> nextStates, int size) {
        if (!nextStates.isEmpty()) {
            // 不是完全隨機選擇，而是傾向選擇衝突較少的狀態
            nextStates.sort(Comparator.comparingInt(State::getConflicts));
            int bound = Math.min(5, nextStates.size());  // 從前5個較好的狀態中選擇
            return nextStates.get(new Random().nextInt(bound));
        } else {
            // 生成初始狀態時，確保皇后分布更均勻
            int[] positions = new int[size];
            List<Integer> availableColumns = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                availableColumns.add(i);
            }
            Collections.shuffle(availableColumns);
            for (int i = 0; i < size; i++) {
                positions[i] = availableColumns.get(i);
            }
            return new State(positions);
        }
    }
}
