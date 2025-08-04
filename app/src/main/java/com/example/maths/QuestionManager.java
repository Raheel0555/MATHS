package com.example.maths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionManager {
    private List<QuestionModel> questions;
    private int current = 0;

    public QuestionManager() {
        questions = new ArrayList<>();
        questions.add(new QuestionModel("2 + 2", new int[]{2, 3, 5, 4}, 3));
        questions.add(new QuestionModel("5 + 2", new int[]{1, 7, 5, 2}, 1));
        questions.add(new QuestionModel("3 + 1", new int[]{3, 5, 4, 2}, 2));
        questions.add(new QuestionModel("6 + 2", new int[]{8, 2, 4, 5}, 0));
        questions.add(new QuestionModel("4 + 1", new int[]{2, 5, 1, 0}, 1));
        Collections.shuffle(questions);
    }

    public QuestionModel nextQuestion() {
        current = (current + 1) % questions.size();
        return questions.get(current);
    }
}
