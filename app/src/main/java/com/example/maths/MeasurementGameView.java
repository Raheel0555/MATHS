package com.example.maths;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class MeasurementGameView extends View {
    private MeasurementChallengeManager challengeManager = new MeasurementChallengeManager();
    private MeasurementObject currentObject;
    private Bitmap objectBitmap;

    // Layout/spacer multipliers for easy tuning
    private static final float HEADLINE_TOP_FRAC = 0.09f;
    private static final float QUESTION_TOP_FRAC = 0.16f;
    private static final float TAPE_Y_FRAC = 0.28f;
    private static final float TAPE_OBJECT_MARGIN_FRAC = 0.10f;
    private static final float OBJ_NAME_MARGIN = 56f;
    private static final float MEASURED_MARGIN = 115f;
    private static final float FEEDBACK_FRAC = 0.91f;

    private float tapeStartX, tapeY, tapeHeadX;
    private float tapeHeightDp = 38f; // <<== smaller tape tool!
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float pixelsPerCm = 0.0f;

    private EditText inputBox;
    private ClosestBarView barView;
    private boolean inputChecked = false;
    private boolean draggingTapeHead = false;
    private float tapeDragOffset = 0;
    private String popupMessage = "";

    private GuessFeedbackListener feedbackListener;
    public interface GuessFeedbackListener { void onGuessChecked(boolean valid); }
    public void setFeedbackListener(GuessFeedbackListener l) { this.feedbackListener = l; }
    public void attachBarView(ClosestBarView bar) { this.barView = bar; }

    public MeasurementGameView(Context c, AttributeSet a) {
        super(c, a);
        loadNextObject();
    }

    public void attachInputBox(EditText box) {
        this.inputBox = box;
        inputBox.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inputBox.setHint("Your guess (cm)");
        inputBox.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) { updateFeedback(); }
            @Override public void afterTextChanged(Editable s) {}
        });
    }
    public void resetInputChecked() { inputChecked = false; }

    private void updateFeedback() {
        popupMessage = "";
        inputChecked = false;
        String input = inputBox == null ? "" : inputBox.getText().toString().trim();
        if (input.isEmpty()) {
            setGuessState(false); updateBar(0f, ""); invalidate(); return;
        }
        float guess = 0, answer = currentObject.trueCmLength;
        String msg = "";
        float closeness = 0f;
        boolean valid = false;
        try { guess = Float.parseFloat(input); }
        catch (NumberFormatException e) { msg = "Please enter a valid number!"; setGuessState(false); updateBar(0f, msg); invalidate(); return; }
        float diff = guess - answer;
        float absdiff = Math.abs(diff);
        float closeNorm = 1f - Math.min(absdiff / Math.max(answer, 1f), 1f);
        if (absdiff < 0.5f) {
            msg = "🎉 Great! That's correct: " + String.format("%.1f", answer) + " cm!";
            valid = true; closeness = 1f;
        } else if (diff < 0 && absdiff < 1.5f) {
            msg = "👍 You're close!";
            valid = true; closeness = Math.max(closeNorm, 0.7f);
        } else if (diff < 0) {
            msg = "That’s less than the real answer!";
            valid = true; closeness = Math.max(closeNorm, 0.45f);
        } else {
            msg = "That’s more than the real answer!";
            valid = true; closeness = Math.max(closeNorm, 0.45f);
        }
        popupMessage = msg;
        setGuessState(valid); updateBar(closeness, msg); invalidate();
    }
    private void updateBar(float closeness, String feedback) {
        if (barView != null) barView.setCloseness(closeness, feedback);
    }
    private void setGuessState(boolean valid) {
        if (feedbackListener != null) feedbackListener.onGuessChecked(valid);
    }

    private void loadNextObject() {
        currentObject = challengeManager.nextObject();
        objectBitmap = BitmapFactory.decodeResource(getResources(), currentObject.drawableRes);
        inputChecked = false;
        if (inputBox != null) inputBox.setText("");
        popupMessage = "";
        setGuessState(false); updateBar(0f, "");
        requestLayout();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float phoneUsablePx = w * 0.82f;
        pixelsPerCm = phoneUsablePx / 20f;
        tapeStartX = (w - phoneUsablePx) / 2f;
        tapeHeadX = tapeStartX;
        tapeY = h * TAPE_Y_FRAC;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas c) {
        int W = getWidth(), H = getHeight();

        // Soft background
        Paint bg = new Paint();
        bg.setShader(new LinearGradient(0, 0, 0, H, 0xFFFEFCF2, 0xFFD6F0FC, Shader.TileMode.CLAMP));
        c.drawRect(0, 0, W, H, bg);

        // Headline and question area
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.parseColor("#512DA8"));
        paint.setTextSize(88f);
        c.drawText("?", W / 2f, H * HEADLINE_TOP_FRAC, paint);

        paint.setColor(Color.parseColor("#176481"));
        paint.setTextSize(50f);
        c.drawText("How long do you think this is?", W / 2f, H * QUESTION_TOP_FRAC, paint);

        // Tape: small, modern
        float tapeH = tapeHeightDp * getResources().getDisplayMetrics().density / 1.6f;
        float tapeMaxW = 20f * pixelsPerCm;
        float tapeW = Math.max(18, tapeHeadX - tapeStartX);

        paint.setColor(Color.WHITE);
        paint.setShadowLayer(13, 0, 8, Color.parseColor("#E0C677"));
        RectF tapeRect = new RectF(tapeStartX, tapeY, tapeStartX + tapeW, tapeY + tapeH);
        c.drawRoundRect(tapeRect, 16f, 16f, paint);
        paint.clearShadowLayer();

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3.2f);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(22f);

        int maxTick = Math.round(tapeMaxW / pixelsPerCm);
        int drawTick = Math.max(Math.round(tapeW / pixelsPerCm), 1);
        for (int i = 0; i <= maxTick; i++) {
            float px = tapeStartX + i * pixelsPerCm;
            if (px > tapeStartX + tapeW) break;
            c.drawLine(px, tapeY, px, tapeY + (i % 5 == 0 ? tapeH * 0.72f : tapeH * 0.38f), paint);
            if (i > 0 && i % 1 == 0 && i <= drawTick && i < 21)
                c.drawText(String.valueOf(i), px, tapeY + tapeH - 14, paint);
        }

        int headColor = Color.parseColor("#FFD000");
        paint.setColor(headColor);
        c.drawCircle(tapeHeadX, tapeY + tapeH / 2f, tapeH * 0.6f, paint);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4.8f);
        c.drawCircle(tapeHeadX, tapeY + tapeH / 2f, tapeH * 0.6f, paint);
        paint.setStyle(Paint.Style.FILL);

        // Object: keep horizontal/accurate, layout more downwards
        if (objectBitmap != null) {
            float objW = currentObject.trueCmLength * pixelsPerCm;
            float objX = tapeStartX;
            float objY = tapeY + tapeH + H * TAPE_OBJECT_MARGIN_FRAC;
            float objH = objectBitmap.getHeight() * (objW / objectBitmap.getWidth());
            RectF objectRect = new RectF(objX, objY, objX + objW, objY + objH);
            c.drawBitmap(objectBitmap, null, objectRect, paint);

            // Object name: visually further down, bigger
            paint.setColor(Color.parseColor("#1A8927"));
            paint.setTextSize(54f);
            paint.setTextAlign(Paint.Align.CENTER);
            c.drawText(currentObject.name, W / 2f, objY + objH + OBJ_NAME_MARGIN, paint);

            // Measured label, far below image
            float measuredY = objY + objH + MEASURED_MARGIN;
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setColor(Color.parseColor("#B45309"));
            paint.setTextSize(43f);
            float liveMeasuredLength = (tapeHeadX - tapeStartX) / pixelsPerCm;
            c.drawText(
                    "Measured: " + String.format("%.1f", Math.max(0, liveMeasuredLength)) + " cm",
                    tapeStartX, measuredY, paint
            );
        }

        // Feedback: large, lower
        if (popupMessage != null && !popupMessage.isEmpty()) {
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(59f);
            if (popupMessage.contains("Great")) paint.setColor(Color.parseColor("#38B000"));
            else if (popupMessage.contains("close")) paint.setColor(Color.parseColor("#ED7D31"));
            else paint.setColor(Color.parseColor("#E5383B"));
            c.drawText(popupMessage, W / 2f, H * FEEDBACK_FRAC, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float tapeH = tapeHeightDp * getResources().getDisplayMetrics().density / 1.6f;
        float tapeHeadY = tapeY + tapeH / 2f;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (Math.abs(event.getX() - tapeHeadX) < tapeH * 0.7f &&
                        Math.abs(event.getY() - tapeHeadY) < tapeH * 0.8f) {
                    draggingTapeHead = true;
                    tapeDragOffset = event.getX() - tapeHeadX;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (draggingTapeHead) {
                    float newHeadX = event.getX() - tapeDragOffset;
                    float maxEnd = tapeStartX + 20f * pixelsPerCm;
                    if (newHeadX < tapeStartX) newHeadX = tapeStartX;
                    if (newHeadX > maxEnd) newHeadX = maxEnd;
                    tapeHeadX = newHeadX;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                draggingTapeHead = false; invalidate(); break;
        }
        return true;
    }

    public void goToNextObject() {
        loadNextObject();
        popupMessage = "";
        inputChecked = false;
        setGuessState(false);
        updateBar(0f, "");
    }
}
