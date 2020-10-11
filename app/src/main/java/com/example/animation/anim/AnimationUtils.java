package com.example.animation.anim;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.example.animation.R;

/**
 * Desc: <功能简述>
 * Author: JS-Dugu
 * Created On: 2019/10/24 20:16
 */
public class AnimationUtils {

    /**
     * 获取礼物入场动画
     *
     * @return
     */
    public static Animation getLeftInAnimation(Context context) {
        return (TranslateAnimation) android.view.animation.AnimationUtils.loadAnimation(context, R.anim.gift_left_in);
    }

    /**
     * 获取礼物出场动画
     *
     * @return
     */
    public static AnimationSet getLeftOutAnimation(Context context) {
        return (AnimationSet) android.view.animation.AnimationUtils.loadAnimation(context, R.anim.gift_left_out);
    }


    /**
     * 获取礼物入场动画
     *
     * @return
     */
    public static Animation getRightInAnimation(Context context) {
        return (TranslateAnimation) android.view.animation.AnimationUtils.loadAnimation(context, R.anim.gift_right_in);
    }

    public static AnimationSet getRightInAnimations(Context context) {
        return (AnimationSet) android.view.animation.AnimationUtils.loadAnimation(context, R.anim.gift_right_in);
    }

    /**
     * 获取礼物出场动画
     *
     * @return
     */
    public static AnimationSet getRightOutAnimation(Context context) {
        return (AnimationSet) android.view.animation.AnimationUtils.loadAnimation(context, R.anim.gift_right_out);
    }

}
