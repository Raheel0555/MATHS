package com.example.maths;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ten_number extends AppCompatActivity {

    private int ladooCount = 0;
    private TextView textStatus;
    private ImageView boxTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ten_number);

        textStatus = findViewById(R.id.textStatus);
        boxTarget = findViewById(R.id.boxTarget);

        int[] ladooIds = {
                R.id.ladoo1, R.id.ladoo2, R.id.ladoo3, R.id.ladoo4, R.id.ladoo5,
                R.id.ladoo6, R.id.ladoo7, R.id.ladoo8, R.id.ladoo9, R.id.ladoo10
        };

        for (int id : ladooIds) {
            ImageView ladoo = findViewById(id);
            ladoo.setOnLongClickListener(view -> {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDragAndDrop(data, shadowBuilder, view, 0);
                return true;
            });
        }

        boxTarget.setOnDragListener(dragListener);
    }

    private final View.OnDragListener dragListener = new View.OnDragListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public boolean onDrag(View view, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DROP:
                    View draggedView = (View) event.getLocalState();
                    ((LinearLayout) draggedView.getParent()).removeView(draggedView);

                    // Move it visually into box (optional)
                    ((ImageView) view).setImageDrawable(null);
                    ladooCount++;

                    if (ladooCount == 10) {
                        textStatus.setText("🎉 १ दशक लाडू!");
                        Toast.makeText(ten_number.this, "शाबास!", Toast.LENGTH_LONG).show();
                    } else {
                        textStatus.setText("लाडू टाकले: " + ladooCount);
                    }
                    break;
            }
            return true;
        }
    };
}
