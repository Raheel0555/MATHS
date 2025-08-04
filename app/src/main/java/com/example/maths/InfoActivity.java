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
            "🟥 हा चौकोन आहे - त्याला ४ बाजू आहेत",
            "🔺 हा त्रिकोण आहे - त्याला ३ बाजू आहेत",
            "🟦 हा आयत आहे - दोन लांब व दोन रुंद बाजू",
            "⚫ हा वर्तुळ आहे - त्याला कोपरे नाहीत",
            "🔢 १० ही दोन अंकी संख्या आहे",
            "📏 एक रेषा सरळ असते",
            "📐 कोन मोजण्यासाठी अँगल वापरतात",
            "📦 घन आकृतींना लांबी, रुंदी, उंची असते"
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
