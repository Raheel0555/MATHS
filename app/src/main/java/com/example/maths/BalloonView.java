package com.example.maths;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;

public class BalloonView extends View {
    private ArrayList<Balloon> balloons = new ArrayList<>();
    private BalloonPoppedListener listener;
    private int balloonImageId = R.drawable.balloon;
    private Bitmap balloonBitmap;
    private int balloonCount = 0;
    private Paint cloudPaint;

    private static final int BALLOON_WIDTH = 80;
    private static final int BALLOON_HEIGHT = 110;

    public interface BalloonPoppedListener {
        void onBalloonPopped(int count);
    }

    public BalloonView(Context context) {
        super(context);
        init();
    }

    public BalloonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        Bitmap original = BitmapFactory.decodeResource(getResources(), balloonImageId);
        if (original != null) {
            balloonBitmap = Bitmap.createScaledBitmap(original, BALLOON_WIDTH, BALLOON_HEIGHT, true);
        }

        // Paint for decorative clouds
        cloudPaint = new Paint();
        cloudPaint.setColor(0x40FFFFFF);
        cloudPaint.setAntiAlias(true);
    }

    public void setupBalloons(int count) {
        this.balloonCount = count;
        balloons.clear();

        int columns = Math.min(count, 5);
        int spacingX = 25;
        int spacingY = 35;

        for (int i = 0; i < count; i++) {
            int col = i % columns;
            int row = i / columns;
            float x = spacingX + col * (BALLOON_WIDTH + spacingX);
            float y = spacingY + row * (BALLOON_HEIGHT + spacingY);
            balloons.add(new Balloon(x, y));
        }
        invalidate();
    }

    public void setListener(BalloonPoppedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw decorative clouds
        drawClouds(canvas);

        if (balloonBitmap == null) return;
        for (Balloon b : balloons) {
            if (!b.isPopped()) {
                canvas.drawBitmap(balloonBitmap, b.getX(), b.getY(), null);
            }
        }
    }

    private void drawClouds(Canvas canvas) {
        // Simple decorative clouds in background
        canvas.drawCircle(100, 80, 30, cloudPaint);
        canvas.drawCircle(120, 70, 25, cloudPaint);
        canvas.drawCircle(140, 75, 20, cloudPaint);

        canvas.drawCircle(getWidth() - 100, 60, 35, cloudPaint);
        canvas.drawCircle(getWidth() - 80, 50, 30, cloudPaint);
        canvas.drawCircle(getWidth() - 60, 55, 25, cloudPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && balloonBitmap != null) {
            float touchX = event.getX();
            float touchY = event.getY();
            for (Balloon b : balloons) {
                if (!b.isPopped()) {
                    if (touchX > b.getX() && touchX < b.getX() + BALLOON_WIDTH &&
                            touchY > b.getY() && touchY < b.getY() + BALLOON_HEIGHT) {
                        b.pop();

                        // Add pop animation
                        animatePop(b);

                        if (listener != null)
                            listener.onBalloonPopped(getPoppedCount());
                        invalidate();
                        break;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private void animatePop(Balloon balloon) {
        // Simple scale animation for popped balloon effect
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.1f, 1f);
        scaleX.setDuration(200);
        scaleX.setInterpolator(new AccelerateInterpolator());
        scaleX.start();
    }

    public int getPoppedCount() {
        int c = 0;
        for (Balloon b : balloons) {
            if (b.isPopped()) c++;
        }
        return c;
    }
}
