package com.twitter.dpr.event;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class SplashSports extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3500;
    ImageView image;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sports);
        image=(ImageView) findViewById(R.id.imgLogo);
        switch (Test1.gethash1) {
            case "sports":

                image.setImageResource(R.drawable.sports);
                break;
            case "politics":

                image.setImageResource(R.drawable.politics);
                break;
            case "business":

                image.setImageResource(R.drawable.business);
                break;
            case "technology":

                image.setImageResource(R.drawable.tech);
                break;
            case "food":

                image.setImageResource(R.drawable.food);
                break;
            case "tourism":

                image.setImageResource(R.drawable.tourism);
                break;
            case "entertainment":

                image.setImageResource(R.drawable.entertain);
                break;
            case "health":

                image.setImageResource(R.drawable.health);
                break;
            case "fashion":

                image.setImageResource(R.drawable.fashion);
                break;
            case "event":

                image.setImageResource(R.drawable.event);
                break;
        }
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashSports.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}