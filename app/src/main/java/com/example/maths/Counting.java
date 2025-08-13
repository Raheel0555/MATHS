package com.example.maths;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Counting extends AppCompatActivity {

    private RecyclerView recyclerGrid;
    private ImageGridAdapter adapter;

    private TextView titleText;
    private TextView roundText;
    private TextView scoreText;

    private Button option1, option2, option3, option4, option5;
    private Button nextButton, resetButton;

    private final Random random = new Random();

    private final int totalRounds = 5;
    private int currentRound = 0;
    private int score = 0;

    private int correctCount = 0;
    private int currentDrawableRes = 0;
    private String currentItemName = "";
    private boolean answeredThisRound = false;

    // Ensure these match files in res/drawable/
    private final int[] itemDrawables = {
            R.drawable.balloon,
            R.drawable.carrot,
            R.drawable.pineapple,
            R.drawable.seashell,
            R.drawable.star
    };

    private final String[] itemNames = {
            "balloon", "carrot", "pineapple", "seashell", "star"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting);

        titleText = findViewById(R.id.titleText);
        roundText = findViewById(R.id.roundText);
        scoreText = findViewById(R.id.scoreText);

        option1 = findViewById(R.id.btnOption1);
        option2 = findViewById(R.id.btnOption2);
        option3 = findViewById(R.id.btnOption3);
        option4 = findViewById(R.id.btnOption4);
        option5 = findViewById(R.id.btnOption5);

        nextButton = findViewById(R.id.btnNext);
        resetButton = findViewById(R.id.btnReset);

        recyclerGrid = findViewById(R.id.recyclerGrid);
        recyclerGrid.setLayoutManager(new GridLayoutManager(this, 3));

        adapter = new ImageGridAdapter();
        recyclerGrid.setAdapter(adapter);

        View.OnClickListener optionClick = v -> {
            if (currentRound == 0 || currentRound > totalRounds || answeredThisRound) return;

            Button b = (Button) v;
            int chosen = Integer.parseInt(b.getText().toString());
            boolean correct = (chosen == correctCount);
            if (correct) {
                score++;
                Toast.makeText(this, "✅ Correct!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "❌ Wrong! Correct was " + correctCount, Toast.LENGTH_SHORT).show();
            }
            answeredThisRound = true;
            updateScoreText();
            setOptionsEnabled(false);
            nextButton.setEnabled(true);
        };

        // Attach to all option buttons
        option1.setOnClickListener(optionClick);
        option2.setOnClickListener(optionClick);
        option3.setOnClickListener(optionClick);
        option4.setOnClickListener(optionClick);
        option5.setOnClickListener(optionClick);

        nextButton.setOnClickListener(v -> nextRound());
        resetButton.setOnClickListener(v -> startGame());

        startGame();
    }

    private void startGame() {
        score = 0;
        currentRound = 0;
        updateScoreText();
        nextButton.setEnabled(false);
        answeredThisRound = false;
        setOptionsEnabled(true);
        nextRound();
    }

    private void nextRound() {
        if (currentRound >= totalRounds) {
            Toast.makeText(this, "Game Over! Score: " + score + "/" + totalRounds,
                    Toast.LENGTH_LONG).show();
            nextButton.setEnabled(false);
            setOptionsEnabled(false);
            return;
        }

        currentRound++;
        answeredThisRound = false;
        roundText.setText("Round " + currentRound + "/" + totalRounds);

        // Random item
        int idx = random.nextInt(itemDrawables.length);
        currentDrawableRes = itemDrawables[idx];
        currentItemName = itemNames[idx];

        // Random count
        correctCount = 1 + random.nextInt(9);
        titleText.setText("How many " + pluralize(currentItemName, correctCount) + "?");

        // Fill the grid
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < correctCount; i++) {
            data.add(currentDrawableRes);
        }
        adapter.submit(data);

        // Prepare 5 answer options
        List<Integer> pool = new ArrayList<>();
        for (int i = 1; i <= 9; i++) pool.add(i);
        pool.remove(Integer.valueOf(correctCount));
        Collections.shuffle(pool, random);

        List<Integer> opts = new ArrayList<>();
        opts.add(correctCount);
        for (int i = 0; i < 4; i++) {
            opts.add(pool.get(i));
        }
        Collections.shuffle(opts, random);

        option1.setText(String.valueOf(opts.get(0)));
        option2.setText(String.valueOf(opts.get(1)));
        option3.setText(String.valueOf(opts.get(2)));
        option4.setText(String.valueOf(opts.get(3)));
        option5.setText(String.valueOf(opts.get(4)));

        setOptionsEnabled(true);
        nextButton.setEnabled(false);
    }

    private String pluralize(String noun, int count) {
        return count == 1 ? noun : noun + "s";
    }

    private void setOptionsEnabled(boolean enabled) {
        option1.setEnabled(enabled);
        option2.setEnabled(enabled);
        option3.setEnabled(enabled);
        option4.setEnabled(enabled);
        option5.setEnabled(enabled);
    }

    private void updateScoreText() {
        scoreText.setText("Score: " + score);
    }
}
