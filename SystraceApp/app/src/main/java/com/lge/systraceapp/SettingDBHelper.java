package com.lge.systraceapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SettingDBHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "systrace_setting.db";
    private final static String DB_TABLE = "setting";
    private final static int    DB_VERSION = 1;
    public static String TAG = "SystraceApp:SettingDB";

    public SettingDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"onCreate");
        // TODO Auto-generated method stub
        db.execSQL("create table if not exists " + DB_TABLE + "(id text primary key, info text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("drop table if exists " + DB_TABLE);
        onCreate(db);
    }
}