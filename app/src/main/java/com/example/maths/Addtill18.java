package com.example.maths;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class Addtill18 extends AppCompatActivity {

    private TextView targetText, sumText, answerHint;
    private LinearLayout optionsLayout, answerBox;
    private SumGameManager sumGameManager;

    private ArrayList<Integer> droppedIndexes = new ArrayList<>();
    private ArrayList<Button> optionButtons = new ArrayList<>();
    private int[] optionBgRes = {
            R.drawable.option_selector_bg_1,
            R.drawable.option_selector_bg_2,
            R.drawable.option_selector_bg_3,
            R.drawable.option_selector_bg_4,
            R.drawable.option_selector_bg_5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtill18);

        targetText = findViewById(R.id.target_text);
        sumText = findViewById(R.id.sum_text);
        optionsLayout = findViewById(R.id.options_layout);
        answerBox = findViewById(R.id.answer_box);
        answerHint = findViewById(R.id.answer_hint);

        sumGameManager = new SumGameManager();

        answerBox.setOnDragListener(answerDragListener);

        startNewRound();
    }

    private void startNewRound() {
        optionsLayout.removeAllViews();
        // Remove everything except the first child (answerHint)
        answerBox.removeViews(1, answerBox.getChildCount() - 1);
        droppedIndexes.clear();
        sumGameManager.generateNewGame();

        targetText.setText("Make: " + sumGameManager.getTarget());
        sumText.setText("Your Sum: 0");
        answerHint.setVisibility(View.VISIBLE);

        ArrayList<Integer> options = sumGameManager.getOptions();
        optionButtons.clear();

        for (int i = 0; i < options.size(); i++) {
            final int idx = i;
            Button btn = new Button(this);
            btn.setText(String.valueOf(options.get(i)));
            btn.setTextSize(22f);
            btn.setAllCaps(false);
            btn.setBackgroundResource(optionBgRes[i % optionBgRes.length]);
            btn.setTextColor(Color.parseColor("#263238"));
            btn.setPadding(36, 20, 36, 20);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(14, 18, 14, 18);
            btn.setLayoutParams(lp);

            // Enable drag
            btn.setOnLongClickListener(v -> {
                ClipData data = ClipData.newPlainText("idx", String.valueOf(idx));
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(data, shadowBuilder, v, 0);
                return true;
            });

            btn.setTag(idx);
            btn.setEnabled(true);
            btn.setVisibility(View.VISIBLE);
            optionsLayout.addView(btn);
            optionButtons.add(btn);
        }
    }

    // Drag listener for the answer box
    private View.OnDragListener answerDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundResource(R.drawable.answer_box_bg);
                    return true;
                case DragEvent.ACTION_DROP: {
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    int idx = Integer.parseInt(item.getText().toString());

                    if (!droppedIndexes.contains(idx)) {
                        droppedIndexes.add(idx);
                        addDroppedButton(idx);
                        optionButtons.get(idx).setVisibility(View.INVISIBLE);
                        optionButtons.get(idx).setEnabled(false);

                        int sum = sumGameManager.calcSum(droppedIndexes);
                        sumText.setText("Your Sum: " + sum);

                        answerHint.setVisibility(View.GONE);

                        if (sum == sumGameManager.getTarget()) {
                            showResultDialog(true);
                        } else if (sum > sumGameManager.getTarget()) {
                            showResultDialog(false);
                        }
                    }
                    return true;
                }
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundResource(R.drawable.answer_box_bg);
                    return true;
                default:
                    return true;
            }
        }
    };

    private void addDroppedButton(final int idx) {
        int val = sumGameManager.getOptions().get(idx);
        Button dropped = new Button(this);
        dropped.setText(String.valueOf(val));
        dropped.setTextSize(22f);
        dropped.setEnabled(true); // Must be clickable to remove
        dropped.setBackgroundResource(optionBgRes[idx % optionBgRes.length]);
        dropped.setTextColor(Color.parseColor("#263238"));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        dropped.setLayoutParams(lp);

        // When the user clicks the dropped number, remove it and send it back to options row
        dropped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerBox.removeView(v);
                droppedIndexes.remove((Integer) idx);
                Button optionBtn = optionButtons.get(idx);
                optionBtn.setVisibility(View.VISIBLE);
                optionBtn.setEnabled(true);

                // Update sum text and re-show hint if box is empty again
                int sum = sumGameManager.calcSum(droppedIndexes);
                sumText.setText("Your Sum: " + sum);
                if (droppedIndexes.isEmpty()) answerHint.setVisibility(View.VISIBLE);
            }
        });

        answerBox.addView(dropped);
    }

    private void showResultDialog(boolean correct) {
        ResultDialog2 dialog = new ResultDialog2(this, correct, sumGameManager.getTarget(), new ResultDialog2.OnDialogNextListener() {
            @Override
            public void onNext() {
                startNewRound();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
}
