package com.toadstudio.first.toadproject.SQLiteDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SettingDBHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 2 ;
    public static final String DBFILE_SETTING = "photomap_setting.db" ;
    public static String TAG = "ToadPrj_SettingDBHelper";
    public SettingDBHelper(Context context) {
        super(context, DBFILE_SETTING, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
        db.execSQL(SettingDBCtrct.SQL_CREATE_TBL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SettingDBCtrct.SQL_DROP_TBL) ;
        onCreate(db) ;
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
