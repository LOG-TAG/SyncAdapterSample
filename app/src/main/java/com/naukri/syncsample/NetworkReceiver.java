package com.naukri.syncsample;

import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.naukri.syncsample.R;

/**
 * Check network state and runs sync when network is connected
 *
 * @author gaurav
 */
public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        //If the network state is connected fire a manual sync immediately
        //for Authority 'com.sync.provider' and account with Name 'sync', Type 'com.sync'
        if (isConnected) {
            Bundle settingsBundle = new Bundle();
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_MANUAL, true);
            //If you don't set the Flag SYNC_EXTRAS_EXPEDITED sync won't fire immediately
            //use the if you want the sync to fire immediately
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            Account account = new Account(SyncActivity.NAME, context.getString(R.string.account_type));
            ContentResolver.requestSync(account, SyncActivity.AUTHORITY,
                    settingsBundle);
        }
    }
}
