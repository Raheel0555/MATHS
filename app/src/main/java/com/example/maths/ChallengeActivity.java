package com.example.maths;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChallengeActivity extends AppCompatActivity {
    private TextView targetText, sumText;
    private LinearLayout optionsLayout, chosenLayout;
    private ChallengeManager manager;
    private ArrayList<CoinInfo> coins;
    private ArrayList<Integer> chosenIndexes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        targetText = findViewById(R.id.challenge_target_text);
        sumText = findViewById(R.id.challenge_sum_text);
        optionsLayout = findViewById(R.id.challenge_options_layout);
        chosenLayout = findViewById(R.id.challenge_chosen_layout);

        coins = new ArrayList<>();
        coins.add(new CoinInfo(1, "₹1 Coin", R.drawable.coin_1, "", false));
        coins.add(new CoinInfo(2, "₹2 Coin", R.drawable.coin_2, "", false));
        coins.add(new CoinInfo(5, "₹5 Coin", R.drawable.coin_5, "", false));
        coins.add(new CoinInfo(10, "₹10 Coin", R.drawable.coin_10, "", false));
        coins.add(new CoinInfo(10, "₹10 Note", R.drawable.bill_10, "", true));
        coins.add(new CoinInfo(20, "₹20 Note", R.drawable.bill_20, "", true));
        coins.add(new CoinInfo(50, "₹50 Note", R.drawable.bill_50, "", true));
        coins.add(new CoinInfo(100, "₹100 Note", R.drawable.bill_100, "", true));

        manager = new ChallengeManager(coins);
        loadNext();
    }

    private void loadNext() {
        optionsLayout.removeAllViews();
        chosenLayout.removeAllViews();
        chosenIndexes.clear();
        manager.nextChallenge();

        targetText.setText("Make: ₹" + manager.getTarget());
        sumText.setText("Your Sum: 0");

        for (int i = 0; i < coins.size(); i++) {
            final int idx = i;
            CoinInfo ci = coins.get(i);
            Button btn = new Button(this);
            btn.setText("₹" + ci.getValue());
            btn.setAllCaps(false);
            btn.setTextSize(22f);
            btn.setBackgroundResource(R.drawable.quiz_option_bg);
            btn.setTextColor(0xFF212121);
            btn.setPadding(0, 28, 0, 28);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(14, 20, 14, 20);
            btn.setLayoutParams(lp);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chosenIndexes.add(idx);
                    addChosenButton(idx);
                    // REMOVE btn.setEnabled(false); to allow multiple uses
                    int sum = manager.sum(chosenIndexes);
                    sumText.setText("Your Sum: " + sum);

                    if (sum == manager.getTarget()) {
                        showResultDialog(true);
                    } else if (sum > manager.getTarget()) {
                        showResultDialog(false);
                    }
                }
            });
            optionsLayout.addView(btn);
        }
    }

    private void addChosenButton(final int idx) {
        CoinInfo ci = coins.get(idx);
        Button btn = new Button(this);
        btn.setText("₹" + ci.getValue());
        btn.setTextSize(22f);
        btn.setBackgroundResource(R.drawable.quiz_option_bg);
        btn.setTextColor(0xFF212121);
        btn.setPadding(0, 20, 0, 20);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        btn.setLayoutParams(lp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenLayout.removeView(v);
                chosenIndexes.remove((Integer) idx);
                int sum = manager.sum(chosenIndexes);
                sumText.setText("Your Sum: " + sum);
            }
        });
        chosenLayout.addView(btn);
    }

    private void showResultDialog(boolean correct) {
        ResultDialog3 dialog = new ResultDialog3(this, correct, manager.getTarget(), new ResultDialog3.OnDialogNextListener() {
            @Override
            public void onNext() {
                loadNext();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
}
