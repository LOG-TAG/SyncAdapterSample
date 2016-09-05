package com.naukri.syncsample;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.content.ContentResolver;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.naukri.syncsample.R;

/**
 * The opening activity for survey demo
 *
 * @author gaurav
 */

public class SyncActivity extends AppCompatActivity
        implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String AUTHORITY = "com.naukri.syncsample.provider";
    public static final String NAME = "sync";
    private static final int ID = 1;
    private Dao dao;
    private SurveyAdapter surveyAdapter;
    private Object handleSyncObserver;
    private Account newAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        findViewById(R.id.submit).setOnClickListener(this);
        dao = new Dao(getApplicationContext());
        ListView listView = (ListView) findViewById(R.id.listview);
        surveyAdapter = new SurveyAdapter(
                getApplicationContext(),
                null,
                false
        );
        listView.setAdapter(surveyAdapter);
        getSupportLoaderManager().initLoader(ID, null, this);
        // Create the account type and default account
        newAccount = new Account(
                NAME, getString(R.string.account_type));
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) getSystemService(
                        ACCOUNT_SERVICE);
        //add a account to account manager with Name 'sync', Type 'com.sync'
        //after this account will be visible in the accounts section of settings
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            //On successful addition of account set a Periodic Sync of interval one day for Authority
            // 'com.sync.provider' and account with Name 'sync', Type 'com.sync'
            //Note:- For the sync to work there should be a content provider registered in your AndroidManifest
            //with same Authority and an account should be added of same Type and Name
            ContentResolver.addPeriodicSync(newAccount, AUTHORITY, Bundle.EMPTY, AlarmManager.INTERVAL_DAY);
        }
    }

    //SyncStatusObserver can be used to observe the status of SyncAdapter
    SyncStatusObserver syncObserver = new SyncStatusObserver() {
        @Override
        public void onStatusChanged(final int which) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (ContentResolver.isSyncActive(newAccount, AUTHORITY)) {
                        Toast.makeText(getApplicationContext(), "Syncing..",
                                Toast.LENGTH_SHORT).show();
                    } else if (ContentResolver.isSyncPending(newAccount, AUTHORITY))
                        Toast.makeText(getApplicationContext(), "Pending..",
                                Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(getApplicationContext(), "Idle..",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //add a StatusChangeListener for observing the status of SyncAdapter
        handleSyncObserver = ContentResolver.addStatusChangeListener(ContentResolver.
                SYNC_OBSERVER_TYPE_ACTIVE |
                ContentResolver.SYNC_OBSERVER_TYPE_PENDING, syncObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handleSyncObserver != null)
            ContentResolver.removeStatusChangeListener(handleSyncObserver);
    }

    @Override
    public void onClick(View v) {
        EditText addressEditText = (EditText) findViewById(R.id.addresss);
        EditText fmEditText = (EditText) findViewById(R.id.family_members);
        dao.insert(addressEditText.getText().toString(), fmEditText.getText().toString());
        fmEditText.setText("");
        addressEditText.setText("");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getApplicationContext(), SyncDatabase.SURVEY_TABLE_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        surveyAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        surveyAdapter.swapCursor(null);
    }
}
