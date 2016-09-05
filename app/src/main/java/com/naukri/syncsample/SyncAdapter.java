package com.naukri.syncsample;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Adapter that encapsulates the data transfer code
 *
 * @author gaurav
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }


    //This method will be called every time a sync is fired
    //Code to sync the data with the server to be added here
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SyncDatabase.SURVEY_SYNCED, 1);
        getContext().getContentResolver().update(SyncDatabase.SURVEY_TABLE_URI, contentValues,
                null, null);

        //Says there was one IOException while performing the sync
        //System will handle the Retry
        syncResult.stats.numIoExceptions++;

    }
}
