package com.example.maths;

import android.graphics.RectF;

import java.util.ArrayList;

public class DropZone {
    private RectF bounds;
    private ArrayList<NumberBlock> containedBlocks;
    private int maxCapacity;
    private String label;

    public DropZone(float left, float top, float right, float bottom, int maxCapacity, String label) {
        this.bounds = new RectF(left, top, right, bottom);
        this.containedBlocks = new ArrayList<>();
        this.maxCapacity = maxCapacity;
        this.label = label;
    }

    public boolean containsPoint(float x, float y) {
        return bounds.contains(x, y);
    }

    public boolean canAcceptBlock() {
        return containedBlocks.size() < maxCapacity;
    }

    public void addBlock(NumberBlock block) {
        if (canAcceptBlock()) {
            containedBlocks.add(block);
            block.setInDropZone(true);
        }
    }

    public void removeBlock(NumberBlock block) {
        containedBlocks.remove(block);
        block.setInDropZone(false);
    }

    public int getTotalValue() {
        int total = 0;
        for (NumberBlock block : containedBlocks) {
            total += block.getValue();
        }
        return total;
    }

    // Getters
    public RectF getBounds() { return bounds; }
    public ArrayList<NumberBlock> getContainedBlocks() { return containedBlocks; }
    public String getLabel() { return label; }
    public int getBlockCount() { return containedBlocks.size(); }
}
