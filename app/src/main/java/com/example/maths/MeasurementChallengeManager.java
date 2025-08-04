package com.example.maths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeasurementChallengeManager {
    private final List<MeasurementObject> objectPool;
    private int idx = -1;

    public MeasurementChallengeManager() {
        objectPool = new ArrayList<>();
        objectPool.add(new MeasurementObject("Apple", R.drawable.apple1, 8.0f));
        objectPool.add(new MeasurementObject("Pencil", R.drawable.pencil, 7.0f));
        objectPool.add(new MeasurementObject("Paintbrush", R.drawable.paintbrush, 10.0f));
        objectPool.add(new MeasurementObject("Dinosaur", R.drawable.dinosaur, 20.0f));
        objectPool.add(new MeasurementObject("Shoe", R.drawable.shoe, 15.0f));
        Collections.shuffle(objectPool);
    }

    public MeasurementObject nextObject() {
        idx = (idx + 1) % objectPool.size();
        return objectPool.get(idx);
    }
}
