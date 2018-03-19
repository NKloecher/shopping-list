package com.example.nicolai.shoppinglist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.nicolai.shoppinglist.model.Deal;
import com.example.nicolai.shoppinglist.model.Product;
import com.example.nicolai.shoppinglist.model.ShoppingList;
import com.example.nicolai.shoppinglist.storage.DealStorage;
import com.example.nicolai.shoppinglist.storage.ProductStorage;
import com.example.nicolai.shoppinglist.storage.ShoppingListStorage;
import com.example.nicolai.shoppinglist.storage.StoreStorage;

public class MainActivity extends AppCompatActivity {

    private ShoppingListStorage storage;
    private EditText newListNameInput;
    private SimpleCursorAdapter adapter;

    //todo Shopping list items updating when items is changed
    //todo Make it look more "streamlined", same context menus/buttons/toats in the whole app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationContext().deleteDatabase("SHOPPING_LIST"); //how to delete database on startup
        getApplicationContext().deleteDatabase("STORE");
        getApplicationContext().deleteDatabase("PRODUCT");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        storage = ShoppingListStorage.getInstance(this);
        new GetAsyncTask().execute();
        ListView lists = findViewById(R.id.mainListView);
        registerForContextMenu(lists);
        new TestDataAsyncTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_store){
            startActivity(new Intent(this, ViewStoresActivity.class));
        }
        if (item.getItemId() == R.id.action_products){
            startActivity(new Intent(this, ViewProductsActivity.class));
        }
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        switch (item.getItemId()) {
            case R.id.list_delete:
                new DeleteAsyncTask(info.id).execute();
                return true;
        }
        return false;
    }

    public void createShoppingList(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter new list name");

        newListNameInput = new EditText(this);
        newListNameInput.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setView(newListNameInput);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new InsertAsyncTask().execute();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    class TestDataAsyncTask extends AsyncTask<Void,Void,Void> {

        private StoreStorage storeStorage;
        private ProductStorage productStorage;

        @Override
        protected Void doInBackground(Void... voids) {
            storeStorage = StoreStorage.getInstance(MainActivity.this);
            productStorage = ProductStorage.getInstance(MainActivity.this);
            if (storeStorage.get(1) == null){


            long netto = storeStorage.insert("Netto","Vestervej 44", "Netto.dk");
            long coop = storeStorage.insert("Coop","Omvejen 2", "Coop.dk");
            long fakta = storeStorage.insert("Fakta", "Ly 1", "Fakta.dk");

            productStorage.insert(15, "Cola", netto,null);
            productStorage.insert(30, "Peanuts", netto, null);

            productStorage.insert(4500, "TV, Samsung", fakta, null);
            productStorage.insert(7, "mælk", fakta, null);

            productStorage.insert(25, "Øl", coop, null);
            productStorage.insert(50, "Påskeøl", coop, DealStorage.getInstance(MainActivity.this).insert(39, coop));
            }

            return null;
        }
    }

        class DeleteAsyncTask extends AsyncTask<Void,Void,Void>{
        long listId;
        public DeleteAsyncTask(long listId) {
            this.listId = listId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            storage.remove(listId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new GetAsyncTask().execute();
        }
    }

    class InsertAsyncTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            storage.insert(newListNameInput.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new GetAsyncTask().execute();
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
            adapter = new SimpleCursorAdapter(MainActivity.this, android.R.layout.simple_list_item_1, cursor,
                    new String[] {ShoppingListStorage.NAME}, new int[] {android.R.id.text1},0);
            ListView listView = findViewById(R.id.mainListView);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(MainActivity.this, ListActivity.class);
                    i.putExtra(ListActivity.LIST_ID_EXTRA, id);
                    startActivity(i);
                }
            });
            }else{
                adapter.changeCursor(cursor);
            }
        }
    }
}
