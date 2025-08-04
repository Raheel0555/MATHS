package com.example.maths;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinViewHolder> {
    private List<CoinInfo> coins;

    public CoinAdapter(List<CoinInfo> coins) {
        this.coins = coins;
    }

    @Override
    public CoinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.coin_item, parent, false);
        return new CoinViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CoinViewHolder holder, int position) {
        CoinInfo coin = coins.get(position);
        holder.name.setText(coin.getName());
        holder.value.setText("₹" + coin.getValue());
        holder.image.setImageResource(coin.getImageRes());
        holder.fact.setText(coin.getFunFact());
    }

    @Override
    public int getItemCount() { return coins.size(); }

    public static class CoinViewHolder extends RecyclerView.ViewHolder {
        public TextView name, value, fact;
        public ImageView image;

        public CoinViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.coin_name);
            value = v.findViewById(R.id.coin_value);
            image = v.findViewById(R.id.coin_image);
            fact = v.findViewById(R.id.coin_fact);
        }
    }
}
