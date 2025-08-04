package com.example.maths;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Coinsandcurrency extends AppCompatActivity {

    private Button btnLearn, btnQuiz, btnChallenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coinsandcurrency);

        btnLearn = findViewById(R.id.btn_learn);
        btnQuiz = findViewById(R.id.btn_quiz);
        btnChallenge = findViewById(R.id.btn_challenge);

        btnLearn.setOnClickListener(v ->
                startActivity(new Intent(Coinsandcurrency.this, IntroActivity.class))
        );

        btnQuiz.setOnClickListener(v ->
                startActivity(new Intent(Coinsandcurrency.this, QuizActivity.class))
        );

        btnChallenge.setOnClickListener(v ->
                startActivity(new Intent(Coinsandcurrency.this, ChallengeActivity.class))
        );
    }
}
