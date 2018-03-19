package com.example.nicolai.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.nicolai.shoppinglist.storage.ProductStorage;

public class EditProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new GetProductsAsyncTask().execute();
    }

    public void onCreateProduct(View view){
        Intent i = new Intent(EditProductActivity.this, ActuallyEditProductActivity.class);
        i.putExtra("StoreID", getIntent().getLongExtra("Store",-1));
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            new GetProductsAsyncTask().execute();
        }
        else if (resultCode == Activity.RESULT_CANCELED){
            //do nothing
        }
        else {
            throw new Error("Ow");
        }
    }

    class GetProductsAsyncTask extends AsyncTask<Void,Void,Cursor> {
        SimpleCursorAdapter productAdapter = null;

        @Override
        protected Cursor doInBackground(Void... voids) {
            long selectedStoreId = getIntent().getLongExtra("Store", -1);
            ProductStorage storage = ProductStorage.getInstance(EditProductActivity.this);
            return storage.getAllInStore(selectedStoreId);
        }

        @Override
        protected void onPostExecute(Cursor products) {
            if (productAdapter == null) {
                productAdapter = new SimpleCursorAdapter(
                        EditProductActivity.this,
                        android.R.layout.simple_list_item_1,
                        products,
                        new String[] {ProductStorage.NAME},
                        new int[] {android.R.id.text1},
                        0);
                ListView productList = findViewById(R.id.edit_products_list);
                productList.setAdapter(productAdapter);
                productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        Intent i = new Intent(EditProductActivity.this, ActuallyEditProductActivity.class);
                        i.putExtra("ProductID", id);
                        i.putExtra("StoreID", getIntent().getLongExtra("Store",-1));
                        startActivityForResult(i, 1);
                    }
                });
            } else {
                productAdapter.changeCursor(products);
            }
        }
    }
}
