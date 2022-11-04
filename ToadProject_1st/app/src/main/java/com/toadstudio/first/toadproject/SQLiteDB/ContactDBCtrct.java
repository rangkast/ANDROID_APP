package com.toadstudio.first.toadproject.SQLiteDB;

/**
 * Created by rangkast.jeong on 2017-11-21.
 */

public class ContactDBCtrct {
    private ContactDBCtrct() {} ;
    public static final String TBL_CONTACT = "CONTACT_T" ;
    public static final String COL_NO = "_id" ;
    public static final String PATH = "PATH" ;
    public static final String LATITUDE = "LATITUDE" ;
    public static final String LONGITUDE = "LONGITUDE" ;
    public static final String INFO = "INFO" ;
    public static final String FAV = "FAV" ;
    public static final String DATA = "DATA" ;
    public static final String LEVEL = "LEVEL" ;
    public static final String STORAGE = "STORAGE" ;

    // CREATE TABLE IF NOT EXISTS CONTACT_T (NO INTEGER NOT NULL, NAME TEXT, PHONE TEXT, OVER20 INTEGER)
    public static final String SQL_CREATE_TBL = "CREATE TABLE IF NOT EXISTS " + TBL_CONTACT + " " +
            "(" +
            COL_NO + " integer primary key autoincrement" + ", " +
            PATH + " TEXT" + ", " +
            LATITUDE + " TEXT" + ", " +
            LONGITUDE + " TEXT" + ", " +
            INFO + " TEXT" + ", " +
            FAV + " TEXT" + ", " +
            DATA + " TEXT" + ", " +
            LEVEL + " TEXT" + ", " +
            STORAGE + " TEXT" +
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
            INFO + ", " +
            FAV + ", " +
            DATA + ", " +
            LEVEL + ", " +
            STORAGE +
            ") VALUES " ;

    // DELETE FROM CONTACT_T
    public static final String SQL_DELETE = "DELETE FROM " + TBL_CONTACT ;
}
