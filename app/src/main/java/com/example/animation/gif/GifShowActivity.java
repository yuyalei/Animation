package com.example.animation.gif;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.animation.R;
import com.example.animation.gif.bean.ReceiveGiftBean;

import java.util.HashMap;

/**
 * Desc:
 * Author: JS-Kylo
 * Created On: 2019/10/31 10:50
 */
public class GifShowActivity extends AppCompatActivity {
    private GiftAnimationLayout giftAnimationLayoutLeft;
    private ReceiveGiftAdapter receiveGiftAdapter;
    private Button bt_send;
    private HashMap<String, ReceiveGiftBean> sendGiftMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_layout);
        giftAnimationLayoutLeft = findViewById(R.id.giftAnimationLayoutLeft);
        bt_send = findViewById(R.id.bt_send);
        receiveGiftAdapter = new ReceiveGiftAdapter(this);

        giftAnimationLayoutLeft.setGiftItemRes(R.layout.gift_animation_item_left);
        giftAnimationLayoutLeft.setGiftAdapter(receiveGiftAdapter);

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gifImg = "http://sta.5yqz2.com/static/avatar/69eea05c82b42834b9236c09f9873016.gif";
                ReceiveGiftBean bean1 = new ReceiveGiftBean(1, 1,  "林喵喵林喵喵林喵喵", "糖果", gifImg, 3800);
                bean1.setTheGiftCount(1);
                bean1.setTheSendGiftSize(1);
                //bean1.setReceiveGiftCount(new Random().nextInt(30));
                bean1.setMsgId(System.currentTimeMillis());

                giftAnimationLayoutLeft.put(bean1);
            }
        });
    }
}
