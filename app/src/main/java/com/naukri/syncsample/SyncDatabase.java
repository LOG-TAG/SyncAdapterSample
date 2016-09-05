package com.naukri.syncsample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Database to store local data of survey.
 *
 * @author gaurav
 */
public class SyncDatabase extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "sync.db";
    private static final int DATABASE_VERSION = 1;

    // DB Table consts
    public static final String BASE_URI = "content://com.naukri.syncsample.provider/";
    public static final String SURVEY_TABLE_NAME = "survey";
    public static final String SURVEY_COL_ID = "_id";
    public static final String SURVEY_ADDRESS = "address";
    public static final String SURVEY_FAMILY_MEMBERS = "fm";
    public static final String SURVEY_SYNCED = "synced";
    public static final Uri SURVEY_TABLE_URI = Uri.parse(BASE_URI + SURVEY_TABLE_NAME);

    public static final String DATABASE_CREATE = "create table "
            + SURVEY_TABLE_NAME + "(" +
            SURVEY_COL_ID + " integer   primary key autoincrement, " +
            SURVEY_ADDRESS + " text not null, " +
            SURVEY_FAMILY_MEMBERS + " integer, " +
            SURVEY_SYNCED + " integer " +
            ");";

    public SyncDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SURVEY_TABLE_NAME);
        onCreate(db);
    }
}
