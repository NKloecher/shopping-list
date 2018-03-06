package com.example.nicolai.shoppinglist.tasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.example.nicolai.shoppinglist.ProductSelectActivity;
import com.example.nicolai.shoppinglist.R;
import com.example.nicolai.shoppinglist.storage.StoreStorage;

/**
 * Created by mathias on 06/03/2018.
 */
public class GetStoresAsyncTask extends AsyncTask<Void, Void, Cursor> {
    private AppCompatActivity context;
    private AdapterView<Adapter> view;
    SimpleCursorAdapter storeAdapter = null;

    public GetStoresAsyncTask(AppCompatActivity context, AdapterView<Adapter> view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected Cursor doInBackground(Void... voids) {
        StoreStorage storage = StoreStorage.getInstance(context);
        return storage.getAll();
    }

    protected void onPostExecute(Cursor stores) {
        if (storeAdapter == null) {
            storeAdapter = new SimpleCursorAdapter(
                    context,
                    android.R.layout.simple_spinner_dropdown_item,
                    stores,
                    new String[]{StoreStorage.NAME},
                    new int[]{android.R.id.text1},
                    0);
            view.setAdapter(storeAdapter);
        } else {
            storeAdapter.changeCursor(stores);
        }
    }
}
