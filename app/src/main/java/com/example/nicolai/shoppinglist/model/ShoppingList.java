package com.example.nicolai.shoppinglist.model;

import com.example.nicolai.shoppinglist.model.ListItem;

import java.util.ArrayList;

/**
 * Created by Nicolai on 22-02-2018.
 */

public class ShoppingList {
    private String name;
    private long id;
    private ArrayList<ListItem> items = new ArrayList<>();

    public ShoppingList(String name, long id){
        this.name = name;
        this.id = id;
    }

    public int totalPrice(){
        int sum = 0;
        for (ListItem item : items){
            sum += item.price();
        }
        return sum;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
