package com.example.animation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.animation.gif.GifShowActivity;
import com.example.animation.gif.GiftAnimationLayout;
import com.example.animation.gif.ReceiveGiftAdapter;
import com.example.animation.gif.bean.ReceiveGiftBean;
import com.example.animation.lottie.LottieShowActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Button bt_send,bt_send1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_send = findViewById(R.id.bt_send);
        bt_send1 = findViewById(R.id.bt_send1);
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GifShowActivity.class));
            }
        });
        bt_send1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LottieShowActivity.class));
            }
        });
    }
}
