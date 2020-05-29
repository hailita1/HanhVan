package com.example.haili.btl.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "my_db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_CATEGORY = "tblCategory";
    public static final String COLUM_CATEGORY_ID = "id";
    public static final String COLUM_CATEGORY_NAME = "name";

    public static final String TABLE_PLACE = "tblPlace";
    public static final String COLUM_PLACE_ID = "id";
    public static final String COLUM_PLACE_NAME = "name";
    public static final String COLUM_PLACE_ADDRESS = "address";
    public static final String COLUM_PLACE_DES = "description";
    public static final String COLUM_PLACE_IMG = "image";
    public static final String COLUM_PLACE_LAT = "lat";
    public static final String COLUM_PLACE_LNG = "lng";
    public static final String COLUM_PLACE_CATEGORY_ID = "idCategory";

    public static final String PRIMARY_KEY = "PRIMARY KEY";
    public static final String NOT_NULL = "NOT NULL";
    public static final String NULL = "NULL";
    public static final String AUTOINCREMENT = "AUTOINCREMENT";

    public static final String INT_DATA_TYPE = "INTEGER";
    public static final String TEXT_DATA_TYPE = "TEXT";
    public static final String REAL_DATA_TYPE = "REAL";
    public static final String BLOB_DATA_TYPE = "BLOB";

    public static final String CREATE_TABLE_CATEGORY_SQL = "CREATE TABLE " + TABLE_CATEGORY + "("
            + COLUM_CATEGORY_ID + " " + INT_DATA_TYPE + " " + PRIMARY_KEY + " " + AUTOINCREMENT + ", "
            + COLUM_CATEGORY_NAME + " " + TEXT_DATA_TYPE + " " + NOT_NULL
            + ")";

    public static final String CREATE_TABLE_PLACE_SQL = "CREATE TABLE " + TABLE_PLACE + "("
            + COLUM_PLACE_ID + " " + INT_DATA_TYPE + " " + PRIMARY_KEY + " " + AUTOINCREMENT + ", "
            + COLUM_PLACE_CATEGORY_ID + " " + INT_DATA_TYPE + " " + NOT_NULL + ", "
            + COLUM_PLACE_NAME + " " + TEXT_DATA_TYPE + " " + NOT_NULL + ", "
            + COLUM_PLACE_ADDRESS + " " + TEXT_DATA_TYPE + " " + NOT_NULL + ", "
            + COLUM_PLACE_DES + " " + TEXT_DATA_TYPE + " " + NULL + ", "
            + COLUM_PLACE_IMG + " " + BLOB_DATA_TYPE + " " + NULL + ", "
            + COLUM_PLACE_LAT + " " + REAL_DATA_TYPE + " " + NULL + ", "
            + COLUM_PLACE_LNG + " " + REAL_DATA_TYPE + " " + NULL
            + ")";

    public static final String INSERT_CATEGORY_SQL = "INSERT INTO " + TABLE_CATEGORY + "(" + COLUM_CATEGORY_NAME + ")"
            + " VALUES "
            + "('Restaurant'), " + "('Cinema'), " + "('Fashion'), " + "('ATM')";


    public SqliteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORY_SQL);
        db.execSQL(CREATE_TABLE_PLACE_SQL);
        db.execSQL(INSERT_CATEGORY_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
