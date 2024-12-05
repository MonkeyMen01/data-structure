package queensolution.model;

import java.util.*;

import static queensolution.model.QueenConstants.OPEN_LOG;

public class TabuSearch {
    private final int maxIterations;
    private final int tabuSize;
    private final Queue<State> tabuList;
    private State bestState;
    private int stagnationCount;
    private final int maxStagnation;

    public TabuSearch(int maxIterations, int tabuSize, int maxStagnation) {
        this.maxIterations = maxIterations;
        this.tabuSize = tabuSize;
        this.tabuList = new LinkedList<>();
        this.maxStagnation = maxStagnation;
        this.stagnationCount = 0;
    }

    public State execute(State initialState) {
        State currentState = initialState;
        bestState = new State(currentState);
        int iteration = 0;
        while (iteration < maxIterations) {
            if (currentState.getConflicts() == 0) {
                System.out.println("找到完美解！" + "共花了" + iteration +"次Loop");
                currentState.printBoard();
                return currentState;
            }

            List<State> candidates = currentState.generateNextStates();
            State bestCandidate = getBestCandidate(candidates);

            if (bestCandidate == null) {
                if(OPEN_LOG){
                    System.out.println("Next States都在Tabu而且當前沒有最佳解，陷入Local Optima 將隨機重啟");
                }
                currentState = diversify(currentState);
                stagnationCount = 0;
                continue;
            }

            currentState = bestCandidate;
            updateTabuList(currentState);

            if (currentState.getConflicts() < bestState.getConflicts()) {
                bestState = new State(currentState);
                stagnationCount = 0;
            } else {
                stagnationCount++;
            }

            if (stagnationCount >= maxStagnation) {
                currentState = diversify(currentState);
                if(OPEN_LOG){
                    System.out.println("陷入Local Optima 將隨機重啟");
                    System.out.println(stagnationCount + "次未找到最佳解");
                }

                stagnationCount = 0;
            }
            iteration++;
            if(iteration == maxIterations) {
                System.out.println(iteration+"次Loop中並未找到最佳解");
            }
        }

        return bestState;
    }

    private State getBestCandidate(List<State> candidates) {
        State bestCandidate = null;
        int bestCandidateConflicts = Integer.MAX_VALUE;

        for (State candidate : candidates) {
            int conflicts = candidate.getConflicts();
            boolean aspirationCriteria = conflicts < bestState.getConflicts();

            if ((conflicts < bestCandidateConflicts) &&
                    (!isTabu(candidate) || aspirationCriteria)) {
                bestCandidate = candidate;
                bestCandidateConflicts = conflicts;
            }
        }
        return bestCandidate;
    }

    private boolean isTabu(State state) {
        return tabuList.contains(state);
    }

    private void updateTabuList(State state) {
        tabuList.offer(state);
        if (tabuList.size() > tabuSize) {
            tabuList.poll();
        }
    }

    private State diversify(State state) {
        List<State> nextStates = state.generateNextStates();
        return nextStates.get(new Random().nextInt(nextStates.size()));
    }
}