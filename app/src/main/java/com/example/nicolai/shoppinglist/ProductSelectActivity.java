package com.example.nicolai.shoppinglist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.nicolai.shoppinglist.storage.StoreStorage;

public class ProductSelectActivity extends AppCompatActivity {

    public static final String SELECTED_PRODUCT_ID_EXTRA = "SELECTED_PRODUCT_ID";
    SimpleCursorAdapter storeAdapter = null;
    ProductAdapter productAdapter = null;
    long selectedStoreId = -1;

    ProductStorage productStorage = ProductStorage.getInstance(ProductSelectActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_select);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new GetStoresAsyncTask().execute();
        Spinner stores = findViewById(R.id.store_spinner);
        stores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedStoreId = l;
                new GetStoresAsyncTask().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onAddProduct(View view) {
        if (selectedStoreId == -1) {
            Toast.makeText(this, "No store selected", Toast.LENGTH_LONG).show();
            return;
        }

        productStorage.insert(new Product(-1, 0, "", new Store(selectedStoreId, null, null, null), null));
        new GetProductsAsyncTask().execute();
    }

    public void onCreateNewStore(View view) {
        startActivityForResult(new Intent(this, StoreActivity.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            new GetStoresAsyncTask().execute();
        } else {
            throw new Error("invalid requestCode or resultCode");
        }
    }



    class GetStoresAsyncTask extends AsyncTask<Void,Void,Cursor> {
        @Override
        protected Cursor doInBackground(Void... voids) {
            StoreStorage storage = StoreStorage.getInstance(ProductSelectActivity.this);
            return storage.getAll();
        }

        @Override
        protected void onPostExecute(Cursor stores) {
            if (storeAdapter == null) {
                storeAdapter = new SimpleCursorAdapter(
                        ProductSelectActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        stores,
                        new String[] {StoreStorage.NAME},
                        new int[] {android.R.id.text1},
                        0);
                Spinner spinner = findViewById(R.id.store_spinner);
                spinner.setAdapter(storeAdapter);
            } else {
                storeAdapter.changeCursor(stores);
            }
        }
    }

    class GetProductsAsyncTask extends AsyncTask<Void,Void,Cursor> {
        @Override
        protected Cursor doInBackground(Void... voids) {
            ProductStorage storage = ProductStorage.getInstance(ProductSelectActivity.this);
            return storage.getAllInStore(selectedStoreId);
        }

        @Override
        protected void onPostExecute(Cursor products) {
            if (productAdapter == null) {
                productAdapter = new ProductAdapter(ProductSelectActivity.this, products, 0);
                ListView productList = findViewById(R.id.product_list);
                productList.setAdapter(productAdapter);
                productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        Intent i = new Intent();
                        i.putExtra(SELECTED_PRODUCT_ID_EXTRA, id);
                        setResult(Activity.RESULT_OK, i);
                    }
                });
            } else {
                productAdapter.changeCursor(products);
            }
        }
    }

    class ProductAdapter extends CursorAdapter {
        public ProductAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final Product product = ((ProductStorage.ProductWrapper)cursor).get();
            final EditText name = view.findViewById(R.id.name);
            final EditText price = view.findViewById(R.id.price);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater cursorInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            return cursorInflater.inflate(R.layout.product_row, parent, false);
        }
    }
}
