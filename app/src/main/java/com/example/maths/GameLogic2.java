package com.example.maths;

import java.util.Random;

public class GameLogic2 {
    private int targetNumber;
    private int currentTens;
    private int currentOnes;
    private Random random;

    public GameLogic2() {
        random = new Random();
        generateNewChallenge();
    }

    public void generateNewChallenge() {
        // Generate numbers between 21-30
        targetNumber = random.nextInt(10) + 21;
        currentTens = 0;
        currentOnes = 0;
    }

    public void updateCounts(int tensCount, int onesCount) {
        currentTens = tensCount;
        currentOnes = onesCount;
    }

    public boolean isCorrect() {
        return (currentTens * 10 + currentOnes) == targetNumber;
    }

    public int getCurrentTotal() {
        return currentTens * 10 + currentOnes;
    }

    public int getTargetNumber() { return targetNumber; }
    public int getRequiredTens() { return targetNumber / 10; }
    public int getRequiredOnes() { return targetNumber % 10; }
}
