package com.example.maths;

import java.util.Arrays;
import java.util.List;

public class GameState {
    public static final List<ShapeOption.Type> SHAPE_SEQUENCE = Arrays.asList(
            ShapeOption.Type.CIRCLE,
            ShapeOption.Type.RECTANGLE,
            ShapeOption.Type.TRIANGLE,
            ShapeOption.Type.SQUARE
    );
    public int currentIndex = 0;
    public boolean isRoundComplete = false;

    public ShapeOption.Type getCurrentShape() {
        return SHAPE_SEQUENCE.get(currentIndex);
    }

    public void nextShape() {
        currentIndex = (currentIndex + 1) % SHAPE_SEQUENCE.size();
        isRoundComplete = false;
    }

    public boolean isLastShape() {
        return currentIndex == SHAPE_SEQUENCE.size() - 1;
    }
}
