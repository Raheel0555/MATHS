package com.example.maths;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class num31to99 extends Activity {
    private StreetView streetView;
    private TextView challengeDescription, progressText, feedbackText;
    private Button resetButton, nextChallengeButton;
    private GameLogic3 gameLogic3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num31to99);

        // Initialize views
        streetView = findViewById(R.id.streetView);
        challengeDescription = findViewById(R.id.challengeDescription);
        progressText = findViewById(R.id.progressText);
        feedbackText = findViewById(R.id.feedbackText);
        resetButton = findViewById(R.id.resetButton);
        nextChallengeButton = findViewById(R.id.nextChallengeButton);

        gameLogic3 = new GameLogic3();

        // Set up listeners
        streetView.setListener(new StreetView.StreetCompletionListener() {
            @Override
            public void onStreetCompleted(boolean isCorrect) {
                if (isCorrect) {
                    feedbackText.setText("🎉 Perfect! Street completed correctly! 🎉");
                    feedbackText.setTextColor(Color.parseColor("#4CAF50"));
                    nextChallengeButton.setVisibility(View.VISIBLE);
                    CelebrationManager.celebrate(feedbackText);
                    CelebrationManager.streetLightUp(streetView);
                } else {
                    feedbackText.setText("❌ Not quite right. Check the order!");
                    feedbackText.setTextColor(Color.parseColor("#FF5722"));
                    CelebrationManager.pulseView(feedbackText);
                }
            }

            @Override
            public void onHousePlaced(int totalPlaced, int totalNeeded) {
                progressText.setText("Houses placed: " + totalPlaced + "/" + totalNeeded);

                if (totalPlaced > 0 && totalPlaced < totalNeeded) {
                    feedbackText.setText("Great start! Keep placing houses in order.");
                    feedbackText.setTextColor(Color.parseColor("#FF9800"));
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCurrentChallenge();
            }
        });

        nextChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextChallenge();
            }
        });

        // Start first challenge
        startNewChallenge();
    }

    private void startNewChallenge() {
        challengeDescription.setText(gameLogic3.getChallengeDescription());
        streetView.setupChallenge(gameLogic3.getCurrentChallenge(), gameLogic3.getAvailableNumbers());

        feedbackText.setText("Drag houses to arrange them on the street!");
        feedbackText.setTextColor(Color.parseColor("#1976D2"));
        progressText.setText("Houses placed: 0/" + gameLogic3.getCurrentChallenge().size());
        nextChallengeButton.setVisibility(View.GONE);
    }

    private void resetCurrentChallenge() {
        streetView.resetStreet();
        feedbackText.setText("Challenge reset! Try again.");
        feedbackText.setTextColor(Color.parseColor("#37474F"));
        progressText.setText("Houses placed: 0/" + gameLogic3.getCurrentChallenge().size());
        nextChallengeButton.setVisibility(View.GONE);
    }

    private void startNextChallenge() {
        gameLogic3.nextLevel();
        startNewChallenge();
    }
}
