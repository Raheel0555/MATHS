package com.example.maths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {
    String questionText;
    String correctAnswer;
    List<String> options;

    public Question(String questionText, String correctAnswer, List<String> wrongOptions) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.options = new ArrayList<>();
        this.options.add(correctAnswer);
        this.options.addAll(wrongOptions);
        Collections.shuffle(this.options); // shuffle options once
    }
}
