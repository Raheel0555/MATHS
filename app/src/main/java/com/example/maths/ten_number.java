package com.example.maths;

import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class ten_number extends AppCompatActivity {



        private int matchedCount = 0;
        private final int totalItems = 50;

        // Count per item type
        private final Map<String, Integer> countMap = new HashMap<>();
        private final Map<String, TextView> countViews = new HashMap<>();

        private final String[] categories = {"ladoo", "ball", "pencil", "apple", "square"};

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ten_number);

            for (String type : categories) {
                countMap.put(type, 0);

                // Setup count text view reference
                String countId = "count" + capitalize(type);
                int resId = getResources().getIdentifier(countId, "id", getPackageName());
                countViews.put(type, findViewById(resId));
            }

            for (String type : categories) {
                for (int i = 1; i <= 10; i++) {
                    int resId = getResources().getIdentifier(type + i, "id", getPackageName());
                    ImageView item = findViewById(resId);
                    if (item != null) {
                        item.setOnLongClickListener(v -> {
                            ClipData data = ClipData.newPlainText("", "");
                            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                            v.startDragAndDrop(data, shadowBuilder, v, 0);
                            return true;
                        });
                    }
                }

                // Drop area setup
                int boxId = getResources().getIdentifier("box" + capitalize(type), "id", getPackageName());
                LinearLayout box = findViewById(boxId);


                box.setOnDragListener((v, event) -> {
                    if (event.getAction() == DragEvent.ACTION_DROP) {
                        View draggedView = (View) event.getLocalState();
                        String draggedTag = (String) draggedView.getTag();
                        String boxTag = (String) v.getTag();

                        if (draggedTag.equals(boxTag)) {
                            // Remove from original parent
                            ViewGroup parent = (ViewGroup) draggedView.getParent();
                            if (parent != null) parent.removeView(draggedView);

                            // Create a copy of the image
                            ImageView newImage = new ImageView(ten_number.this);
                            newImage.setImageDrawable(((ImageView) draggedView).getDrawable());
                            newImage.setLayoutParams(new LinearLayout.LayoutParams(60, 60));
                            newImage.setPadding(4, 4, 4, 4);

                            // Add image to LinearLayout box
                            ((LinearLayout) v).addView(newImage);

                            matchedCount++;

                            int newCount = countMap.get(draggedTag) + 1;
                            countMap.put(draggedTag, newCount);

                            TextView countText = countViews.get(draggedTag);
                            if (newCount < 10) {
                                countText.setText(newCount + " " + getMarathiName(draggedTag));
                            } else if (newCount == 10) {
                                countText.setText("1 दशक पूर्ण झाले");
                            }

                            if (matchedCount == totalItems) {
                                Toast.makeText(this, "🎉 सर्व वस्तू योग्यपणे ठेवण्यात आल्या!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "❌ चुकीचा बॉक्स!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                });


            }
        }

        private String capitalize(String s) {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }

        private String getMarathiName(String tag) {
            switch (tag) {
                case "ladoo": return "लाडू";
                case "ball": return "गोल";
                case "pencil": return "पेन्सिल";
                case "apple": return "सफरचंद";
                case "square": return "चौरस";
                default: return "वस्तू";
            }
        }
    }
