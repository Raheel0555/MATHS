package com.example.maths;

public class NumberBlock {
    private int value; // 10 for tens, 1 for ones
    private float x, y;
    private boolean isDragging;
    private boolean isInDropZone;
    private int id;

    public NumberBlock(int value, float x, float y, int id) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.id = id;
        this.isDragging = false;
        this.isInDropZone = false;
    }

    // Getters and setters
    public int getValue() { return value; }
    public float getX() { return x; }
    public float getY() { return y; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public boolean isDragging() { return isDragging; }
    public void setDragging(boolean dragging) { isDragging = dragging; }
    public boolean isInDropZone() { return isInDropZone; }
    public void setInDropZone(boolean inDropZone) { isInDropZone = inDropZone; }
    public int getId() { return id; }
}
