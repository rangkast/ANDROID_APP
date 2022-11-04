package com.toadstudio.first.toadproject.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.toadstudio.first.toadproject.R;
import com.toadstudio.first.toadproject.Settings.Settings_GridActivity;
import com.toadstudio.first.toadproject.Settings.Settings_GridAdapter;

import java.util.ArrayList;

/**
 * Created by rangkast.jeong on 2018-03-20.
 */

public class GridCustomDialog extends Dialog implements View.OnClickListener{
    public static String TAG = "ToadPrj_GridCustom";
    private GridCustomListener dialogListener;
    private static final int LAYOUT = R.layout.grid_custom_dialog;
    private Context context;
    private TextView cancelTv;
    private TextView searchTv;

    private TextView mtextTitle;
    private TextView mtextQuestion;
    private TextView mtextMessage;

    private String text_title;
    private String text_question;
    private String edit_text;
    private String text_message;
    private int data;

    private ImageView imageView;

    private TextInputEditText makeFolder;

    private static GridView gridView;
    private static Settings_GridAdapter adapter;
    private ArrayList<Integer> imgs = new ArrayList<>();

    private int old_position = -1;
    private int marker_pos = -1;
    Vibrator vibrator;

    public GridCustomDialog(Context context, String text_title, String text_message, String text_question, String edit_text, int data, int marker_pos){
        super(context);
        this.context = context;
        this.text_title = text_title;
        this.data = data;
        this.text_question=text_question;
        this.edit_text = edit_text;
        this.text_message = text_message;
        this.marker_pos = marker_pos;
    }

    public void setDialogListener(GridCustomListener dialogListener){
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        Settings_GridActivity settings_gridActivity = new Settings_GridActivity();
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        searchTv = (TextView) findViewById(R.id.grid_dialog_ok);
        cancelTv = (TextView) findViewById(R.id.grid_dialog_cancel);

        mtextTitle = (TextView)findViewById(R.id.grid_text_title);
        mtextQuestion = (TextView)findViewById(R.id.grid_text_question);
        mtextMessage = (TextView)findViewById(R.id.grid_text_message);

        StringBuilder info = new StringBuilder("");

        mtextTitle.setText(text_title);
        mtextQuestion.setText(text_question);
        mtextMessage.setText(text_message);

        cancelTv.setOnClickListener(this);
        searchTv.setOnClickListener(this);

        imgs = settings_gridActivity.get_data(data);

        gridView = (GridView) findViewById(R.id.grid_custom_dialog);
        adapter = new Settings_GridAdapter(context, R.layout.settings_grid_row, imgs);

        imageView = (ImageView)findViewById(R.id.grid_custom_dialog_image);

        final Integer[] array = imgs.toArray(new Integer[imgs.size()]);

        if (marker_pos > -1)
            imageView.setImageResource(array[marker_pos]);

        makeFolder = (TextInputEditText) findViewById(R.id.make_folder);

        if (text_title.contains("생성") || text_title.contains("수정")) {
            makeFolder.setFocusableInTouchMode(true);
            makeFolder.setClickable(true);
            makeFolder.setFocusable(true);
        }
        /*
        else if (text_title.contains("수정")) {
            makeFolder.setFocusableInTouchMode(false);
            makeFolder.setFocusable(false);
            makeFolder.setClickable(false);
        }
*/
        if (edit_text != null)
            makeFolder.setText(edit_text);

        gridView.setAdapter(adapter);

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


    //            Log.d(TAG, "item clicked" + position);
                vibrator.vibrate(100);
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.pic_dialog);

                ImageView iv = (ImageView) dialog.findViewById(R.id.pic_dialog_img);
                Glide
                        .with(context)
                        .load(array[position])
                        .into(iv);

                dialog.show();

          //      Log.d(TAG, "scale x" + view.getWidth());
         //       Log.d(TAG, "scale y" + view.getHeight());

                Window window = dialog.getWindow();

                int x = (int)(view.getWidth() * 3f);
                int y = (int)(view.getHeight() * 3f);

                window.setLayout(x,y);

                return false;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
       //         Log.d(TAG, "onItemClick" + position);

                //여기서 팝업 띄울 수 있음
                imageView.setImageResource(array[position]);




/*
                if (old_position == i) {
                    view.setBackgroundResource(R.drawable.text_back); //여기서 색칠
                } else if (i > -1) {
                    view.setBackgroundResource(R.drawable.text_back); //여기서 색칠

                    if (old_position < adapterView.getChildCount()) {
                        if (old_position == -1) {
                            Log.d(TAG, "old_position 1 :" + old_position);
                        } else {
                            Log.d(TAG, "old_position 2 :" + old_position);
                            adapterView.getChildAt(old_position).setBackgroundColor(Color.WHITE);
                        }
                    }
                }
                */

                old_position = position;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.grid_dialog_cancel:
                dialogListener.onNegativeClicked();
          //      Toast.makeText(getContext(), "취소 되었습니다", Toast.LENGTH_SHORT).show();
                dismiss();
                break;

            case R.id.grid_dialog_ok:
                try {
                    int status = 0;
                    Integer cursor = old_position;
                    String path = makeFolder.getText().toString();
                    status = dialogListener.onPositiveClicked(cursor, path);
                    //      Log.d(TAG, "return status: " + status);
                    //     dismiss();
                } catch (Exception e) {

                }
                break;
        }
    }

}
