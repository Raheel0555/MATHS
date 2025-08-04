package com.example.maths;
import java.util.Random;

public class GameLogic {
    private int minuend, subtrahend, answer;
    private Random random = new Random();

    public void newProblem() {
        minuend = random.nextInt(6) + 5; // 5-10
        subtrahend = random.nextInt(minuend - 1) + 1; // 1 to (minuend-1)
        answer = minuend - subtrahend;
    }

    public int getMinuend() { return minuend; }
    public int getSubtrahend() { return subtrahend; }
    public int getAnswer() { return answer; }
}
