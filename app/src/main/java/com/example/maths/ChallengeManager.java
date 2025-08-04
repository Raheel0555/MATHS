package com.example.maths;

import java.util.ArrayList;
import java.util.Random;

public class ChallengeManager {
    private int target;
    private ArrayList<CoinInfo> coinOptions;
    private Random rand = new Random();

    public ChallengeManager(ArrayList<CoinInfo> coins) {
        this.coinOptions = coins;
    }

    public void nextChallenge() {
        target = 5 + rand.nextInt(46); // 5...50
    }

    public int getTarget() {
        return target;
    }

    public ArrayList<CoinInfo> getOptions() {
        return coinOptions;
    }

    public int sum(ArrayList<Integer> indexes) {
        int total = 0;
        for (int idx : indexes) total += coinOptions.get(idx).getValue();
        return total;
    }
}
