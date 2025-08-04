package com.example.maths;

public class QuestionModel {
    public String question;
    public int[] options;
    public int answerIndex;

    public QuestionModel(String question, int[] options, int answerIndex) {
        this.question = question;
        this.options = options;
        this.answerIndex = answerIndex;
    }
}
