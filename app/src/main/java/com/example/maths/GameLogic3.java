package com.example.maths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameLogic3 {
    private ArrayList<Integer> currentChallenge;
    private ArrayList<Integer> availableNumbers;
    private int challengeType; // 0 = sequence, 1 = skip counting, 2 = missing numbers
    private int currentLevel;
    private Random random;

    public GameLogic3() {
        random = new Random();
        currentLevel = 1;
        generateNewChallenge();
    }

    public void generateNewChallenge() {
        currentChallenge = new ArrayList<>();
        availableNumbers = new ArrayList<>();

        // Determine challenge type based on level
        challengeType = currentLevel % 3;

        switch (challengeType) {
            case 0:
                generateSequenceChallenge();
                break;
            case 1:
                generateSkipCountingChallenge();
                break;
            case 2:
                generateMissingNumberChallenge();
                break;
        }
    }

    private void generateSequenceChallenge() {
        // Generate 5 consecutive numbers between 31-95
        int start = random.nextInt(60) + 31; // 31 to 90
        for (int i = 0; i < 5; i++) {
            currentChallenge.add(start + i);
            availableNumbers.add(start + i);
        }
        Collections.shuffle(availableNumbers);
    }

    private void generateSkipCountingChallenge() {
        // Generate skip counting sequence (by 2s, 5s, or 10s)
        int[] skipValues = {2, 5, 10};
        int skipBy = skipValues[random.nextInt(skipValues.length)];
        int start = random.nextInt(20) + 31; // 31 to 50

        for (int i = 0; i < 5; i++) {
            currentChallenge.add(start + (i * skipBy));
            availableNumbers.add(start + (i * skipBy));
        }
        Collections.shuffle(availableNumbers);
    }

    private void generateMissingNumberChallenge() {
        // Generate sequence with some numbers missing
        int start = random.nextInt(40) + 31; // 31 to 70
        ArrayList<Integer> fullSequence = new ArrayList<>();

        // Create full sequence of 7 numbers
        for (int i = 0; i < 7; i++) {
            fullSequence.add(start + i);
        }

        // Remove 2 random numbers to create "missing" challenge
        Collections.shuffle(fullSequence);
        for (int i = 0; i < 5; i++) {
            currentChallenge.add(fullSequence.get(i));
            availableNumbers.add(fullSequence.get(i));
        }

        // Sort the challenge to show correct order
        Collections.sort(currentChallenge);
        Collections.shuffle(availableNumbers);
    }

    public boolean isSequenceCorrect(ArrayList<Integer> userSequence) {
        if (userSequence.size() != currentChallenge.size()) return false;

        for (int i = 0; i < currentChallenge.size(); i++) {
            if (!userSequence.get(i).equals(currentChallenge.get(i))) {
                return false;
            }
        }
        return true;
    }

    public void nextLevel() {
        currentLevel++;
        generateNewChallenge();
    }

    public String getChallengeDescription() {
        switch (challengeType) {
            case 0:
                return "Arrange houses in numerical order from " +
                        Collections.min(currentChallenge) + " to " + Collections.max(currentChallenge);
            case 1:
                int skip = currentChallenge.get(1) - currentChallenge.get(0);
                return "Create a skip counting pattern by " + skip + "s";
            case 2:
                return "Fill in the missing house numbers";
            default:
                return "Arrange the house numbers correctly";
        }
    }

    // Getters
    public ArrayList<Integer> getCurrentChallenge() { return currentChallenge; }
    public ArrayList<Integer> getAvailableNumbers() { return availableNumbers; }
    public int getChallengeType() { return challengeType; }
    public int getCurrentLevel() { return currentLevel; }
}
