package com.toadstudio.first.toadproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by rangkast.jeong on 2018-03-07.
 */

public class LogoActivity extends AppCompatActivity{
    private static final String TAG = "ToadPrj_Logo";
    static int display_width = 0;
    static int display_height = 0;
    private static Context context;
    private static ImageView iv;
    static Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

  //      Log.d(TAG, "onCreate");
        context = getApplicationContext();
        activity = LogoActivity.this;
        context = getApplicationContext();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.logo_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        display_width = dm.widthPixels;
        display_height = dm.heightPixels;


        iv = (ImageView)findViewById(R.id.logo_image);
        //iv.setImageResource(R.drawable.img);


                Glide.get(context).clearMemory();
                Glide.with(context)
                        .load(R.drawable.toad_studio_logo3)
                        .into(iv);



/*
        GlideApp
                .with(LogoActivity.this)
                .load(R.drawable.toad_studio_qhd)
                .thumbnail(0.1f)
                .placeholder(R.drawable.black_color)
                .into(iv);
*/
        startLogo();

    }

    private void startLogo() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //To Do
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("from", "LogoActivity");
                startActivity(intent);

                finish();

            }
        }, 1500);
    }


}
