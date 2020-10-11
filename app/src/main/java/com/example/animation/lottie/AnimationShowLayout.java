package com.example.animation.lottie;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.animation.lottie.bean.AnimationIdentify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Desc:
 * Author: JS-Kylo
 * Created On: 2019/10/31 10:40
 */
public class AnimationShowLayout extends FrameLayout {
    public final String TAG = this.getClass().getSimpleName();
    public static final int MAX_COUNT_DEFAULT = 1;
    public static final int MAX_THREAD = 1;
    private int maxGiftCount = MAX_COUNT_DEFAULT;
    private int giftItemLayout = 0;

    private int childWidth;
    private int childHeight;

    List<AnimationIdentify> beans;
    private AnimationAdapter adapter;
    private AnimationSet outAnim = null;

    private AnimationClearer clearer;
    private AnimationSender sender;
    private AnimationQueue queue;
    private AnimationInterface clearTask;
    private AnimationInterface senderTask;

    private ScheduledExecutorService clearService;
    private ExecutorService sendService;

    public AnimationAdapter getAdapter() {
        return adapter;
    }

    public void setGiftAdapter(AnimationAdapter adapter) {
        this.adapter = adapter;
    }

    public AnimationShowLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public AnimationShowLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        beans = new ArrayList<>();
        clearTask = new AnimationInterface() {
            @Override
            public void doSomething() {
                clearTask();
            }
        };

        senderTask = new AnimationInterface() {
            @Override
            public void doSomething() {
                sendTask();
            }
        };

        clearer = new AnimationClearer(clearTask);
        sender = new AnimationSender(senderTask);
        queue = new AnimationQueue();
        clearService = Executors.newScheduledThreadPool(MAX_THREAD);
        sendService = Executors.newFixedThreadPool(MAX_THREAD);
        startClearService();
        startSendGiftService();
    }

    /**
     * 清除动画
     */
    private void clearTask() {
        int count = getChildCount();
        for (int i =0;i<count;i++){
            final int index = i;
            ViewGroup viewG = (ViewGroup) getChildAt(index);
            for (int j = 0;j<viewG.getChildCount();j++){
                final View view = viewG.getChildAt(j);
                // 可能判断完获取的时候还是为空,保底try catch，如果改用handler postDelay去倒计时，频繁的取消，
                // 开始,维护每个倒计时，修改时间,也会很麻烦，暂时先这么处理，有什么改进方案，欢迎pr o_o!!!
                if (view != null){
                    final AnimationIdentify tag = (AnimationIdentify) view.getTag();
                    if (tag != null && view.isEnabled()){
                        long nowtime = System.currentTimeMillis();
                        long upTime = tag.getTheLatestRefreshTime();
                        if ((nowtime - upTime) >= tag.getTheGiftStay()) {
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    removeGiftViewAnim(index);
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    /**
     * 添加动画
     */
    private void sendTask() {
        boolean interrupted = false;
        try {
            while (true) {
                AnimationIdentify animation = queue.takeGift();
                if (animation != null) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            addAnimation(animation);
                        }
                    });
                }
            }
        } catch (InterruptedException e) {
            interrupted = true;
            Log.d(TAG, "InterruptedException=" + e.getMessage());
            e.printStackTrace();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException=" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(TAG, "Exception=" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    //添加动画
    private void addAnimation(AnimationIdentify sBean) {
        if (null == adapter) return;
        AnimationIdentify bean = null;

        for (AnimationIdentify b : beans) {
            if (adapter.checkUnique(b, sBean))
                bean = b;
        }
        if (null == bean) {
            bean = adapter.generateBean(sBean);
            if (bean == null) {
                throw new NullPointerException("clone return null");
            }
            beans.add(bean);
        }
        AnimationIdentify mBean = null;
        View giftView = findSameUserGiftView(bean);
        if (null == giftView) {
            mBean = bean;
            /*如果正在显示的礼物的个数超过MAX_GIFT_COUNT个，那么就移除最后一次更新时间比较长的*/
            if (getCurrentGiftCount() > maxGiftCount - 1) {
                List<AnimationIdentify> list = new ArrayList();
                for (int i = 0; i < getChildCount(); i++) {
                    ViewGroup group = (ViewGroup) getChildAt(i);
                    for (int j = 0; j < group.getChildCount(); j++) {
                        if (group.getChildAt(j).isEnabled()) {
                            AnimationIdentify gBean = (AnimationIdentify) group.getChildAt(j).getTag();
                            list.add(gBean);
                        }
                    }
                }
                // 根据加入时间排序所有child中giftview
                Collections.sort(list);
                if (list.size() > 0) {
                    removeGiftViewAnim(findSameUserGiftView(list.get(0)));
                }
                addGiftViewAnim(mBean);
            } else {
                addGiftViewAnim(mBean);
            }
        } else {
            if (giftView.isEnabled()) {
                mBean = (AnimationIdentify) giftView.getTag();
                // 相同礼物需要更新SendGiftSize
                //mBean.setTheSendGiftSize(sBean.getTheSendGiftSize());
                if (adapter != null) {
                    giftView = adapter.onUpdate(giftView, mBean, sBean);
                }
                mBean.setTheLatestRefreshTime(System.currentTimeMillis());
                giftView.setTag(mBean);
                ViewGroup vg = (ViewGroup) giftView.getParent();
                vg.setTag(mBean.getTheLatestRefreshTime());
            }
        }

    }

    /**
     * 删除指定framelayout下的view的礼物动画
     *
     * @param index
     */
    private void removeGiftViewAnim(final int index) {
        final View removeView = getChildGift(index);
        if (removeView != null) {
            // 标记该giftview不可用
            removeView.setEnabled(false);
            if (adapter != null) {
                //adapter.onComboEnd((GiftIdentify) removeView.getTag());
                outAnim = adapter.outAnim();
                outAnim.setFillAfter(true);
                outAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                removeChildGift(removeView);
                            }
                        });

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                post(new Runnable() {
                    @Override
                    public void run() {
                        removeView.startAnimation(outAnim);
                    }
                });

            }

        }
    }

    /**
     * 删除指定framelayout下的view的礼物动画
     *
     * @param view
     */
    private void removeGiftViewAnim(final View view) {
        if (view != null) {
            view.setEnabled(false);
            if (adapter != null) {
                outAnim = adapter.outAnim();
                outAnim.setFillAfter(true);
                outAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                removeChildGift(view);
                            }
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                post(new Runnable() {
                    @Override
                    public void run() {
                        view.startAnimation(outAnim);
                    }
                });
            }
        }
    }


    /**
     * 获取指定framelayout下的礼物view
     *
     * @param index
     * @return
     */
    private View getChildGift(int index) {
        View view = null;
        ViewGroup vg = (ViewGroup) getChildAt(index);
        for (int i = 0; i < vg.getChildCount(); i++) {
            if (vg.getChildAt(i).isEnabled()) {
                view = vg.getChildAt(i);
            }
        }
        return view;
    }

    private void addGiftViewAnim(final AnimationIdentify mBean) {
        View giftView = null;
        if (adapter != null) {
            giftView = adapter.onInit(getGiftView(), mBean);
        }
        mBean.setTheLatestRefreshTime(System.currentTimeMillis());
        giftView.setTag(mBean);
        // 标记该giftview可用
        giftView.setEnabled(true);

        addChildGift(giftView);
        invalidate();

        if (adapter != null) {
            adapter.addAnim(giftView);
        }
    }

    /**
     * 添加礼物到空闲的framelayout，在覆盖的时候可能存在礼物离场动画还么结束view还没有被remove的情况下
     * 根据该view的enable判断
     *
     * @param view
     */
    private void addChildGift(View view) {
        for (int i = 0; i < getChildCount(); i++) {
            if (((ViewGroup) getChildAt(i)).getChildCount() == 0) {
                //view.setLayoutParams(getAnimationViewParams());
                ((ViewGroup) getChildAt(i)).addView(view);
                getChildAt(i).setTag(((AnimationIdentify) view.getTag()).getTheLatestRefreshTime());
                break;
            } else {
                boolean isAllCancel = true;
                for (int j = 0; j < ((ViewGroup) getChildAt(i)).getChildCount(); j++) {
                    if (((ViewGroup) getChildAt(i)).getChildAt(j).isEnabled()) {
                        isAllCancel = false;
                        break;
                    }
                }
                if (isAllCancel) {
                    //view.setLayoutParams(getAnimationViewParams());
                    ((ViewGroup) getChildAt(i)).addView(view);
                    getChildAt(i).setTag(((AnimationIdentify) view.getTag()).getTheLatestRefreshTime());
                    break;
                }
            }
        }
    }

    /**
     * 设置动画view的布局
     */
    private LinearLayout.LayoutParams getAnimationViewParams(){
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        param.leftMargin = 240;
        param.gravity = Gravity.CENTER;  //必须要加上这句，setMargins才会起作用，而且此句还必须在setMargins下面
        return param;
    }

    /**
     * 移除指定framelayout下面的礼物view
     *
     * @param view
     */
    private void removeChildGift(View view) {
        for (int i = 0; i < getChildCount(); i++) {
            ViewGroup vg = (ViewGroup) getChildAt(i);
            final int index = vg.indexOfChild(view);
            if (index >= 0) {
                AnimationIdentify bean = (AnimationIdentify) view.getTag();
                long giftId = bean.getTheGiftId();
                long userId = bean.getTheUserId();
                for (Iterator<AnimationIdentify> it = beans.iterator(); it.hasNext(); ) {
                    AnimationIdentify value = it.next();
                    if (value.getTheGiftId() == giftId && value.getTheUserId() == userId) {
                        it.remove();
                    }
                }
                vg.removeView(view);
                view = null;
            }
        }
    }

    /**
     * 获取当前在显示的礼物数量
     *
     * @return
     */
    private int getCurrentGiftCount() {
        int count = 0;
        for (int i = 0; i < getChildCount(); i++) {
            for (int j = 0; j < ((ViewGroup) getChildAt(i)).getChildCount(); j++) {
                if (((ViewGroup) getChildAt(i)).getChildAt(j).isEnabled() == true) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 将礼物放入队列
     *
     * @param bean
     */
    public void put(AnimationIdentify bean) {
        if (queue != null) {
            try {
                queue.putGift(bean);
            } catch (InterruptedException e) {
                Log.d(TAG, "IllegalStateException=" + e.getMessage());
            }
        }
    }

    /**
     * 找出唯一识别的礼物
     *
     * @param target
     * @return
     */
    private View findSameUserGiftView(AnimationIdentify target) {
        if (adapter == null) {
            return null;
        }
        for (int i = 0; i < getChildCount(); i++) {
            for (int j = 0; j < ((ViewGroup) getChildAt(i)).getChildCount(); j++) {
                AnimationIdentify rGiftBean = (AnimationIdentify) ((ViewGroup) getChildAt(i)).getChildAt(j).getTag();
                if (adapter.checkUnique(rGiftBean, target)) {
                    return ((ViewGroup) getChildAt(i)).getChildAt(j);
                }
            }
        }
        return null;
    }

    private void initViews() {
        if (getChildCount() != 0) {
            removeAllViews();
        }
        for (int i = 0; i < maxGiftCount; i++) {
            FrameLayout linearLayout = new FrameLayout(getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                    (getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) / maxGiftCount);
            //FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.gravity= Gravity.CENTER;
            linearLayout.setLayoutParams(params);
            //linearLayout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
            addView(linearLayout);
        }
        //invalidate();
    }

    /**
     * 测量礼物view的高度和宽度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        View child = getGiftView();
        measureChild(child, widthMeasureSpec, heightMeasureSpec);
        MarginLayoutParams lp = (MarginLayoutParams) child
                .getLayoutParams();
        // 当前子空间实际占据的宽度,此处已用FrameLayout包裹，这里的margin≡0，
        childWidth = child.getMeasuredWidth() + lp.leftMargin
                + lp.rightMargin;
        // 当前子空间实际占据的高度
        childHeight = child.getMeasuredHeight() + lp.topMargin
                + lp.bottomMargin;

        int totalWidth = childWidth + getPaddingLeft() + getPaddingRight();
        int totalHeight = childHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(measureWidth(widthMeasureSpec, totalWidth, child.getLayoutParams().width)
                , measureHeight(heightMeasureSpec, totalHeight, child.getLayoutParams().height));
    }

    /**
     * 向rewardlayout中添加MAX_GIFT_COUNT个子framelayout
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initViews();
    }

    /**
     * 测量宽度，结合item布局的宽参数
     *
     * @param measureSpec
     * @param viewGroupWidth
     * @param childLp
     * @return
     */
    private int measureWidth(int measureSpec, int viewGroupWidth, int childLp) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                if (childLp == ViewGroup.LayoutParams.MATCH_PARENT) {
                    result = specSize;
                } else {
                    result = Math.min(viewGroupWidth, specSize);
                }
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            default:
                result = specSize;
                break;
        }
        return result;
    }


    /**
     * 测量高度，结合item布局的高参数
     *
     * @param measureSpec
     * @param viewGroupHeight
     * @param childLp
     * @return
     */
    private int measureHeight(int measureSpec, int viewGroupHeight, int childLp) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                if (childLp == ViewGroup.LayoutParams.MATCH_PARENT) {
                    result = specSize;
                } else {
                    result = Math.min(viewGroupHeight, specSize);
                }
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            default:
                result = specSize;
                break;
        }
        return result;
    }

    /**
     * 从xml布局中加载礼物view
     */
    private View getGiftView() {
        FrameLayout root = new FrameLayout(getContext());
        View view = LayoutInflater.from(getContext()).inflate(getGiftRes(), root, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(view.getLayoutParams().width, view.getLayoutParams().height);
        root.setLayoutParams(lp);
        root.addView(view);
        return root;
    }

    private int getGiftRes() {
        if (giftItemLayout != 0) {
            return giftItemLayout;
        } else {
            throw new NullPointerException("u should init gift item resource first");
        }
    }

    /**
     * 定时清除礼物
     */
    private void startClearService() {
        if (!clearService.isShutdown()) {
            clearService.scheduleWithFixedDelay(clearer, 0, 20, TimeUnit.MILLISECONDS);
        } else {
            clearService = Executors.newScheduledThreadPool(MAX_THREAD);
            clearService.scheduleWithFixedDelay(clearer, 0, 20, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 不断取礼物队列
     */
    private void startSendGiftService() {
        if (!sendService.isShutdown()) {
            sendService.execute(sender);
        } else {
            sendService = Executors.newFixedThreadPool(MAX_THREAD);
            sendService.execute(sender);
        }
    }

    public void setMaxGift(int max) {
        maxGiftCount = max;
    }

    public int getMaxGiftCount() {
        return maxGiftCount;
    }

    /**
     * before view attachtowindow
     *
     * @param res
     */
    public void setGiftItemRes(int res) {
        giftItemLayout = res;
    }

    public void onResume() {
        if (clearService != null) {
            if (clearService.isShutdown()) {
                startClearService();
            }
        } else {
            clearService = Executors.newScheduledThreadPool(MAX_THREAD);
            startClearService();
        }
        if (sendService != null) {
            if (sendService.isShutdown()) {
                startSendGiftService();
            }
        } else {
            sendService = Executors.newFixedThreadPool(MAX_THREAD);
            startSendGiftService();
        }
    }

    public void onPause() {
        if (clearService != null) {
            clearService.shutdown();
        }
        if (sendService != null) {
            sendService.shutdown();
        }
    }

    public void onDestroy() {
        if (clearService != null) {
            clearService.shutdownNow();
            clearService = null;
        }
        if (sendService != null) {
            sendService.shutdownNow();
            sendService = null;
        }
        clearTask = null;
        senderTask = null;
        clearer = null;
        sender = null;
        queue = null;
        adapter = null;
    }
}
