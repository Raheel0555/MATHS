package com.example.maths;

public class House {
    private int number;
    private float x, y;
    private boolean isPlaced;
    private boolean isCorrectPosition;
    private int targetPosition;
    private int id;

    public House(int number, float x, float y, int id) {
        this.number = number;
        this.x = x;
        this.y = y;
        this.id = id;
        this.isPlaced = false;
        this.isCorrectPosition = false;
        this.targetPosition = -1;
    }

    // Getters and setters
    public int getNumber() { return number; }
    public float getX() { return x; }
    public float getY() { return y; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public boolean isPlaced() { return isPlaced; }
    public void setPlaced(boolean placed) { isPlaced = placed; }
    public boolean isCorrectPosition() { return isCorrectPosition; }
    public void setCorrectPosition(boolean correctPosition) { isCorrectPosition = correctPosition; }
    public int getTargetPosition() { return targetPosition; }
    public void setTargetPosition(int targetPosition) { this.targetPosition = targetPosition; }
    public int getId() { return id; }
}
