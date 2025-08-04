package com.example.maths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SumGameManager {
    private static final int MAX_TARGET = 18; // Or change to other upper bound

    private int target;
    private ArrayList<Integer> options;
    private Random random = new Random();

    public void generateNewGame() {
        // Target: random from 5 to 18 inclusive
        target = 5 + random.nextInt(MAX_TARGET - 4); // [5, 18]
        options = generateOptions(target);
    }

    public int getTarget() {
        return target;
    }

    public ArrayList<Integer> getOptions() {
        return options;
    }

    public int calcSum(ArrayList<Integer> selectedIndexes) {
        int sum = 0;
        for (int idx : selectedIndexes) {
            sum += options.get(idx);
        }
        return sum;
    }

    // Generates a list of 5 options, at least 2 of which sum to 'target'
    private ArrayList<Integer> generateOptions(int target) {
        ArrayList<Integer> opts = new ArrayList<>();
        // Create at least a "solution"
        int part1 = 1 + random.nextInt(target - 1); // at least 1
        int part2 = target - part1;

        opts.add(part1);
        opts.add(part2);

        // Fill with distractors (random numbers, no 0s)
        while (opts.size() < 5) {
            int n = 1 + random.nextInt(MAX_TARGET - 1);
            // Avoid exact target and duplicate parts (for more challenge)
            if (n != target && !opts.contains(n)) {
                opts.add(n);
            }
        }
        Collections.shuffle(opts);
        return opts;
    }
}
