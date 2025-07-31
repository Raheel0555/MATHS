package com.example.maths;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class TimeActivit extends AppCompatActivity {

    DrawingView drawingView;
    TextView numberText;
    Button btnClear, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        drawingView = findViewById(R.id.drawingView);
        numberText = findViewById(R.id.numberText);
        btnClear = findViewById(R.id.btnClear);
        btnNext = findViewById(R.id.btnNext);

        loadRandomNumber(); // Show first random number

        // Clear drawing
        btnClear.setOnClickListener(v -> drawingView.clear());

        // Load new number on next button click
        btnNext.setOnClickListener(v -> {
            loadRandomNumber();     // change number
            drawingView.clear();    // clear canvas
        });
    }

    private void loadRandomNumber() {
        int randomNumber = new Random().nextInt(101); // 0 to 100 inclusive
        numberText.setText("Write: " + randomNumber);
    }
}
