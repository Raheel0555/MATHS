package com.example.maths;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ShapeIntroPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] shapeNames = {"Circle", "Triangle", "Square", "Rectangle"};
    private int[] shapeDrawables = {
            R.drawable.circle_shape,
            R.drawable.triangle_shape,
            R.drawable.square_shape,
            R.drawable.rectangle_shape,
    };
    private String[] shapeDescriptions = {
            "All points equal distance from center, looks like a ring or coin.",
            "Three straight sides, three angles, like a slice of pizza.",
            "Four equal sides and four right angles, like a chessboard square.",
            "Four sides with opposite sides equal, like a door or book."
    };

    public ShapeIntroPagerAdapter(Context ctx) {
        context = ctx;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return shapeNames.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.item_shape_intro, container, false);

        ImageView imageView = view.findViewById(R.id.shapeImage);
        TextView textName = view.findViewById(R.id.shapeName);
        TextView textDesc = view.findViewById(R.id.shapeDescription);

        imageView.setImageResource(shapeDrawables[position]);
        textName.setText(shapeNames[position]);
        textDesc.setText(shapeDescriptions[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
