package com.toadstudio.first.toadproject.Image;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.toadstudio.first.toadproject.FileController;
import com.toadstudio.first.toadproject.MainActivity;
import com.toadstudio.first.toadproject.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.toadstudio.first.toadproject.Cam.Camera2BasicFragment.UPDATE_VIEW;

/**
 * Created by rangkast.jeong on 2018-03-06.
 */

public class FocusViewPager extends AppCompatActivity implements View.OnClickListener{
    static NewViewPager pager;
    public static String TAG = "ToadPrj_FocusViewPager";
    static String path = null;
    static String img[] = {};
    static Activity activity;
    static int pic_id = 0;
    private int rotate = 0; //default
    static int focus_storage = -1;
    private static CustomAdapter adapter;
    private static EditText editText;
    private static TextView textView;
    private ImageButton okBtn, cancelBtn, camBtn, rotateBtn;
    private static int thisID = 0;
    private static int prev_ID = -1;
    private MainActivity mainActivity;
    private ImagesView imagesView;
    private FileController fileController;

    final static int WRITE_DB = 1;
    final static int READ_DB = 2;
    final static int DELETE_DB = 3;

    static final int WRITE_DB_RES = 2;

    static final int WRITE_OK = 0;
    static final int WRITE_FAIL = 1;
    static final int CLOSE_APP = 3;
    static final int DELETE_EXTERNAL = 4;

    static final int UPDATE_VIEW_EDIT_TEXT = 0;
    static final int UPDATE_VIEW_PAGER = 1;


    private static String strToPrint = null;
    static List<String> images;

    private String external_file_name = null;
    private String to_external_img_path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = FocusViewPager.this;

        mainActivity = new MainActivity();
        imagesView = new ImagesView();
        fileController = new FileController();
        Intent intent = getIntent() ;
        path = intent.getStringExtra("focus_path");
        img = intent.getStringArrayExtra("imgs");
        pic_id = intent.getIntExtra("id", 0);
        focus_storage = intent.getIntExtra("focus_storage", 0);
    //    Log.d(TAG, "img.length:" + img.length + " basePath:" + path + " pic_id:" + pic_id);
   //     Log.d(TAG, "focus_path: " + path);

        setContentView(R.layout.focus_viewpager);

    //    pager = new NewViewPager(activity);

        pager= (NewViewPager) findViewById(R.id.focus_pager);

        //ViewPager에 설정할 Adapter 객체 생성
        //ListView에서 사용하는 Adapter와 같은 역할.
        //다만. ViewPager로 스크롤 될 수 있도록 되어 있다는 것이 다름
        //PagerAdapter를 상속받은 CustomAdapter 객체 생성
        //CustomAdapter에게 LayoutInflater 객체 전달

        adapter = new CustomAdapter(activity, pager, img, path, pic_id, rotate);
        //      adapter.setPath(path);
        //ViewPager에 Adapter 설정
        pager.setAdapter(adapter);

    //    Log.d(TAG, "set pic_id: " + pic_id);
        pager.setCurrentItem(pic_id, true);

        editText = (EditText)findViewById(R.id.focus_edit);
        textView = (TextView)findViewById(R.id.focus_text);
        MemoDBTask task = new MemoDBTask(this, READ_DB, -1);
        task.execute();

        okBtn = (ImageButton)findViewById(R.id.focus_ok);
        okBtn.setOnClickListener(this);
        cancelBtn = (ImageButton)findViewById(R.id.focus_delete);
        cancelBtn.setOnClickListener(this);
        camBtn = (ImageButton)findViewById(R.id.focus_camera);
        camBtn.setOnClickListener(this);
        rotateBtn = (ImageButton)findViewById(R.id.focus_rotate);
        rotateBtn.setOnClickListener(this);

        thisID = pic_id;
   //     prev_ID = pic_id;
        set_edit(thisID);
    }

    static void set_edit(int id) {
        try {
            textView.setText("사진 경로: " + path + "\n" +
                    "사진 제목: " + img[id]);

            thisID = id;



            FocusViewPager focusViewPager = new FocusViewPager();
            focusViewPager.run_activiy(activity, READ_DB);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int run_activiy(Context context, int control) {
        switch (control) {
            case WRITE_DB:
                break;
            case READ_DB:
                MemoDBTask task = new MemoDBTask(this, READ_DB, -1);
                task.execute();
                break;
        }

        return 0;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.focus_rotate:


                if (prev_ID != thisID) {
                    prev_ID = thisID;
                    rotate = 90; //90 start

                } else {
                    rotate += 90;
                    if (rotate == 360)
                        rotate = 0;

                }
        //        Log.d(TAG, "focus_rotate btn clicked: ID<" + thisID + ">" + " rotate: " + rotate);

                try {
                    mHandler.sendMessage(mHandler.obtainMessage(
                            UPDATE_VIEW,
                            UPDATE_VIEW_PAGER, 0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.focus_ok:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("메모를 저장 하시겠습니까?");
                alertDialog.setIcon(R.drawable.toad_app_icon_v4);

                alertDialog.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MemoDBTask task_write = new MemoDBTask(v.getContext(), WRITE_DB, -1);
                        task_write.execute();
                        dialog.cancel();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
         //               Toast.makeText(activity, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                if (editText.getText().toString().length() > 0) {
                    alertDialog.show();
                } else {
                    mHandler.sendMessage(mHandler.obtainMessage(
                            WRITE_DB_RES,
                            WRITE_FAIL, 0));

                }
                break;
            case R.id.focus_delete:
                AlertDialog.Builder alertDialog_delete = new AlertDialog.Builder(activity);
                alertDialog_delete.setTitle("메모를 삭제 하시겠습니까?");
                alertDialog_delete.setIcon(R.drawable.toad_app_icon_v4);

                alertDialog_delete.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            MemoDBTask task_delete = new MemoDBTask(v.getContext(), DELETE_DB, -1);
                            task_delete.execute();
                            Toast.makeText(getApplicationContext(), "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.cancel();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog_delete.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
             //           Toast.makeText(activity, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                if (editText.getText().toString().length() > 0) {
                    alertDialog_delete.show();
                } else {
                    mHandler.sendMessage(mHandler.obtainMessage(
                            WRITE_DB_RES,
                            WRITE_FAIL, 0));

                }
                break;

            case R.id.focus_camera:

                AlertDialog.Builder alertDialog_delete_pic = new AlertDialog.Builder(activity);
                alertDialog_delete_pic.setTitle("사진을 삭제 하시겠습니까?");
                alertDialog_delete_pic.setIcon(R.drawable.toad_app_icon_v4);

                alertDialog_delete_pic.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            if (editText.getText().toString().length() > 0) {
                                MemoDBTask task_delete = new MemoDBTask(v.getContext(), DELETE_DB, -1);
                                task_delete.execute();
                            }

                            DeletePic task = new DeletePic(getBaseContext(), thisID);
                            task.execute(getBaseContext(), thisID);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getApplicationContext(), "삭제 되었습니다.", Toast.LENGTH_SHORT).show();

                        dialog.cancel();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog_delete_pic.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
              //          Toast.makeText(activity, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                if (editText.getText().toString().length() > 0) {
                    alertDialog_delete_pic.setTitle("사진과 메모를 삭제 하시겠습니까?");
                    alertDialog_delete_pic.show();
                } else {
                    alertDialog_delete_pic.setTitle("사진을 삭제 하시겠습니까?");
                    alertDialog_delete_pic.show();
                }

                break;

        }
    }

    public class DeletePic extends AsyncTask {
        private int id;
        private Context mContext;

        public DeletePic(Context context, int Id) {
            id = Id;
            mContext = context;
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            String file_path = null;

            try {
                images = new ArrayList<>();
                //     images = Arrays.asList(img);

                for (int i = 0; i < img.length; i++) {
                    images.add(i, img[i]);
                    //       Log.d(TAG, "images : " + images.get(i));
                }

                if (focus_storage == imagesView.INTERNAL_STORAGE) {
                    file_path = path + File.separator + images.get(thisID);
                    //       Log.d(TAG, "path to delete " + file_path);
                    File mFile = new File(file_path);

                    if (mFile.delete() == true) {

                        //       Log.d(TAG, "images length " + images.size());
                        images.remove(thisID);

                        img = images.toArray(new String[images.size()]);

                        if (thisID >= img.length) {
                            thisID = img.length - 1;
                        }

                        //     Log.d(TAG, "changed thisID " + thisID + "img length " + img.length);

                        mHandler.sendMessage(mHandler.obtainMessage(
                                UPDATE_VIEW,
                                UPDATE_VIEW_PAGER, 0));

                        //        Camera2BasicFragment camera2BasicFragment = new Camera2BasicFragment();
                        //         camera2BasicFragment.check_delete_status(1);

                    } else {
                        Toast.makeText(getApplicationContext(), "삭제 실패 했습니다..", Toast.LENGTH_SHORT).show();
                    }
                } else if (focus_storage == imagesView.EXTERNAL_STORAGE) {
                    to_external_img_path = path.replace(mainActivity.get_sdcard_path_header(), "");
                    external_file_name = images.get(thisID);

          //          Log.d(TAG, "to_external_img_path: " + to_external_img_path + " external_file_name: " + images.get(thisID));

                    mHandler.sendEmptyMessageDelayed(DELETE_EXTERNAL, 0);

                }
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

    public void extern_memoDB_ctrl(int ctrl, int id) {
      //  Log.d(TAG, "extern_memoDB_ctrl " + ctrl);
        switch (ctrl) {
            case WRITE_DB:
                MemoDBTask task_write = new MemoDBTask(this, WRITE_DB, id);
                task_write.execute();
                break;
            case READ_DB:
                break;
            case DELETE_DB:
                MemoDBTask task_delete = new MemoDBTask(this, DELETE_DB, id);
                task_delete.execute();
                break;
        }
    }

    public class MemoDBTask extends AsyncTask {
        private Context mContext;
        private int mMode;
        private int mId;
        public MemoDBTask(Context context, int mode, int id) {
            mContext = context;
            mMode = mode;
            mId = id;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
  //          Log.d(TAG, "memodDBTask doInBackground + " + mMode);
            switch (mMode) {
                case WRITE_DB:
                    if (mId > -1) {
                        thisID = mId;
                    }
                    try {
                            mainActivity.writeMemoDB(img[thisID], path, editText.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        strToPrint = editText.getText().toString();
              //          Log.d(TAG, "strToPrint(W) " +strToPrint);
                        mHandler.sendMessage(mHandler.obtainMessage(
                                WRITE_DB_RES,
                                WRITE_OK, 0));
                    }
                    break;
                case READ_DB:
                    try {
                        String str = mainActivity.readMemoDB(path, img[thisID]);
                        strToPrint = str;
                    } catch (Exception e) {
                        strToPrint = null;
                        e.printStackTrace();
                    } finally {
               //         Log.d(TAG, "strToPrint(R) " +strToPrint);
                        mHandler.sendMessage(mHandler.obtainMessage(
                                UPDATE_VIEW,
                                UPDATE_VIEW_EDIT_TEXT, 0));
                    }



                    break;

                case DELETE_DB:
                    if (mId > -1) {
                        thisID = mId;
                    }

                    try {
                        mainActivity.deleteMemoDB(path, img[thisID]);
                        strToPrint = null;

                        mHandler.sendMessage(mHandler.obtainMessage(
                                UPDATE_VIEW,
                                UPDATE_VIEW_EDIT_TEXT, 0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }
                    break;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {

                case UPDATE_VIEW:
                    switch (msg.arg1) {
                        case UPDATE_VIEW_EDIT_TEXT:
                            if (strToPrint != null) {
                                editText.setText(strToPrint);
                            } else {
                        /*
                        editText.setText("이미지 basepath: " + path + "\n" +
                                "사진id: " + thisID + " (" + img[thisID] + ")");
                         */
                                editText.setText("");
                                editText.setHint("여기에 메모를 해보세요.");
                            }
                            break;
                        case UPDATE_VIEW_PAGER:

                            adapter = new CustomAdapter(activity, pager, img, path, thisID, rotate);
                            //      adapter.setPath(path);
                            //ViewPager에 Adapter 설정
                            pager.setAdapter(adapter);
                            pager.setCurrentItem(thisID, true);

                            adapter.notifyDataSetChanged();

                            if (img.length == 0)
                                mHandler.sendEmptyMessageDelayed(CLOSE_APP, 10);
                            break;
                    }
                    break;

                case WRITE_DB_RES:
                    switch (msg.arg1) {
                        case WRITE_OK:
                            Toast.makeText(getApplicationContext(), "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                            break;
                        case WRITE_FAIL:
                            Toast.makeText(getApplicationContext(), "메모가 없습니다.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);

                    break;

                case CLOSE_APP:

                    String edit_to_text = editText.getText().toString();
           //         Log.d(TAG, "edit (" + edit_to_text + ")" + " strToPrint (" + strToPrint + ")");
                    if (edit_to_text.equals(strToPrint)) {
                  //      Log.d(TAG, "same text");
                        onStop();
                        finish();
                    } else {
                        if (strToPrint == null && edit_to_text.length() == 0) {
                       //     Log.d(TAG, "no change");
                            onStop();
                            finish();
                        } else {
                        //    Log.d(TAG, "different text");


                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(FocusViewPager.this);

                            alertDialog.setTitle("저장되지 않은 메모가 있습니다.");

                            // Setting Dialog Message
                            alertDialog.setMessage("\n그래도 종료 하시겠습니까?");

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.toad_app_icon_v4);

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    onStop();
                                    finish();
                                }
                            });

                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            alertDialog.show();

                        }
                    }

                    break;
                case DELETE_EXTERNAL:

                    if (fileController.file_controller(mainActivity.return_mainActivity_context(),mainActivity.get_main_path(),  to_external_img_path, null, external_file_name, fileController.DELETE_FILE) == 0) {


                        //       Log.d(TAG, "images length " + images.size());
                        images.remove(thisID);

                        img = images.toArray(new String[images.size()]);

                        if (thisID >= img.length) {
                            thisID = img.length - 1;
                        }

                        //     Log.d(TAG, "changed thisID " + thisID + "img length " + img.length);

                        mHandler.sendMessage(mHandler.obtainMessage(
                                UPDATE_VIEW,
                                UPDATE_VIEW_PAGER, 0));

                        //        Camera2BasicFragment camera2BasicFragment = new Camera2BasicFragment();
                        //         camera2BasicFragment.check_delete_status(1);

                    } else {
                        Toast.makeText(getApplicationContext(), "삭제 실패 했습니다..", Toast.LENGTH_SHORT).show();
                    }

                        break;
            }
            super.handleMessage(msg);
        }
    };

    private int back_key_pressed = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
   //     Log.d(TAG, "keyevent " + event.getAction() + " keycode" + keyCode);

        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                break;
            case KeyEvent.KEYCODE_HOME:
                break;
            case KeyEvent.KEYCODE_BACK:
                back_key_pressed = 1;
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
    //    Log.d(TAG, "onBackPressed");

            if (back_key_pressed == 1) {
                mHandler.sendEmptyMessageDelayed(CLOSE_APP, 0);
            } else {
                super.onBackPressed();
            }
    }

}




