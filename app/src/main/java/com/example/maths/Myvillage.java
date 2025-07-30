package com.example.maths;



import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Myvillage extends AppCompatActivity {

    Button submitQuizBtn;
    TextView resultText;

    // Correct answers
    int[] correctOptionIds = {
            R.id.q1a,  // Q1
            R.id.q2b,  // Q2
            R.id.q4a,  // Q3
            R.id.q5b,  // Q4
            R.id.q9b,  // Q5
            R.id.q10a  // Q6
    };

    RadioGroup[] questionGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myvillage);

        submitQuizBtn = findViewById(R.id.submitQuizBtn);
        resultText = findViewById(R.id.resultText);

        // Initialize groups
        questionGroups = new RadioGroup[]{
                findViewById(R.id.q1_group),
                findViewById(R.id.q2_group),
                findViewById(R.id.q4_group),  // Q3
                findViewById(R.id.q5_group),  // Q4
                findViewById(R.id.q9_group),  // Q5
                findViewById(R.id.q10_group)  // Q6
        };

        submitQuizBtn.setOnClickListener(v -> checkAnswers());
    }

    private void checkAnswers() {
        int score = 0;

        for (int i = 0; i < questionGroups.length; i++) {
            RadioGroup group = questionGroups[i];
            int correctId = correctOptionIds[i];

            if (group != null) {
                int selectedId = group.getCheckedRadioButtonId();

                // Highlight all options to reset colors first
                for (int j = 0; j < group.getChildCount(); j++) {
                    if (group.getChildAt(j) instanceof RadioButton) {
                        ((RadioButton) group.getChildAt(j)).setTextColor(Color.BLACK);
                    }
                }

                if (selectedId == correctId) {
                    score++;
                    RadioButton correctRB = group.findViewById(correctId);
                    if (correctRB != null) correctRB.setTextColor(Color.GREEN);
                } else {
                    // Highlight selected wrong answer in RED
                    RadioButton wrongRB = group.findViewById(selectedId);
                    if (wrongRB != null) wrongRB.setTextColor(Color.RED);

                    // Highlight the correct answer in GREEN
                    RadioButton correctRB = group.findViewById(correctId);
                    if (correctRB != null) correctRB.setTextColor(Color.GREEN);
                }
            }
        }

        resultText.setText("You got " + score + " out of 6 correct!");
    }
}
