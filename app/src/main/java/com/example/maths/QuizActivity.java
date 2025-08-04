package com.example.maths;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    private ImageView quizImage;
    private LinearLayout choicesLayout;
    private QuizManager quizManager;
    private ArrayList<CoinInfo> coinList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizImage = findViewById(R.id.quiz_image);
        choicesLayout = findViewById(R.id.choices_layout);

        coinList = new ArrayList<>();
        coinList.add(new CoinInfo(1, "₹1 Coin", R.drawable.coin_1, "", false));
        coinList.add(new CoinInfo(2, "₹2 Coin", R.drawable.coin_2, "", false));
        coinList.add(new CoinInfo(5, "₹5 Coin", R.drawable.coin_5, "", false));
        coinList.add(new CoinInfo(10, "₹10 Coin", R.drawable.coin_10, "", false));
        coinList.add(new CoinInfo(10, "₹10 Note", R.drawable.bill_10, "", true));
        coinList.add(new CoinInfo(20, "₹20 Note", R.drawable.bill_20, "", true));
        coinList.add(new CoinInfo(50, "₹50 Note", R.drawable.bill_50, "", true));
        coinList.add(new CoinInfo(100, "₹100 Note", R.drawable.bill_100, "", true));

        quizManager = new QuizManager(coinList);
        loadNextQuestion();
    }

    private void loadNextQuestion() {
        choicesLayout.removeAllViews();
        quizManager.nextQuestion();
        quizImage.setImageResource(quizManager.getCurrentCoin().getImageRes());

        for (final int v : quizManager.getChoices()) {
            Button btn = new Button(this);
            btn.setText("₹" + v);
            btn.setBackgroundResource(R.drawable.quiz_option_bg);
            btn.setTextSize(28f); // Larger text
            btn.setTextColor(0xFF111111); // Dark text
            btn.setTypeface(null, android.graphics.Typeface.BOLD);
            btn.setAllCaps(false);
            btn.setPadding(0, 32, 0, 32); // More height
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 24, 10, 24); // Larger gaps top-bottom
            btn.setLayoutParams(lp);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v2) {
                    boolean correct = quizManager.check(v);
                    showResultDialog(correct);
                }
            });
            choicesLayout.addView(btn);
        }
    }

    private void showResultDialog(boolean correct) {
        ResultDialog3 dialog = new ResultDialog3(this, correct, quizManager.getCurrentCoin().getValue(), new ResultDialog3.OnDialogNextListener() {
            @Override
            public void onNext() {
                loadNextQuestion();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
}
