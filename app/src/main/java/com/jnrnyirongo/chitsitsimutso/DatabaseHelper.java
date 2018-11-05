package com.jnrnyirongo.chitsitsimutso;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jnr.Nyirongo on 23/02/2018.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    public  static final String DATABASE_NAME = "favourite.db";
    public  static final String TABLE_NAME = "favourite_songs";
    public  static final String COL_1 = "ID";
    public  static final String COL_2 = "SONGLANGUAGE";
    public  static final String COL_3 = "SONGTITLE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE favourite_songs (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " SONGLANGUAGE TEXT, SONGTITLE TEXT NOT NULL UNIQUE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS favourite_songs");
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String songLanguage, String songTitle){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues my_content_value = new ContentValues();
        my_content_value.put(COL_2, songLanguage);
        my_content_value.put(COL_3, songTitle);
        long res = db.insert(TABLE_NAME, null, my_content_value);

        if(res == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        return res;
    }


    public List<String> getFavouriteSongs(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT SONGTITLE FROM "+TABLE_NAME+" ORDER BY ID ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }



    public boolean checkIfFavouriteExist(String songTitle){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SONGTITLE FROM favourite_songs WHERE SONGTITLE = '" +songTitle+ "'", null);
        if(cursor.moveToFirst()){
            return  true;
        }
        else{
            return  false;
        }
    }


    //METHOD FOR DELETING
    public void deleteItem(String passedTitle){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME+ " WHERE SONGTITLE = '"+passedTitle+"'");
    }



}
