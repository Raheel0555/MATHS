package com.example.maths;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Near_farActivity extends AppCompatActivity {

    Button button1, button2;         // For question 1: Cow vs Sun
    Button buttonBoy, buttonGirl;    // For question 2: Boy vs Girl

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_far);

        // Question 1: गाय जवळ आहे की सूर्य?
        button1 = findViewById(R.id.button1);     // गाय
        button2 = findViewById(R.id.button2);     // सूर्य

        button1.setOnClickListener(v -> showToast("✅ correct!caw is near."));
        button2.setOnClickListener(v -> showToast("❌ sun is far."));

        // Question 2: मुलगा दूर आहे का मुलगी?
        buttonBoy = findViewById(R.id.buttonboy);     // मुलगा
        buttonGirl = findViewById(R.id.buttongirl);   // मुलगी

        buttonBoy.setOnClickListener(v -> showToast("✅ correct! boy is far."));
        buttonGirl.setOnClickListener(v -> showToast("❌ girl is near."));
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
