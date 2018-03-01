package com.example.nicolai.shoppinglist.activities;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.nicolai.shoppinglist.R;
import com.example.nicolai.shoppinglist.storage.ShoppingListStorage;

public class MainActivity extends AppCompatActivity {

    private ShoppingListStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        storage = ShoppingListStorage.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //todo toolbar actions
        return true;
    }

    class GetAsyncTask extends AsyncTask<Void,Void,Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return storage.getAll();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainActivity.this, android.R.layout.simple_list_item_1, cursor,
                    new String[] {ShoppingListStorage.NAME}, new int[] {android.R.id.text1},0);
            ListView listView = findViewById(R.id.mainListView);
            listView.setAdapter(adapter);
            //todo setOnClick
        }
    }
}
