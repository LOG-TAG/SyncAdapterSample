package com.naukri.syncsample;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Content provider to access local data
 *
 * @author gaurav
 */
public class SyncProvider extends ContentProvider {


    private SyncDatabase dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new SyncDatabase(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(SyncDatabase.SURVEY_TABLE_NAME);
        Cursor cursor = builder.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(SyncDatabase.SURVEY_TABLE_NAME, null, values);
        if (id != -1)
            getContext().getContentResolver().notifyChange(uri, null);
        //In the SyncAdapter Config if supportsUploading is set to true and you call the
        //notifyChange method with syncToNetwork true sync will fire
        getContext().getContentResolver().notifyChange(uri,
                null, true);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(SyncDatabase.SURVEY_TABLE_NAME, selection, selectionArgs);
        if (rowsDeleted != -1)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updatedRow = dbHelper.getWritableDatabase().update(
                SyncDatabase.SURVEY_TABLE_NAME, values, selection, selectionArgs);
        if (updatedRow > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updatedRow;
    }

}
