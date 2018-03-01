package com.example.nicolai.shoppinglist.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.example.nicolai.shoppinglist.model.Deal;
import com.example.nicolai.shoppinglist.model.Product;
import com.example.nicolai.shoppinglist.model.Store;

/**
 * Created by mathias on 01/03/2018.
 */

public class DealStorage {
    private static final String TABLE_NAME = "DEAL";
    private static final String _id = "_id";
    private static final String DEAL_PRICE = "DEAL_PRICE";
    private static final String STORE_ID = "STORE_ID";

    private static DealStorage instance;
    private static ShoppingListOpenHelper openHelper;
    private  static StoreStorage ss;

    public static DealStorage getInstance(Context context) {
        if (instance == null) instance = new DealStorage(context);
        return instance;
    }

    private DealStorage(Context context) {
        openHelper = ShoppingListOpenHelper.getInstance(context);
        ss = ss.getInstance(context);
    }

    public long insert(Deal d) {
        try (SQLiteDatabase db = openHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(DEAL_PRICE, d.price());
            values.put(STORE_ID, d.getStore().getId());
            return db.insert(TABLE_NAME, null, values);
        }
    }

    public long remove(Deal d) {
        try (SQLiteDatabase db = openHelper.getWritableDatabase()) {
            return db.delete(TABLE_NAME, "_id=?", new String[] {Long.toString(d.getId())});
        }
    }

    public long update(Deal d) {
        try (SQLiteDatabase db = openHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(DEAL_PRICE, d.price());
            values.put(STORE_ID, d.getStore().getId());
            return db.update(TABLE_NAME, values, "_id=?", new String[] {Long.toString(d.getId())});
        }
    }

    public Deal get(long id) {
        try (SQLiteDatabase db = openHelper.getReadableDatabase();
             DealWrapper cursor = new DealWrapper(db.query(TABLE_NAME, new String[]{_id, DEAL_PRICE, STORE_ID}, "_id=?", new String[] {Long.toString(id)},
                     null, null,null,null))) {
            cursor.moveToNext();
            return cursor.get();
        }
    }

    public DealWrapper getAll() {
        try (SQLiteDatabase db = openHelper.getReadableDatabase()) {
            return new DealWrapper(db.query(TABLE_NAME, new String[]{_id, DEAL_PRICE, STORE_ID}, null, null, null, null, null, null));
        }
    }

    class DealWrapper extends CursorWrapper {
        public DealWrapper(Cursor cursor) {
            super(cursor);
        }

        public Deal get() {
            if (isBeforeFirst() || isAfterLast()) return null;

            int storeId = getInt(getColumnIndex(STORE_ID));
            Store s = ss.get(storeId);

            return new Deal(
                    getInt(getColumnIndex(_id)),
                    getInt(getColumnIndex(DEAL_PRICE)),
                    s);
        }
    }
}