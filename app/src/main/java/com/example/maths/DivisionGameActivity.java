package com.example.maths;

import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DivisionGameActivity extends AppCompatActivity {



        TextView tvQuestion, tvScore, answerBox;
        TextView[] optionViews;
        int correctAnswer, score = 0;
        Random random = new Random();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_division_game);

            tvQuestion = findViewById(R.id.tvQuestion);
            tvScore = findViewById(R.id.tvScore);
            answerBox = findViewById(R.id.answerBox);

            optionViews = new TextView[]{
                    findViewById(R.id.option1),
                    findViewById(R.id.option2),
                    findViewById(R.id.option3)
            };

            for (TextView option : optionViews) {
                option.setOnLongClickListener(view -> {
                    ClipData data = ClipData.newPlainText("number", ((TextView) view).getText());
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDragAndDrop(data, shadowBuilder, view, 0);
                    return true;
                });
            }

            answerBox.setOnDragListener((view, event) -> {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DROP:
                        ClipData.Item item = event.getClipData().getItemAt(0);
                        String droppedText = item.getText().toString();
                        answerBox.setText(droppedText);
                        checkAnswer(Integer.parseInt(droppedText));
                        return true;
                }
                return true;
            });

            loadNewQuestion();
        }

        private void checkAnswer(int selected) {
            if (selected == correctAnswer) {
                score++;
                tvScore.setText("गुण: " + score);
                Toast.makeText(this, "✅ बरोबर!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "❌ चूक आहे, पुन्हा प्रयत्न करा", Toast.LENGTH_SHORT).show();
            }
            loadNewQuestion();
        }

    private void loadNewQuestion() {
        // Ensure simple division with small numbers
        int[] easyDivisors = {1, 2, 3, 4, 5, 6, 10};
        int divisor = easyDivisors[random.nextInt(easyDivisors.length)];
        correctAnswer = random.nextInt(10) + 1; // answer between 1–10
        int dividend = divisor * correctAnswer; // ensures whole number division

        tvQuestion.setText(dividend + " ÷ " + divisor + " = ?");

        List<Integer> options = new ArrayList<>();
        options.add(correctAnswer);

        while (options.size() < 3) {
            int wrong = correctAnswer + random.nextInt(5) - 2;
            if (wrong > 0 && wrong != correctAnswer && !options.contains(wrong)) {
                options.add(wrong);
            }
        }

        Collections.shuffle(options);
        for (int i = 0; i < 3; i++) {
            optionViews[i].setText(String.valueOf(options.get(i)));
        }

        answerBox.setText("इथे टाका");
    }
}
