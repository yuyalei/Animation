package com.example.animation.gif;

/**
 * Desc: <礼物清理者>  (清楚界面中的动画view)
 * Author: JS-Dugu
 * Created On: 2019/10/24 19:48
 */
public class GiftClearer implements Runnable {

    private GiftInterface mInterface;

    public GiftClearer(GiftInterface mInterface) {
        this.mInterface = mInterface;
    }

    @Override
    public void run() {
        if (mInterface != null) {
            mInterface.doSomething();
        }
    }
}