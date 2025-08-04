package com.example.maths;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class StreetView extends View {
    private ArrayList<House> houses;
    private Street street;
    private Paint housePaint, slotPaint, textPaint, streetPaint;
    private House draggedHouse;
    private StreetCompletionListener listener;
    private Drawable houseIcon;

    private static final int HOUSE_WIDTH = 140;
    private static final int HOUSE_HEIGHT = 160;
    private static final int SLOT_WIDTH = 150;
    private static final int SLOT_HEIGHT = 170;

    public interface StreetCompletionListener {
        void onStreetCompleted(boolean isCorrect);
        void onHousePlaced(int totalPlaced, int totalNeeded);
    }

    public StreetView(Context context) {
        super(context);
        init();
    }

    public StreetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        houses = new ArrayList<>();
        street = new Street("Main Street", 0);

        // Load house icon
        houseIcon = ContextCompat.getDrawable(getContext(), R.drawable.house_icon);

        setupPaints();
        setupStreet();
    }

    private void setupPaints() {
        housePaint = new Paint();
        housePaint.setAntiAlias(true);

        slotPaint = new Paint();
        slotPaint.setColor(0x60888888);
        slotPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(0xFF000000);
        textPaint.setTextSize(32);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);

        streetPaint = new Paint();
        streetPaint.setColor(0xFF666666);
        streetPaint.setAntiAlias(true);
    }

    private void setupStreet() {
        // ADJUSTED: Move drop zones back to the left by reducing the offset
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int slotX = screenWidth - 300;  // Changed from 200 to 300 (moved left by 100px)
        int startY = 80;
        int verticalSpacing = 180;

        for (int i = 0; i < 5; i++) {
            float left = slotX;
            float top = startY + (i * verticalSpacing);
            float right = left + SLOT_WIDTH;
            float bottom = top + SLOT_HEIGHT;

            street.addHouseSlot(left, top, right, bottom);
        }
    }

    public void setupChallenge(ArrayList<Integer> sequence, ArrayList<Integer> availableNumbers) {
        houses.clear();
        street.getPlacedHouses().clear();
        street.setCorrectSequence(sequence);

        // Keep draggable houses on left side
        int startX = 50;
        int startY = 80;
        int verticalSpacing = 180;

        for (int i = 0; i < availableNumbers.size(); i++) {
            float x = startX;
            float y = startY + (i * verticalSpacing);

            houses.add(new House(availableNumbers.get(i), x, y, i));
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // UPDATED: Draw wider road exactly at the center of the screen
        int screenWidth = getWidth();
        int roadWidth = 160;  // Increased from 120 to 160 (40px wider)
        int roadLeft = (screenWidth - roadWidth) / 2;  // Center calculation
        int roadRight = roadLeft + roadWidth;

        canvas.drawRect(roadLeft, 60, roadRight, getHeight() - 100, streetPaint);

        // Draw "STREET" label in the centered road
        textPaint.setColor(0xFFFFFFFF);
        textPaint.setTextSize(22);  // Slightly larger text for wider road
        canvas.save();
        canvas.rotate(-90, screenWidth / 2, 300);
        canvas.drawText("MAIN STREET", (screenWidth / 2) - 60, (screenWidth / 2) + 10, textPaint);
        canvas.restore();

        // Draw house slots (position boxes) - now properly positioned on right
        for (int i = 0; i < street.getHouseSlots().size(); i++) {
            RectF slot = street.getHouseSlots().get(i);
            canvas.drawRoundRect(slot, 20, 20, slotPaint);

            // Draw position labels
            if (i >= street.getPlacedHouses().size() || street.getPlacedHouses().get(i) == null) {
                textPaint.setColor(0xFFFFFFFF);
                textPaint.setTextSize(16);
                canvas.drawText("Position " + (i + 1), slot.centerX(), slot.centerY() - 10, textPaint);
                textPaint.setTextSize(40);
                canvas.drawText("?", slot.centerX(), slot.centerY() + 20, textPaint);
            }
        }

        // Draw placed houses in slots
        for (int i = 0; i < street.getPlacedHouses().size(); i++) {
            House house = street.getPlacedHouses().get(i);
            if (house != null) {
                RectF slot = street.getHouseSlots().get(i);
                drawHouseWithIcon(canvas, slot.centerX() - HOUSE_WIDTH/2, slot.centerY() - HOUSE_HEIGHT/2, house, true);
            }
        }

        // Draw draggable houses - vertical column on left
        for (House house : houses) {
            if (!house.isPlaced() && house != draggedHouse) {
                drawHouseWithIcon(canvas, house.getX(), house.getY(), house, false);
            }
        }

        // Draw dragged house on top
        if (draggedHouse != null) {
            drawHouseWithIcon(canvas, draggedHouse.getX(), draggedHouse.getY(), draggedHouse, false);
        }

        // Draw instruction arrow from left to center-right
        drawInstructionArrow(canvas);
    }

    private void drawInstructionArrow(Canvas canvas) {
        Paint arrowPaint = new Paint();
        arrowPaint.setColor(0x80FF9800);
        arrowPaint.setStrokeWidth(8);
        arrowPaint.setAntiAlias(true);

        // UPDATED: Adjust arrow positioning for new drop zone location
        int screenWidth = getWidth();
        int arrowStart = 220;
        int arrowEnd = screenWidth - 380;  // Adjusted for new drop zone position

        canvas.drawLine(arrowStart, 200, arrowEnd, 200, arrowPaint);
        canvas.drawLine(arrowEnd, 200, arrowEnd - 20, 180, arrowPaint);  // Arrow head top
        canvas.drawLine(arrowEnd, 200, arrowEnd - 20, 220, arrowPaint);  // Arrow head bottom
    }

    private void drawHouseWithIcon(Canvas canvas, float x, float y, House house, boolean inSlot) {
        // Set bounds for the house icon
        houseIcon.setBounds((int)x, (int)y, (int)(x + HOUSE_WIDTH), (int)(y + HOUSE_HEIGHT));

        // Draw the house icon
        houseIcon.draw(canvas);

        // Enhanced colored overlay based on state
        Paint overlay = new Paint();
        overlay.setAntiAlias(true);

        if (inSlot) {
            // Red overlay for wrong position, green for correct position
            overlay.setColor(house.isCorrectPosition() ? 0x404CAF50 : 0x40FF5722);  // Green or RED
        } else {
            // Orange overlay for draggable houses
            overlay.setColor(0x40FF9800);
        }

        RectF houseRect = new RectF(x, y, x + HOUSE_WIDTH, y + HOUSE_HEIGHT);
        canvas.drawRoundRect(houseRect, 18, 18, overlay);

        // Draw house number prominently
        textPaint.setColor(0xFF000000);
        textPaint.setTextSize(36);
        canvas.drawText(String.valueOf(house.getNumber()),
                x + HOUSE_WIDTH/2, y + HOUSE_HEIGHT/2 + 12, textPaint);

        // Border color matches overlay state
        Paint borderPaint = new Paint();
        if (inSlot) {
            borderPaint.setColor(house.isCorrectPosition() ? 0xFF4CAF50 : 0xFFFF5722);  // Green or RED
        } else {
            borderPaint.setColor(0xFF000000);  // Black for draggable
        }
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(3);
        borderPaint.setAntiAlias(true);
        canvas.drawRoundRect(houseRect, 18, 18, borderPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                draggedHouse = findHouseAt(x, y);
                if (draggedHouse != null) {
                    if (draggedHouse.isPlaced()) {
                        street.removeHouse(draggedHouse.getTargetPosition());
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (draggedHouse != null) {
                    draggedHouse.setX(x - HOUSE_WIDTH/2);
                    draggedHouse.setY(y - HOUSE_HEIGHT/2);
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                if (draggedHouse != null) {
                    handleDrop(draggedHouse, x, y);
                    draggedHouse = null;
                    checkStreetCompletion();
                    invalidate();
                }
                break;
        }
        return true;
    }

    private House findHouseAt(float x, float y) {
        // Check placed houses first
        for (int i = 0; i < street.getPlacedHouses().size(); i++) {
            House house = street.getPlacedHouses().get(i);
            if (house != null) {
                RectF slot = street.getHouseSlots().get(i);
                if (x >= slot.left && x <= slot.right && y >= slot.top && y <= slot.bottom) {
                    return house;
                }
            }
        }

        // Check draggable houses
        for (House house : houses) {
            if (!house.isPlaced() &&
                    x >= house.getX() && x <= house.getX() + HOUSE_WIDTH &&
                    y >= house.getY() && y <= house.getY() + HOUSE_HEIGHT) {
                return house;
            }
        }
        return null;
    }

    private void handleDrop(House house, float x, float y) {
        int slotIndex = street.findSlotAt(x, y);

        if (slotIndex != -1) {
            street.placeHouse(house, slotIndex);

            boolean isCorrect = (slotIndex < street.getCorrectSequence().size() &&
                    house.getNumber() == street.getCorrectSequence().get(slotIndex));
            house.setCorrectPosition(isCorrect);
        } else {
            resetHousePosition(house);
        }
    }

    private void resetHousePosition(House house) {
        // Reset to vertical positions on left side
        int houseId = house.getId();

        house.setX(50);
        house.setY(80 + houseId * 180);
        house.setPlaced(false);
        house.setTargetPosition(-1);
    }

    private void checkStreetCompletion() {
        int placedCount = 0;
        for (House house : street.getPlacedHouses()) {
            if (house != null) placedCount++;
        }

        if (listener != null) {
            listener.onHousePlaced(placedCount, street.getCorrectSequence().size());

            if (placedCount == street.getCorrectSequence().size()) {
                listener.onStreetCompleted(street.isSequenceCorrect());
            }
        }
    }

    public void setListener(StreetCompletionListener listener) {
        this.listener = listener;
    }

    public void resetStreet() {
        for (House house : houses) {
            resetHousePosition(house);
        }
        street.getPlacedHouses().clear();
        invalidate();
    }
}
