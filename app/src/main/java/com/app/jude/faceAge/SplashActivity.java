package com.app.jude.faceAge;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    ImageView ivTop,ivBot,logo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivTop = findViewById(R.id.topView);
        ivBot = findViewById(R.id.botView);
        logo = findViewById(R.id.logo);

        animateViews();



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);

            }
        }, 2000);


    }

    private void animateViews(){
        ObjectAnimator animationTop = ObjectAnimator.ofFloat(ivTop, "translationY", 500f);
        animationTop.setDuration(2000);
        animationTop.start();

        ObjectAnimator animationBot = ObjectAnimator.ofFloat(ivBot, "translationY", -500f);
        animationBot.setDuration(2000);
        animationBot.start();

        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
        animation1.setDuration(2000);
        //animation1.setStartOffset(5000);
        animation1.setFillAfter(true);
        logo.startAnimation(animation1);
    }

}
