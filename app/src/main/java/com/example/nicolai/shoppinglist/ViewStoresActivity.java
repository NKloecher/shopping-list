package com.example.nicolai.shoppinglist;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.nicolai.shoppinglist.storage.StoreStorage;

/**
 * Created by Nicolai on 07-03-2018.
 */

public class ViewStoresActivity extends AppCompatActivity{

    private StoreStorage storage;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stores);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        storage = StoreStorage.getInstance(this);
        new GetAsyncTask().execute();

    }

    class GetAsyncTask extends AsyncTask<Void,Void,Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return storage.getAll();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (adapter == null){
                adapter = new SimpleCursorAdapter(ViewStoresActivity.this, android.R.layout.simple_list_item_1, cursor,
                        new String[] {StoreStorage.NAME}, new int[] {android.R.id.text1},0);
                ListView listView = findViewById(R.id.store_index);
                listView.setAdapter(adapter);
            }else{
                adapter.changeCursor(cursor);
            }
        }
    }
}
