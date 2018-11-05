package com.jnrnyirongo.chitsitsimutso;

/**
 * Created by Jnr.Nyirongo on 20/08/2018.
 */

//The database has two tables name chichewa and chitumbuka

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }


    //Method for accessing the song Titles
    //The songLanguage will correspond to a database_table
    public List<String> getSongTitles(String songLanguage){

        int songNumber = 1; //To be used for displaying the song Numbers

        List<String> list = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT Title FROM "+songLanguage+" ORDER BY ID ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add( songNumber +".   "+ cursor.getString(0));
            cursor.moveToNext();

            songNumber++;
        }
        cursor.close();
        return list;
    }

    //Method to retrieve songNumber without songLanguage
    public String getSongNumber(String songTitle) {
        String songNumber = null;
        Cursor cursor = database.rawQuery("SELECT SONGNUMBER FROM (SELECT TITLE, SONGNUMBER FROM chichewa UNION SELECT TITLE, SONGNUMBER FROM chitumbuka) WHERE TITLE = '" +songTitle+ "'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            songNumber = cursor.getString(0);
            cursor.moveToNext();
        }
        cursor.close();
        return songNumber;
    }

    //Method retrieves song Number
    public String getSongNumber(String songTitle, String songLanguage){
        String songNumber = null;
        Cursor cursor = database.rawQuery("SELECT SONGNUMBER FROM "+songLanguage+" WHERE TITLE = '" +songTitle+ "'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            songNumber = cursor.getString(0);
            cursor.moveToNext();
        }
        cursor.close();
        return songNumber;
    }

    //Method to retrieve body without songLanguage
    public String getBody(String songTitle) {
        String body = null;
        Cursor cursor = database.rawQuery("SELECT BODY FROM (SELECT TITLE, BODY FROM chichewa UNION SELECT TITLE, BODY FROM chitumbuka) WHERE TITLE = '" +songTitle+ "'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            body = cursor.getString(0);
            cursor.moveToNext();
        }
        cursor.close();
        return body;
    }

    //Method will retrieve the entire lyrics of a particular song
    public String getBody(String songTitle, String songLanguage){
        String body = null;
        Cursor cursor = database.rawQuery("SELECT BODY FROM "+songLanguage+" WHERE TITLE = '" +songTitle+ "'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            body = cursor.getString(0);
            cursor.moveToNext();
        }
        cursor.close();
        return body;
    }

}