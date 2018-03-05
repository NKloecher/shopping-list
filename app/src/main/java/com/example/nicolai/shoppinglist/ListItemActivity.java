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

import com.example.nicolai.shoppinglist.model.ListItem;
import com.example.nicolai.shoppinglist.model.Product;
import com.example.nicolai.shoppinglist.model.Store;
import com.example.nicolai.shoppinglist.storage.ProductStorage;
import com.example.nicolai.shoppinglist.storage.ShoppingListStorage;
import com.example.nicolai.shoppinglist.storage.StoreStorage;

public class ListItemActivity extends AppCompatActivity {

    long productId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_list_item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            productId = data.getLongExtra(ProductSelectActivity.SELECTED_PRODUCT_ID_EXTRA, -1);
            new GetProductAsyncTask().execute();
        } else {
            throw new Error("invalid requestCode or resultCode");
        }
    }

    public void onChangeProduct(View view) {
        startActivityForResult(new Intent(this, ProductSelectActivity.class), 1);
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
            price.setText(product.price());
            store.setText(product.getStore().getName());
        }
    }
}
