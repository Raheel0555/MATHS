package com.example.maths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QuizManager {
    private ArrayList<CoinInfo> coinPool;
    private CoinInfo currentCoin;
    private ArrayList<Integer> choices;
    private Random rand = new Random();

    public QuizManager(ArrayList<CoinInfo> coins) {
        this.coinPool = coins;
    }

    public void nextQuestion() {
        currentCoin = coinPool.get(rand.nextInt(coinPool.size()));
        choices = new ArrayList<>();
        choices.add(currentCoin.getValue());
        while (choices.size() < 4) {
            int v = coinPool.get(rand.nextInt(coinPool.size())).getValue();
            if (!choices.contains(v)) choices.add(v);
        }
        Collections.shuffle(choices);
    }

    public CoinInfo getCurrentCoin() { return currentCoin; }
    public ArrayList<Integer> getChoices() { return choices; }
    public boolean check(int value) { return value == currentCoin.getValue(); }
}
