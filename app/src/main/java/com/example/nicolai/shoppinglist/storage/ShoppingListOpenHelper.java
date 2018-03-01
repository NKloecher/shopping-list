package com.example.nicolai.shoppinglist.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.example.nicolai.shoppinglist.model.ShoppingList;

/**
 * Created by Nicolai on 01-03-2018.
 */

public class ShoppingListOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "SHOPPINGLIST";
    private static final int VERSION = 1;
    private static ShoppingListOpenHelper instance;

    public static ShoppingListOpenHelper getInstance(Context context){
        if (instance == null){
            instance = new ShoppingListOpenHelper(context);
        }
        return instance;
    }

    private ShoppingListOpenHelper(Context context){
        super(context.getApplicationContext(), DB_NAME, null,VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDb(db,0, VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDb(db,oldVersion, newVersion);
    }

    private void updateDb(SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion < 1){
            db.execSQL("CREATE TABLE SHOPPINGLIST(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NAME TEXT)");
        }
    }
}
