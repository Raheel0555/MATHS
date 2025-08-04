package com.example.maths;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TimeMeasurement extends AppCompatActivity implements AnalogClockView.OnTimeSetListener {

    private TextView questionText;
    private TimeQuestionManager questionManager;
    private AnalogClockView analogClockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timemeasurement);

        questionText = findViewById(R.id.question_text);
        analogClockView = findViewById(R.id.analog_clock);
        questionManager = new TimeQuestionManager();

        generateNewQuestion();
        analogClockView.setOnTimeSetListener(this);
    }

    private void generateNewQuestion() {
        questionManager.generateQuestion();
        questionText.setText("Set the clock to: " + questionManager.getQuestionTimeText());
        analogClockView.setInitialTime(0, 0);
    }

    @Override
    public void onTimeSet(int hour, int minute) {
        boolean correct = questionManager.checkAnswer(hour, minute);
        showResultDialog(correct, questionManager.getQuestionTimeText());
    }

    private void showResultDialog(boolean correct, String correctTime) {
        ResultDialog dialog = new ResultDialog(this, correct, correctTime, new ResultDialog.OnDialogNextListener() {
            @Override
            public void onNext() {
                generateNewQuestion();
            }
        });
        dialog.show();
    }
}
