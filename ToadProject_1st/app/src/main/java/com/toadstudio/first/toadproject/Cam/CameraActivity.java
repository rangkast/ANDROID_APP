package com.toadstudio.first.toadproject.Cam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.toadstudio.first.toadproject.Image.FocusViewPager;
import com.toadstudio.first.toadproject.MainActivity;
import com.toadstudio.first.toadproject.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rangkast.jeong on 2018-02-25.
 */

public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "ToadPrj_CameraActivity";
    static String intent_from = null;
    static Activity activity;
    static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d(TAG, "onCreate");

        final Intent intent = getIntent();
        intent_from = intent.getStringExtra("from");
    //    Log.d(TAG, "intent from: " + intent_from);

        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }
        activity = CameraActivity.this;
        mainActivity = new MainActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
 //       Log.d(TAG, "keyevent " + event.getAction() + " keycode" + keyCode);
        Camera2BasicFragment fragment = new Camera2BasicFragment();
        fragment.OnKeyDown(keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:  //버튼 반응없음(막기)
                return true;
  //          case KeyEvent.KEYCODE_BACK:  //버튼 반응없음(막기)
  //              return true;
            case KeyEvent.KEYCODE_HOME:  //버튼 반응없음(막기)
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                return true;

            case KeyEvent.KEYCODE_VOLUME_UP:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStop() {
    //    Log.d(TAG, "onStop");
        if (intent_from.contains("WidgetMain")) {
            finish();
        }
        super.onStop();
    }


    public void show_recycle_view_by_cam(String path, int id) {
    //    Context context = getBaseContext();
        String r_img[] = {};
        File file = new File(path);
        r_img = file.list();

        Camera2BasicFragment fragment = new Camera2BasicFragment();

        List<String> r_mImg = null;
        r_mImg = new ArrayList();

        r_mImg = fragment.return_mImage();
/*
        for (int i = 0; i < r_img.length; i++) {
            if (r_img[i].contains("."))
                r_mImg.add(r_img[i]);
        }

        Collections.sort(r_mImg);
        Collections.reverse(r_mImg);
*/


        String[] recycle_img = r_mImg.toArray(new String[r_mImg.size()]);
        Intent intent_focus_image = new Intent(activity, FocusViewPager.class);
        intent_focus_image.putExtra("focus_path", path);
        intent_focus_image.putExtra("imgs", recycle_img);
        intent_focus_image.putExtra("id", id);
        intent_focus_image.putExtra("focus_storage", mainActivity.get_storage_choice());
        if (recycle_img.length != 0)
            activity.startActivity(intent_focus_image);
    }

}