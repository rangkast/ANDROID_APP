package com.toadstudio.first.toadproject.Image;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.toadstudio.first.toadproject.MainActivity;
import com.toadstudio.first.toadproject.R;

/**
 * Created by rangkast.jeong on 2018-03-08.
 */

public class ProgressActivity extends AppCompatActivity {
    public static String TAG = "ToadPrj_Progress";
    private LoadingTh mLoadingTh;
    public boolean mLDThreadRunning = false;
    static int length = 0;
    private ImagesView mView;
    private MainActivity mMain;
    String call_from = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_activity);

        Intent intent = getIntent();
        call_from = intent.getStringExtra("from");
        if (call_from.equals("ImagesView")) {
            mView = new ImagesView();
        } else if (call_from.equals("MainActivity")) {
            mMain = new MainActivity();
        }

        startThread();
        Log.d(TAG, "onCreate: call from(" + call_from + ")");
    }
    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            }
        };

    public class LoadingTh extends Thread {

        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                    mLDThreadRunning = true;
                    if (call_from.equals("ImagesView")) {
                        length = mView.imgLength();
                        Log.d(TAG, "img.length" + length);
                    }
                }
            });

            while (mLDThreadRunning) {
                if (call_from.equals("ImagesView")) {
                    if (mView.imgPosition()) {
                        mView.setEndProgress(1);
                        onFinish();
                    }
                } else if (call_from.equals("MainActivity")) {
                    if (mMain.return_update_status() == 1) {
                        onFinish();
                    }
                }
                try {
                    //          while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(100);   //sleep func이 있는 상태에서 interrupt를 발생시키면 Exception 발생
                    //        }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } finally {
                    //    Log.d(TAG, "thread finally dead");
                }
            }
        }
    }
    public void startThread() {
        Log.d(TAG, "progress thread start");
        mLDThreadRunning = true;
        mLoadingTh = new LoadingTh();
        mLoadingTh.start();
    }

    public void killThread() {
        if (mLoadingTh != null) {
            Log.d(TAG, "progress thread kill");
            mLDThreadRunning = false;
            mLoadingTh.interrupt();
            mLoadingTh = null;
        }
    }

    @Override
    protected void onPause() {
        killThread();
        super.onPause();
    }

    public void onFinish() {
        killThread();
        finish();
    }
}
