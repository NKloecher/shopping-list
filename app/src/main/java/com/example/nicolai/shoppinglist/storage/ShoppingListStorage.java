package com.example.nicolai.shoppinglist.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.nicolai.shoppinglist.model.ListItem;
import com.example.nicolai.shoppinglist.model.ShoppingList;

import java.util.ArrayList;

public class ShoppingListStorage {
    public static final String NAME = "NAME";
    public static final String ID = "_id";
    public static final String TABLE_NAME = "SHOPPING_LIST";


    private static ShoppingListStorage instance;
    private static ShoppingListOpenHelper helper;
    private static Context context;

    public static ShoppingListStorage getInstance(Context context){
        if (instance == null){
            instance = new ShoppingListStorage(context);
        }
        return instance;
    }

    private ShoppingListStorage(Context context){
        helper = ShoppingListOpenHelper.getInstance(context);
    }

    public long insert(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues shoppingListValues = new ContentValues();
        shoppingListValues.put(NAME, name);
        return db.insert(TABLE_NAME, null, shoppingListValues);
    }

    public long update(ShoppingList list, String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        return db.update(TABLE_NAME, values, "_id=?", new String[] {Long.toString(list.getId())});
    }

    public long remove(long id){
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(TABLE_NAME, "_id=?", new String[] {Long.toString(id)});
    }

    public ShoppingList get(long id){
        SQLiteDatabase db = helper.getReadableDatabase();
        ShoppingListWrapper wrapper = new ShoppingListWrapper(db.query(TABLE_NAME, new String[]{NAME,ID},"_id=?",
                new String[]{Long.toString(id)},null,null,null,null ));
        wrapper.moveToNext();
        return wrapper.get();
    }

    public ShoppingListWrapper getAll(){
        SQLiteDatabase db = helper.getReadableDatabase();
        return new ShoppingListWrapper(db.query(TABLE_NAME, new String[] {ID, NAME}, null, null, null, null, null, null));
    }

    class ShoppingListWrapper extends CursorWrapper{

        public ShoppingListWrapper(Cursor cursor) {
            super(cursor);
        }

        public ShoppingList get(){
            if (isBeforeFirst() || isAfterLast())
                return null;

            String nameText = getString(getColumnIndex(NAME));
            int id = getInt(getColumnIndex(ID));
            ListItemStorage listItemStorage = ListItemStorage.getInstance(context);
            ListItemStorage.ListItemWrapper items = listItemStorage.getAllItemsInList(id);
            return new ShoppingList(nameText, id, items);
        }
    }
}
