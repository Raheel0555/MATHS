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
        questionList.add(new Question("10, 15, ?, 25, 30", "20", Arrays.asList("5", "16", "3")));
        questionList.add(new Question("51, 52, 53, ?, 55", "54", Arrays.asList("56", "53", "54")));
        questionList.add(new Question("11, 12, 13, ?, 15", "14", Arrays.asList("14", "16", "12")));
        questionList.add(new Question("?, 2, 3, 4, 5", "1", Arrays.asList("1", "2", "3")));  // Missing 1st
        questionList.add(new Question("6, ?, 8, 9, 10", "7", Arrays.asList("7", "8", "6"))); // Missing 2nd
        questionList.add(new Question("10, 11, ?, 13, 14", "12", Arrays.asList("12", "11", "15"))); // Missing 3rd
        questionList.add(new Question("21, 22, 23, ?, 25", "24", Arrays.asList("24", "26", "23"))); // Missing 4th
        questionList.add(new Question("31, 32, 33, 34, ?", "35", Arrays.asList("35", "36", "33"))); // Missing 5th
        questionList.add(new Question("?, 42, 43, 44, 45", "41", Arrays.asList("41", "42", "40"))); // Missing 1st
        questionList.add(new Question("51, ?, 53, 54, 55", "52", Arrays.asList("52", "56", "53"))); // Missing 2nd
        questionList.add(new Question("61, 62, ?, 64, 65", "63", Arrays.asList("63", "66", "62"))); // Missing 3rd
        questionList.add(new Question("71, 72, 73, ?, 75", "74", Arrays.asList("74", "76", "73"))); // Missing 4th
        questionList.add(new Question("81, 82, 83, 84, ?", "85", Arrays.asList("85", "86", "83"))); // Missing 5th
        questionList.add(new Question("?, 92, 93, 94, 95", "91", Arrays.asList("91", "96", "93"))); // Missing 1st
        questionList.add(new Question("101, ?, 103, 104, 105", "102", Arrays.asList("102", "106", "103"))); // Missing 2nd
        questionList.add(new Question("201, 202, ?, 204, 205", "203", Arrays.asList("203", "206", "202"))); // Missing 3rd
        questionList.add(new Question("301, 302, 303, ?, 305", "304", Arrays.asList("304", "306", "303"))); // Missing 4th
        questionList.add(new Question("401, 402, 403, 404, ?", "405", Arrays.asList("405", "406", "403"))); // Missing 5th


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
