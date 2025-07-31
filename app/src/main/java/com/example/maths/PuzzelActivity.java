package com.example.maths;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PuzzelActivity extends AppCompatActivity {

    RadioGroup radioGroup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_puzzel);

        // Handle system insets (optional)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find the RadioGroup
        radioGroup = findViewById(R.id.radioGroup);

        // Listen for selection change
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadio = findViewById(checkedId);
            if (selectedRadio != null) {
                String answer = selectedRadio.getText().toString().trim();
                if (answer.equalsIgnoreCase("Four")) {
                    Toast.makeText(PuzzelActivity.this, "✅ Correct! Well done!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PuzzelActivity.this, "❌ Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
