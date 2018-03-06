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
import com.example.nicolai.shoppinglist.model.ShoppingList;
import com.example.nicolai.shoppinglist.storage.ListItemStorage;
import com.example.nicolai.shoppinglist.storage.ShoppingListStorage;

import org.w3c.dom.Text;

public class ListActivity extends AppCompatActivity {

    public static String LIST_ID_EXTRA = "LIST_ID";

    ListItemStorage storage;
    Toolbar toolbar;
    long listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        storage = ListItemStorage.getInstance(this);
        listId = getIntent().getExtras().getLong(LIST_ID_EXTRA);
    }

    @Override
    protected void onResume() {
        super.onResume();

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
                i.putExtra(ListItemActivity.LIST_ID_EXTRA, listId);
                startActivity(i);
                break;
        }
        return true;
    }

    class GetAsyncResult {
        public Cursor listItemCursor;
        public ShoppingList list;

        public GetAsyncResult(Cursor listItemCursor, ShoppingList list) {
            this.listItemCursor = listItemCursor;
            this.list = list;
        }
    }

    class GetAsyncTask extends AsyncTask<Void,Void,GetAsyncResult> {
        @Override
        protected GetAsyncResult doInBackground(Void... voids) {
            ShoppingList list = ShoppingListStorage.getInstance(ListActivity.this).get(listId);
            Cursor listItemCursor = storage.getAllItemsInList(listId);

            return new GetAsyncResult(listItemCursor, list);
        }

        @Override
        protected void onPostExecute(GetAsyncResult result) {
            ListView list = findViewById(R.id.list);
            TextView total = findViewById(R.id.total);
            list.setAdapter(new ListAdapter(ListActivity.this, result.listItemCursor, 0));
            toolbar.setTitle(result.list.getName());
            total.setText("Total: " + Integer.toString(result.list.totalPrice()) + "kr.");
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
            TextView nameT = view.findViewById(R.id.name);
            TextView oldPriceT = view.findViewById(R.id.old_price);
            TextView priceT = view.findViewById(R.id.price);
            TextView amountT = view.findViewById(R.id.amount);
            nameT.setText(listItem.getProduct().getName());
            int oldPrice = listItem.getProduct().getPrice();
            int price = listItem.getProduct().price();

            if (oldPrice > price) {
                oldPriceT.setText(Integer.toString(oldPrice)+"kr.");
                oldPriceT.setPaintFlags(oldPriceT.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                oldPriceT.setVisibility(TextView.INVISIBLE);
            }

            priceT.setText(Integer.toString(price)+"kr.");
            amountT.setText(Integer.toString(listItem.getAmount()));
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater cursorInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            return cursorInflater.inflate(R.layout.list_row, parent, false);
        }
    }
}
