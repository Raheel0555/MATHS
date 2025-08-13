package com.example.maths;



import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    TextView tvFact;
    Button btnNextFact;

    String[] facts = {
            "🟥 This is a square – it has 4 sides.",

             "🔺 This is a triangle – it has 3 sides.",

            "🟦 This is a rectangle – it has two long and two short sides.",

            "⚫ This is a circle – it has no corners.",

            "🔢 10 is a two-digit number.",

            "📏 A line is straight.",

           "📐 An angle is used to measure corners.",

           "📦 Solid shapes have length, width, and height."
    };

    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        tvFact = findViewById(R.id.tvFact);
        btnNextFact = findViewById(R.id.btnNextFact);

        showFact();

        btnNextFact.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
            currentIndex = (currentIndex + 1) % facts.length;
            showFact();
        });
    }

    private void showFact() {
        tvFact.setText(facts[currentIndex]);
    }
}
