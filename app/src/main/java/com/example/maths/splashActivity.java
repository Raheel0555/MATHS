package com.example.maths;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class splashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logo); // give this id to your ImageView
        TextView title = findViewById(R.id.title); // give this id to your TextView

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(fadeIn);
        title.startAnimation(fadeIn);



        new Handler().postDelayed(() -> {
            startActivity(new Intent(splashActivity.this, MainActivity.class));
            finish();
        }, 3000); // 2 seconds delay
    }
}
