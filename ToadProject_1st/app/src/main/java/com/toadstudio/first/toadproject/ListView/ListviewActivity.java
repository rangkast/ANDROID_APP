package com.toadstudio.first.toadproject.ListView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.toadstudio.first.toadproject.GMap.MapInfo;
import com.toadstudio.first.toadproject.GMap.SettingInfo;
import com.toadstudio.first.toadproject.Image.ImagesView;
import com.toadstudio.first.toadproject.R;
import com.toadstudio.first.toadproject.Settings.Settings_GridActivity;

import java.util.ArrayList;

/**
 * Created by rangkast.jeong on 2018-03-15.
 */

public class ListviewActivity extends AppCompatActivity{
    public static String TAG = "ToadPrj_ListView";
    ArrayList<Listviewitem> data= new ArrayList<>();
    Listviewitem items;
    ArrayList<MapInfo> mapInfos;
    ArrayList<SettingInfo> mSettinginfo;
    private ArrayList<Integer> custom_marker;
    private  static Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        final ListView listView=(ListView)findViewById(R.id.listview);
        final Intent intent = getIntent();
        mapInfos = (ArrayList<MapInfo>)getIntent().getSerializableExtra("mMapInfo");
        mSettinginfo = (ArrayList<SettingInfo>)getIntent().getSerializableExtra("mSetting");

        for (int i = 0; i <mapInfos.size(); i++) {
            items = new Listviewitem(mSettinginfo.get(0).getMarker(), mapInfos.get(i).getdata(), mapInfos.get(i).getfolder(), mapInfos.get(i).getreserved(),Integer.parseInt(mapInfos.get(i).getFav()),
                    Integer.parseInt(mapInfos.get(i).getStorage()));
            data.add(items);
        }

        ListviewAdapter adapter=new ListviewAdapter(this, R.layout.listview_item, data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       //         Log.d(TAG, "onItemClick " + i);
                Intent intent_toad_project = new Intent(getBaseContext(), ImagesView.class);
                intent_toad_project.putExtra("f_path", mapInfos.get(i).getfolder());
                intent_toad_project.putExtra("id", i);
                intent_toad_project.putExtra("enable", 0);
                intent_toad_project.putExtra("mMapInfo", mapInfos);
                intent_toad_project.putExtra("mSetting", mSettinginfo);
                startActivity(intent_toad_project);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListviewActivity.this);
                Settings_GridActivity settings_gridActivity = new Settings_GridActivity();

                vibrator.vibrate(100);

           //     Log.d(TAG, "onMapLongClick");
                // Setting Dialog Title
                alertDialog.setTitle("폴더 정보");

                StringBuilder str = new StringBuilder("");
                str.append("폴더경로:" + mapInfos.get(i).getfolder() + "\n");
                str.append("경도:" + mapInfos.get(i).getlatitude() + " 위도:" + mapInfos.get(i).getlongitude() + "\n");
                str.append("사진 수:" + mapInfos.get(i).getreserved() + "장");

                // Setting Dialog Message
                alertDialog.setMessage(str.toString());

                custom_marker = settings_gridActivity.get_data(Integer.parseInt(mSettinginfo.get(0).getMarker()));

                // Setting Icon to Dialog
                alertDialog.setIcon(custom_marker.get(Integer.parseInt(mapInfos.get(i).getdata())));

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
/*
                        Toast.makeText(getApplicationContext(), "아직 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
*/
                        // Write your code here to invoke YES event
                        //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                    }
                });
/*
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        //        Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        //       dialog.cancel();
                    }
                });
*/
                // Showing Alert Message
                alertDialog.show();

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
   //     Log.d(TAG, "onResume");
    }
}
