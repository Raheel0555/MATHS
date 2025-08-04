package com.example.maths;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Measureit extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measureit);

        MeasurementGameView gameView = findViewById(R.id.gameView);
        EditText inputBox = findViewById(R.id.inputGuess);
        Button nextBtn = findViewById(R.id.nextBtn);
        ClosestBarView closenessBar = findViewById(R.id.closenessBar);

        gameView.attachInputBox(inputBox);
        gameView.attachBarView(closenessBar);

        nextBtn.setEnabled(false);

        gameView.setFeedbackListener(valid -> runOnUiThread(() -> nextBtn.setEnabled(valid)));

        nextBtn.setOnClickListener(v -> {
            gameView.goToNextObject();
            inputBox.setText("");
            nextBtn.setEnabled(false);
        });
    }
}
