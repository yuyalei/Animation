package com.example.animation.lottie;

/**
 * Desc: 发送礼物
 * Author: JS-Kylo
 * Created On: 2019/10/31 13:27
 */
public class AnimationSender implements Runnable{
    private AnimationInterface mInterface;

    public AnimationSender(AnimationInterface mInterface) { this.mInterface = mInterface; }

    @Override
    public void run() {
        if (null!=mInterface)
            mInterface.doSomething();
    }
}
