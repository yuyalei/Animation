package com.example.animation.gif;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.animation.gif.bean.GiftIdentify;

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
 * Created On: 2019/10/29 19:04
 */
public class GiftAnimationLayout extends LinearLayout {
    public final String TAG = this.getClass().getSimpleName();

    public static final int MAX_COUNT_DEFAULT = 3;
    public static final int MAX_THREAD = 1;
    private int maxGiftCount = MAX_COUNT_DEFAULT;
    private int giftItemLayout = 0;

    private int childWidth;
    private int childHeight;

    private List<GiftIdentify> beans;
    private GiftAnimationAdapter adapter;
    private AnimationSet outAnim = null;

    private ScheduledExecutorService clearService;
    private ExecutorService takeService;

    private GiftClearer clearer;
    private GiftTaker taker;
    private GiftBasket basket;
    private GiftInterface clearTask;
    private GiftInterface takeTask;

    public GiftAnimationAdapter getAdapter() {
        return adapter;
    }

    public void setGiftAdapter(GiftAnimationAdapter adapter) {
        this.adapter = adapter;
    }

    public GiftAnimationLayout(Context context) {
        super(context);
        init(context,null);
    }

    public GiftAnimationLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        setOrientation(VERTICAL);
        beans = new ArrayList<>();
        clearTask = new GiftInterface() {
            @Override
            public void doSomething() {
                try {
                    clearTask();
                } catch (Exception e) {
                    Log.d(TAG, "clearException=" + e.getMessage());
                }
            }
        };
        takeTask = new GiftInterface() {
            @Override
            public void doSomething() {
                takeTask();
            }
        };
        clearer = new GiftClearer(clearTask);
        basket = new GiftBasket();
        taker = new GiftTaker(takeTask);
        clearService = Executors.newScheduledThreadPool(MAX_THREAD);
        takeService = Executors.newFixedThreadPool(MAX_THREAD);
        startClearService();
        startTakeGiftService();
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
        int totalHeight = childHeight * getMaxGiftCount() + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(measureWidth(widthMeasureSpec, totalWidth, child.getLayoutParams().width)
                , measureHeight(heightMeasureSpec, totalHeight, child.getLayoutParams().height));
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
        if (getChildCount() != 0) {
            removeAllViews();
        }
        for (int i = 0; i < maxGiftCount; i++) {
            FrameLayout linearLayout = new FrameLayout(getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                    (getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) / maxGiftCount);
            linearLayout.setLayoutParams(params);
            addView(linearLayout);
        }
    }

    /**
     * 外部调用方法，添加礼物view到 layout 中
     *
     * @param sBean
     */
    private void showGift(GiftIdentify sBean) {
        if (adapter == null) {
            return;
        }
        GiftIdentify bean = null;
        for (GiftIdentify baseGiftBean : beans) {
            if (adapter.checkUnique(baseGiftBean, sBean)) {
                bean = baseGiftBean;
            }
        }
        if (bean == null) {
            bean = adapter.generateBean(sBean);
            if (bean == null) {
                throw new NullPointerException("clone return null");
            }
            beans.add(bean);
        }
        GiftIdentify mBean = null;
        View giftView = findSameUserGiftView(bean);
        /*该用户不在礼物显示列表*/
        if (giftView == null) {
            mBean = bean;
            /*如果正在显示的礼物的个数超过MAX_GIFT_COUNT个，那么就移除最后一次更新时间比较长的*/
            if (getCurrentGiftCount() > maxGiftCount - 1) {
                List<GiftIdentify> list = new ArrayList();
                for (int i = 0; i < getChildCount(); i++) {
                    ViewGroup vg = (ViewGroup) getChildAt(i);
                    for (int j = 0; j < vg.getChildCount(); j++) {
                        if (vg.getChildAt(j).isEnabled()) {
                            GiftIdentify gBean = (GiftIdentify) vg.getChildAt(j).getTag();
                            gBean.setTheCurrentIndex(i);
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
                mBean = (GiftIdentify) giftView.getTag();
                // 相同礼物需要更新SendGiftSize
                mBean.setTheSendGiftSize(sBean.getTheSendGiftSize());
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
     * 手动更新礼物过期时间
     *
     * @param cBean
     */
    public void updateRefreshTime(GiftIdentify cBean) {
        updateRefreshTime(cBean, 0);
    }

    /**
     * 手动更新礼物过期时间
     *
     * @param cBean
     * @param delay
     */
    public void updateRefreshTime(GiftIdentify cBean, long delay) {
        if (adapter == null) {
            throw new IllegalArgumentException("setAdapter first");
        }
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final int index = i;
            ViewGroup viewG = (ViewGroup) getChildAt(index);
            for (int j = 0; j < viewG.getChildCount(); j++) {
                final View view = viewG.getChildAt(j);
                final GiftIdentify tag = (GiftIdentify) view.getTag();
                if (tag != null && view.isEnabled()) {
                    if (adapter.checkUnique(tag, cBean)) {
                        if (delay == 0) {
                            if (cBean.getTheLatestRefreshTime() != 0 && cBean.getTheLatestRefreshTime() > tag.getTheLatestRefreshTime()) {
                                tag.setTheLatestRefreshTime(cBean.getTheLatestRefreshTime());
                            } else {
                                tag.setTheLatestRefreshTime(System.currentTimeMillis());
                            }
                        } else {
                            tag.setTheLatestRefreshTime(tag.getTheLatestRefreshTime() + delay);
                        }
                    }
                }
            }
        }
    }

    /**
     * 从xml布局中加载礼物view
     */
    private View getGiftView() {
        FrameLayout root = new FrameLayout(getContext());
        View view = LayoutInflater.from(getContext()).inflate(getGiftRes(), root, false);
        LayoutParams lp = new LayoutParams(view.getLayoutParams().width, view.getLayoutParams().height);
        root.setLayoutParams(lp);
        root.addView(view);
        return root;
    }

    private void addGiftViewAnim(final GiftIdentify mBean) {
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
     * 删除指定framelayout下的view的礼物动画
     *
     * @param view
     */
    private void removeGiftViewAnim(final View view) {
        if (view != null) {
            // 标记该giftview不可用
            view.setEnabled(false);
            if (adapter != null) {
                adapter.onKickEnd((GiftIdentify) view.getTag());
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
                adapter.onComboEnd((GiftIdentify) removeView.getTag());
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
    private void startTakeGiftService() {
        if (!takeService.isShutdown()) {
            takeService.execute(taker);
        } else {
            takeService = Executors.newFixedThreadPool(MAX_THREAD);
            takeService.execute(taker);
        }
    }


    /**
     * 移除指定framelayout下面的礼物view
     *
     * @param index
     */
    private void removeChildGift(int index) {
        if (index < getChildCount() && getChildAt(index) instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) getChildAt(index);
            if (vg.getChildCount() > 0) {
                vg.removeViewAt(0);
            }
        }
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
                GiftIdentify bean = (GiftIdentify) view.getTag();
                long giftId = bean.getTheGiftId();
                long userId = bean.getTheUserId();
                for (Iterator<GiftIdentify> it = beans.iterator(); it.hasNext(); ) {
                    GiftIdentify value = it.next();
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


    /**
     * 添加礼物到空闲的framelayout，在覆盖的时候可能存在礼物离场动画还么结束view还没有被remove的情况下
     * 根据该view的enable判断
     *
     * @param view
     */
    private void addChildGift(View view) {
        for (int i = 0; i < getChildCount(); i++) {
            if (((ViewGroup) getChildAt(i)).getChildCount() == 0) {
                ((ViewGroup) getChildAt(i)).addView(view);
                getChildAt(i).setTag(((GiftIdentify) view.getTag()).getTheLatestRefreshTime());
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
                    ((ViewGroup) getChildAt(i)).addView(view);
                    getChildAt(i).setTag(((GiftIdentify) view.getTag()).getTheLatestRefreshTime());
                    break;
                }
            }
        }
    }


    /**
     * 找出唯一识别的礼物
     *
     * @param target
     * @return
     */
    private View findSameUserGiftView(GiftIdentify target) {
        if (adapter == null) {
            return null;
        }
        for (int i = 0; i < getChildCount(); i++) {
            for (int j = 0; j < ((ViewGroup) getChildAt(i)).getChildCount(); j++) {
                GiftIdentify rGiftBean = (GiftIdentify) ((ViewGroup) getChildAt(i)).getChildAt(j).getTag();
                if (adapter.checkUnique(rGiftBean, target)) {
                    return ((ViewGroup) getChildAt(i)).getChildAt(j);
                }
            }
        }
        return null;
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


    public void onPause() {
        if (clearService != null) {
            clearService.shutdown();
        }
        if (takeService != null) {
            takeService.shutdown();
        }
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
        if (takeService != null) {
            if (takeService.isShutdown()) {
                startTakeGiftService();
            }
        } else {
            takeService = Executors.newFixedThreadPool(MAX_THREAD);
            startTakeGiftService();
        }
    }

    public void onDestroy() {
        if (clearService != null) {
            clearService.shutdownNow();
            clearService = null;
        }
        if (takeService != null) {
            takeService.shutdownNow();
            takeService = null;
        }
        clearTask = null;
        takeTask = null;
        clearer = null;
        taker = null;
        basket = null;
        adapter = null;
    }


    private int getGiftRes() {
        if (giftItemLayout != 0) {
            return giftItemLayout;
        } else {
            throw new NullPointerException("u should init gift item resource first");
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

    /**
     * 将礼物放入队列
     *
     * @param bean
     */
    public void put(GiftIdentify bean) {
        if (basket != null) {
            try {
                basket.putGift(bean);
            } catch (InterruptedException e) {
                Log.d(TAG, "IllegalStateException=" + e.getMessage());
            }
        }
    }


    /**
     * 清礼物并执行礼物连击结束回调
     */
    private void clearTask() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final int index = i;
            ViewGroup viewG = (ViewGroup) getChildAt(index);
            for (int j = 0; j < viewG.getChildCount(); j++) {
                final View view = viewG.getChildAt(j);
                // 可能判断完获取的时候还是为空,保底try catch，如果改用handler postDelay去倒计时，频繁的取消，
                // 开始,维护每个倒计时，修改时间,也会很麻烦，暂时先这么处理，有什么改进方案，欢迎pr o_o!!!
                if (view != null) {
                    final GiftIdentify tag = (GiftIdentify) view.getTag();
                    if (tag != null && view.isEnabled()) {
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
     * 取礼物
     */
    private void takeTask() {
        boolean interrupted = false;
        try {
            while (true) {
                final GiftIdentify gift = basket.takeGift();
                if (gift != null) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            showGift(gift);
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
}
