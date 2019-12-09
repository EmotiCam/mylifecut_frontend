package com.example.lifecut.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "people_table";
    private static final String TABLE_NAME2 = "nickname";
    private static final String COL1 = "ID";
    private static final String COL2 = "name";

    private static final String COMMENT = "comment";
    private static final String DATE="cal";
    private static final String URL ="url";
    private static final String ANGER = "anger";
    private static final String CONTEMPT = "contempt";
    private static final String DISGUST = "disgust";
    private static final String FEAR = "fear";
    private static final String HAPPINESS = "happiness";
    private static final String NEUTRAL =  "neutral";
    private static final String SADNESS = "sadness";
    private static final String SURPRISE = "surprise";
    private static final String MAINEMOTION = "main";

    private static final String NICKNAME = "nickname";
    private static final String MAINCOMMENT = "maincomment";

    public DatabaseHelper(Context context) {

        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME +
                "(" +
                COMMENT +" TEXT," +
                DATE +" TEXT, " +
                URL +" TEXT," +
                ANGER +" REAL," +
                CONTEMPT +" REAL," +
                DISGUST +" REAL," +
                FEAR +" REAL," +
                HAPPINESS +" REAL," +
                NEUTRAL +" REAL," +
                SADNESS +" REAL," +
                SURPRISE +" REAL," +
                MAINEMOTION +" TEXT" +
                ")";
        db.execSQL(createTable);

        String createTable2 = "CREATE TABLE " + TABLE_NAME2 +
                "(" +
                NICKNAME +" TEXT," +
                MAINCOMMENT +" TEXT" +
                ")";
        db.execSQL(createTable2);


    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String comment, String date, String url, double anger, double contempt, double disgust, double fear, double happiness, double neutral, double sadness, double surprise, String main) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMMENT, comment);
        contentValues.put(DATE, date);
        contentValues.put(URL, url);
        contentValues.put(ANGER +"", anger);
        contentValues.put(CONTEMPT+"", contempt);
        contentValues.put(DISGUST+"", disgust);
        contentValues.put(FEAR+"", fear);
        contentValues.put(HAPPINESS+"", happiness);
        contentValues.put(NEUTRAL+"", neutral);
        contentValues.put(SADNESS+"", sadness);
        contentValues.put(SURPRISE+"", surprise);
        contentValues.put(MAINEMOTION+"", main);

        Log.d(TAG, "addData: Adding " + url+ "\n" + date+ "\n" + neutral+ "\n"  + happiness+ "\n"   + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean addDataNickname(String nickName, String mainComment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMMENT, nickName);
        contentValues.put(DATE, mainComment);

        Log.d(TAG, "addData: Adding " + nickName+ "\n" + mainComment   + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getDataNickname(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME2;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns only the ID that matches the name passed in
     * @param name
     * @return
     */
    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the name field
     * @param newName
     * @param id
     * @param oldName
     */
    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    /**
     * Delete from database
     * @param id
     * @param name
     */
    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }



}