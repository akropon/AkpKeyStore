package com.akropon.akpkeystore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "AkpKeyStoreDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("akropon", "onCreate database");

        db.execSQL("create table users ("
                + "id integer primary key autoincrement,"
                + "login blob,"
                + "password blob" + ");");

        db.execSQL("create table bundles ("
                + "id integer primary key autoincrement,"
                + "userId integer,"
                + "name blob,"
                + "description blob,"
                + "login blob,"
                + "password blob" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: 16.11.18 impl ?
        Log.e("akropon", "onUpdate() not realized. That may cause problems. " +
                "oldVersion=" + oldVersion +" newVersion=" + newVersion);
    }
}