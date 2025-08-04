package com.example.maths;

import android.graphics.PointF;
import android.graphics.RectF;

public class ShapeOption {
    public enum Type { CIRCLE, TRIANGLE, SQUARE, RECTANGLE }
    public Type type;
    public int color;
    public PointF center;  // for drawing
    public float size;
    public boolean isLoaded; // on truck
    public PointF originalPos;

    public ShapeOption(Type type, int color, PointF center, float size) {
        this.type = type;
        this.color = color;
        this.center = center;
        this.size = size;
        this.isLoaded = false;
        this.originalPos = new PointF(center.x, center.y);
    }

    public void resetPosition() {
        center.set(originalPos.x, originalPos.y);
        isLoaded = false;
    }

    // Used for hit-test
    public RectF getBounds() {
        return new RectF(center.x - size, center.y - size,
                center.x + size, center.y + size);
    }
}
