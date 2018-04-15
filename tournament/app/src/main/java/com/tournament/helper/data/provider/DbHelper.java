package com.tournament.helper.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tournament.helper.Contract;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tournaments.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_TABLE = "CREATE TABLE " +
            Contract.TABLE_NAME + "(" +
            Contract._ID + " TEXT NOT NULL, " +
            Contract.COL_NAME_TEXT + " TEXT NOT NULL" +
            ")";

        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + Contract.TABLE_NAME;

        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);

    }
}

