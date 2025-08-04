package com.example.maths;

import android.content.ClipData;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShapeActivity extends AppCompatActivity {

    ImageView objectImage;
    TextView feedbackText;

    // Shape drop targets
    ImageView shapeApple, shapeIceCream, shapeCircle, shapeBottle;
    ImageView shapeTriangle2, shapeTriangle, shapeRectangle, shapeRound;

    int currentObjectIndex = 0;

    List<Integer> shuffledImages = new ArrayList<>();
    List<String> shuffledAnswers = new ArrayList<>();

    // Original object images and their correct shape names
    int[] objectImages = {
            R.drawable.apple1,
            R.drawable.icecreame,
            R.drawable.circle,
            R.drawable.bottel,
            R.drawable.trangle2,
            R.drawable.ttrangle,
            R.drawable.rectangle,
            R.drawable.round
    };

    String[] correctAnswers = {
            "apple",
            "icecreame",
            "circle",
            "bottel",
            "trangle2",
            "ttrangle",
            "rectangle",
            "round"
    };

    class ShapeItem {
        int imageRes;
        String answer;

        ShapeItem(int res, String ans) {
            this.imageRes = res;
            this.answer = ans;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape);

        objectImage = findViewById(R.id.objectImage);
        feedbackText = findViewById(R.id.feedbackText);

        // Shape drop targets
        shapeApple = findViewById(R.id.shapeSphere);
        shapeIceCream = findViewById(R.id.shapeCone);
        shapeCircle = findViewById(R.id.shapeCuboid);
        shapeBottle = findViewById(R.id.shapebottel);
        shapeTriangle2 = findViewById(R.id.shapetrangle2);
        shapeTriangle = findViewById(R.id.shapetrangle);
        shapeRectangle = findViewById(R.id.shaperectangle);
        shapeRound = findViewById(R.id.shapeCylinder);

        // Shuffle the objects
        shuffleObjects();

        // Load the first image
        loadObject();

        // Make the object image draggable
        objectImage.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("dragged_object", shuffledAnswers.get(currentObjectIndex));
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDragAndDrop(data, shadowBuilder, view, 0);
                return true;
            }
            return false;
        });

        // Set drop targets on each shape
        setDropTarget(shapeApple, "apple");
        setDropTarget(shapeIceCream, "icecreame");
        setDropTarget(shapeRectangle, "rectangle");
        setDropTarget(shapeBottle, "bottel");
        setDropTarget(shapeTriangle2, "trangle2");
        setDropTarget(shapeTriangle, "ttrangle");
        setDropTarget(shapeCircle, "circle");
        setDropTarget(shapeRound, "round");
    }

    private void shuffleObjects() {
        List<ShapeItem> list = new ArrayList<>();
        for (int i = 0; i < objectImages.length; i++) {
            list.add(new ShapeItem(objectImages[i], correctAnswers[i]));
        }
        Collections.shuffle(list);

        for (ShapeItem item : list) {
            shuffledImages.add(item.imageRes);
            shuffledAnswers.add(item.answer);
        }
    }

    private void loadObject() {
        if (currentObjectIndex < shuffledImages.size()) {
            objectImage.setImageResource(shuffledImages.get(currentObjectIndex));
            feedbackText.setText("Drag to the correct shape.");
        }
    }
    private String capitalize(String name) {
        if (name == null || name.isEmpty()) return "";
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }


    private void setDropTarget(View targetView, String expectedAnswer) {
        targetView.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DROP:
                    String actualAnswer = shuffledAnswers.get(currentObjectIndex);
                    if (actualAnswer.equals(expectedAnswer)) {
                        // ✨ Show object name after correct drop
                        String displayName = capitalize(actualAnswer);
                        feedbackText.setText("✅ Correct! It's a " + displayName);
                        currentObjectIndex++;
                        if (currentObjectIndex < shuffledImages.size()) {
                            new Handler().postDelayed(this::loadObject, 1500);
                        } else {
                            feedbackText.setText("🎉 All done!");
                        }
                    } else {
                        feedbackText.setText("❌ Try again.");
                    }
                    return true;
            }
            return true;
        });
    }
}
