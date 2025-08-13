package com.example.maths;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.VH> {

    private final List<Integer> items = new ArrayList<>();

    public void submit(List<Integer> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        int resId = items.get(position);
        Drawable d = ContextCompat.getDrawable(holder.itemView.getContext(), resId);
        holder.image.setImageDrawable(d);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView image;
        VH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgCell);
        }
    }
}
