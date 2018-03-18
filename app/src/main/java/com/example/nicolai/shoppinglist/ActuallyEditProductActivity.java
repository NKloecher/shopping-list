package com.example.nicolai.shoppinglist;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nicolai.shoppinglist.model.Deal;
import com.example.nicolai.shoppinglist.model.Product;
import com.example.nicolai.shoppinglist.model.ShoppingList;
import com.example.nicolai.shoppinglist.model.Store;
import com.example.nicolai.shoppinglist.storage.DealStorage;
import com.example.nicolai.shoppinglist.storage.ListItemStorage;
import com.example.nicolai.shoppinglist.storage.ProductStorage;
import com.example.nicolai.shoppinglist.storage.ShoppingListStorage;
import com.example.nicolai.shoppinglist.storage.StoreStorage;

public class ActuallyEditProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actually_edit_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button remove = findViewById(R.id.remove_product_button);
        remove.setVisibility(View.INVISIBLE);
        if (getIntent().getLongExtra("ProductID", -1) != -1){
            remove.setVisibility(View.VISIBLE);
            new GetAsyncTask().execute();
            toolbar.setTitle("Update Product");
        }

    }

    public void onCreateProduct(View view){
        if (getIntent().getLongExtra("ProductID", -1) != -1){
            new UpdateAsyncTask().execute();
        }
        else {
            new InsertAsyncTask().execute();
        }
    }

    public void onDeleteProduct(View view){
        new DeleteAsyncTask().execute();
    }


    public void toastMaker(String string){
        final String text = string;
        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(ActuallyEditProductActivity.this, text, Toast.LENGTH_SHORT).show();
            }
    });
//        Toast.makeText(ActuallyEditProductActivity.this, string,Toast.LENGTH_LONG);
    }



//todo fucking deal productStorage....................
    class DeleteAsyncTask extends AsyncTask<Void, Void, Void>{

    private ProductStorage productStorage;
    private Product product;

    //test to remove item references everywhere
    private ListItemStorage itemStorage;


    @Override
    protected Void doInBackground(Void... voids) {
        productStorage = ProductStorage.getInstance(ActuallyEditProductActivity.this);
        product = productStorage.get(getIntent().getLongExtra("ProductID",-1));
        productStorage.remove(product);

        //todo should probably remove deals too...
        //todo should remove from shopping list too

        itemStorage = ListItemStorage.getInstance(ActuallyEditProductActivity.this);

        Cursor itemList = itemStorage.getAll();
        try {
            while (itemList.moveToNext()){
                if (itemList.getLong(itemList.getColumnIndex("PRODUCT_ID")) == product.getId()){
                    itemStorage.remove(itemList.getInt(itemList.getColumnIndex("_id")));
                }
            }
        }finally {
            itemList.close();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        //return another result
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }
}

    class InsertAsyncTask extends AsyncTask<Void, Void,Void>{

        private ProductStorage storage;
        private long storeId = getIntent().getLongExtra("StoreID",-1);

        @Override
        protected Void doInBackground(Void... voids) {

            //todo checking for incorrect values!
            int price = Integer.parseInt(((EditText) findViewById(R.id.edit_product_price)).getText().toString());
            String name = ((EditText) findViewById(R.id.edit_product_name)).getText().toString();
            Long dealID = null;
            EditText deal = findViewById(R.id.edit_product_deal);
            if (!deal.getText().toString().equals("")){
                dealID = DealStorage.getInstance(ActuallyEditProductActivity.this).insert(Integer.parseInt(deal.getText().toString()),storeId);
            }

            storage = ProductStorage.getInstance(ActuallyEditProductActivity.this);
            storage.insert(price, name, storeId, dealID);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setResult(Activity.RESULT_OK, new Intent());
            finish();
        }
    }

    class UpdateAsyncTask extends AsyncTask<Void, Void,Void>{


        private DealStorage dealStorage;
        private ProductStorage productStorage;
        private StoreStorage storeStorage;
        private Product product;

        @Override
        protected Void doInBackground(Void... voids) {

        String name = ((EditText) findViewById(R.id.edit_product_name)).getText().toString();
        String price = ((EditText) findViewById(R.id.edit_product_price)).getText().toString();
        String dealText = ((EditText) findViewById(R.id.edit_product_deal)).getText().toString();
        int dealTextInt = -1;
        if (!dealText.equals("")) dealTextInt = Integer.parseInt(dealText);

        dealStorage = DealStorage.getInstance(ActuallyEditProductActivity.this);
        storeStorage = StoreStorage.getInstance(ActuallyEditProductActivity.this);
        productStorage = ProductStorage.getInstance(ActuallyEditProductActivity.this);
        product = productStorage.get(getIntent().getLongExtra("ProductID",-1));

        if (name.equals("")){
            //todo !!HANDLER IS DONE!!
            toastMaker("YOU NEED A NAME FOR THIS TO BE VALID!!");
            cancel(true);
        }

        ContentValues values = new ContentValues();
        values.put("PRICE", price);
        values.put("NAME", name);
        //inserts a new deal every time....................................
        if (dealTextInt != -1) values.put("DEAL_ID", dealStorage.insert(dealTextInt, getIntent().getLongExtra("StoreID",-1)));
        else {
            //Remove deal from deal storage......
            values.putNull("DEAL_ID");
        }

        productStorage.update(product.getId(),values);
        return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setResult(Activity.RESULT_OK, new Intent());
            finish();
        }
    }


    class GetAsyncTask extends AsyncTask<Void,Void,Void>{

    private ProductStorage storage;
    private Product product;
    @Override
    protected Void doInBackground(Void... voids) {
        storage = ProductStorage.getInstance(ActuallyEditProductActivity.this);
        product = storage.get(getIntent().getLongExtra("ProductID",-1));
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ((EditText) findViewById(R.id.edit_product_name)).setText(product.getName());
        ((EditText) findViewById(R.id.edit_product_price)).setText(Integer.toString(product.getPrice()));
      if (product.getDeal() != null)((EditText) findViewById(R.id.edit_product_deal)).setText(""+product.getDeal().price());
      else ((EditText) findViewById(R.id.edit_product_deal)).setText("");
    }
}

}
