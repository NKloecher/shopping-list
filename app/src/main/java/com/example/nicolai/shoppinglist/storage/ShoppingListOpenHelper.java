package com.example.nicolai.shoppinglist.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShoppingListOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "SHOPPING_LIST";
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
            db.execSQL("CREATE TABLE SHOPPING_LIST (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NAME TEXT)");

            db.execSQL("CREATE TABLE LIST_ITEM (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "AMOUNT INTEGER," +
                    "SHOPPING_LIST_ID INTEGER," +
                    "PRODUCT_ID INTEGER," +
                    "FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCT(_id)," +
                    "FOREIGN KEY (SHOPPING_LIST_ID) REFERENCES SHOPPINGLIST(_id))");

            db.execSQL("CREATE TABLE PRODUCT (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "IMAGE_ID INTEGER," +
                    "PRICE INTEGER," +
                    "NAME TEXT," +
                    "DEAL_ID INTEGER," +
                    "STORE_ID INTEGER," +
                    "FOREIGN KEY (DEAL_ID) REFERENCES DEAL(_id)," +
                    "FOREIGN KEY (STORE_ID) REFERENCES STORE(_id))");

            db.execSQL("CREATE TABLE DEAL (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "DEAL_PRICE INTEGER," +
                    "STORE_ID INTEGER," +
                    "FOREIGN KEY (STORE_ID) REFERENCES STORE(_id))");

            db.execSQL("CREATE TABLE STORE (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NAME TEXT," +
                    "ADDRESS TEXT," +
                    "WEBSITE TEXT)");
        }
    }
}