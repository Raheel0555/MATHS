package com.example.maths;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MinMaxPickerActivity extends AppCompatActivity {

    TextView tvNumbersSet, tvAnswer;
    Button btnPickSmallest, btnPickLargest;
    Integer[] currentSet = new Integer[4];
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_min_max_picker);

        tvNumbersSet = findViewById(R.id.tvNumbersSet);
        tvAnswer = findViewById(R.id.tvAnswer);
        btnPickSmallest = findViewById(R.id.btnPickSmallest);
        btnPickLargest = findViewById(R.id.btnPickLargest);

        generateNumbers();

        btnPickSmallest.setOnClickListener(v -> {
            int min = Collections.min(Arrays.asList(currentSet));
            tvAnswer.setText("👶 लहान संख्या: " + min);
        });

        btnPickLargest.setOnClickListener(v -> {
            int max = Collections.max(Arrays.asList(currentSet));
            tvAnswer.setText("👴 मोठी संख्या: " + max);
        });
    }

    private void generateNumbers() {
        for (int i = 0; i < 4; i++) {
            currentSet[i] = random.nextInt(20) + 1; // Random 1 to 20
        }

        // Shuffle the numbers
        List<Integer> list = Arrays.asList(currentSet);
        Collections.shuffle(list);
        list.toArray(currentSet); // optional, currentSet is already modified

        tvNumbersSet.setText("संख्या: " + Arrays.toString(currentSet));
        tvAnswer.setText("उत्तर येथे दिसेल");
    }
}
