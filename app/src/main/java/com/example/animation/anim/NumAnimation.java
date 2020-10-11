package com.example.animation.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Desc: <功能简述>
 * Author: JS-Dugu
 * Created On: 2019/10/24 21:23
 */
public class NumAnimation {

    private Animator lastAnimator = null;

    public void start(View view) {
        if (lastAnimator != null) {
            lastAnimator.removeAllListeners();
            lastAnimator.end();
            lastAnimator.cancel();
        }
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "scaleX", 1.6f, 1.0f);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "scaleY", 1.6f, 1.0f);
        AnimatorSet animSet = new AnimatorSet();
        lastAnimator = animSet;
        animSet.setDuration(400);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animX, animY);
        animSet.start();
    }

}
