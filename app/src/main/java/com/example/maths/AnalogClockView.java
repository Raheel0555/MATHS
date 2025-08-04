package com.example.maths;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class AnalogClockView extends View {

    public interface OnTimeSetListener {
        void onTimeSet(int hour, int minute);
    }

    private int hour = 0, minute = 0;
    private float centerX, centerY, radius;
    private Paint clockPaint, handPaint, minuteHandPaint, hourHandPaint, circlePaint, textPaint, tickPaint;
    private OnTimeSetListener listener;
    private Handler handler = new Handler(Looper.getMainLooper());
    private float animMinute = 0, animHour = 0;
    private boolean isAnimating = false;

    public AnalogClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        clockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        clockPaint.setStyle(Paint.Style.FILL);
        clockPaint.setColor(Color.parseColor("#B2EBF2"));

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(Color.parseColor("#0288D1"));
        circlePaint.setStrokeWidth(16);

        handPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        handPaint.setStyle(Paint.Style.STROKE);

        hourHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hourHandPaint.setColor(Color.parseColor("#FFD600"));
        hourHandPaint.setStrokeWidth(14f);
        hourHandPaint.setStrokeCap(Paint.Cap.ROUND);

        minuteHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        minuteHandPaint.setColor(Color.parseColor("#D32F2F"));
        minuteHandPaint.setStrokeWidth(10f);
        minuteHandPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(44);
        textPaint.setTextAlign(Paint.Align.CENTER);

        tickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tickPaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;
        radius = Math.min(centerX, centerY) - 30;

        canvas.drawCircle(centerX, centerY, radius + 20, clockPaint);
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        for (int i = 0; i < 60; i++) {
            float tickLength = (i % 5 == 0) ? 32 : 18;
            tickPaint.setColor(i % 5 == 0 ? Color.parseColor("#43A047") : Color.parseColor("#FF8A65"));
            float angle = (float) (Math.PI * 2 * i / 60);
            float sx = (float) (centerX + Math.cos(angle) * (radius - tickLength));
            float sy = (float) (centerY + Math.sin(angle) * (radius - tickLength));
            float ex = (float) (centerX + Math.cos(angle) * (radius - 2));
            float ey = (float) (centerY + Math.sin(angle) * (radius - 2));
            canvas.drawLine(sx, sy, ex, ey, tickPaint);
        }

        int[] numberColors = {
                Color.parseColor("#D32F2F"),
                Color.parseColor("#388E3C"),
                Color.parseColor("#1976D2"),
                Color.parseColor("#FBC02D"),
                Color.parseColor("#F57C00"),
                Color.parseColor("#7B1FA2"),
                Color.parseColor("#0288D1"),
                Color.parseColor("#C2185B"),
                Color.parseColor("#0097A7"),
                Color.parseColor("#FBC02D"),
                Color.parseColor("#388E3C"),
                Color.parseColor("#D32F2F")
        };

        for (int i = 1; i <= 12; i++) {
            double angle = Math.PI / 6 * (i - 3);
            float x = (float) (centerX + Math.cos(angle) * (radius - 70));
            float y = (float) (centerY + Math.sin(angle) * (radius - 70) + 18);
            textPaint.setColor(numberColors[(i - 1) % numberColors.length]);
            canvas.drawText(String.valueOf(i), x, y, textPaint);
        }

        float displayedMinute = isAnimating ? animMinute : minute;
        float displayedHour = isAnimating ? animHour : hour;

        float hourAngle = (float) ((displayedHour % 12 + displayedMinute / 60.0) * Math.PI / 6 - Math.PI / 2);
        float hourX = (float) (centerX + Math.cos(hourAngle) * (radius * 0.5));
        float hourY = (float) (centerY + Math.sin(hourAngle) * (radius * 0.5));
        canvas.drawLine(centerX, centerY, hourX, hourY, hourHandPaint);

        float minuteAngle = (float) (displayedMinute * Math.PI / 30 - Math.PI / 2);
        float minX = (float) (centerX + Math.cos(minuteAngle) * (radius * 0.75));
        float minY = (float) (centerY + Math.sin(minuteAngle) * (radius * 0.75));
        canvas.drawLine(centerX, centerY, minX, minY, minuteHandPaint);

        Paint dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setStyle(Paint.Style.FILL);
        dotPaint.setColor(Color.parseColor("#FF6F00"));
        canvas.drawCircle(centerX, centerY, 18, dotPaint);
    }

    public void setInitialTime(int hour, int minute) {
        animateToTime(hour, minute);
    }

    private void animateToTime(int targetHour, int targetMinute) {
        final float oldMinute = this.minute;
        final float oldHour = this.hour;
        final float newMinute = targetMinute;
        final float newHour = targetHour;

        final int duration = 350;
        final int frames = 32;
        isAnimating = true;

        handler.removeCallbacksAndMessages(null);
        for (int i = 0; i <= frames; i++) {
            final float progress = (float) i / frames;
            handler.postDelayed(() -> {
                animMinute = oldMinute + (newMinute - oldMinute) * progress;
                animHour = oldHour + (newHour - oldHour) * progress;
                invalidate();
                if (progress >= 1.0f) {
                    AnalogClockView.this.hour = targetHour;
                    AnalogClockView.this.minute = targetMinute;
                    isAnimating = false;
                    invalidate();
                }
            }, duration * i / frames);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                float dx = event.getX() - centerX;
                float dy = event.getY() - centerY;
                double angle = Math.atan2(dy, dx);
                int newMinute = (int) ((Math.toDegrees(angle) + 90 + 360) % 360 / 6);

                if (Math.abs(newMinute - this.minute) > 30) {
                    if (newMinute < this.minute) {
                        hour = (hour + 1) % 12;
                    } else {
                        hour = (hour + 11) % 12;
                    }
                }
                this.minute = newMinute;
                invalidate();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                if (listener != null) {
                    listener.onTimeSet(hour, minute);
                }
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    public void setOnTimeSetListener(OnTimeSetListener listener) {
        this.listener = listener;
    }
}
