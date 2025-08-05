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

    RadioGroup radioGroup,radioGroup2,radioGroup3,radioGroup4,radioGroup5;

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
        radioGroup2 = findViewById(R.id.radioGroup2);
        radioGroup3 = findViewById(R.id.radioGroup3);
        radioGroup4 = findViewById(R.id.radioGroup4);
        radioGroup5 = findViewById(R.id.radioGroup5);

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

        radioGroup2.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadio = findViewById(checkedId);
            if (selectedRadio != null) {
                String answer = selectedRadio.getText().toString().trim();
                if (answer.equalsIgnoreCase("Three")) {
                    Toast.makeText(PuzzelActivity.this, "✅ Correct! Well done!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PuzzelActivity.this, "❌ Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        radioGroup3.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadio = findViewById(checkedId);
            if (selectedRadio != null) {
                String answer = selectedRadio.getText().toString().trim();
                if (answer.equalsIgnoreCase("Five")) {
                    Toast.makeText(PuzzelActivity.this, "✅ Correct! Well done!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PuzzelActivity.this, "❌ Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        radioGroup4.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadio = findViewById(checkedId);
            if (selectedRadio != null) {
                String answer = selectedRadio.getText().toString().trim();
                if (answer.equalsIgnoreCase("Two")) {
                    Toast.makeText(PuzzelActivity.this, "✅ Correct! Well done!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PuzzelActivity.this, "❌ Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        radioGroup5.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadio = findViewById(checkedId);
            if (selectedRadio != null) {
                String answer = selectedRadio.getText().toString().trim();
                if (answer.equalsIgnoreCase("Three")) {
                    Toast.makeText(PuzzelActivity.this, "✅ Correct! Well done!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PuzzelActivity.this, "❌ Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
