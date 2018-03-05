package com.example.nicolai.shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nicolai.shoppinglist.model.ListItem;
import com.example.nicolai.shoppinglist.storage.ListItemStorage;
import com.example.nicolai.shoppinglist.storage.ShoppingListStorage;

public class ListActivity extends AppCompatActivity {

    public static String LIST_ID_EXTRA = "LIST_ID";

    ListItemStorage storage;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        storage = ListItemStorage.getInstance(this);
        new GetAsyncTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list_item:
                Intent i = new Intent(this, ListItemActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }

    class GetAsyncResult {
        public Cursor listItemCursor;
        public String listName;

        public GetAsyncResult(Cursor listItemCursor, String listName) {
            this.listItemCursor = listItemCursor;
            this.listName = listName;
        }
    }

    class GetAsyncTask extends AsyncTask<Void,Void,GetAsyncResult> {
        @Override
        protected GetAsyncResult doInBackground(Void... voids) {
            long listId = getIntent().getExtras().getLong(LIST_ID_EXTRA);
            String listName = ShoppingListStorage.getInstance(ListActivity.this).get(listId).getName();
            Cursor listItemCursor = storage.getAllItemsInList(listId);

            return new GetAsyncResult(listItemCursor, listName);
        }

        @Override
        protected void onPostExecute(GetAsyncResult result) {
            ListView list = findViewById(R.id.list);
            list.setAdapter(new ListAdapter(ListActivity.this, result.listItemCursor, 0));
            toolbar.setTitle(result.listName);
            // todo view item on click
        }
    }

    class ListAdapter extends CursorAdapter {

        public ListAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ListItem listItem = ((ListItemStorage.ListItemWrapper)cursor).get();
            TextView name = findViewById(R.id.name);
            TextView oldPrice = findViewById(R.id.old_price);
            TextView price = findViewById(R.id.price);
            name.setText(listItem.getProduct().getName());
            oldPrice.setText(listItem.getProduct().getPrice());
            oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            price.setText(listItem.getProduct().getPrice());
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater cursorInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            return cursorInflater.inflate(R.layout.list_row, parent, false);
        }
    }
}
