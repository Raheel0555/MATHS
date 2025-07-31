package com.example.maths;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Before_After_Activity extends AppCompatActivity {

    ImageView optionA1, optionB1, optionA2, optionB2;
    ImageView optionA3, optionB3, optionA4, optionB4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_after);

        // Set 1 - Brushing
        optionA1 = findViewById(R.id.optionA1); // Food
        optionB1 = findViewById(R.id.optionB1); // Toothbrush

        optionA1.setOnClickListener(view -> showToast("❌ Try again!"));
        optionB1.setOnClickListener(view -> showToast("✅ Correct! This happened before."));

        // Set 2 - Scooter
        optionA2 = findViewById(R.id.optionA2); // Helmet
        optionB2 = findViewById(R.id.optionB2); // Bike

        optionA2.setOnClickListener(view -> showToast("✅ Correct! Helmet comes before riding."));
        optionB2.setOnClickListener(view -> showToast("❌ Not before!"));

        // Set 3 - Tree (Ask: What happened AFTER?)
        optionA3 = findViewById(R.id.optionA3); // Water
        optionB3 = findViewById(R.id.optionB3); // Saw

        optionA3.setOnClickListener(view -> showToast("❌ Watering is BEFORE the tree grows."));
        optionB3.setOnClickListener(view -> showToast("✅ Correct! Tree grew, then got cut."));

        // Set 4 - Eating (Ask: What happened AFTER?)
        optionA4 = findViewById(R.id.optionA4); // Handwash
        optionB4 = findViewById(R.id.optionB4); // Run

        optionA4.setOnClickListener(view -> showToast("❌ Handwash is BEFORE eating."));
        optionB4.setOnClickListener(view -> showToast("✅ Correct! You can run after eating."));

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
