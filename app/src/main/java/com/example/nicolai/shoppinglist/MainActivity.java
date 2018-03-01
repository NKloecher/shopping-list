package com.example.nicolai.shoppinglist;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.nicolai.shoppinglist.model.ShoppingList;
import com.example.nicolai.shoppinglist.storage.ShoppingListStorage;

public class MainActivity extends AppCompatActivity {

    private ShoppingListStorage storage;
    private EditText newListNameInput;
    private SimpleCursorAdapter adapter;

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

    class InsertAsyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            ShoppingList list = new ShoppingList(newListNameInput.getText().toString(), -1);
            storage.insert(list);
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
            //todo setOnClick
            }else{
                adapter.changeCursor(cursor);
            }
        }
    }
}
