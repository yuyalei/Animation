package com.example.animation.gif;

/**
 * Desc: <礼物消费者> （在布局上添加动画view）
 * Author: JS-Dugu
 * Created On: 2019/10/24 19:48
 */
public class GiftTaker implements Runnable {

    private String TAG = "TakeGifter";

    private GiftInterface mInterface;

    public GiftTaker(GiftInterface mInterface) {
        this.mInterface = mInterface;
    }

    @Override
    public void run() {
        if (mInterface != null) {
            mInterface.doSomething();
        }
    }

}
