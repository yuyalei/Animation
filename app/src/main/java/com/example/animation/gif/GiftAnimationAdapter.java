package com.example.animation.gif;

import android.view.View;
import android.view.animation.AnimationSet;

import com.example.animation.gif.bean.GiftIdentify;

/**
 * Desc: <礼物动画适配器>
 *
 * Author: JS-Dugu
 * Created On: 2019/10/24 19:41
 */
public interface GiftAnimationAdapter<T extends GiftIdentify> {

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
     * 礼物展示结束，可能由于送礼者过多，轨道被替换导致结束
     *
     * @param bean
     * @return
     */
    void onKickEnd(T bean);

    /**
     * 礼物连击结束,即被系统自动清理时回调
     *
     * @param bean
     * @return
     */
    void onComboEnd(T bean);

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
