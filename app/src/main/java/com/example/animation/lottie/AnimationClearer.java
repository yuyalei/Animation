package com.example.animation.lottie;

/**
 * Desc: 清除动画
 * Author: JS-Kylo
 * Created On: 2019/10/31 13:25
 */
public class AnimationClearer implements Runnable{
    private AnimationInterface mInterface;

    public AnimationClearer(AnimationInterface mInterface) { this.mInterface = mInterface; }

    @Override
    public void run() {
        if (null!=mInterface)
            mInterface.doSomething();
    }
}
