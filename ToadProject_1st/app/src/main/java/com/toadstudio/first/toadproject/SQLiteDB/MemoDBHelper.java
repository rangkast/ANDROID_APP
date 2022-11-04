package com.toadstudio.first.toadproject.SQLiteDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MemoDBHelper extends SQLiteOpenHelper{
    private final static String DB_NAME = "photomap_memo.db";
    private final static String DB_TABLE = "memo";
    private final static int    DB_VERSION = 1;
    public static String TAG = "ToadPrj_MemoDBHelper";

    public MemoDBHelper(Context context) {
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
