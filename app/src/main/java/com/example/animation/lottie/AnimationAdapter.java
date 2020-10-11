package com.example.animation.lottie;

import android.view.View;
import android.view.animation.AnimationSet;

import com.example.animation.gif.bean.GiftIdentify;
import com.example.animation.lottie.bean.AnimationIdentify;

/**
 * Desc:
 * Author: JS-Kylo
 * Created On: 2019/10/31 11:44
 */
public interface AnimationAdapter <T extends AnimationIdentify>{
    /**
     * 初始化
     *
     * @param view
     * @param bean
     * @return
     */
    View onInit(View view, T bean);

    /**
     * 更新
     *
     * @param view
     * @param o 原礼物对象
     * @param t 新礼物对象
     * @return
     */
    View onUpdate(View view, T o, T t);

    /**
     * 添加进入动画
     *
     * @param view
     */
    void addAnim(View view);

    /**
     * 添加退出动画
     *
     * @return
     */
    AnimationSet outAnim();

    /**
     * 鉴别礼物唯一性，
     *
     * @param o 已存在的礼物bean
     * @param t 新传入的礼物bean
     * @return 返回比对后的结果
     */
    boolean checkUnique(T o, T t);

    T generateBean(T bean);
}
