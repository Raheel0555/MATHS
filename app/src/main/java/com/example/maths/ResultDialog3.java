package com.example.maths;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultDialog3 extends Dialog {
    public interface OnDialogNextListener { void onNext(); }
    private boolean correct;
    private int correctVal;
    private OnDialogNextListener listener;

    public ResultDialog3(Context context, boolean correct, int correctVal, OnDialogNextListener listener) {
        super(context);
        this.correct = correct;
        this.correctVal = correctVal;
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
            message.setText("🎉 Correct!\nWell done!");
            icon.setImageResource(R.drawable.ic_trophy);
        } else {
            message.setText("🙊 Oops!\nTry again! (Target: ₹" + correctVal + ")");
            icon.setImageResource(R.drawable.ic_sad);
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
