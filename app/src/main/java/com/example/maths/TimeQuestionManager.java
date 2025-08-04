package com.example.maths;

import java.util.Random;

public class TimeQuestionManager {
    private int correctHour, correctMinute;
    private Random random = new Random();

    public void generateQuestion() {
        correctHour = random.nextInt(12); // 0-11
        correctMinute = random.nextInt(12) * 5; // 0,5,10,...55
    }

    public boolean checkAnswer(int hour, int minute) {
        return (hour == correctHour && minute == correctMinute);
    }

    public String getQuestionTimeText() {
        String h = String.format("%02d", correctHour == 0 ? 12 : correctHour);
        String m = String.format("%02d", correctMinute);
        return h + ":" + m;
    }
}
