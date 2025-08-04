package com.example.maths;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OrderActivity extends AppCompatActivity {

    TextView tvNumbers, tvResult;
    Button btnAsc, btnDesc, btnNewSet;
    Integer[] currentNumbers = new Integer[3];
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        tvNumbers = findViewById(R.id.tvNumbers);
        tvResult = findViewById(R.id.tvResult);
        btnAsc = findViewById(R.id.btnAsc);
        btnDesc = findViewById(R.id.btnDesc);
        btnNewSet = findViewById(R.id.btnNewSet);

        generateNewNumbers();

        btnAsc.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            Integer[] copy = currentNumbers.clone();
            Arrays.sort(copy);
            tvResult.setText("चढता क्रम: " + Arrays.toString(copy));
        });

        btnDesc.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            Integer[] copy = currentNumbers.clone();
            Arrays.sort(copy, Collections.reverseOrder());
            tvResult.setText("उतरता क्रम: " + Arrays.toString(copy));
        });

        btnNewSet.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            generateNewNumbers();
        });
    }

    private void generateNewNumbers() {
        for (int i = 0; i < 3; i++) {
            currentNumbers[i] = random.nextInt(90) + 10; // Random 2-digit numbers
        }

        // Shuffle numbers before showing
        List<Integer> list = Arrays.asList(currentNumbers);
        Collections.shuffle(list);
        list.toArray(currentNumbers);

        tvNumbers.setText("संख्या: " + Arrays.toString(currentNumbers));
        tvResult.setText("उत्तर येथे दिसेल");
    }
}
