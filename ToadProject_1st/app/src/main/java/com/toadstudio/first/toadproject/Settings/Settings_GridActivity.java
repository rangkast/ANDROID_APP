package com.toadstudio.first.toadproject.Settings;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.toadstudio.first.toadproject.R;

import java.util.ArrayList;

/**
 * Created by rangkast.jeong on 2018-03-19.
 */

public class Settings_GridActivity extends AppCompatActivity {
    public static String TAG = "ToadPrj_SettingsGrid";
    private static GridView gridView;
    private static Settings_GridAdapter adapter;
    private ArrayList<Integer> imgs = new ArrayList<>();
    private static Context mcontext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_marker_grid);

        Intent intent = getIntent();
        mcontext = Settings_GridActivity.this;

        imgs = get_data(intent.getIntExtra("data", 0));
        gridView = (GridView) findViewById(R.id.gridView_marker);
        adapter = new Settings_GridAdapter(this, R.layout.settings_grid_row, imgs);
        gridView.setAdapter(adapter);

        final Integer[] array = imgs.toArray(new Integer[imgs.size()]);
/*
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

           //     Log.d(TAG, "item clicked" + position);
                Dialog dialog = new Dialog(mcontext);
                dialog.setContentView(R.layout.pic_dialog);

                ImageView iv = (ImageView) dialog.findViewById(R.id.pic_dialog_img);
                Glide
                        .with(getBaseContext())
                        .load(array[position])
                        .into(iv);

                dialog.show();

         //       Log.d(TAG, "scale x" + view.getWidth());
        //        Log.d(TAG, "scale y" + view.getHeight());

                Window window = dialog.getWindow();

                int x = (int)(view.getWidth() * 4f);
                int y = (int)(view.getHeight() * 4f);

                window.setLayout(x,y);

            }
        });
*/
    }

    private final int DEFAULT_MARKER = 0;
    private final int CHAR_MARKER = 1;
    private final int FLAG_MARKER = 2;
    private final int START_MARKER = 3;
    private final int COFFEE_MARKER = 4;

    public ArrayList<Integer> get_data (int data) {
        ArrayList<Integer> data_list = new ArrayList<>();
   //     Log.d(TAG, "data:" + data);
        switch (data) {
            case DEFAULT_MARKER:
                data_list.add(R.drawable.marker_magenta_full);
                data_list.add(R.drawable.marker_magenta);
                data_list.add(R.drawable.marker_darkblue_full);
                data_list.add(R.drawable.marker_darkblue);
                data_list.add(R.drawable.marker_gray_full);
                data_list.add(R.drawable.marker_gray);
                data_list.add(R.drawable.marker_green_full);
                data_list.add(R.drawable.marker_green);
                data_list.add(R.drawable.marker_purple_full);
                data_list.add(R.drawable.marker_purple);
                data_list.add(R.drawable.marker_red_full);
                data_list.add(R.drawable.marker_red);
                data_list.add(R.drawable.marker_sky_full);
                data_list.add(R.drawable.marker_sky);
                data_list.add(R.drawable.marker_yellow_full);
                data_list.add(R.drawable.marker_yellow);
                data_list.add(R.drawable.marker_transparent);
                data_list.add(R.drawable.marker_error);
                data_list.add(R.drawable.marker_angry);
                data_list.add(R.drawable.marker_happy);
                data_list.add(R.drawable.marker_sad);
                data_list.add(R.drawable.marker_love);

                data_list.add(R.drawable.marker_angry_1);
                data_list.add(R.drawable.marker_darkblue_1);
                data_list.add(R.drawable.marker_darkblue_full_1);
                data_list.add(R.drawable.marker_error_1);
                data_list.add(R.drawable.marker_error_full_1);
                data_list.add(R.drawable.marker_gray_1);
                data_list.add(R.drawable.marker_gray_full_1);
                data_list.add(R.drawable.marker_green_1);
                data_list.add(R.drawable.marker_green_full_1);
                data_list.add(R.drawable.marker_happy_1);
                data_list.add(R.drawable.marker_love_1);
                data_list.add(R.drawable.marker_magenta_1);
                data_list.add(R.drawable.marker_magenta_full_1);
                data_list.add(R.drawable.marker_purple_1);
                data_list.add(R.drawable.marker_purple_full_1);
                data_list.add(R.drawable.marker_red_1);
                data_list.add(R.drawable.marker_red_full_1);
                data_list.add(R.drawable.marker_sad_1);
                data_list.add(R.drawable.marker_sky_1);
                data_list.add(R.drawable.marker_sky_full_1);
                data_list.add(R.drawable.marker_transparent_1);
                data_list.add(R.drawable.marker_transparent_full_1);
                data_list.add(R.drawable.marker_yellow_1);
                data_list.add(R.drawable.marker_yellow_full_1);


                break;


            case CHAR_MARKER:
                data_list.add(R.drawable.c_cat1);
                data_list.add(R.drawable.c_cat1_1);
                data_list.add(R.drawable.c_cat2);
                data_list.add(R.drawable.c_cat2_1);
                data_list.add(R.drawable.c_cat3);
                data_list.add(R.drawable.c_cat3_1);
                data_list.add(R.drawable.c_cat4);
                data_list.add(R.drawable.c_cat4_1);
                data_list.add(R.drawable.c_cat5);
                data_list.add(R.drawable.c_cat5_1);
                data_list.add(R.drawable.c_cat6);
                data_list.add(R.drawable.c_cat6_1);
                data_list.add(R.drawable.c_cat7);
                data_list.add(R.drawable.c_cat7_1);
                data_list.add(R.drawable.c_cat8);
                data_list.add(R.drawable.c_cat8_1);

                data_list.add(R.drawable.marker_puppy);
                data_list.add(R.drawable.marker_puppy_1);
                data_list.add(R.drawable.marker_puppy2);
                data_list.add(R.drawable.marker_puppy2_1);
                data_list.add(R.drawable.marker_puppy3);
                data_list.add(R.drawable.marker_puppy3_1);
                data_list.add(R.drawable.marker_puppy4);
                data_list.add(R.drawable.marker_puppy4_1);
                data_list.add(R.drawable.marker_puppy5);
                data_list.add(R.drawable.marker_puppy5_1);
                data_list.add(R.drawable.marker_puppy6);
                data_list.add(R.drawable.marker_puppy6_1);
                data_list.add(R.drawable.marker_puppy7);
                data_list.add(R.drawable.marker_puppy7_1);
                data_list.add(R.drawable.marker_puppy8);
                data_list.add(R.drawable.marker_puppy8_1);



                break;


            case FLAG_MARKER:
                data_list.add(R.drawable.f_kor);
                data_list.add(R.drawable.f_argentina);
                data_list.add(R.drawable.f_austraillia);
                data_list.add(R.drawable.f_belgium);
                data_list.add(R.drawable.f_brazil);
                data_list.add(R.drawable.f_canada);
                data_list.add(R.drawable.f_chez);
                data_list.add(R.drawable.f_china);
                data_list.add(R.drawable.f_croatia);
                data_list.add(R.drawable.f_denmark);
                data_list.add(R.drawable.f_egypt);
                data_list.add(R.drawable.f_england);
                data_list.add(R.drawable.f_france);
                data_list.add(R.drawable.f_ger);
                data_list.add(R.drawable.f_greece);
                data_list.add(R.drawable.f_hongkong);
                data_list.add(R.drawable.f_hungary);
                data_list.add(R.drawable.f_indi);
                data_list.add(R.drawable.f_indonesia);
                data_list.add(R.drawable.f_iran);
                data_list.add(R.drawable.f_italy);
                data_list.add(R.drawable.f_japan);
                data_list.add(R.drawable.f_kambodia);
                data_list.add(R.drawable.f_laos);
                data_list.add(R.drawable.f_malaysia);
                data_list.add(R.drawable.f_mexico);
                data_list.add(R.drawable.f_nepal);
                data_list.add(R.drawable.f_net);
                data_list.add(R.drawable.f_newzil);
                data_list.add(R.drawable.f_ngr);
                data_list.add(R.drawable.f_norway);
                data_list.add(R.drawable.f_ostria);
                data_list.add(R.drawable.f_philipin);
                data_list.add(R.drawable.f_piland);
                data_list.add(R.drawable.f_polland);
                data_list.add(R.drawable.f_portugal);
                data_list.add(R.drawable.f_russia);
                data_list.add(R.drawable.f_safr);
                data_list.add(R.drawable.f_saudi);
                data_list.add(R.drawable.f_singapore);
                data_list.add(R.drawable.f_spain);
                data_list.add(R.drawable.f_sweden);
                data_list.add(R.drawable.f_swiss);
                data_list.add(R.drawable.f_taipei);
                data_list.add(R.drawable.f_turkey);
                data_list.add(R.drawable.f_uae);
                data_list.add(R.drawable.f_ukraine);
                data_list.add(R.drawable.f_usa);
                data_list.add(R.drawable.f_vent);
                break;
            case START_MARKER:
                data_list.add(R.drawable.star_1);
                data_list.add(R.drawable.star_1_1);
                data_list.add(R.drawable.star_2);
                data_list.add(R.drawable.star_2_1);
                data_list.add(R.drawable.star_3);
                data_list.add(R.drawable.star_3_1);
                data_list.add(R.drawable.star_4);
                data_list.add(R.drawable.star_4_1);
                data_list.add(R.drawable.star_5);
                data_list.add(R.drawable.star_5_1);
                data_list.add(R.drawable.star_6);
                data_list.add(R.drawable.star_6_1);
                data_list.add(R.drawable.star_7);
                data_list.add(R.drawable.star_7_1);
                data_list.add(R.drawable.star_8);
                data_list.add(R.drawable.star_8_1);
                data_list.add(R.drawable.star_9);
                data_list.add(R.drawable.star_9_1);
                data_list.add(R.drawable.star_10);
                data_list.add(R.drawable.star_10_1);
                data_list.add(R.drawable.star_11);
                data_list.add(R.drawable.star_11_1);
                data_list.add(R.drawable.star_12);
                data_list.add(R.drawable.star_12_1);
                data_list.add(R.drawable.star_13);
                data_list.add(R.drawable.star_13_1);
                data_list.add(R.drawable.star_14);
                data_list.add(R.drawable.star_14_1);
                data_list.add(R.drawable.star_15);
                data_list.add(R.drawable.star_15_1);
                data_list.add(R.drawable.star_16);
                data_list.add(R.drawable.star_16_1);
                data_list.add(R.drawable.star_17);
                data_list.add(R.drawable.star_17_1);
                data_list.add(R.drawable.star_18);
                data_list.add(R.drawable.star_18_1);
                data_list.add(R.drawable.star_19);
                data_list.add(R.drawable.star_19_1);
                data_list.add(R.drawable.star_20);
                data_list.add(R.drawable.star_20_1);
                data_list.add(R.drawable.star_21);
                data_list.add(R.drawable.star_21_1);
                data_list.add(R.drawable.star_22);
                data_list.add(R.drawable.star_22_1);
                data_list.add(R.drawable.star_23);
                data_list.add(R.drawable.star_23_1);

                break;

            case COFFEE_MARKER:

                data_list.add(R.drawable.beer_1);
                data_list.add(R.drawable.beer_2);
                data_list.add(R.drawable.beer_3);
                data_list.add(R.drawable.c_macaron1_1);
                data_list.add(R.drawable.c_macaron2_1);
                data_list.add(R.drawable.c_macaron3_1);
                data_list.add(R.drawable.c_macaron4_1);
                data_list.add(R.drawable.c_macaron5_1);
                data_list.add(R.drawable.c_macaron6_1);
                data_list.add(R.drawable.c_macaron1);
                data_list.add(R.drawable.c_macaron2);
                data_list.add(R.drawable.c_macaron3);
                data_list.add(R.drawable.c_macaron4);
                data_list.add(R.drawable.c_macaron5);
                data_list.add(R.drawable.c_macaron6);
                data_list.add(R.drawable.coffee_1);
                data_list.add(R.drawable.coffee_2);
                data_list.add(R.drawable.coffee_3);
                data_list.add(R.drawable.coffee_4);
                data_list.add(R.drawable.coffee_5);
                data_list.add(R.drawable.coffee_6);

                break;
        }


        return data_list;
    }

}
