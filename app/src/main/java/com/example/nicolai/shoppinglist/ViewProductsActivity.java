package com.example.nicolai.shoppinglist;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.nicolai.shoppinglist.storage.ProductStorage;
import com.example.nicolai.shoppinglist.storage.StoreStorage;

public class ViewProductsActivity extends AppCompatActivity {

    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new GetAsyncTask().execute();
    }



    class GetAsyncTask extends AsyncTask<Void,Void,Cursor> {

        private StoreStorage storage;


        @Override
        protected Cursor doInBackground(Void... voids) {
            storage = StoreStorage.getInstance(ViewProductsActivity.this);
            return storage.getAll();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (adapter == null) {
                adapter = new SimpleCursorAdapter(ViewProductsActivity.this, android.R.layout.simple_list_item_1, cursor,
                        new String[]{StoreStorage.NAME}, new int[]{android.R.id.text1}, 0);
                ListView listView = findViewById(R.id.content_view);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(ViewProductsActivity.this, EditProductActivity.class);
                        i.putExtra("Store", id);
                        startActivity(i);
                    }
                });
            } else {
                adapter.changeCursor(cursor);
            }
        }

    }


}
