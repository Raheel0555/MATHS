package com.example.maths;

public class MeasurementObject {
    public final String name;
    public final int drawableRes;
    public final float trueCmLength;

    public MeasurementObject(String name, int drawableRes, float trueCmLength) {
        this.name = name;
        this.drawableRes = drawableRes;
        this.trueCmLength = trueCmLength;
    }
}
