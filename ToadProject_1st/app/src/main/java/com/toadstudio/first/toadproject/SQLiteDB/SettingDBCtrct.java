package com.toadstudio.first.toadproject.SQLiteDB;

public class SettingDBCtrct {
    private SettingDBCtrct() {} ;
    public static final String TBL_CONTACT = "SETTING_T" ;
    public static final String COL_NO = "_id" ;
    public static final String PATH = "PATH" ; //external root path
    public static final String LATITUDE = "LATITUDE" ;
    public static final String LONGITUDE = "LONGITUDE" ;
    public static final String X_CORD = "XCORD" ;
    public static final String Y_CORD = "YCORD" ;
    public static final String MARKER = "MARKER" ;
    public static final String LEVEL = "LEVEL" ; //cam data
    public static final String STORAGE_STATUS = "STORAGE_STATUS" ;
    public static final String MAP_STATUS = "MAP_STATUS" ;

    // CREATE TABLE IF NOT EXISTS CONTACT_T (NO INTEGER NOT NULL, NAME TEXT, PHONE TEXT, OVER20 INTEGER)
    public static final String SQL_CREATE_TBL = "CREATE TABLE IF NOT EXISTS " + TBL_CONTACT + " " +
            "(" +
            COL_NO + " integer primary key autoincrement" + ", " +
            PATH + " TEXT" + ", " +
            LATITUDE + " TEXT" + ", " +
            LONGITUDE + " TEXT" + ", " +
            X_CORD + " TEXT" + ", " +
            Y_CORD + " TEXT" + ", " +
            MARKER + " TEXT" + ", " +
            LEVEL + " TEXT" + ", " +
            STORAGE_STATUS + " TEXT" + ", " +
            MAP_STATUS + " TEXT" +
            ")" ;

    // DROP TABLE IF EXISTS CONTACT_T
    public static final String SQL_DROP_TBL = "DROP TABLE IF EXISTS " + TBL_CONTACT ;
    // SELECT * FROM CONTACT_T
    public static final String SQL_SELECT = "SELECT * FROM " + TBL_CONTACT ;
    // INSERT OR REPLACE INTO CONTACT_T (NO, NAME, PHONE, OVER20) VALUES (x, x, x, x)
    public static final String SQL_INSERT = "INSERT OR REPLACE INTO " + TBL_CONTACT + " " +
            "(" +
            COL_NO + ", " +
            PATH + ", " +
            LATITUDE + ", " +
            LONGITUDE + ", " +
            X_CORD + ", " +
            Y_CORD + ", " +
            MARKER + ", " +
            LEVEL + ", " +
            STORAGE_STATUS + ", " +
            MAP_STATUS +
            ") VALUES " ;

    // DELETE FROM CONTACT_T
    public static final String SQL_DELETE = "DELETE FROM " + TBL_CONTACT ;
}
