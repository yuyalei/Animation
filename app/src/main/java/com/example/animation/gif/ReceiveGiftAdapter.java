package com.example.animation.gif;

import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.animation.R;
import com.example.animation.anim.AnimationUtils;
import com.example.animation.anim.NumAnimation;
import com.example.animation.gif.bean.ReceiveGiftBean;

/**
 * Desc: <功能简述>
 * Author: JS-Dugu
 * Created On: 2019/10/24 20:48
 */
public class ReceiveGiftAdapter implements GiftAnimationAdapter<ReceiveGiftBean> {

    private Context context;
    private SendCallback sendCallback;
    private Paint mPaint = null;

    public ReceiveGiftAdapter(Context context) {
        this.context = context;
        //获取屏幕密度和字体适用的密度
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float screenDensity = dm.density;
        float screenScaledDensity = dm.scaledDensity;
        mPaint = new Paint();
        mPaint.setTextSize(15 * screenScaledDensity);
    }

    public void setSendCallback(SendCallback callback) {
        this.sendCallback = callback;
    }


    private void loadGiftImage(String imgUrl, ImageView imageView) {
        Glide.with(context).load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_chat_gift)
                .error(R.mipmap.ic_chat_gift)
                .into(imageView);
    }

    private void loadLottieAnimation(String url){

    }

    private String getUserName(String userName) {
//        //第一种获取文本高度的方式
//        Paint.FontMetrics fm = mPaint.getFontMetrics();
//        float textHeight = fm.descent - fm.ascent;
//        // 第二种获取文本高度的方式
//        float textHeight = mPaint.descent() - mPaint.ascent();
        //第三种获取文本高度的方式
//        Rect bounds = new Rect();
//        mPaint.getTextBounds("0.00", 0, userName.length(), bounds);
//        // textHeight3 = bounds.height();
//        //获取文本宽度
//        float textWidth = bounds.width();
//        if (textWidth > DensityUtil.dp2px(60)) {
//            return userName;
//        }
        return userName + " ";
    }


    @Override
    public View onInit(View view, ReceiveGiftBean bean) {
        System.out.println("onInit---->ReceiveGiftBean: bean=" + bean);

        ImageView giftImage = (ImageView) view.findViewById(R.id.iv_gift_img);
        final TextView giftNum = (TextView) view.findViewById(R.id.tv_gift_amount);
        TextView userName = (TextView) view.findViewById(R.id.tv_user_name);
        TextView giftName = (TextView) view.findViewById(R.id.tv_gift_name);
        // 初始化数据
        if (bean.getReceiveGiftCount() > 0) {
            giftNum.setText("x1");
        } else {
            giftNum.setText("x" + bean.getTheSendGiftSize());
            bean.setTheGiftCount(bean.getTheSendGiftSize());
        }
        loadGiftImage(bean.getGiftImg(), giftImage);
        ///userName.setText(getUserName(bean.getUserName()));
        // giftName.setText("送出 " + bean.getGiftName());

        final RelativeLayout llContentLayout = view.findViewById(R.id.llContentLayout);
        /*if (llContentLayout != null) {
            llContentLayout.post(new Runnable() {
                @Override
                public void run() {
                    int width = llContentLayout.getMeasuredWidth();
                    // System.out.println("width:" + width);
                    view.findViewById(R.id.viewBg).getLayoutParams().width = width + DensityUtil.dp2px(2);
                }
            });
        }*/

        return view;
    }

    @Override
    public View onUpdate(View view, ReceiveGiftBean o, ReceiveGiftBean bean) {
        System.out.println("onUpdate---->ReceiveGiftBean: o=" + o);
        System.out.println("onUpdate---->ReceiveGiftBean: bean=" + bean);

        ImageView giftImage = (ImageView) view.findViewById(R.id.iv_gift_img);
        TextView giftNum = (TextView) view.findViewById(R.id.tv_gift_amount);

        int showNum = 0;
        if (bean.getReceiveGiftCount() > 0) {
            showNum = (Integer) bean.getTheGiftCount();
        } else {
            showNum = (Integer) o.getTheGiftCount() + o.getTheSendGiftSize();
        }
        // 刷新已存在的giftview界面数据
        giftNum.setText("x" + showNum);
        loadGiftImage(bean.getGiftImg(), giftImage);

        final RelativeLayout llContentLayout = view.findViewById(R.id.llContentLayout);
        /*if (llContentLayout != null) {
            llContentLayout.post(new Runnable() {
                @Override
                public void run() {
                    int width = llContentLayout.getMeasuredWidth();
                    System.out.println("width:" + width);
                    view.findViewById(R.id.viewBg).getLayoutParams().width = width + DensityUtil.dp2px(2);
                }
            });
        }*/

        // 数字刷新动画
        new NumAnimation().start(giftNum);
        // 更新累计礼物数量
        o.setTheGiftCount(showNum);
        return view;
    }

    @Override
    public void onKickEnd(ReceiveGiftBean bean) {
        // 踢掉
    }

    @Override
    public void onComboEnd(ReceiveGiftBean bean) {
        System.out.println("onComboEnd---->ReceiveGiftBean:" + bean);
        // 完成
        if (sendCallback != null && bean.getReceiveGiftCount() == 0) {
            sendCallback.completed(bean);
        }
    }

    @Override
    public void addAnim(View view) {
        final TextView textView = (TextView) view.findViewById(R.id.tv_gift_amount);
        ImageView img = (ImageView) view.findViewById(R.id.iv_gift_img);
        // 整个giftview动画
        Animation giftInAnim = AnimationUtils.getLeftInAnimation(context);
        // 礼物图像动画
        Animation imgInAnim = AnimationUtils.getLeftInAnimation(context);
        // 首次连击动画
        final NumAnimation comboAnim = new NumAnimation();
        imgInAnim.setStartTime(500);
        imgInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                textView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.setVisibility(View.VISIBLE);
                comboAnim.start(textView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(giftInAnim);
        img.startAnimation(imgInAnim);
    }

    @Override
    public AnimationSet outAnim() {
        return AnimationUtils.getLeftOutAnimation(context);
    }

    @Override
    public boolean checkUnique(ReceiveGiftBean o, ReceiveGiftBean receiveGiftBean) {
       /* boolean same = o.getTheGiftId() == receiveGiftBean.getTheGiftId() && o.getMsgId() == receiveGiftBean.getMsgId()
                && o.getTheUserId() == receiveGiftBean.getTheUserId();*/
        boolean same = o.getTheGiftId() == receiveGiftBean.getTheGiftId() &&  o.getTheUserId() == receiveGiftBean.getTheUserId();
        return false;
    }

    @Override
    public ReceiveGiftBean generateBean(ReceiveGiftBean bean) {
        try {
            return (ReceiveGiftBean) bean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
