package com.naukri.syncsample;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.naukri.syncsample.R;

/**
 * Adapter for survey demo
 *
 * @author gaurav
 */
public class SurveyAdapter extends CursorAdapter {
    public SurveyAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public SurveyAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public SurveyAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(R.layout.list_item, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String address = cursor.getString(cursor.getColumnIndex(SyncDatabase.SURVEY_ADDRESS));
        String fm = cursor.getString(cursor.getColumnIndex(SyncDatabase.SURVEY_FAMILY_MEMBERS));
        int synced = cursor.getInt(cursor.getColumnIndex(SyncDatabase.SURVEY_SYNCED));
        TextView addressTextView = (TextView) view.findViewById(R.id.addresss);
        TextView fmTextView = (TextView) view.findViewById(R.id.family_mambers);
        TextView syncedTextView = (TextView) view.findViewById(R.id.synced);
        addressTextView.setText(address);
        fmTextView.setText(fm);
        syncedTextView.setText(synced == 1 ? "Synced" : "Un Synced");
    }
}
