package com.vayrotech.fourrocksgallery.DatabaseStuff;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Date;

public class ModelDatabase extends SQLiteOpenHelper {

    public ModelDatabase( Context context) {
        super(context,"four.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create table tableimage(path TEXT PRIMARY KEY, title TEXT, date TEXT, classification TEXT); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
            DB.execSQL("drop table if exists tableimage");

    }
    public boolean insertData(String path, String title, String date, String classification){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("path", path);
        contentValues.put("title",title);
        contentValues.put("date", date);
        contentValues.put("classification", classification);
        long ins = MyDB.insert("tableimage", null, contentValues);
        if(ins==-1) return false;
        else return true;

    }



    public Cursor getData ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from tableimage", null);
        return cursor;

    }

    public void clearDatabase(String TABLE_NAME) {
        SQLiteDatabase db = this.getReadableDatabase();
        String clearDBQuery = "DELETE FROM " + TABLE_NAME;
        db.execSQL(clearDBQuery);
    }


}
