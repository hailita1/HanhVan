package com.example.haili.btl.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.haili.btl.model.Category;
import com.example.haili.btl.model.Place;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {

    private SqliteHelper sqliteHelper;
    private static DatabaseUtil dbUtil;

    private DatabaseUtil(Context context) {
        sqliteHelper = new SqliteHelper(context);
    }

    public static DatabaseUtil getInstance(Context context) {
        if (dbUtil == null) {
            dbUtil = new DatabaseUtil(context);
        }
        return dbUtil;
    }

    public List<Category> getListCategories() {
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        String sql = "SELECT * FROM " + SqliteHelper.TABLE_CATEGORY;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_CATEGORY_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_CATEGORY_NAME));
            list.add(new Category(String.valueOf(id), name));
            cursor.moveToNext();
        }
        if (cursor != null) cursor.close();
        db.close();
        return list;
    }

    public List<Place> getListPlaces() {
        List<Place> list = new ArrayList<>();
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        String sql = "SELECT * FROM " + SqliteHelper.TABLE_PLACE;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_ID));
            int idCategory = cursor.getInt(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_CATEGORY_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_NAME));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_ADDRESS));
            String des = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_DES));
            byte[] img = cursor.getBlob(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_IMG));
            double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_LAT));
            double lng = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_LNG));

            Place place = new Place.Builder()
                    .setPlaceId(String.valueOf(id))
                    .setCategoryId(String.valueOf(idCategory))
                    .setName(name)
                    .setDescription(des)
                    .setAddress(address)
                    .setImage(img)
                    .setLat(lat)
                    .setLng(lng)
                    .build();

            list.add(place);
            cursor.moveToNext();
        }
        if (cursor != null) cursor.close();
        db.close();
        return list;
    }

    public List<Place> getListPlacesWithCategory(String categoryId) {
        List<Place> list = new ArrayList<>();
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        String sql = "SELECT * FROM " + SqliteHelper.TABLE_PLACE + " WHERE " + SqliteHelper.COLUM_PLACE_CATEGORY_ID + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{categoryId});
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_ID));
            int idCategory = cursor.getInt(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_CATEGORY_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_NAME));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_ADDRESS));
            String des = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_DES));
            byte[] img = cursor.getBlob(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_IMG));
            double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_LAT));
            double lng = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_LNG));

            Place place = new Place.Builder()
                    .setPlaceId(String.valueOf(id))
                    .setCategoryId(String.valueOf(idCategory))
                    .setName(name)
                    .setDescription(des)
                    .setAddress(address)
                    .setImage(img)
                    .setLat(lat)
                    .setLng(lng)
                    .build();

            list.add(place);
            cursor.moveToNext();
        }
        if (cursor != null) cursor.close();
        db.close();
        return list;
    }

    public Place getPlace(String id) {
        Place place = null;
        //Để mở database
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        String sql = "SELECT * FROM " + SqliteHelper.TABLE_PLACE + " WHERE " + SqliteHelper.COLUM_PLACE_ID + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int idCategory = cursor.getInt(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_CATEGORY_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_NAME));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_ADDRESS));
            String des = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_DES));
            byte[] img = cursor.getBlob(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_IMG));
            double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_LAT));
            double lng = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_PLACE_LNG));

            place = new Place.Builder()
                    .setPlaceId(String.valueOf(id))
                    .setCategoryId(String.valueOf(idCategory))
                    .setName(name)
                    .setDescription(des)
                    .setAddress(address)
                    .setImage(img)
                    .setLat(lat)
                    .setLng(lng)
                    .build();
            cursor.moveToNext();
        }
        if (cursor != null) cursor.close();
        db.close();
        return place;
    }

    public Category category(String categoryId) {
        Category category = null;
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        String sql = "SELECT * FROM " + SqliteHelper.TABLE_CATEGORY + " WHERE " + SqliteHelper.COLUM_CATEGORY_ID + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{categoryId});
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUM_CATEGORY_NAME));

            category = new Category(categoryId, name);
            cursor.moveToNext();
        }
        if (cursor != null) cursor.close();
        db.close();
        return category;
    }

    public void insertCategory(Category category) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.COLUM_CATEGORY_NAME, category.getName());
        db.insert(SqliteHelper.TABLE_CATEGORY, null, contentValues);
        db.close();
    }

    //Thêm địa chỉ
    public void insertPlace(Place place) {
        //Mở cơ sở dữ liệu
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        //contentValues lưu dữ liệu để lưu vào csdl
        ContentValues contentValues = new ContentValues();
        //thêm cái ID của place vào cột COLUM_PLACE_CATEGORY_ID
        contentValues.put(SqliteHelper.COLUM_PLACE_CATEGORY_ID, place.getCategoryId());
        //Tên
        contentValues.put(SqliteHelper.COLUM_PLACE_NAME, place.getName());
        //Địa chỉ
        contentValues.put(SqliteHelper.COLUM_PLACE_ADDRESS, place.getAddress());
        //Đặc trưng
        contentValues.put(SqliteHelper.COLUM_PLACE_DES, place.getDescription());
        contentValues.put(SqliteHelper.COLUM_PLACE_LAT, place.getLat());
        contentValues.put(SqliteHelper.COLUM_PLACE_LNG, place.getLng());
        contentValues.put(SqliteHelper.COLUM_PLACE_IMG, place.getImage());
        db.insert(SqliteHelper.TABLE_PLACE, null, contentValues);
        db.close();
    }

    //Sửa địa chỉ
    public void updatePlace(Place place) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.COLUM_PLACE_CATEGORY_ID, place.getCategoryId());
        contentValues.put(SqliteHelper.COLUM_PLACE_NAME, place.getName());
        contentValues.put(SqliteHelper.COLUM_PLACE_ADDRESS, place.getAddress());
        contentValues.put(SqliteHelper.COLUM_PLACE_DES, place.getDescription());
        contentValues.put(SqliteHelper.COLUM_PLACE_LAT, place.getLat());
        contentValues.put(SqliteHelper.COLUM_PLACE_LNG, place.getLng());
        contentValues.put(SqliteHelper.COLUM_PLACE_IMG, place.getImage());
        db.update(SqliteHelper.TABLE_PLACE, contentValues,
                SqliteHelper.COLUM_PLACE_ID + " = ?", new String[]{String.valueOf(place.getPlaceId())});
        db.close();
    }

    //Xóa địa chỉ
    public void deletePlace(Place place) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + SqliteHelper.TABLE_PLACE + " WHERE " + SqliteHelper.COLUM_PLACE_ID + " = " + place.getPlaceId());
        db.close();
    }
}

