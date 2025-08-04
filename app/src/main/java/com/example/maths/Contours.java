package com.example.maths;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Contours extends AppCompatActivity {
    private ShapeTruckGameView gameView;
    private Button nextBtn, startLearningBtn;
    private TextView introText;
    private View introLayout;
    private CardView shapeIntroCard;
    private int introIndex = 0;

    private static final String[] intros = {
            "CIRCLE: All points at equal distance from center.\n\n🔵 Examples: coins, wheels, plates.",
            "RECTANGLE: Opposite sides equal, four right angles.\n\n📱 Examples: books, tablets, doors.",
            "TRIANGLE: Has 3 straight sides and 3 angles.\n\n🔺 Examples: traffic signs, pizza slice.",
            "SQUARE: All four sides equal length, four right angles.\n\n⬜ Examples: chessboard, sticky notes."
    };
    private String[] shapeNames = {"circle", "rectangle", "triangle", "square"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contours);

        // Initialize views
        introLayout = findViewById(R.id.introLayout);
        shapeIntroCard = findViewById(R.id.shapeIntroCard);
        introText = findViewById(R.id.introText);
        gameView = findViewById(R.id.gameView);
        nextBtn = findViewById(R.id.nextBtn);
        startLearningBtn = findViewById(R.id.startLearningBtn);

        // Show welcome screen initially
        showWelcomeScreen();

        // Welcome screen button
        startLearningBtn.setOnClickListener(v -> {
            System.out.println("DEBUG: Start Learning clicked");
            showShapeIntro();
        });

        // Shape intro navigation
        nextBtn.setOnClickListener(v -> {
            System.out.println("DEBUG: Next button clicked, introIndex = " + introIndex);
            if (introIndex < intros.length - 1) {
                introIndex++;
                showShapeIntro();
            } else {
                startGame();
            }
        });
    }

    private void showWelcomeScreen() {
        introLayout.setVisibility(View.VISIBLE);
        shapeIntroCard.setVisibility(View.GONE);
        gameView.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
    }

    private void showShapeIntro() {
        introLayout.setVisibility(View.GONE);
        shapeIntroCard.setVisibility(View.VISIBLE);
        gameView.setVisibility(View.GONE);
        nextBtn.setVisibility(View.VISIBLE);
        nextBtn.setEnabled(true);
        nextBtn.setClickable(true);
        nextBtn.bringToFront();

        introText.setText("SHAPE: " + shapeNames[introIndex].toUpperCase() + "\n\n" + intros[introIndex] + "\n\n✨ Tap NEXT to continue!");
        nextBtn.setText(introIndex == intros.length - 1 ? "🎮 Start Game" : "➡️ Next");
    }

    private void startGame() {
        System.out.println("DEBUG: Starting game");
        introLayout.setVisibility(View.GONE);
        shapeIntroCard.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        gameView.setVisibility(View.VISIBLE);

        setupGameListener();
        gameView.resetForCurrentShape();
    }

    private void setupGameListener() {
        gameView.setListener(new ShapeTruckGameView.Listener() {
            @Override
            public void onRoundFinished(boolean lastShape) {
                System.out.println("DEBUG: onRoundFinished called, lastShape = " + lastShape);

                runOnUiThread(() -> {
                    nextBtn.bringToFront();
                    nextBtn.setVisibility(View.VISIBLE);
                    nextBtn.setEnabled(true);
                    nextBtn.setClickable(true);
                    nextBtn.setFocusable(true);
                    nextBtn.setText(lastShape ? "🏆 Finish Game" : "➡️ Next Shape");

                    nextBtn.setOnClickListener(null);
                    nextBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("DEBUG: NEXT SHAPE BUTTON CLICKED!!!");
                            handleNextShape(lastShape);
                        }
                    });

                    nextBtn.requestLayout();
                    nextBtn.invalidate();
                });
            }

            @Override
            public void onWrongShape() {
                System.out.println("DEBUG: onWrongShape called");
            }
        });
    }

    private void handleNextShape(boolean lastShape) {
        System.out.println("DEBUG: handleNextShape called, lastShape = " + lastShape);

        nextBtn.setEnabled(false);
        nextBtn.setClickable(false);

        if (!lastShape) {
            System.out.println("DEBUG: Moving to next shape");
            nextBtn.setVisibility(View.GONE);
            gameView.nextShape();
            gameView.resetForCurrentShape();
        } else {
            System.out.println("DEBUG: Game finished");
            // Show completion in the shape intro card with rounded corners
            shapeIntroCard.setVisibility(View.VISIBLE);
            introText.setText("🎉 CONGRATULATIONS! 🎉\n\nYou've mastered all the shapes!\n\n🌟 Great job, little learner! 🌟");
            gameView.setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);
        }
    }
}
