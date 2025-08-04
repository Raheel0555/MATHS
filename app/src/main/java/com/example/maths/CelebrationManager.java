package com.example.maths;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;

public class CelebrationManager {

    public static void celebrate(View view) {
        AnimatorSet celebrationSet = new AnimatorSet();

        ObjectAnimator scaleXUp = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.3f);
        ObjectAnimator scaleYUp = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.3f);
        ObjectAnimator scaleXDown = ObjectAnimator.ofFloat(view, "scaleX", 1.3f, 1f);
        ObjectAnimator scaleYDown = ObjectAnimator.ofFloat(view, "scaleY", 1.3f, 1f);

        scaleXUp.setDuration(400);
        scaleYUp.setDuration(400);
        scaleXDown.setDuration(300);
        scaleYDown.setDuration(300);

        scaleXUp.setInterpolator(new BounceInterpolator());
        scaleYUp.setInterpolator(new BounceInterpolator());
        scaleXDown.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYDown.setInterpolator(new AccelerateDecelerateInterpolator());

        celebrationSet.play(scaleXUp).with(scaleYUp);
        celebrationSet.play(scaleXDown).with(scaleYDown).after(scaleXUp);
        celebrationSet.start();
    }

    public static void pulseView(View view) {
        ObjectAnimator pulse = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.5f, 1f);
        pulse.setDuration(600);
        pulse.setRepeatCount(3);
        pulse.start();
    }

    public static void streetLightUp(View view) {
        ObjectAnimator glow = ObjectAnimator.ofFloat(view, "alpha", 0.8f, 1f);
        glow.setDuration(500);
        glow.setRepeatCount(5);
        glow.setRepeatMode(ObjectAnimator.REVERSE);
        glow.start();
    }
}
