package com.toadstudio.first.toadproject.GMap;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.toadstudio.first.toadproject.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by rangkast.jeong on 2018-02-23.
 */

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.gmap_custom_info_window, null);

        TextView mTextView = (TextView) view.findViewById(R.id.g_name);
        TextView mTextView_1 = (TextView) view.findViewById(R.id.g_details);
        TextView mTextView_2 = (TextView) view.findViewById(R.id.g_hotels);
        TextView mTextView_3 = (TextView) view.findViewById(R.id.g_food);
        TextView mTextView_4 = (TextView) view.findViewById(R.id.g_transport);
        ImageView mImageView = (ImageView)view.findViewById(R.id.g_pic);

//        TextView name_tv = view.findViewById(R.id.g_name);
//        TextView details_tv = view.findViewById(R.id.g_details);
//        ImageView img = view.findViewById(g_pic);

//        TextView hotel_tv = view.findViewById(R.id.g_hotels);
//        TextView food_tv = view.findViewById(R.id.g_food);
//        TextView transport_tv = view.findViewById(R.id.g_transport);

        mTextView.setText(marker.getTitle());
        mTextView_1.setText(marker.getSnippet());

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        int imageId = context.getResources().getIdentifier(infoWindowData.getImage().toLowerCase(),
                "drawable", context.getPackageName());
        mImageView.setImageResource(imageId);

        mTextView_2.setText(infoWindowData.getHotel());
        mTextView_3.setText(infoWindowData.getFood());
        mTextView_4.setText(infoWindowData.getTransport());

        return view;
    }
}
