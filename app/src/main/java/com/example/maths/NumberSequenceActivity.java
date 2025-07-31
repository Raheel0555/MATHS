package com.example.maths;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NumberSequenceActivity extends AppCompatActivity {

    TextView questionText;
    Button btn1, btn2, btn3, btn4, btnNext;
    List<Question> questionList;
    int currentIndex = 0;
    Question currentQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_sequence);

        // Link UI
        questionText = findViewById(R.id.question);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btnNext = findViewById(R.id.btnnext);

        // Setup questions
        setupQuestions();

        // Button click listeners
        View.OnClickListener listener = v -> {
            Button clicked = (Button) v;
            String selected = clicked.getText().toString();
            if (selected.equals(currentQuestion.correctAnswer)) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
            }
        };

        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
        btn3.setOnClickListener(listener);
        btn4.setOnClickListener(listener);

        btnNext.setOnClickListener(v -> loadNextQuestion());

        loadNextQuestion();
    }

    private void setupQuestions() {
        questionList = new ArrayList<>();

        questionList.add(new Question("1, 2, ?, 4, 5", "3", Arrays.asList("5", "7", "9")));
        questionList.add(new Question("6, 7, ?, 9, 10", "8", Arrays.asList("4", "6", "11")));
        questionList.add(new Question("10, 15, ?, 20, 25", "20", Arrays.asList("5", "16", "3")));
        questionList.add(new Question("51, 52, 53, ?, 55", "54", Arrays.asList("56", "53", "54")));
        questionList.add(new Question("11, 12, 13, ?, 15", "14", Arrays.asList("14", "16f", "12")));

        Collections.shuffle(questionList); // Shuffle question order
    }

    private void loadNextQuestion() {
        if (currentIndex >= questionList.size()) {
            Toast.makeText(this, "Quiz finished!", Toast.LENGTH_SHORT).show();
            currentIndex = 0; // restart
            Collections.shuffle(questionList); // reshuffle again
        }

        currentQuestion = questionList.get(currentIndex);
        questionText.setText(currentQuestion.questionText);

        List<String> shuffledOptions = new ArrayList<>(currentQuestion.options);
        Collections.shuffle(shuffledOptions);

        btn1.setText(shuffledOptions.get(0));
        btn2.setText(shuffledOptions.get(1));
        btn3.setText(shuffledOptions.get(2));
        btn4.setText(shuffledOptions.get(3));

        currentIndex++;
    }
}
