package com.example.maths;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IntroActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CoinAdapter adapter;
    private ArrayList<CoinInfo> coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        coins = new ArrayList<>();
        coins.add(new CoinInfo(1, "₹1 Coin", R.drawable.coin_1, "Smallest value coin.", false));
        coins.add(new CoinInfo(2, "₹2 Coin", R.drawable.coin_2, "Has a wave design!", false));
        coins.add(new CoinInfo(5, "₹5 Coin", R.drawable.coin_5, "Heavier and silver.", false));
        coins.add(new CoinInfo(10, "₹10 Coin", R.drawable.coin_10, "Largest common coin.", false));
        coins.add(new CoinInfo(10, "₹10 Note", R.drawable.bill_10, "Orange colored.", true));
        coins.add(new CoinInfo(20, "₹20 Note", R.drawable.bill_20, "Small green note.", true));
        coins.add(new CoinInfo(50, "₹50 Note", R.drawable.bill_50, "Blue colored.", true));
        coins.add(new CoinInfo(100, "₹100 Note", R.drawable.bill_100, "Purple colored!", true));

        adapter = new CoinAdapter(coins);
        recyclerView.setAdapter(adapter);
    }
}
