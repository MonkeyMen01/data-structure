package QueenSolution.model;

import java.util.List;
import java.util.Random;

public class SimulatedAnnealing {
    private final double initialTemperature;
    private final double coolingRate;
    private final int maxIterations;
    private static final Random random = new Random();

    public SimulatedAnnealing(double initialTemperature, double coolingRate, int maxIterations) {
        this.initialTemperature = initialTemperature;
        this.coolingRate = coolingRate;
        this.maxIterations = maxIterations;
    }

    public State execute(State initialState) {
        State currentState = initialState;
        State bestState = new State(currentState);
        double temperature = initialTemperature;
        int iteration = 0;

        while (iteration < maxIterations && temperature > 0.1) {
            if (currentState.getConflicts() == 0) {
                System.out.println("找到完美解！");
                currentState.printBoard();
                return currentState;
            }

            State neighborState = getRandomNeighbor(currentState);
            int deltaE = neighborState.getConflicts() - currentState.getConflicts();

            if (deltaE < 0 || acceptanceProbability(deltaE, temperature) > random.nextDouble()) {
                currentState = neighborState;
                if (currentState.getConflicts() < bestState.getConflicts()) {
                    bestState = new State(currentState);
                }
            }

            temperature *= coolingRate;
            iteration++;

            if (iteration % 100 == 0) {
                System.out.printf("迭代: %d, 溫度: %.2f, 當前衝突: %d%n",
                        iteration, temperature, currentState.getConflicts());
            }
        }
        return bestState;
    }

    private State getRandomNeighbor(State state) {
        List<State> neighbors = state.generateNextStates();
        return neighbors.get(random.nextInt(neighbors.size()));
    }

    private double acceptanceProbability(int deltaE, double temperature) {
        return Math.exp(-deltaE / temperature);
    }
}