        package com.example.maths;

        import android.content.Intent;
        import android.os.Bundle;
        import android.widget.Button;

        import androidx.appcompat.app.AppCompatActivity;

        public class Multi extends AppCompatActivity {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_multi);

                Button startGameBtn = findViewById(R.id.startGameBtn);
                startGameBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(Multi.this, MultiplicationGameActivity.class);
                    startActivity(intent);
                });
            }
        }
