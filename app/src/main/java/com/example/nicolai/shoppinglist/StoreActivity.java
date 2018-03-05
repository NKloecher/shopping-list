package com.example.nicolai.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.example.nicolai.shoppinglist.model.Store;
import com.example.nicolai.shoppinglist.storage.StoreStorage;

public class StoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
    }

    public void onCreateStore(View view) {
        EditText name = findViewById(R.id.name);
        EditText address = findViewById(R.id.address);
        EditText website = findViewById(R.id.website);
        Store store = new Store(-1, name.getText().toString(), address.getText().toString(), website.getText().toString());
        new InsertAsyncTask().execute(store);
    }

    class InsertAsyncTask extends AsyncTask<Store, Void, Long> {
        @Override
        protected Long doInBackground(Store... ss) {
            StoreStorage storage = StoreStorage.getInstance(StoreActivity.this);
            return storage.insert(ss[0]);
        }

        @Override
        protected void onPostExecute(Long storeId) {
            setResult(Activity.RESULT_OK, new Intent());
            finish();
        }
    }
}
