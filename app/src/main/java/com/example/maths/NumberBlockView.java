package com.example.maths;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class NumberBlockView extends View {
    private ArrayList<NumberBlock> tensBlocks;
    private ArrayList<NumberBlock> onesBlocks;
    private ArrayList<DropZone> dropZones;
    private Paint tensPaint, onesPaint, dropZonePaint, textPaint, instructionPaint;
    private NumberBlock draggedBlock;
    private GameLogic2 gameLogic;
    private NumberBuildingListener listener;

    // Block sizes remain the same
    private static final int TENS_BLOCK_SIZE = 120;
    private static final int ONES_BLOCK_SIZE = 90;
    private static final int TENS_COLOR = 0xFF4CAF50;  // Green
    private static final int ONES_COLOR = 0xFF2196F3;  // Blue

    public interface NumberBuildingListener {
        void onNumberBuilt(boolean isCorrect, int currentTotal, boolean isOverflow);
    }

    public NumberBlockView(Context context) {
        super(context);
        init();
    }

    public NumberBlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        tensBlocks = new ArrayList<>();
        onesBlocks = new ArrayList<>();
        dropZones = new ArrayList<>();
        gameLogic = new GameLogic2();

        setupPaints();
        setupBlocks();
        setupDropZones();
    }

    private void setupPaints() {
        tensPaint = new Paint();
        tensPaint.setColor(TENS_COLOR);
        tensPaint.setAntiAlias(true);

        onesPaint = new Paint();
        onesPaint.setColor(ONES_COLOR);
        onesPaint.setAntiAlias(true);

        dropZonePaint = new Paint();
        dropZonePaint.setColor(0x40808080);
        dropZonePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(0xFFFFFFFF);
        textPaint.setTextSize(32);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);

        instructionPaint = new Paint();
        instructionPaint.setColor(0xFF37474F);
        instructionPaint.setTextSize(18);
        instructionPaint.setAntiAlias(true);
        instructionPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void setupBlocks() {
        // Tens blocks - moved up and better spaced
        for (int i = 0; i < 3; i++) {
            tensBlocks.add(new NumberBlock(10, 60 + i * (TENS_BLOCK_SIZE + 40), 60, i));
        }

        // Ones blocks - more vertical spacing between rows
        for (int i = 0; i < 10; i++) {
            int row = i / 5;
            int col = i % 5;
            onesBlocks.add(new NumberBlock(1, 60 + col * (ONES_BLOCK_SIZE + 25),
                    220 + row * (ONES_BLOCK_SIZE + 35), i + 10));
        }
    }

    private void setupDropZones() {
        // Drop zones pushed further down with more space
        dropZones.add(new DropZone(100, 480, 420, 640, 3, "Tens"));
        dropZones.add(new DropZone(460, 480, 780, 640, 10, "Ones"));
    }

    public void newChallenge() {
        gameLogic.generateNewChallenge();
        resetBlocks();
        invalidate();
    }

    private void resetBlocks() {
        // Reset tens blocks with new spacing
        for (int i = 0; i < tensBlocks.size(); i++) {
            NumberBlock block = tensBlocks.get(i);
            block.setX(60 + i * (TENS_BLOCK_SIZE + 40));
            block.setY(60);
            block.setInDropZone(false);
        }

        // Reset ones blocks with improved spacing
        for (int i = 0; i < onesBlocks.size(); i++) {
            NumberBlock block = onesBlocks.get(i);
            int row = i / 5;
            int col = i % 5;
            block.setX(60 + col * (ONES_BLOCK_SIZE + 25));
            block.setY(220 + row * (ONES_BLOCK_SIZE + 35));
            block.setInDropZone(false);
        }

        // Clear drop zones
        for (DropZone zone : dropZones) {
            zone.getContainedBlocks().clear();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Section labels with better positioning
        textPaint.setColor(0xFF2E7D32);
        textPaint.setTextSize(28);
        canvas.drawText("Tens Blocks (Green)", 250, 35, textPaint);
        canvas.drawText("Ones Blocks (Blue)", 250, 195, textPaint);

        // Draw tens blocks
        for (NumberBlock block : tensBlocks) {
            if (!block.isDragging()) {
                drawTensBlock(canvas, block.getX(), block.getY());
            }
        }

        // Draw ones blocks
        for (NumberBlock block : onesBlocks) {
            if (!block.isDragging()) {
                drawOnesBlock(canvas, block.getX(), block.getY());
            }
        }

        // Draw spacious drop zones
        for (DropZone zone : dropZones) {
            Paint borderPaint = new Paint();
            borderPaint.setColor(0xFF757575);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(6);
            borderPaint.setAntiAlias(true);

            canvas.drawRoundRect(zone.getBounds(), 25, 25, dropZonePaint);
            canvas.drawRoundRect(zone.getBounds(), 25, 25, borderPaint);

            textPaint.setColor(0xFF000000);
            textPaint.setTextSize(26);
            canvas.drawText(zone.getLabel(), zone.getBounds().centerX(),
                    zone.getBounds().centerY(), textPaint);
        }

        // Draw instruction with more breathing room
        drawSimpleInstruction(canvas);

        // Draw dragged block on top
        if (draggedBlock != null) {
            if (draggedBlock.getValue() == 10) {
                drawTensBlock(canvas, draggedBlock.getX(), draggedBlock.getY());
            } else {
                drawOnesBlock(canvas, draggedBlock.getX(), draggedBlock.getY());
            }
        }
    }

    private void drawSimpleInstruction(Canvas canvas) {
        // More space from bottom edge
        canvas.drawText("Green blocks = 10, Blue blocks = 1",
                getWidth() / 2, getHeight() - 50, instructionPaint);
    }

    private void drawTensBlock(Canvas canvas, float x, float y) {
        RectF rect = new RectF(x, y, x + TENS_BLOCK_SIZE, y + TENS_BLOCK_SIZE);
        canvas.drawRoundRect(rect, 15, 15, tensPaint);

        Paint borderPaint = new Paint();
        borderPaint.setColor(0xFF388E3C);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(4);
        borderPaint.setAntiAlias(true);
        canvas.drawRoundRect(rect, 15, 15, borderPaint);

        textPaint.setColor(0xFFFFFFFF);
        textPaint.setTextSize(36);
        canvas.drawText("10", x + TENS_BLOCK_SIZE/2, y + TENS_BLOCK_SIZE/2 + 12, textPaint);
    }

    private void drawOnesBlock(Canvas canvas, float x, float y) {
        RectF rect = new RectF(x, y, x + ONES_BLOCK_SIZE, y + ONES_BLOCK_SIZE);
        canvas.drawRoundRect(rect, 12, 12, onesPaint);

        Paint borderPaint = new Paint();
        borderPaint.setColor(0xFF1976D2);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(3);
        borderPaint.setAntiAlias(true);
        canvas.drawRoundRect(rect, 12, 12, borderPaint);

        textPaint.setColor(0xFFFFFFFF);
        textPaint.setTextSize(28);
        canvas.drawText("1", x + ONES_BLOCK_SIZE/2, y + ONES_BLOCK_SIZE/2 + 8, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                draggedBlock = findBlockAt(x, y);
                if (draggedBlock != null) {
                    draggedBlock.setDragging(true);
                    removeFromDropZones(draggedBlock);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (draggedBlock != null) {
                    if (draggedBlock.getValue() == 10) {
                        draggedBlock.setX(x - TENS_BLOCK_SIZE/2);
                        draggedBlock.setY(y - TENS_BLOCK_SIZE/2);
                    } else {
                        draggedBlock.setX(x - ONES_BLOCK_SIZE/2);
                        draggedBlock.setY(y - ONES_BLOCK_SIZE/2);
                    }
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                if (draggedBlock != null) {
                    handleDrop(draggedBlock, x, y);
                    draggedBlock.setDragging(false);
                    draggedBlock = null;
                    checkAnswer();
                    invalidate();
                }
                break;
        }
        return true;
    }

    private NumberBlock findBlockAt(float x, float y) {
        // Check tens blocks
        for (NumberBlock block : tensBlocks) {
            if (x >= block.getX() && x <= block.getX() + TENS_BLOCK_SIZE &&
                    y >= block.getY() && y <= block.getY() + TENS_BLOCK_SIZE) {
                return block;
            }
        }

        // Check ones blocks
        for (NumberBlock block : onesBlocks) {
            if (x >= block.getX() && x <= block.getX() + ONES_BLOCK_SIZE &&
                    y >= block.getY() && y <= block.getY() + ONES_BLOCK_SIZE) {
                return block;
            }
        }
        return null;
    }

    private void handleDrop(NumberBlock block, float x, float y) {
        for (DropZone zone : dropZones) {
            if (zone.containsPoint(x, y) && zone.canAcceptBlock()) {
                if ((zone.getLabel().equals("Tens") && block.getValue() == 10) ||
                        (zone.getLabel().equals("Ones") && block.getValue() == 1)) {

                    zone.addBlock(block);
                    positionBlockInZone(block, zone);
                    return;
                }
            }
        }
        resetBlockPosition(block);
    }

    private void positionBlockInZone(NumberBlock block, DropZone zone) {
        RectF bounds = zone.getBounds();
        int index = zone.getContainedBlocks().size() - 1;

        if (zone.getLabel().equals("Tens")) {
            block.setX(bounds.left + 40 + (index % 3) * (TENS_BLOCK_SIZE * 0.7f + 20));
            block.setY(bounds.top + 30);
        } else {
            block.setX(bounds.left + 30 + (index % 6) * (ONES_BLOCK_SIZE * 0.6f + 15));
            block.setY(bounds.top + 30 + (index / 6) * (ONES_BLOCK_SIZE * 0.6f + 15));
        }
    }

    private void removeFromDropZones(NumberBlock block) {
        for (DropZone zone : dropZones) {
            zone.removeBlock(block);
        }
    }

    private void resetBlockPosition(NumberBlock block) {
        if (block.getValue() == 10) {
            int index = tensBlocks.indexOf(block);
            block.setX(60 + index * (TENS_BLOCK_SIZE + 40));
            block.setY(60);
        } else {
            int index = onesBlocks.indexOf(block);
            int row = index / 5;
            int col = index % 5;
            block.setX(60 + col * (ONES_BLOCK_SIZE + 25));
            block.setY(220 + row * (ONES_BLOCK_SIZE + 35));
        }
    }

    private void checkAnswer() {
        int tensCount = 0;
        int onesCount = 0;

        for (DropZone zone : dropZones) {
            if (zone.getLabel().equals("Tens")) {
                tensCount = zone.getBlockCount();
            } else {
                onesCount = zone.getBlockCount();
            }
        }

        gameLogic.updateCounts(tensCount, onesCount);
        int currentTotal = gameLogic.getCurrentTotal();
        int targetNumber = gameLogic.getTargetNumber();

        boolean isOverflow = currentTotal > targetNumber;

        if (listener != null) {
            listener.onNumberBuilt(gameLogic.isCorrect(), currentTotal, isOverflow);
        }
    }

    public void setListener(NumberBuildingListener listener) {
        this.listener = listener;
    }

    public int getTargetNumber() {
        return gameLogic.getTargetNumber();
    }
}
