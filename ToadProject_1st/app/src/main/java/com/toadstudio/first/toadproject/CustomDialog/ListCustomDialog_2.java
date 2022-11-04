package com.toadstudio.first.toadproject.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.toadstudio.first.toadproject.GMap.MapInfo;
import com.toadstudio.first.toadproject.ListView.ListviewAdapter;
import com.toadstudio.first.toadproject.ListView.Listviewitem;
import com.toadstudio.first.toadproject.R;
import com.toadstudio.first.toadproject.Settings.Settings_GridActivity;

import java.util.ArrayList;

/**
 * Created by rangkast.jeong on 2018-03-20.
 */

public class ListCustomDialog_2 extends Dialog implements View.OnClickListener{
    public static String TAG = "ToadPrj_ListCustom2";
    private ListDialogListener dialogListener;
    private static final int LAYOUT = R.layout.listview_custom_dialog_2;
    private Context context;
    private TextView cancelTv;
    private TextView searchTv;

    private TextView mtextTitle;
    private TextView mtextQuestion;
    private TextView mtextMessage;
    private TextView mtextReply;

    private String text_title;
    private String text_question;
    private String edit_text;
    private String text_message;
    private ArrayList<Listviewitem> data = new ArrayList<>();
    private ArrayList<String> path = new ArrayList<>();
    private ArrayList<Integer> imgs = new ArrayList<>();
    private ImageView imageView;

    private static ListView listView;
    private static ListviewAdapter listviewAdapter;
    private Listviewitem items;

    private String marker;
    Vibrator vibrator;

    int status = 0;

    private int pos = -1;

    public ListCustomDialog_2(Context context, String text_title, String text_message, String text_question, ArrayList<String> path, String marker){
        super(context);
        this.context = context;
        this.text_title = text_title;
        this.path = path;
        this.text_question=text_question;
        this.text_message = text_message;
        this.marker = marker;
    }

    public void setDialogListener(ListDialogListener dialogListener){
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        searchTv = (TextView) findViewById(R.id.list_dialog_ok_2);
        cancelTv = (TextView) findViewById(R.id.list_dialog_cancel_2);

        mtextTitle = (TextView)findViewById(R.id.list_text_title_2);
        mtextQuestion = (TextView)findViewById(R.id.list_text_question_2);
        mtextMessage = (TextView)findViewById(R.id.list_text_message_2);
        mtextReply = (TextView)findViewById(R.id.list_text_reply_2);

        mtextTitle.setText(text_title);
        mtextQuestion.setText(text_question);
        mtextMessage.setText(text_message);

        cancelTv.setOnClickListener(this);
        searchTv.setOnClickListener(this);


        imageView = (ImageView)findViewById(R.id.list_custom_dialog_image_2);

        listView = (ListView)findViewById(R.id.list_custom_dialog_2);

        for (int i = 0; i < path.size(); i++) {
            items = new Listviewitem(marker,
                    "0",
                    path.get(i),
                    "0",
                    0,
                    -1);
            data.add(items);
        }

        listviewAdapter = new ListviewAdapter(context, R.layout.listview_item, data);
        listView.setAdapter(listviewAdapter);

        final Integer[] array = imgs.toArray(new Integer[imgs.size()]);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //          Log.d(TAG, "onItemClick" + position);
                pos = position;

                if (pos > -1) {
                    mtextReply.setText(path.get(pos));

                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.list_dialog_cancel_2:

                dialogListener.onNegativeClicked(status);

                dismiss();
                break;

            case R.id.list_dialog_ok_2:
                String path = null;
                int pos_of_mapinfo = -1;
                if (pos < 0) {

                } else {
                    path = mtextReply.getText().toString();
                }
                status = pos;

                status = dialogListener.onPositiveClicked(status, path);

                break;
        }
    }

}
