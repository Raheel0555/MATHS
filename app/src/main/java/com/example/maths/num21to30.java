package com.example.maths;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class num21to30 extends Activity {
    private TextView challengeText, feedbackText, liveTotal, tensTotal, onesTotal;
    private Button nextProblem, submitAnswer;
    private LinearLayout tensContainer, onesRow1, onesRow2, tensDropZone, onesDropZone;
    private LinearLayout tensDropContainer, onesDropContainer;
    private GameLogic2 gameLogic;

    private int tensInDropZone = 0;
    private int onesInDropZone = 0;

    // Track original blocks for restoration
    private ArrayList<View> hiddenTensBlocks = new ArrayList<>();
    private ArrayList<View> hiddenOnesBlocks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num21to30);

        // Initialize views
        challengeText = findViewById(R.id.challengeText);
        feedbackText = findViewById(R.id.feedbackText);
        liveTotal = findViewById(R.id.liveTotal);
        tensTotal = findViewById(R.id.tensTotal);
        onesTotal = findViewById(R.id.onesTotal);
        nextProblem = findViewById(R.id.nextProblem);
        submitAnswer = findViewById(R.id.submitAnswer);
        tensContainer = findViewById(R.id.tensContainer);
        onesRow1 = findViewById(R.id.onesRow1);
        onesRow2 = findViewById(R.id.onesRow2);
        tensDropZone = findViewById(R.id.tensDropZone);
        onesDropZone = findViewById(R.id.onesDropZone);
        tensDropContainer = findViewById(R.id.tensDropContainer);
        onesDropContainer = findViewById(R.id.onesDropContainer);

        gameLogic = new GameLogic2();
        setupDropZones();
        setupClickToRemove();

        submitAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        nextProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewChallenge();
            }
        });

        startNewChallenge();
    }

    private void setupDropZones() {
        tensDropZone.setOnDragListener(new DropZoneListener("tens"));
        onesDropZone.setOnDragListener(new DropZoneListener("ones"));
    }

    private void setupClickToRemove() {
        // Click on tens drop zone to remove last tens block
        tensDropContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tensInDropZone > 0) {
                    removeLastBlock("tens");
                    android.widget.Toast.makeText(num21to30.this, "Removed one tens block!", android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Click on ones drop zone to remove last ones block
        onesDropContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onesInDropZone > 0) {
                    removeLastBlock("ones");
                    android.widget.Toast.makeText(num21to30.this, "Removed one ones block!", android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void removeLastBlock(String blockType) {
        if (blockType.equals("tens") && tensInDropZone > 0) {
            // Remove visual block from drop container
            int lastIndex = tensDropContainer.getChildCount() - 1;
            if (lastIndex >= 0) {
                tensDropContainer.removeViewAt(lastIndex);
            }

            // Restore original block
            if (!hiddenTensBlocks.isEmpty()) {
                View originalBlock = hiddenTensBlocks.remove(hiddenTensBlocks.size() - 1);
                originalBlock.setVisibility(View.VISIBLE);
                originalBlock.setAlpha(1.0f);
            }

            // Update counter
            tensInDropZone--;

        } else if (blockType.equals("ones") && onesInDropZone > 0) {
            // Remove visual block from drop container
            int lastIndex = onesDropContainer.getChildCount() - 1;
            if (lastIndex >= 0) {
                onesDropContainer.removeViewAt(lastIndex);
            }

            // Restore original block
            if (!hiddenOnesBlocks.isEmpty()) {
                View originalBlock = hiddenOnesBlocks.remove(hiddenOnesBlocks.size() - 1);
                originalBlock.setVisibility(View.VISIBLE);
                originalBlock.setAlpha(1.0f);
            }

            // Update counter
            onesInDropZone--;
        }

        // Update live totals and feedback
        updateLiveTotals();
        clearExcessFeedback();
    }

    private void clearExcessFeedback() {
        int currentTotal = tensInDropZone * 10 + onesInDropZone;
        int targetNumber = gameLogic.getTargetNumber();

        if (currentTotal <= targetNumber) {
            feedbackText.setText("");
        }
    }

    private void startNewChallenge() {
        gameLogic.generateNewChallenge();
        challengeText.setText("Build the number: " + gameLogic.getTargetNumber());
        feedbackText.setText("");
        nextProblem.setVisibility(View.GONE);
        submitAnswer.setEnabled(true);

        // Reset counters and tracking arrays
        tensInDropZone = 0;
        onesInDropZone = 0;
        hiddenTensBlocks.clear();
        hiddenOnesBlocks.clear();

        // Clear all containers
        tensContainer.removeAllViews();
        onesRow1.removeAllViews();
        onesRow2.removeAllViews();
        tensDropContainer.removeAllViews();
        onesDropContainer.removeAllViews();

        // Update live totals
        updateLiveTotals();

        // Add blocks
        for (int i = 0; i < 3; i++) {
            addTensBlock(i);
        }

        for (int i = 0; i < 10; i++) {
            if (i < 5) {
                addOnesBlock(i, onesRow1);
            } else {
                addOnesBlock(i, onesRow2);
            }
        }
    }

    private void addTensBlock(int id) {
        TextView tensBlock = new TextView(this);
        tensBlock.setText("10");
        tensBlock.setTextSize(24);
        tensBlock.setTextColor(Color.WHITE);
        tensBlock.setBackgroundResource(R.drawable.tens_block);
        tensBlock.setGravity(android.view.Gravity.CENTER);
        tensBlock.setTag("tens_" + id);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(120, 120);
        params.setMargins(8, 0, 8, 0);
        tensBlock.setLayoutParams(params);

        tensBlock.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
                    ClipData dragData = new ClipData((CharSequence) v.getTag(),
                            new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

                    v.startDragAndDrop(dragData, shadowBuilder, v, 0);
                    v.setAlpha(0.5f);
                    return true;
                }
                return false;
            }
        });

        tensContainer.addView(tensBlock);
    }

    private void addOnesBlock(int id, LinearLayout container) {
        TextView onesBlock = new TextView(this);
        onesBlock.setText("1");
        onesBlock.setTextSize(20);
        onesBlock.setTextColor(Color.WHITE);
        onesBlock.setBackgroundResource(R.drawable.ones_block);
        onesBlock.setGravity(android.view.Gravity.CENTER);
        onesBlock.setTag("ones_" + id);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(90, 90);
        params.setMargins(6, 0, 6, 0);
        onesBlock.setLayoutParams(params);

        onesBlock.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
                    ClipData dragData = new ClipData((CharSequence) v.getTag(),
                            new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

                    v.startDragAndDrop(dragData, shadowBuilder, v, 0);
                    v.setAlpha(0.5f);
                    return true;
                }
                return false;
            }
        });

        container.addView(onesBlock);
    }

    private void createVisualBlockInDropZone(String blockType, String tag) {
        if (blockType.equals("tens")) {
            TextView visualBlock = new TextView(this);
            visualBlock.setText("10");
            visualBlock.setTextSize(16);
            visualBlock.setTextColor(Color.WHITE);
            visualBlock.setBackgroundResource(R.drawable.tens_block);
            visualBlock.setGravity(android.view.Gravity.CENTER);
            visualBlock.setTag(tag + "_visual");

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60, 60);
            params.setMargins(4, 0, 4, 0);
            visualBlock.setLayoutParams(params);

            tensDropContainer.addView(visualBlock);
        } else {
            TextView visualBlock = new TextView(this);
            visualBlock.setText("1");
            visualBlock.setTextSize(14);
            visualBlock.setTextColor(Color.WHITE);
            visualBlock.setBackgroundResource(R.drawable.ones_block);
            visualBlock.setGravity(android.view.Gravity.CENTER);
            visualBlock.setTag(tag + "_visual");

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(45, 45);
            params.setMargins(2, 0, 2, 0);
            visualBlock.setLayoutParams(params);

            onesDropContainer.addView(visualBlock);
        }
    }

    private void updateLiveTotals() {
        int totalTens = tensInDropZone * 10;
        int totalOnes = onesInDropZone;
        int grandTotal = totalTens + totalOnes;

        // Update individual totals
        tensTotal.setText(tensInDropZone > 0 ? "= " + totalTens : "");
        onesTotal.setText(onesInDropZone > 0 ? "= " + totalOnes : "");

        // Update live grand total
        liveTotal.setText("Total: " + grandTotal);

        // Color coding based on target
        int target = gameLogic.getTargetNumber();
        if (grandTotal == target) {
            liveTotal.setTextColor(Color.parseColor("#4CAF50")); // Green for correct
        } else if (grandTotal > target) {
            liveTotal.setTextColor(Color.parseColor("#FF5722")); // Red for over
        } else {
            liveTotal.setTextColor(Color.parseColor("#FF6B35")); // Orange for building
        }
    }

    private class DropZoneListener implements View.OnDragListener {
        private String zoneType;

        public DropZoneListener(String zoneType) {
            this.zoneType = zoneType;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundResource(R.drawable.drop_zone_active);
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundResource(R.drawable.rounded_drop_zone);
                    return true;

                case DragEvent.ACTION_DROP:
                    String tag = event.getClipData().getItemAt(0).getText().toString();

                    if ((zoneType.equals("tens") && tag.startsWith("tens_")) ||
                            (zoneType.equals("ones") && tag.startsWith("ones_"))) {

                        // Get original dragged view
                        View draggedView = (View) event.getLocalState();

                        // Update counters
                        if (zoneType.equals("tens")) {
                            tensInDropZone++;
                            hiddenTensBlocks.add(draggedView); // Track for removal
                        } else {
                            onesInDropZone++;
                            hiddenOnesBlocks.add(draggedView); // Track for removal
                        }

                        // Create visual block in drop zone
                        createVisualBlockInDropZone(zoneType, tag);

                        // Update live totals
                        updateLiveTotals();

                        // Hide original dragged view
                        draggedView.setVisibility(View.INVISIBLE);

                        checkForOverflow();
                        return true;
                    }
                    return false;

                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundResource(R.drawable.rounded_drop_zone);
                    View draggedView = (View) event.getLocalState();
                    if (draggedView != null && !event.getResult()) {
                        draggedView.setAlpha(1.0f);
                        draggedView.setVisibility(View.VISIBLE);
                    }
                    return true;

                default:
                    return false;
            }
        }
    }

    private void checkForOverflow() {
        int currentTotal = tensInDropZone * 10 + onesInDropZone;
        int targetNumber = gameLogic.getTargetNumber();

        if (currentTotal > targetNumber) {
            feedbackText.setText("⚠️ Too much! Click the gray boxes to remove blocks one by one! ⚠️");
            feedbackText.setTextColor(Color.parseColor("#FF5722"));
        }
    }

    private void checkAnswer() {
        int currentTotal = tensInDropZone * 10 + onesInDropZone;
        int targetNumber = gameLogic.getTargetNumber();

        if (currentTotal == targetNumber) {
            // Perfect answer - show success
            feedbackText.setText("🎉 Perfect! You built " + currentTotal + "! 🎉");
            feedbackText.setTextColor(Color.parseColor("#4CAF50"));
            nextProblem.setVisibility(View.VISIBLE);
            nextProblem.setText("NEXT CHALLENGE");
            submitAnswer.setEnabled(false);
            celebrateSuccess();

        } else if (currentTotal > targetNumber) {
            // Too many blocks - existing overflow message
            feedbackText.setText("⚠️ Too much! Click the gray boxes to remove blocks! ⚠️");
            feedbackText.setTextColor(Color.parseColor("#FF5722"));

        } else if (currentTotal < targetNumber && currentTotal > 0) {
            // NEW: Not enough blocks - encourage adding more
            int needed = targetNumber - currentTotal;
            feedbackText.setText("📈 Numbers are low! You need " + needed + " more to reach " + targetNumber + "!");
            feedbackText.setTextColor(Color.parseColor("#FF9800"));

        } else {
            // No blocks at all - encourage starting
            feedbackText.setText("Try tapping and dragging some blocks to the zones!");
            feedbackText.setTextColor(Color.parseColor("#FFC107"));
        }
    }


    private void celebrateSuccess() {
        // Simple celebration without external CelebrationManager
        feedbackText.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(300)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        feedbackText.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(300);
                    }
                });
    }
}
