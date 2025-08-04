package com.example.maths;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class ShapeTruckGameView extends View {
    private ArrayList<ShapeOption> options = new ArrayList<>();
    private ShapeOption dragging = null;
    private float dragOffsetX, dragOffsetY;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private GameState gameState = new GameState();

    private Bitmap truckBitmap;
    private float truckDrawW, truckDrawH;
    private float truckY, truckX, truckTargetX, truckStartX, truckEndX;
    private boolean truckDrivingIn = true, truckDrivingOut = false, truckWaiting = false;
    private float truckSpeed = 15f;
    private int shapesPerRow = 3;
    private Listener listener;
    private String popupMsg = null;
    private long popupShowTime = 0;

    public interface Listener {
        void onRoundFinished(boolean lastShape);
        void onWrongShape();
    }

    public ShapeTruckGameView(Context c, AttributeSet a) {
        super(c, a);
        truckBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.truck);
    }

    public void setListener(Listener l) {
        this.listener = l;
        System.out.println("DEBUG: Listener set in ShapeTruckGameView");
    }

    public void nextShape() {
        System.out.println("DEBUG: nextShape() called, current index before: " + gameState.currentIndex);
        gameState.nextShape();
        System.out.println("DEBUG: nextShape() called, current index after: " + gameState.currentIndex);
    }

    public GameState getGameState() { return gameState; }

    public void resetForCurrentShape() {
        System.out.println("DEBUG: resetForCurrentShape called for shape: " + gameState.getCurrentShape());

        if (getWidth() == 0 || getHeight() == 0) {
            post(this::resetForCurrentShape);
            return;
        }
        options.clear();
        gameState.isRoundComplete = false;

        int W = getWidth(), H = getHeight();

        float imgAspect = truckBitmap != null ? ((float)truckBitmap.getWidth() / truckBitmap.getHeight()) : 2.3f;
        truckDrawH = H * 0.22f;
        truckDrawW = imgAspect * truckDrawH;

        truckY = H * 0.63f;
        truckStartX = -truckDrawW;
        truckTargetX = (W - truckDrawW) / 2f;
        truckEndX = W + truckDrawW * 0.35f;
        truckX = truckStartX;

        float cargoLeft = truckTargetX + truckDrawW * 0.10f;
        float cargoRight = truckTargetX + truckDrawW * 0.75f;
        float slotSize = Math.min((cargoRight - cargoLeft) * 0.27f, truckDrawH * 0.58f);

        float usableW = W * 0.94f;
        float leftPad = W * 0.03f;
        float topGap = H * 0.12f;
        float rowSpacing = slotSize * 1.25f;

        // Create 4 rows of shapes: Circles, Rectangles, Triangles, Squares
        for (int i = 0; i < shapesPerRow; i++) {
            float x = leftPad + (usableW / (shapesPerRow - 1)) * i;

            // Row 1: Circles
            ShapeOption circle = new ShapeOption(ShapeOption.Type.CIRCLE, getColorForType(ShapeOption.Type.CIRCLE), new PointF(x, topGap), slotSize * 0.8f);
            circle.originalPos.set(x, topGap);
            options.add(circle);

            // Row 2: Rectangles
            ShapeOption rectangle = new ShapeOption(ShapeOption.Type.RECTANGLE, getColorForType(ShapeOption.Type.RECTANGLE), new PointF(x, topGap + rowSpacing), slotSize * 0.85f);
            rectangle.originalPos.set(x, topGap + rowSpacing);
            options.add(rectangle);

            // Row 3: Triangles
            ShapeOption triangle = new ShapeOption(ShapeOption.Type.TRIANGLE, getColorForType(ShapeOption.Type.TRIANGLE), new PointF(x, topGap + 2 * rowSpacing), slotSize * 0.8f);
            triangle.originalPos.set(x, topGap + 2 * rowSpacing);
            options.add(triangle);

            // Row 4: Squares
            ShapeOption square = new ShapeOption(ShapeOption.Type.SQUARE, getColorForType(ShapeOption.Type.SQUARE), new PointF(x, topGap + 3 * rowSpacing), slotSize * 0.8f);
            square.originalPos.set(x, topGap + 3 * rowSpacing);
            options.add(square);
        }

        truckDrivingIn = true;
        truckDrivingOut = false;
        truckWaiting = false;
        postDelayed(this::animateTick, 14);
        invalidate();
    }

    private void animateTick() {
        boolean needsNext = false;
        if (truckDrivingIn) {
            truckX += truckSpeed;
            if (truckX >= truckTargetX) {
                truckX = truckTargetX;
                truckDrivingIn = false;
                truckWaiting = true;
            }
            needsNext = true;
        }
        if (truckDrivingOut) {
            truckX += truckSpeed;
            if (truckX > truckEndX) {
                truckDrivingOut = false;
                truckWaiting = false;
            } else {
                needsNext = true;
            }
        }
        if (needsNext) {
            invalidate();
            postDelayed(this::animateTick, 14);
        }
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        int W = getWidth(), H = getHeight();

        // Draw road
        paint.setColor(Color.rgb(205, 205, 205));
        c.drawRect(0, H - H/5f, W, H, paint);

        // Draw truck and slots
        if (truckBitmap != null) {
            RectF truckRect = new RectF(truckX, truckY, truckX + truckDrawW, truckY + truckDrawH);
            c.drawBitmap(truckBitmap, null, truckRect, paint);

            float slotSize = Math.min((truckDrawW * 0.65f) / 3f, truckDrawH * 0.58f);
            float cargoLeft = truckX + truckDrawW * 0.10f;
            float slotGap = (truckDrawW * 0.65f - slotSize * 3) / 2f;
            float cy = truckY + truckDrawH * 0.54f;

            for (int i = 0; i < 3; i++) {
                float cx = cargoLeft + slotSize / 2f + i * (slotSize + slotGap);
                paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(4); paint.setColor(Color.LTGRAY);
                c.drawRoundRect(cx - slotSize/2, cy - slotSize/2, cx + slotSize/2, cy + slotSize/2, slotSize*0.27f, slotSize*0.27f, paint);
                paint.setStyle(Paint.Style.FILL);
            }

            int slot = 0;
            for (ShapeOption o : options) {
                if (o.isLoaded && slot < 3) {
                    float cx = cargoLeft + slotSize / 2f + slot * (slotSize + slotGap);
                    float cy_slot = cy;
                    o.center.set(cx, cy_slot);
                    drawShape(c, o);
                    slot++;
                }
            }
        }

        // Draw unplaced shapes
        for (ShapeOption o : options) {
            if (!o.isLoaded && o != dragging) {
                drawShape(c, o);
            }
        }

        if (dragging != null) drawShape(c, dragging);

        // Draw prompt at top
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(30f);
        String shapeName = getNameForType(gameState.getCurrentShape());
        c.drawText("Load the " + shapeName + "s into the truck!", W/2f, H * 0.07f, paint);

        // Draw popup
        if (popupMsg != null && System.currentTimeMillis() - popupShowTime < 1000) {
            paint.setColor(Color.RED); paint.setTextSize(26f);
            c.drawText(popupMsg, W/2f, truckY - 44, paint);
        } else if (popupMsg != null) {
            popupMsg = null; invalidate();
        }
    }

    private void drawShape(Canvas c, ShapeOption o) {
        paint.setColor(o.color);
        switch (o.type) {
            case CIRCLE:
                c.drawCircle(o.center.x, o.center.y, o.size * 0.5f, paint); break;
            case TRIANGLE:
                Path p = new Path();
                p.moveTo(o.center.x, o.center.y - o.size*0.67f);
                p.lineTo(o.center.x - o.size*0.67f, o.center.y + o.size*0.53f);
                p.lineTo(o.center.x + o.size*0.67f, o.center.y + o.size*0.53f);
                p.close();
                c.drawPath(p, paint); break;
            case SQUARE:
                c.drawRect(o.center.x - o.size*0.67f, o.center.y - o.size*0.67f, o.center.x + o.size*0.67f, o.center.y + o.size*0.67f, paint); break;
            case RECTANGLE:
                c.drawRect(o.center.x - o.size, o.center.y - o.size*0.45f, o.center.x + o.size, o.center.y + o.size*0.45f, paint); break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (gameState.isRoundComplete || truckDrivingIn || truckDrivingOut) return true;

        float x = ev.getX(), y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = options.size() - 1; i >= 0; i--) {
                    ShapeOption o = options.get(i);
                    if (!o.isLoaded && o.getBounds().contains(x, y)) {
                        dragging = o;
                        dragOffsetX = x - o.center.x;
                        dragOffsetY = y - o.center.y;
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (dragging != null) {
                    dragging.center.set(x - dragOffsetX, y - dragOffsetY);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (dragging != null) {
                    float slotSize = Math.min((truckDrawW * 0.65f) / 3f, truckDrawH * 0.58f);
                    float cargoLeft = truckX + truckDrawW * 0.10f;
                    float slotGap = (truckDrawW * 0.65f - slotSize * 3) / 2f;
                    float cy = truckY + truckDrawH * 0.54f;
                    boolean loaded = false;

                    for (int i = 0; i < 3; i++) {
                        float cx = cargoLeft + slotSize / 2f + i * (slotSize + slotGap);
                        RectF slotRect = new RectF(cx - slotSize / 2, cy - slotSize / 2, cx + slotSize / 2, cy + slotSize / 2);
                        if (slotRect.contains(dragging.center.x, dragging.center.y)
                                && !slotFilledAt(i)
                                && dragging.type == gameState.getCurrentShape()) {
                            dragging.isLoaded = true;
                            loaded = true;
                            break;
                        }
                    }

                    if (!loaded) {
                        dragging.resetPosition();
                        showPopup("Wrong shape! Try again");
                        if (listener != null) {
                            postDelayed(() -> listener.onWrongShape(), 50);
                        }
                    }
                    dragging = null;
                    invalidate();

                    // Check if round complete
                    int numInTruck = 0;
                    for (ShapeOption o : options) {
                        if (o.isLoaded && o.type == gameState.getCurrentShape()) {
                            numInTruck++;
                        }
                    }

                    System.out.println("DEBUG: numInTruck = " + numInTruck);
                    if (numInTruck == 3) {
                        System.out.println("DEBUG: Round complete, calling onRoundFinished");
                        gameState.isRoundComplete = true;
                        postDelayed(() -> {
                            truckWaiting = false;
                            truckDrivingOut = true;
                            animateTick();
                        }, 600);
                        postDelayed(() -> {
                            System.out.println("DEBUG: About to call listener.onRoundFinished, listener = " + listener);
                            if (listener != null) {
                                listener.onRoundFinished(gameState.isLastShape());
                            } else {
                                System.out.println("ERROR: listener is null!");
                            }
                        }, 1925);
                    }
                }
                break;
        }
        return true;
    }

    private boolean slotFilledAt(int idx) {
        int slot = 0;
        for (ShapeOption o : options) {
            if (o.isLoaded && o.type == gameState.getCurrentShape()) {
                if (slot == idx) return true;
                slot++;
            }
        }
        return false;
    }

    private String getNameForType(ShapeOption.Type t) {
        switch (t) {
            case CIRCLE: return "circle";
            case TRIANGLE: return "triangle";
            case SQUARE: return "square";
            case RECTANGLE: return "rectangle";
        }
        return "";
    }

    private int getColorForType(ShapeOption.Type t) {
        switch (t) {
            case CIRCLE: return Color.parseColor("#95C11F");
            case TRIANGLE: return Color.parseColor("#21A8DF");
            case SQUARE: return Color.parseColor("#FFA832");
            case RECTANGLE: return Color.parseColor("#E24B7B");
        }
        return Color.GRAY;
    }

    private void showPopup(String msg) {
        popupMsg = msg;
        popupShowTime = System.currentTimeMillis();
        invalidate();
    }
}
