package com.example.nicolai.shoppinglist.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.example.nicolai.shoppinglist.model.Deal;
import com.example.nicolai.shoppinglist.model.Product;
import com.example.nicolai.shoppinglist.model.Store;

public class ProductStorage {
    public static final String TABLE_NAME = "PRODUCT";
    public static final String _id = "_id";
    public static final String PRICE = "PRICE";
    public static final String NAME = "NAME";
    public static final String DEAL_ID = "DEAL_ID";
    public static final String STORE_ID = "STORE_ID";


    private static ProductStorage instance;
    private static ShoppingListOpenHelper openHelper;
    private static DealStorage ds;
    private static StoreStorage ss;

    public static ProductStorage getInstance(Context context) {
        if (instance == null) instance = new ProductStorage(context);
        return instance;
    }

    private ProductStorage(Context context) {
        openHelper = ShoppingListOpenHelper.getInstance(context);
        ds = DealStorage.getInstance(context);
        ss = StoreStorage.getInstance(context);
    }

    public long insert(int price, String name, long storeId, Long dealId) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRICE, price);
        values.put(NAME, name);
        values.put(DEAL_ID, dealId);
        values.put(STORE_ID, storeId);
        return db.insert(TABLE_NAME, null, values);
    }

    public long remove(Product p) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db.delete(TABLE_NAME, "_id=?", new String[] {Long.toString(p.getId())});
    }

    public long update(long id, ContentValues values) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db.update(TABLE_NAME, values, "_id=?", new String[] {Long.toString(id)});
    }

    public Product get(long id) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        ProductWrapper cursor = new ProductWrapper(db.query(TABLE_NAME, new String[]{_id, PRICE, NAME, DEAL_ID, STORE_ID}, "_id=?", new String[] {Long.toString(id)},
                 null, null,null,null));
        cursor.moveToNext();
        return cursor.get();
    }

    public ProductWrapper getAll() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return new ProductWrapper(db.query(TABLE_NAME, new String[]{_id, PRICE, NAME, DEAL_ID, STORE_ID}, null, null, null, null, null, null));
    }

    public Cursor getAllInStore(long storeId) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return new ProductWrapper(db.query(TABLE_NAME, new String[]{_id, PRICE, NAME, DEAL_ID, STORE_ID}, STORE_ID + "=?", new String[] {Long.toString(storeId)}, null, null, null, null));
    }

    public class ProductWrapper extends CursorWrapper {
        public ProductWrapper(Cursor cursor) {
            super(cursor);
        }

        public Product get() {
            if (isBeforeFirst() || isAfterLast()) return null;

            int dealId = getInt(getColumnIndex(DEAL_ID));
            int storeId = getInt(getColumnIndex(STORE_ID));
            Deal d = ds.get(dealId);
            Store s = ss.get(storeId);

            return new Product(getInt(getColumnIndex(_id)),
                    getInt(getColumnIndex(PRICE)),
                    getString(getColumnIndex(NAME)),
                    s,
                    d);
        }
    }
}
