package com.example.nicolai.shoppinglist.model;

import com.example.nicolai.shoppinglist.model.ListItem;
import com.example.nicolai.shoppinglist.storage.ListItemStorage;

import java.util.ArrayList;

/**
 * Created by Nicolai on 22-02-2018.
 */

public class ShoppingList {
    private String name;
    private long id;
    private ListItemStorage.ListItemWrapper items;

    public ShoppingList(String name, long id, ListItemStorage.ListItemWrapper items){
        this.name = name;
        this.id = id;
        this.items = items;
    }

    public int totalPrice(){
        int sum = 0;
        items.moveToFirst();
        while (!items.isAfterLast()) {
            ListItem item = items.get();
            sum += item.price();
            items.moveToNext();
        }
        return sum;
    }
    public int normalPrice(){
        int sum = 0;
        items.moveToFirst();
        while (!items.isAfterLast()) {
            ListItem item = items.get();
            sum += item.normalPrice();
            items.moveToNext();
        }
        return sum;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public ListItemStorage.ListItemWrapper getItems() {
        items.moveToFirst();
        return items;
    }
}
