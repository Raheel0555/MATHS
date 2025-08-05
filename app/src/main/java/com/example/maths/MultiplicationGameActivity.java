package com.example.maths;

import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MultiplicationGameActivity extends AppCompatActivity {

    ImageView apple, basket;
    TextView questionText, feedbackText;
    Button nextButton;

    int correctAnswer;
    int droppedApples = 0;
    int currentQuestion = 1;
    final int totalQuestions = 5;

    int[][] easyQuestions = {
            {1, 1}, {1, 2}, {1, 3},
            {2, 1}, {2, 2}, {2, 3},
            {3, 1}, {3, 2}, {3, 3}
    };

    int currentA, currentB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplication_game);

        apple = findViewById(R.id.apple);
        basket = findViewById(R.id.basket);
        questionText = findViewById(R.id.questionText);
        feedbackText = findViewById(R.id.feedbackText);
        nextButton = findViewById(R.id.nextButton);

        generateNewQuestion();

        apple.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(data, shadowBuilder, v, 0);
                return true;
            }
            return false;
        });

        basket.setOnDragListener((v, event) -> {
            if (event.getAction() == DragEvent.ACTION_DROP) {
                droppedApples++;

                if (droppedApples == correctAnswer) {
                    feedbackText.setText("✅ Correct! Tap NEXT.");
                    nextButton.setVisibility(View.VISIBLE);
                } else if (droppedApples < correctAnswer) {
                    feedbackText.setText("🍎 " + droppedApples + " apple(s) dropped...");
                } else {
                    feedbackText.setText("❌ Too many apples! Try again.");
                    droppedApples = 0;
                }
            }
            return true;
        });

        nextButton.setOnClickListener(v -> {
            if (currentQuestion < totalQuestions) {
                currentQuestion++;
                generateNewQuestion();
            } else {
                questionText.setText("🎉 Well done! You completed all 5!");
                feedbackText.setText("⭐ You're awesome!");
                nextButton.setVisibility(View.GONE);
            }
        });
    }

    private void generateNewQuestion() {
        droppedApples = 0;
        feedbackText.setText("");
        nextButton.setVisibility(View.GONE);

        int randomIndex = new Random().nextInt(easyQuestions.length);
        currentA = easyQuestions[randomIndex][0];
        currentB = easyQuestions[randomIndex][1];
        correctAnswer = currentA * currentB;

        questionText.setText("Q" + currentQuestion + ": " + currentA + " × " + currentB + " = ?");
    }
}
