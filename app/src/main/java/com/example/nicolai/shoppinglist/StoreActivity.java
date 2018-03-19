package com.example.nicolai.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.example.nicolai.shoppinglist.model.Store;
import com.example.nicolai.shoppinglist.storage.ListItemStorage;
import com.example.nicolai.shoppinglist.storage.ProductStorage;
import com.example.nicolai.shoppinglist.storage.StoreStorage;

public class StoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button remove = findViewById(R.id.remove_button);
        remove.setVisibility(View.INVISIBLE);
        if (getIntent().getLongExtra("Store", -1) != -1){
            remove.setVisibility(View.VISIBLE);
            new GetAsyncTask().execute();
            toolbar.setTitle("Update Store");
        }
    }

    public void onCreateStore(View view) {
        EditText name = findViewById(R.id.name);
        EditText address = findViewById(R.id.address);
        EditText website = findViewById(R.id.website);
        if (getIntent().getLongExtra("Store", -1) != -1){
            new UpdateAsyncTask(name.getText().toString(), address.getText().toString(), website.getText().toString()).execute();
        }
        else new InsertAsyncTask(name.getText().toString(), address.getText().toString(), website.getText().toString()).execute();
    }

    public void onDeleteStore(View view){
        new DeleteAsyncTask().execute();
    }

    class DeleteAsyncTask extends AsyncTask<Void,Void,Void>{

        private StoreStorage storage;
        private Store store;

        //test to remove item references everywhere
        private ListItemStorage itemStorage;
        private ProductStorage productStorage;


        @Override
        protected Void doInBackground(Void... voids) {
            storage = StoreStorage.getInstance(StoreActivity.this);
            store = storage.get(getIntent().getLongExtra("Store",-1));
            storage.remove(store);
            //todo Remove list associations! and all items from lists-- Done I think!

            itemStorage = ListItemStorage.getInstance(StoreActivity.this);
            productStorage = ProductStorage.getInstance(StoreActivity.this);

            //so ineffective.....
            Cursor itemList = itemStorage.getAll();
            try {
                while (itemList.moveToNext()){
                    Cursor productList = productStorage.getAll();

                    try {
                        while ((productList.moveToNext())){
                    if (store.getId() == productList.getLong(productList.getColumnIndex("STORE_ID")) && itemList.getLong(itemList.getColumnIndex("PRODUCT_ID")) == productList.getLong(productList.getColumnIndex("_id"))){
                        itemStorage.remove(itemList.getInt(itemList.getColumnIndex("_id")));
                    }

                        }
                    }finally {
                        productList.close();
                    }
                }
            }finally {
                itemList.close();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setResult(Activity.RESULT_OK, new Intent());
            finish();
        }
    }

    class UpdateAsyncTask extends AsyncTask<Void, Void, Void>{

        String name;
        String address;
        String website;

        public UpdateAsyncTask(String name, String address, String website) {
            this.name = name;
            this.address = address;
            this.website = website;
        }

        private StoreStorage storage;
        private Store store;
        @Override
        protected Void doInBackground(Void... voids) {
            storage = StoreStorage.getInstance(StoreActivity.this);
            store = storage.get(getIntent().getLongExtra("Store",-1));
            store.setName(name);
            store.setAddress(address);
            store.setWebsite(website);
            storage.update(store);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setResult(Activity.RESULT_OK, new Intent());
            finish();
        }
    }

    class GetAsyncTask extends AsyncTask<Void, Void, Void>{

        private StoreStorage storage;
        private Store store;
        @Override
        protected Void doInBackground(Void... voids) {
            storage = StoreStorage.getInstance(StoreActivity.this);
            store = storage.get(getIntent().getLongExtra("Store",-1));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ((EditText) findViewById(R.id.name)).setText(store.getName());
            ((EditText) findViewById(R.id.address)).setText(store.getAddress());
            ((EditText) findViewById(R.id.website)).setText(store.getWebsite());
        }
    }

    class InsertAsyncTask extends AsyncTask<Store, Void, Long> {
        String name;
        String address;
        String website;

        public InsertAsyncTask(String name, String address, String website) {
            this.name = name;
            this.address = address;
            this.website = website;
        }

        @Override
        protected Long doInBackground(Store... ss) {
            StoreStorage storage = StoreStorage.getInstance(StoreActivity.this);
            return storage.insert(name, address, website);
        }

        @Override
        protected void onPostExecute(Long storeId) {
            setResult(Activity.RESULT_OK, new Intent());
            finish();
        }
    }


}
