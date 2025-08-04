package com.example.maths;

public class Balloon {
    private float x, y;
    private boolean popped;

    public Balloon(float x, float y) {
        this.x = x;
        this.y = y;
        this.popped = false;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public boolean isPopped() { return popped; }
    public void pop() { this.popped = true; }
}
