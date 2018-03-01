package com.example.nicolai.shoppinglist.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.nicolai.shoppinglist.model.ShoppingList;

public class ShoppingListStorage {
    private static final String NAME = "NAME";
    private static final String ID = "_id";
    private static final String SHOPPINGLIST = "SHOPPINGLIST";


    private static ShoppingListStorage instance;
    private static ShoppingListOpenHelper helper;

    public static ShoppingListStorage getInstance(Context context){
        if (instance == null){
            instance = new ShoppingListStorage(context);
        }
        return instance;
    }

    private ShoppingListStorage(Context context){
        helper = ShoppingListOpenHelper.getInstance(context);
    }

    public long insert(ShoppingList list){
        try (SQLiteDatabase db = helper.getWritableDatabase()){
        ContentValues shoppingListValues = new ContentValues();
        shoppingListValues.put(NAME, list.getName());
        return db.insert(SHOPPINGLIST, null, shoppingListValues);
        }
    }

    public long update(ShoppingList list, String name){
        try (SQLiteDatabase db = helper.getWritableDatabase()){
            ContentValues values = new ContentValues();
            values.put(NAME, name);
            return db.update(SHOPPINGLIST, values, "_id=?", new String[] {Long.toString(list.getId())});
        }
    }

    public long remove(ShoppingList list){
        try (SQLiteDatabase db = helper.getWritableDatabase()){
            return db.delete(list.getName(), "_id=?", new String[] {Long.toString(list.getId())});
        }
    }

    public ShoppingList get(long id){
        try (SQLiteDatabase db = helper.getReadableDatabase();
        ShoppingListWrapper wrapper = new ShoppingListWrapper(db.query(SHOPPINGLIST, new String[]{NAME,ID},"_id=?",
                new String[]{Long.toString(id)},null,null,null,null ))){
            wrapper.moveToNext();
            return wrapper.get();
        }
    }

    public ShoppingListWrapper getAll(){
        try (SQLiteDatabase db = helper.getReadableDatabase()){
             return new ShoppingListWrapper(db.query(SHOPPINGLIST, new String[]{NAME,ID},
             null,null,null,null,null,null ));
        }
    }

    public class ShoppingListWrapper extends CursorWrapper{

        public ShoppingListWrapper(Cursor cursor) {
            super(cursor);
        }

        public ShoppingList get(){
            if (isBeforeFirst() || isAfterLast())
                return null;
            String nameText = getString(getColumnIndex(NAME));
            int id = getInt(getColumnIndex(ID));
            return new ShoppingList(nameText, id);
        }
    }



}
