package com.example.maths;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Subballoon extends Activity {
    private BalloonView balloonView;
    private TextView problemText, feedbackText;
    private Button nextProblem, submitAnswer;
    private EditText answerInput;
    private GameLogic gameLogic;

    private int all, pop, correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subballoon);

        balloonView = findViewById(R.id.balloonView);
        problemText = findViewById(R.id.problemText);
        feedbackText = findViewById(R.id.feedbackText);
        nextProblem = findViewById(R.id.nextProblem);
        answerInput = findViewById(R.id.answerInput);
        submitAnswer = findViewById(R.id.submitAnswer);

        gameLogic = new GameLogic();

        balloonView.setListener(count -> {
            // Add a small bounce animation when balloon is popped
            animateBounce(feedbackText);
        });

        submitAnswer.setOnClickListener(v -> {
            animateButton(submitAnswer);
            checkAnswer();
        });

        nextProblem.setOnClickListener(v -> {
            animateButton(nextProblem);
            setupGame();
        });

        setupGame();
    }

    private void setupGame() {
        gameLogic.newProblem();
        all = gameLogic.getMinuend();
        pop = gameLogic.getSubtrahend();
        correctAnswer = gameLogic.getAnswer();

        problemText.setText(all + " - " + pop + " = ?");
        feedbackText.setText("");
        feedbackText.setTextColor(Color.parseColor("#1976D2"));
        balloonView.setupBalloons(all);
        answerInput.setText("");

        // Hide Next button at start of each question
        nextProblem.setVisibility(View.GONE);
        // Enable Submit button
        submitAnswer.setEnabled(true);

        // Animate problem text
        animateBounce(problemText);
    }

    private void checkAnswer() {
        String ansStr = answerInput.getText().toString().trim();
        if (ansStr.isEmpty()) {
            feedbackText.setText("📝 Enter an answer!");
            feedbackText.setTextColor(Color.parseColor("#FF5722"));
            animateShake(answerInput);
            return;
        }

        int userAns = Integer.parseInt(ansStr);

        if (userAns == correctAnswer) {
            feedbackText.setText("🎉 Correct! Great job! 🎉");
            feedbackText.setTextColor(Color.parseColor("#4CAF50"));
            animateBounce(feedbackText);
            animateSuccess(answerInput);

            // Show Next button only when answer is correct
            nextProblem.setVisibility(View.VISIBLE);
            // Disable Submit to prevent multiple submissions
            submitAnswer.setEnabled(false);

        } else {
            feedbackText.setText("🤔 Try again! You can do it!");
            feedbackText.setTextColor(Color.parseColor("#FF9800"));
            answerInput.setText("");
            animateShake(answerInput);

            // Keep Next button hidden for wrong answers
            nextProblem.setVisibility(View.GONE);
        }
    }

    private void animateBounce(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.2f, 1f);
        scaleX.setDuration(500);
        scaleY.setDuration(500);
        scaleX.setInterpolator(new BounceInterpolator());
        scaleY.setInterpolator(new BounceInterpolator());
        scaleX.start();
        scaleY.start();
    }

    private void animateButton(View button) {
        ObjectAnimator scaleDown = ObjectAnimator.ofFloat(button, "scaleX", 1f, 0.9f);
        ObjectAnimator scaleUp = ObjectAnimator.ofFloat(button, "scaleX", 0.9f, 1f);
        scaleDown.setDuration(100);
        scaleUp.setDuration(100);
        scaleDown.start();
        scaleUp.setStartDelay(100);
        scaleUp.start();
    }

    private void animateShake(View view) {
        ObjectAnimator shake = ObjectAnimator.ofFloat(view, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0);
        shake.setDuration(500);
        shake.start();
    }

    private void animateSuccess(View view) {
        view.setBackgroundColor(Color.parseColor("#C8E6C9"));
        ObjectAnimator pulse = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.5f, 1f);
        pulse.setDuration(300);
        pulse.setRepeatCount(2);
        pulse.start();
    }
}
