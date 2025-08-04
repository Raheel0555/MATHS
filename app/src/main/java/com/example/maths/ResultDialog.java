package com.example.maths;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultDialog extends Dialog {
    public interface OnDialogNextListener { void onNext(); }
    private boolean correct;
    private String correctTime;
    private OnDialogNextListener listener;

    public ResultDialog(Context context, boolean correct, String correctTime, OnDialogNextListener listener) {
        super(context);
        this.correct = correct;
        this.correctTime = correctTime;
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
            message.setText("🎉 Great job!\nThat's correct!");
            icon.setImageResource(R.drawable.ic_trophy); // You must provide this drawable
        } else {
            message.setText("😅 Oops!\nThe correct time was: " + correctTime);
            icon.setImageResource(R.drawable.ic_sad); // You must provide this drawable
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
