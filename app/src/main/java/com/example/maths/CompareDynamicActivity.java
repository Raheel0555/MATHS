package com.example.maths;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class CompareDynamicActivity extends AppCompatActivity {

    LinearLayout layoutLeft, layoutRight;
    TextView tvResult, tvInstruction;
    Button btnNew;

    int leftCount, rightCount;
    Random random;
    String[] emoji = {"🥜", "🍌", "🍎", "🍓", "🍇", "🍒", "🍍", "🥥"};

    // true = find less, false = find more
    boolean findLess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_images);

        layoutLeft = findViewById(R.id.layoutLeft);
        layoutRight = findViewById(R.id.layoutRight);
        tvResult = findViewById(R.id.tvResult);
        tvInstruction = findViewById(R.id.tvInstruction); // 📌 Add in XML
        btnNew = findViewById(R.id.btnNew);

        random = new Random();

        generateNew();

        btnNew.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            generateNew();
        });

        layoutLeft.setOnClickListener(v -> {
            checkAnswer("left");
        });

        layoutRight.setOnClickListener(v -> {
            checkAnswer("right");
        });
    }

    private void generateNew() {
        layoutLeft.removeAllViews();
        layoutRight.removeAllViews();
        tvResult.setText("Answear showing here");

        String emojiSymbol = emoji[random.nextInt(emoji.length)];

        // Pick random counts (2 to 10)
        do {
            leftCount = random.nextInt(9) + 2;
            rightCount = random.nextInt(9) + 2;
        } while (leftCount == rightCount);

        // Swap randomly
        if (random.nextBoolean()) {
            int temp = leftCount;
            leftCount = rightCount;
            rightCount = temp;
        }

        // Randomly decide whether to find less or more
        findLess = random.nextBoolean();
        if (findLess) {
            tvInstruction.setText("🔍 Identify less Item");
        } else {
            tvInstruction.setText("🔍 Identify More Item");
        }

        addEmojis(layoutLeft, leftCount, emojiSymbol);
        addEmojis(layoutRight, rightCount, emojiSymbol);
    }

    private void addEmojis(LinearLayout layout, int count, String emojiChar) {
        for (int i = 0; i < count; i++) {
            TextView tv = new TextView(this);
            tv.setText(emojiChar);
            tv.setTextSize(32f);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(Typeface.DEFAULT_BOLD);
            layout.addView(tv);
        }
    }

    private void checkAnswer(String chosen) {
        boolean correct;
        if (findLess) {
            correct = (chosen.equals("left") && leftCount < rightCount)
                    || (chosen.equals("right") && rightCount < leftCount);
        } else {
            correct = (chosen.equals("left") && leftCount > rightCount)
                    || (chosen.equals("right") && rightCount > leftCount);
        }

        if (correct) {
            tvResult.setText("✅ Correct");
        } else {
            tvResult.setText("❌ Wrong");
        }
    }
}
