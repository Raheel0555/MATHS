package com.example.maths;

public class CoinInfo {
    private int value;
    private String name;
    private int imageRes;
    private String funFact;
    private boolean isNote;

    public CoinInfo(int value, String name, int imageRes, String funFact, boolean isNote) {
        this.value = value;
        this.name = name;
        this.imageRes = imageRes;
        this.funFact = funFact;
        this.isNote = isNote;
    }
    public int getValue() { return value; }
    public String getName() { return name; }
    public int getImageRes() { return imageRes; }
    public String getFunFact() { return funFact; }
    public boolean isNote() { return isNote; }
}
