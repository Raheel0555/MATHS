package com.example.maths;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class ClosestBarView extends View {
    private float closeness = 0f; // 0: far left/wrong, 1: far right/correct, 0.5 = 50% close
    private String indicatorText = "";

    public ClosestBarView(Context context, AttributeSet attrs) { super(context, attrs); }

    @Override
    protected void onDraw(Canvas canvas) {
        int W = getWidth(), H = getHeight();

        // Draw left (red) and right (green) halves
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        RectF rect = new RectF(0, 0, W, H);
        LinearGradient grad = new LinearGradient(0, 0, W, 0,
                new int[]{ Color.parseColor("#EA4335"), Color.parseColor("#53D769") },
                new float[]{ 0.5f, 0.5f }, Shader.TileMode.CLAMP);
        p.setShader(grad);
        canvas.drawRoundRect(rect, H/2f, H/2f, p);
        p.setShader(null);

        // Draw bar edge
        p.setColor(0x18000000);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3f);
        canvas.drawRoundRect(rect, H/2f, H/2f, p);

        // Draw the marker (dot/knob)
        int barMargin = (int)(H * 0.16f);
        float posX = barMargin + ((W - 2*barMargin) * closeness);
        Paint knobPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        knobPaint.setColor(Color.WHITE);
        knobPaint.setStrokeWidth(8f);
        canvas.drawCircle(posX, H/2f, H/2.5f, knobPaint);

        // Draw border on knob
        knobPaint.setColor(Color.argb(128,0,0,0));
        knobPaint.setStyle(Paint.Style.STROKE);
        knobPaint.setStrokeWidth(3f);
        canvas.drawCircle(posX, H/2f, H/2.5f, knobPaint);

        // Draw indicator text above bar
        if (indicatorText != null && !indicatorText.isEmpty()) {
            Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setTextSize(H * 0.93f);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setColor(Color.BLACK);
            canvas.drawText(indicatorText, W/2f, 0-H*0.28f, textPaint);
        }
    }

    // Closeness: 0 = red end, 1 = green end
    public void setCloseness(float val, String feedback) {
        this.closeness = Math.max(0f, Math.min(val, 1f));
        this.indicatorText = feedback;
        invalidate();
    }
}
