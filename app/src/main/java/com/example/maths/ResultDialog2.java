package com.example.maths;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultDialog2 extends Dialog {
    public interface OnDialogNextListener { void onNext(); }
    private boolean correct;
    private int correctSum;
    private OnDialogNextListener listener;

    public ResultDialog2(Context context, boolean correct, int correctSum, OnDialogNextListener listener) {
        super(context);
        this.correct = correct;
        this.correctSum = correctSum;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_result);

        TextView message = findViewById(R.id.result_message);
        ImageView icon = findViewById(R.id.result_icon);
        Button nextBtn = findViewById(R.id.next_button);

        if (correct) {
            message.setText("🎉 Correct!\nYou made " + correctSum + "!");
            icon.setImageResource(R.drawable.ic_trophy); // You must add this drawable
        } else {
            message.setText("🙊 Too high!\nTry again...");
            icon.setImageResource(R.drawable.ic_sad); // You must add this drawable
        }
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) listener.onNext();
            }
        });
    }
}
