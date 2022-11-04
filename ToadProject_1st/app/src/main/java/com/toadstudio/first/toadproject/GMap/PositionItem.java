package com.toadstudio.first.toadproject.GMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by rangkast.jeong on 2018-02-27.
 */

public class PositionItem implements ClusterItem {
    private LatLng mPosition;
    private String address;
    private String snippet;
    public int bg;
    public int folder_level;

    public PositionItem(LatLng location, String address, String snippet, int bg, int folder_level) {
        this.mPosition = location;
        this.address = address;
        this.snippet = snippet;
        this.bg = bg;
        this.folder_level =folder_level;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle()
    {
        return address;
    }

    @Override
    public String getSnippet()
    {
        return snippet;
    }

    public int getBg()
    {
        return bg;
    }

    public int getFolder_level()
    {
        return folder_level;
    }

}