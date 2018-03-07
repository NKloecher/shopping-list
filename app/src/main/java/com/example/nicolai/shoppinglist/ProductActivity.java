package com.example.nicolai.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nicolai.shoppinglist.model.Deal;
import com.example.nicolai.shoppinglist.model.Product;
import com.example.nicolai.shoppinglist.model.Store;
import com.example.nicolai.shoppinglist.storage.DealStorage;
import com.example.nicolai.shoppinglist.storage.ProductStorage;
import com.example.nicolai.shoppinglist.storage.StoreStorage;

public class ProductActivity extends AppCompatActivity {

    private static final int CREATE_STORE_RESULT = 1;
    private static long selectedStoreId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new GetStoresAsyncTask().execute();
        Spinner storeSpinner = findViewById(R.id.store_spinner);
        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                selectedStoreId = id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    public void onCreateNewStore(View view) {
        startActivityForResult(new Intent(this, StoreActivity.class), CREATE_STORE_RESULT);
    }

    public void onCreateProduct(View view) {
        if (selectedStoreId == -1) {
            Toast.makeText(this, "no store selected", Toast.LENGTH_LONG).show();
            return;
        }

        EditText name = findViewById(R.id.name);
        EditText price = findViewById(R.id.price);
        EditText deal = findViewById(R.id.deal);

        if (name.getText().length() == 0) {
            Toast.makeText(this, "name is required", Toast.LENGTH_LONG).show();
            return;
        }
        if (price.getText().length() == 0) {
            Toast.makeText(this, "price is required", Toast.LENGTH_LONG).show();
            return;
        }

        Long dealId = null;

        if (deal.getText().length() > 0) {
            int d = Integer.parseInt(deal.getText().toString());
            dealId = DealStorage.getInstance(this).insert(d, selectedStoreId);
        }

        ProductStorage.getInstance(this).insert(Integer.parseInt(price.getText().toString()), name.getText().toString(), selectedStoreId, dealId);

        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_STORE_RESULT && resultCode == Activity.RESULT_OK) {
            new GetStoresAsyncTask().execute();
        }
        else {
            throw new Error("invalid requestCode or resultCode");
        }
    }

    public class GetStoresAsyncTask extends AsyncTask<Void, Void, Cursor> {
        SimpleCursorAdapter storeAdapter = null;

        @Override
        protected Cursor doInBackground(Void... voids) {
            StoreStorage storage = StoreStorage.getInstance(ProductActivity.this);
            return storage.getAll();
        }

        protected void onPostExecute(Cursor stores) {
            if (storeAdapter == null) {
                storeAdapter = new SimpleCursorAdapter(
                        ProductActivity.this,
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
        }
    }
}
