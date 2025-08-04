package com.example.maths;

import android.graphics.RectF;

import java.util.ArrayList;

public class Street {
    private ArrayList<RectF> houseSlots;
    private ArrayList<Integer> correctSequence;
    private ArrayList<House> placedHouses;
    private String streetName;
    private int sequenceType; // 0 = regular, 1 = skip counting, 2 = missing numbers

    public Street(String streetName, int sequenceType) {
        this.streetName = streetName;
        this.sequenceType = sequenceType;
        this.houseSlots = new ArrayList<>();
        this.correctSequence = new ArrayList<>();
        this.placedHouses = new ArrayList<>();
    }

    public void addHouseSlot(float left, float top, float right, float bottom) {
        houseSlots.add(new RectF(left, top, right, bottom));
    }

    public void setCorrectSequence(ArrayList<Integer> sequence) {
        this.correctSequence = new ArrayList<>(sequence);
    }

    public boolean isHouseInSlot(float x, float y, int slotIndex) {
        if (slotIndex < 0 || slotIndex >= houseSlots.size()) return false;
        RectF slot = houseSlots.get(slotIndex);
        return slot.contains(x, y);
    }

    public int findSlotAt(float x, float y) {
        for (int i = 0; i < houseSlots.size(); i++) {
            if (houseSlots.get(i).contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isSequenceCorrect() {
        if (placedHouses.size() != correctSequence.size()) return false;

        for (int i = 0; i < placedHouses.size(); i++) {
            if (placedHouses.get(i) == null ||
                    placedHouses.get(i).getNumber() != correctSequence.get(i)) {
                return false;
            }
        }
        return true;
    }

    public void placeHouse(House house, int slotIndex) {
        // Ensure the list is large enough
        while (placedHouses.size() <= slotIndex) {
            placedHouses.add(null);
        }
        placedHouses.set(slotIndex, house);
        house.setPlaced(true);
        house.setTargetPosition(slotIndex);
    }

    public void removeHouse(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < placedHouses.size()) {
            House house = placedHouses.get(slotIndex);
            if (house != null) {
                house.setPlaced(false);
                house.setTargetPosition(-1);
                placedHouses.set(slotIndex, null);
            }
        }
    }

    // Getters
    public ArrayList<RectF> getHouseSlots() { return houseSlots; }
    public ArrayList<Integer> getCorrectSequence() { return correctSequence; }
    public ArrayList<House> getPlacedHouses() { return placedHouses; }
    public String getStreetName() { return streetName; }
    public int getSequenceType() { return sequenceType; }
}
