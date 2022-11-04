package com.toadstudio.first.toadproject.SQLiteDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rangkast.jeong on 2017-11-21.
 */

public class ContactDBHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1 ;
    public static final String DBFILE_MAP = "photomap_data.db" ;
    public static String TAG = "ToadPrj_ContactDBHelper";
    public ContactDBHelper(Context context) {
        super(context, DBFILE_MAP, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"onCreate");
        db.execSQL(ContactDBCtrct.SQL_CREATE_TBL) ;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ContactDBCtrct.SQL_DROP_TBL) ;
        onCreate(db) ;
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         onUpgrade(db, oldVersion, newVersion);
    }

}
