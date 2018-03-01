package com.example.nicolai.shoppinglist.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.example.nicolai.shoppinglist.model.Store;

public class StoreStorage {
    public static final String TABLE_NAME = "STORE";
    public static final String _id = "_id";
    public static final String NAME = "NAME";
    public static final String ADDRESS = "ADDRESS";
    public static final String WEBSITE = "WEBSITE";

    private static StoreStorage instance;
    private static ShoppingListOpenHelper openHelper;

    public static StoreStorage getInstance(Context context) {
        if (instance == null) instance = new StoreStorage(context);
        return instance;
    }

    private StoreStorage(Context context) {
        openHelper = ShoppingListOpenHelper.getInstance(context);
    }

    public long insert(Store s) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, s.getName());
        values.put(ADDRESS, s.getAddress());
        values.put(WEBSITE, s.getWebsite());
        return db.insert(TABLE_NAME, null, values);
    }

    public long remove(Store s) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db.delete(TABLE_NAME, "_id=?", new String[] {Long.toString(s.getId())});
    }

    public long update(Store s) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, s.getName());
        values.put(ADDRESS, s.getAddress());
        values.put(WEBSITE, s.getWebsite());
        return db.update(TABLE_NAME, values, "_id=?", new String[] {Long.toString(s.getId())});
    }

    public Store get(long id) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        StoreWrapper cursor = new StoreWrapper(db.query(TABLE_NAME, new String[]{_id, NAME, ADDRESS, WEBSITE}, "_id=?", new String[] {Long.toString(id)},
                 null, null,null,null));
        cursor.moveToNext();
        return cursor.get();
    }

    public StoreWrapper getAll() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return new StoreWrapper(db.query(TABLE_NAME, new String[]{_id, NAME, ADDRESS, WEBSITE}, null, null, null, null, null, null));
    }

    class StoreWrapper extends CursorWrapper {
        public StoreWrapper(Cursor cursor) {
            super(cursor);
        }

        public Store get() {
            if (isBeforeFirst() || isAfterLast()) return null;

            return new Store(
                    getInt(getColumnIndex(_id)),
                    getString(getColumnIndex(NAME)),
                    getString(getColumnIndex(ADDRESS)),
                    getString(getColumnIndex(WEBSITE)));
        }
    }
}