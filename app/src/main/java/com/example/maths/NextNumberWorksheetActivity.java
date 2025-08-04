package com.example.maths;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NextNumberWorksheetActivity extends AppCompatActivity {

    EditText editAnswer;
    Button btnCheck;
    TextView tvResult;

    final int correctAnswer = 8; // ९ च्या मागची संख्या

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_number);

        editAnswer = findViewById(R.id.editAnswer);
        btnCheck = findViewById(R.id.btnCheck);
        tvResult = findViewById(R.id.tvResult);

        btnCheck.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));

            String userInput = editAnswer.getText().toString().trim();

            if (userInput.isEmpty()) {
                tvResult.setText("कृपया उत्तर भरा");
            } else {
                int userAnswer = Integer.parseInt(userInput);
                if (userAnswer == correctAnswer) {
                    tvResult.setText("✅ बरोबर! ९ च्या मागची संख्या ८ आहे");
                } else {
                    tvResult.setText("❌ चूक. योग्य उत्तर: ८");
                }
            }
        });
    }
}
