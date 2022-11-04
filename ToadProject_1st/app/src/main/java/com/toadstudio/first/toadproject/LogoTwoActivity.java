package com.toadstudio.first.toadproject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by rangkast.jeong on 2018-03-07.
 */

public class LogoTwoActivity extends AppCompatActivity{
    private static final String TAG = "ToadPrj_LogoTwo";
    static int display_width = 0;
    static int display_height = 0;
    private static Context context;
    private static ImageView iv;
    static Activity activity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     //   Log.d(TAG, "onCreate");
        context = getApplicationContext();
        activity = LogoTwoActivity.this;

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.logo_two_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        display_width = dm.widthPixels;
        display_height = dm.heightPixels;


        iv = (ImageView)findViewById(R.id.logo_image_two);
        //iv.setImageResource(R.drawable.img);


        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearMemory();
                Glide.with(context)
                        .load(R.drawable.toad_studio_loading)
                        .into(iv);
            }
        });

//       mHandler.sendEmptyMessageDelayed(UPDATE_UI, 0);

        startLogo();

    }

    private void startLogo() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
/*
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("from", "LogoActivity");
                startActivity(intent);
*/
                Glide.get(context).clearMemory();
                finish();

            }
        }, 1500);
    }

/*
  static final int UPDATE_UI = 0;
  Handler mHandler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case UPDATE_UI:
          GlideTask glideTask = new GlideTask();
          glideTask.execute();
          break;
      }

      super.handleMessage(msg);
    }
  };

  private class GlideTask extends AsyncTask {

    public GlideTask() {
    }

    @Override
    protected Object doInBackground(Object[] objects) {
      try {
         Log.d(TAG, "GlideTask");

        Glide.get(context).clearMemory();
        Glide
                .with(context)
                .load(R.drawable.toadgif)
                .into(iv);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }
  }
*/
}
