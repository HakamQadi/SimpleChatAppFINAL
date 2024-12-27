package com.example.simplechatappfinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ChatApp.db";
    public static final String TABLE_NAME = "Chats";
    public static final String COLUMN_ID = "User_ID";
    public static final String COLUMN_TITLE = "Message_Title";
    public static final String COLUMN_BODY = "Message_Body";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_TITLE + " TEXT NOT NULL, " + COLUMN_BODY + " TEXT NOT NULL)");
    }

    public boolean sendMessage(int userId, String title, String body) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, userId);
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_BODY, body);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllChats() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor getMessage(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + COLUMN_BODY + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " =" + userId, null);
    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
