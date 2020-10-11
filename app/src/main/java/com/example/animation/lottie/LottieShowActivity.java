package com.example.animation.lottie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.animation.MainActivity;
import com.example.animation.R;
import com.example.animation.gif.GifShowActivity;
import com.example.animation.gif.bean.ReceiveGiftBean;
import com.example.animation.lottie.bean.BaseAnimationBean;

/**
 * Desc:
 * Author: JS-Kylo
 * Created On: 2019/10/31 10:54
 */
public class LottieShowActivity  extends AppCompatActivity {
    private Button bt_show,bt_show1;
    private AnimationShowLayout animationLayoutLeft;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.activity_lottie_layout);
        animationLayoutLeft = findViewById(R.id.animationLayoutLeft);
        animationLayoutLeft.setGiftItemRes(R.layout.lottie_animation_item);
        animationLayoutLeft.setGiftAdapter(new AnimationAdapterImp(this));
        bt_show = findViewById(R.id.bt_show);
        bt_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gifImg ="https://assets5.lottiefiles.com/datafiles/zc3XRzudyWE36ZBJr7PIkkqq0PFIrIBgp4ojqShI/newAnimation.json";;
                if (i==0){
                    gifImg = "https://assets1.lottiefiles.com/datafiles/HN7OcWNnoqje6iXIiZdWzKxvLIbfeCGTmvXmEm1h/data.json";
                }else if (i == 1){
                    gifImg = "https://assets9.lottiefiles.com/datafiles/MUp3wlMDGtoK5FK/data.json";
                }else if (i == 2){
                    gifImg = "https://assets5.lottiefiles.com/datafiles/zc3XRzudyWE36ZBJr7PIkkqq0PFIrIBgp4ojqShI/newAnimation.json";
                }
                i++;
                if (i == 3)
                    i = 0;
                //String gifImg = "http://cdn.trojx.me/blog_raw/lottie_data_origin.json";
                BaseAnimationBean bean1 = new BaseAnimationBean(1, i,  "林喵喵林喵喵林喵喵", "糖果", gifImg, 3800);
                bean1.setTheGiftCount(1);
                //bean1.setTheSendGiftSize(1);
                //bean1.setReceiveGiftCount(new Random().nextInt(30));
                //bean1.setMsgId(System.currentTimeMillis());
                animationLayoutLeft.put(bean1);
            }
        });
        bt_show1 = findViewById(R.id.bt_show1);
        bt_show1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gifImg ="https://assets5.lottiefiles.com/datafiles/zc3XRzudyWE36ZBJr7PIkkqq0PFIrIBgp4ojqShI/newAnimation.json";;
                if (i==0){
                    gifImg = "https://assets1.lottiefiles.com/datafiles/HN7OcWNnoqje6iXIiZdWzKxvLIbfeCGTmvXmEm1h/data.json";
                }else if (i == 1){
                    gifImg = "https://assets9.lottiefiles.com/datafiles/MUp3wlMDGtoK5FK/data.json";
                }else if (i == 2){
                    gifImg = "https://assets5.lottiefiles.com/datafiles/zc3XRzudyWE36ZBJr7PIkkqq0PFIrIBgp4ojqShI/newAnimation.json";
                }
                i++;
                if (i == 3)
                    i = 0;
                //String gifImg = "http://cdn.trojx.me/blog_raw/lottie_data_origin.json";
                BaseAnimationBean bean1 = new BaseAnimationBean(1, i,  "林喵喵林喵喵林喵喵", "糖果", gifImg, 3800);
                bean1.setTheGiftCount(1);
                //bean1.setTheSendGiftSize(1);
                //bean1.setReceiveGiftCount(new Random().nextInt(30));
                //bean1.setMsgId(System.currentTimeMillis());
                animationLayoutLeft.put(bean1);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationLayoutLeft!=null)
            animationLayoutLeft.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animationLayoutLeft != null) {
            animationLayoutLeft.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animationLayoutLeft != null) {
            animationLayoutLeft.onDestroy();
        }
    }
}
