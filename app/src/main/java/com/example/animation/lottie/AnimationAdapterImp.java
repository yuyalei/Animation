package com.example.animation.lottie;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AnimationSet;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieResult;
import com.airbnb.lottie.LottieTask;
import com.example.animation.R;
import com.example.animation.anim.AnimationUtils;
import com.example.animation.gif.GiftAnimationAdapter;
import com.example.animation.lottie.bean.BaseAnimationBean;

/**
 * Desc:
 * Author: JS-Kylo
 * Created On: 2019/10/31 11:46
 */
public class AnimationAdapterImp implements AnimationAdapter<BaseAnimationBean> {
    private Context context;

    public AnimationAdapterImp(Context context) {
        this.context = context;
    }

    @Override
    public View onInit(View view, BaseAnimationBean bean) {
        LottieAnimationView lottieView = view.findViewById(R.id.lottie_layer);
        loadAnimation(lottieView,bean);
        return view;
    }

    @Override
    public View onUpdate(View view, BaseAnimationBean o, BaseAnimationBean baseAnimationBean) {
        LottieAnimationView lottieView = view.findViewById(R.id.lottie_layer);
        //loadAnimation(lottieView,baseAnimationBean);
        loadAnimationSync(lottieView,baseAnimationBean);
        return view;
    }

    private void loadAnimation(LottieAnimationView lottieView,BaseAnimationBean bean){
        LottieTask<LottieComposition> lottieTask =LottieCompositionFactory.fromUrl(context,bean.getGiftImg());

        lottieTask.addListener(result -> {
            lottieView.setComposition(result);
            lottieView.playAnimation();
            lottieView.setRepeatCount(ValueAnimator.INFINITE);
        });
    }
    private void loadAnimationSync(LottieAnimationView lottieView,BaseAnimationBean bean){
        LottieResult<LottieComposition> lottieCompositionLottieResult = LottieCompositionFactory.fromUrlSync(context, bean.getGiftImg());
        LottieComposition lottieComposition=lottieCompositionLottieResult.getValue();

        lottieView.setProgress(0);
        lottieView.loop(true);
        lottieView.setComposition(lottieComposition);
        lottieView.playAnimation();
    }


    @Override
    public void addAnim(View view) {
        //return AnimationUtils.getRightInAnimations(context);
    }

    @Override
    public AnimationSet outAnim() {
        return AnimationUtils.getLeftOutAnimation(context);
    }

    @Override
    public boolean checkUnique(BaseAnimationBean o, BaseAnimationBean baseAnimationBean) {
        return o.getTheGiftId() == baseAnimationBean.getTheGiftId() && o.getTheUserId() == baseAnimationBean.getTheUserId();
    }

    @Override
    public BaseAnimationBean generateBean(BaseAnimationBean bean) {
        try {
            return (BaseAnimationBean) bean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
