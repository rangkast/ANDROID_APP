package com.toadstudio.first.toadproject.Settings;


import android.net.Uri;

import java.io.Serializable;

public class PathInfo implements Serializable {
    public Uri uri = null;
    public String folder = "";

    public PathInfo(Uri uri, String folder) {
        this.uri = uri;
        this.folder = folder;
    }

    public Uri getUri() {
        return  uri;
    }
    public String getFolderPath() {
        return  folder;
    }

}

