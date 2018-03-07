package com.example.nicolai.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.nicolai.shoppinglist.storage.StoreStorage;

/**
 * Created by Nicolai on 07-03-2018.
 */

public class ViewStoresActivity extends AppCompatActivity{

    private static final int CREATE_STORE_RESULT = 1;
    private static final int UPDATE_STORE_RESULT = 2;
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

    public void onCreateStoreInIndex(View view){
        startActivityForResult(new Intent(this, StoreActivity.class), CREATE_STORE_RESULT);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_STORE_RESULT && resultCode == Activity.RESULT_OK) {
            new GetAsyncTask().execute();
        }
        else if (requestCode == UPDATE_STORE_RESULT && resultCode == Activity.RESULT_OK){
            new GetAsyncTask().execute();
        }
        else if (resultCode == Activity.RESULT_CANCELED){
            //Working as intended
        }
        else {
            throw new Error("invalid requestCode or resultCode");
        }
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
                listView.setAdapter(adapter);listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(ViewStoresActivity.this, StoreActivity.class);
                        i.putExtra("Store", id);
                        Log.d("Storage Test", storage.get(id).getName());
                        startActivityForResult(i, CREATE_STORE_RESULT);
                    }
                });
            }else{
                adapter.changeCursor(cursor);
            }
        }
    }
}
