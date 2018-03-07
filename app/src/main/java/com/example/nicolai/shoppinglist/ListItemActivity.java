package com.example.nicolai.shoppinglist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolai.shoppinglist.model.ListItem;
import com.example.nicolai.shoppinglist.model.Product;
import com.example.nicolai.shoppinglist.model.Store;
import com.example.nicolai.shoppinglist.storage.ListItemStorage;
import com.example.nicolai.shoppinglist.storage.ProductStorage;
import com.example.nicolai.shoppinglist.storage.ShoppingListStorage;
import com.example.nicolai.shoppinglist.storage.StoreStorage;

public class ListItemActivity extends AppCompatActivity {

    public static final String LIST_ID_EXTRA = "LIST_ID";
    private static final int PRODUCT_SELECT_RESULT = 1;

    long listId;
    long productId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_list_item);
        listId = getIntent().getExtras().getLong(LIST_ID_EXTRA);
    }

    public void onDone(View view) {
        if (productId == -1) {
            Toast.makeText(this, "no product selected", Toast.LENGTH_LONG).show();
            return;
        }

        Product p = new Product(productId, -1, null, null, null);
        int amount = Integer.parseInt(((EditText)findViewById(R.id.amount)).getText().toString());
        ListItem listItem = new ListItem(-1, p, amount);
        ListItemStorage.getInstance(ListItemActivity.this).insert(listItem, listId);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PRODUCT_SELECT_RESULT && resultCode == Activity.RESULT_OK) {
            productId = data.getLongExtra(ProductSelectActivity.SELECTED_PRODUCT_ID_EXTRA, -1);


            new GetProductAsyncTask().execute();
        }
        else if (resultCode == Activity.RESULT_CANCELED){
            //Working as intended
        }
        else {
            throw new Error("invalid requestCode or resultCode");
        }
    }

    public void onChangeProduct(View view) {
        startActivityForResult(new Intent(this, ProductSelectActivity.class), PRODUCT_SELECT_RESULT);
    }

    class GetProductAsyncTask extends AsyncTask<Void, Void, Product> {
        @Override
        protected Product doInBackground(Void... voids) {
            ProductStorage storage = ProductStorage.getInstance(ListItemActivity.this);
            return storage.get(productId);
        }

        @Override
        protected void onPostExecute(Product product) {
            TextView name = findViewById(R.id.name);
            TextView price = findViewById(R.id.price);
            TextView store = findViewById(R.id.store);

            name.setText(product.getName());
            price.setText(Integer.toString(product.price()));
            store.setText(product.getStore().getName());
        }
    }
}
