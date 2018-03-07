package com.example.nicolai.shoppinglist.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.example.nicolai.shoppinglist.model.Deal;
import com.example.nicolai.shoppinglist.model.Store;

public class DealStorage {
    public static final String TABLE_NAME = "DEAL";
    public static final String _id = "_id";
    public static final String DEAL_PRICE = "DEAL_PRICE";
    public static final String STORE_ID = "STORE_ID";

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

    public long insert(int deal, long storeId) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DEAL_PRICE, deal);
        values.put(STORE_ID, storeId);
        return db.insert(TABLE_NAME, null, values);
    }

    public long remove(Deal d) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db.delete(TABLE_NAME, "_id=?", new String[] {Long.toString(d.getId())});
    }

    public long update(Deal d) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DEAL_PRICE, d.price());
        values.put(STORE_ID, d.getStore().getId());
        return db.update(TABLE_NAME, values, "_id=?", new String[] {Long.toString(d.getId())});
    }

    public Deal get(long id) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
         DealWrapper cursor = new DealWrapper(db.query(TABLE_NAME, new String[]{_id, DEAL_PRICE, STORE_ID}, "_id=?", new String[] {Long.toString(id)},
                 null, null,null,null));
        cursor.moveToNext();
        return cursor.get();
    }

    public DealWrapper getAll() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return new DealWrapper(db.query(TABLE_NAME, new String[]{_id, DEAL_PRICE, STORE_ID}, null, null, null, null, null, null));
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
