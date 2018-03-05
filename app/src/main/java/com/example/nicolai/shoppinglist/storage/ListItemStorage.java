package com.example.nicolai.shoppinglist.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.example.nicolai.shoppinglist.model.ListItem;
import com.example.nicolai.shoppinglist.model.Product;

public class ListItemStorage {
    public static final String TABLE_NAME = "LIST_ITEM";
    public static final String _id = "_id";
    public static final String AMOUNT = "AMOUNT";
    public static final String PRODUCT_ID = "PRODUCT_ID";
    public static final String SHOPPING_LIST_ID = "SHOPPING_LIST_ID";

    private static ListItemStorage instance;
    private static ShoppingListOpenHelper openHelper;
    private static ProductStorage ps;

    public static ListItemStorage getInstance(Context context) {
        if (instance == null) instance = new ListItemStorage(context);
        return instance;
    }

    private ListItemStorage(Context context) {
        openHelper = ShoppingListOpenHelper.getInstance(context);
        ps = ProductStorage.getInstance(context);
    }

    public long insert(ListItem listItem, int listId) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AMOUNT, listItem.getAmount());
        values.put(PRODUCT_ID, listItem.getProduct().getId());
        values.put(SHOPPING_LIST_ID, listId);
        return db.insert(TABLE_NAME, null, values);
    }

    public long remove(ListItem listItem) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db.delete(TABLE_NAME, "_id=?", new String[] {Long.toString(listItem.getId())});
    }

    public long update(ListItem listItem, int listId) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AMOUNT, listItem.getAmount());
        values.put(PRODUCT_ID, listItem.getProduct().getId());
        values.put(SHOPPING_LIST_ID, listId);
        return db.update(TABLE_NAME, values, "_id=?", new String[] {Long.toString(listItem.getId())});
    }

    public ListItem get(long id) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        ListItemWrapper cursor = new ListItemWrapper(db.query(TABLE_NAME, new String[]{_id, AMOUNT, PRODUCT_ID}, "_id=?", new String[] {Long.toString(id)},
            null, null,null,null));
        cursor.moveToNext();
        return cursor.get();
    }

    public ListItemWrapper getAll() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return new ListItemWrapper(db.query(TABLE_NAME, new String[]{_id, AMOUNT, PRODUCT_ID}, null, null, null, null, null, null));
    }

    public ListItemWrapper getAllItemsInList(long listId) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return new ListItemWrapper(db.query(TABLE_NAME, new String[]{_id, AMOUNT, PRODUCT_ID}, SHOPPING_LIST_ID + "=?" , new String[]{Long.toString(listId)}, null, null, null, null));
    }

    public class ListItemWrapper extends CursorWrapper {
        public ListItemWrapper(Cursor cursor) {
            super(cursor);
        }

        public ListItem get() {
            if (isBeforeFirst() || isAfterLast()) return null;

            int productId = getInt(getColumnIndex(PRODUCT_ID));
            Product p = ps.get(productId);

            return new ListItem(
                getInt(getColumnIndex(_id)),
                p,
                getInt(getColumnIndex(AMOUNT)));
        }
    }
}
