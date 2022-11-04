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

public class ListCustomDialog extends Dialog implements View.OnClickListener{
    public static String TAG = "ToadPrj_ListCustom";
    private ListDialogListener dialogListener;
    private static final int LAYOUT = R.layout.listview_custom_dialog;
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
    private ArrayList<MapInfo> mMapinfo = new ArrayList<>();
    private ArrayList<Integer> imgs = new ArrayList<>();
    private ImageView imageView;

    private static ListView listView;
    private static ListviewAdapter listviewAdapter;
    private Listviewitem items;

    private String marker;
    Vibrator vibrator;

    int status = 0;

    private int pos = -1;

    public ListCustomDialog(Context context, String text_title, String text_message, String text_question, ArrayList<MapInfo> mapinfo, String marker){
        super(context);
        this.context = context;
        this.text_title = text_title;
        this.mMapinfo = mapinfo;
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
        Settings_GridActivity settings_gridActivity = new Settings_GridActivity();
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        searchTv = (TextView) findViewById(R.id.list_dialog_ok);
        cancelTv = (TextView) findViewById(R.id.list_dialog_cancel);

        mtextTitle = (TextView)findViewById(R.id.list_text_title);
        mtextQuestion = (TextView)findViewById(R.id.list_text_question);
        mtextMessage = (TextView)findViewById(R.id.list_text_message);
        mtextReply = (TextView)findViewById(R.id.list_text_reply);

        mtextTitle.setText(text_title);
        mtextQuestion.setText(text_question);
        mtextMessage.setText(text_message);

        cancelTv.setOnClickListener(this);
        searchTv.setOnClickListener(this);

        imgs = settings_gridActivity.get_data(Integer.parseInt(marker));

        imageView = (ImageView)findViewById(R.id.list_custom_dialog_image);

        listView = (ListView)findViewById(R.id.list_custom_dialog);

        for (int i = 0; i < mMapinfo.size(); i++) {
            items = new Listviewitem(marker,
                    mMapinfo.get(i).getdata(),
                    mMapinfo.get(i).getfolder(),
                    mMapinfo.get(i).getreserved(),
                    Integer.parseInt(mMapinfo.get(i).getFav()),
                    Integer.parseInt(mMapinfo.get(i).getStorage()));
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
                    mtextReply.setText("("+ mMapinfo.get(pos).getfolder() + ")"
                            + "\n폴더가 선택되었습니다."+
                            "\n\n합치겠습니까?");
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.list_dialog_cancel:

                dialogListener.onNegativeClicked(status);

                dismiss();
                break;

            case R.id.list_dialog_ok:
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
