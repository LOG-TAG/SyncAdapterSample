package com.naukri.syncsample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Database interactions
 *
 * @author gaurav
 */
public class Dao {


    private Context context;

    public Dao(Context context) {
        this.context = context;
    }

    public void insert(String address, String fm) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SyncDatabase.SURVEY_ADDRESS, address);
        contentValues.put(SyncDatabase.SURVEY_FAMILY_MEMBERS, fm);
        context.getContentResolver().insert(SyncDatabase.SURVEY_TABLE_URI, contentValues);
    }

    public Cursor queryAll() {
        return context.getContentResolver().query(SyncDatabase.SURVEY_TABLE_URI, null,
                null, null, null);
    }
}
