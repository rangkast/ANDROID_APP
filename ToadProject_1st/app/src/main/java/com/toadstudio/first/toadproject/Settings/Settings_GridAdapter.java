package com.toadstudio.first.toadproject.Settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.toadstudio.first.toadproject.R;

import java.util.List;

/**
 * Created by rangkast.jeong on 2018-03-19.
 */

public class Settings_GridAdapter extends BaseAdapter {
    List<Integer> mImg;
    Context context;
    private LayoutInflater inflater;
    private int layout;
    public static String TAG = "ToadPrj_GridAdapter";
    public Settings_GridAdapter(Context context, int layout,  List<Integer> mImg) {
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.mImg = mImg;
        this.layout = layout;

    }

    @Override
    public int getCount() {
        return mImg.size();
    }

    @Override
    public Object getItem(int position) {
        return mImg.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        ImageView iv = (ImageView) convertView.findViewById(R.id.imageView_grid);
        final Integer[] array = mImg.toArray(new Integer[mImg.size()]);
        iv.setImageResource(array[position]);
/*
        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Log.d(TAG, "item clicked" + position);
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.pic_dialog);

                ImageView iv = (ImageView) dialog.findViewById(R.id.pic_dialog_img);
                Glide
                        .with(context)
                        .load(array[position])
                        .into(iv);

                dialog.show();

                Log.d(TAG, "scale x" + parent.getWidth());
                Log.d(TAG, "scale y" + parent.getHeight());

                Window window = dialog.getWindow();

                int x = (int)(parent.getWidth() * 0.4f);
                int y = (int)(parent.getHeight() * 0.3f);

                window.setLayout(x,y);

                return false;
            }
        });
*/
        return convertView;
    }

}
