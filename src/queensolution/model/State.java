package queensolution.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static queensolution.model.QueenConstants.OPEN_NEXT_STATES_COUNT;

public class State {
    private int[] position;
    private int conflicts;
    private String conflictsMessage;

    public int[] getPosition() {
        return position.clone();
    }

    public int getConflicts() {
        return conflicts;
    }

    public String getConflictsMessage() {
        return conflictsMessage;
    }

    public State(int[] position) {
        this.position = position.clone();
        calculateConflicts(position);
    }

    public State(State other) {
        this.position = other.position.clone();
        this.conflicts = other.conflicts;
        this.conflictsMessage = other.conflictsMessage;
    }

    private void calculateConflicts(int[] positions) {
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
        this.conflictsMessage = logMessage.toString();
        this.conflicts = conflictCount;
    }

    public List<State> generateNextStates(){
        List<State> neighbors = new ArrayList<>();
        int boardSize = position.length;
        for(int i = 0; i < boardSize; i++){
            int[] leftMove = position.clone();
            if(position[i] == 0 ){
                leftMove[i] = boardSize -1;
            }else {
                leftMove[i] = position[i] - 1;
            }
            neighbors.add(new State(leftMove));

            int[] rightMove = position.clone();
            if(position[i] == boardSize -1 ){
                rightMove[i] = 0;
            }else {
                rightMove[i] = position[i] + 1;
            }
            neighbors.add(new State(rightMove));
        }
        if(OPEN_NEXT_STATES_COUNT){
            System.out.println("next states count: " + neighbors.size());
        }
        return neighbors;
    }

    public void printBoard() {
        for (int i = 0; i < position.length; i++) {
            for (int j = 0; j < position.length; j++) {
                System.out.print(position[j] == i ? "Q " : ". ");
            }
            System.out.println();
        }
        System.out.print("棋盤位置"+ Arrays.toString(position));
        System.out.println("衝突數:"+ conflicts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Arrays.equals(position, state.position);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(position);
    }
}
