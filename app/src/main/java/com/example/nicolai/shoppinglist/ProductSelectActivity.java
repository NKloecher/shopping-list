package com.example.nicolai.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.example.nicolai.shoppinglist.storage.ProductStorage;
import com.example.nicolai.shoppinglist.storage.StoreStorage;

public class ProductSelectActivity extends AppCompatActivity {

    public static final String SELECTED_PRODUCT_ID_EXTRA = "SELECTED_PRODUCT_ID";
    private static final int CREATE_STORE_RESULT = 1;
    private static final int CREATE_PRODUCT_RESULT = 2;
    long selectedStoreId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_select);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new GetStoresAsyncTask().execute();
        Spinner storeSpinner = findViewById(R.id.store_spinner);
        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedStoreId = l;
                new GetProductsAsyncTask().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onAddProduct(View view) {
        startActivityForResult(new Intent(this, ProductActivity.class), CREATE_PRODUCT_RESULT);
    }

    public void onCreateNewStore(View view) {
        startActivityForResult(new Intent(this, StoreActivity.class), CREATE_STORE_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == CREATE_STORE_RESULT || requestCode == CREATE_PRODUCT_RESULT) && resultCode == Activity.RESULT_OK) {
            new GetStoresAsyncTask().execute();
        }
        //todo crashes if no amount/product given when clicking "done"
        else if (resultCode == Activity.RESULT_CANCELED){
            //do nothing
        }
        else {
            throw new Error("invalid requestCode or resultCode");
        }
    }

    public class GetStoresAsyncTask extends AsyncTask<Void, Void, Cursor> {
        SimpleCursorAdapter storeAdapter = null;

        @Override
        protected Cursor doInBackground(Void... voids) {
            StoreStorage storage = StoreStorage.getInstance(ProductSelectActivity.this);
            return storage.getAll();
        }

        protected void onPostExecute(Cursor stores) {
            if (storeAdapter == null) {
                storeAdapter = new SimpleCursorAdapter(
                        ProductSelectActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        stores,
                        new String[]{StoreStorage.NAME},
                        new int[]{android.R.id.text1},
                        0);
                Spinner storeSpinner = findViewById(R.id.store_spinner);
                storeSpinner.setAdapter(storeAdapter);
            } else {
                storeAdapter.changeCursor(stores);
            }
            selectedStoreId = Math.min(stores.getCount(), 1);
            new GetProductsAsyncTask().execute();
        }
    }


    class GetProductsAsyncTask extends AsyncTask<Void,Void,Cursor> {
        SimpleCursorAdapter productAdapter = null;

        @Override
        protected Cursor doInBackground(Void... voids) {
            ProductStorage storage = ProductStorage.getInstance(ProductSelectActivity.this);
            return storage.getAllInStore(selectedStoreId);
        }

        @Override
        protected void onPostExecute(Cursor products) {
            if (productAdapter == null) {
                productAdapter = new SimpleCursorAdapter(
                        ProductSelectActivity.this,
                        android.R.layout.simple_list_item_1,
                        products,
                        new String[] {ProductStorage.NAME},
                        new int[] {android.R.id.text1},
                        0);
                ListView productList = findViewById(R.id.product_list);
                productList.setAdapter(productAdapter);
                productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        Intent i = new Intent();
                        i.putExtra(SELECTED_PRODUCT_ID_EXTRA, id);
                        setResult(Activity.RESULT_OK, i);
                        finish();
                    }
                });
            } else {
                productAdapter.changeCursor(products);
            }
        }
    }
}
