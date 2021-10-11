package com.vayrotech.fourrocksgallery;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ModelDatabase extends SQLiteOpenHelper {

    public ModelDatabase( Context context) {
        super(context,"name.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tableimage (name text, imageblob); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("drop table if exists tableimage");

    }
    public boolean insertdata(String username, byte[] img){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", username);
        contentValues.put("image",img);
        long ins = MyDB.insert("tableimage", null, contentValues);
        if(ins==-1) return false;
        else return true;

    }
}
