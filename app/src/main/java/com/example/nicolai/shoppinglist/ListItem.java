package com.example.nicolai.shoppinglist;

/**
 * Created by Nicolai on 22-02-2018.
 */

public class ListItem {
    private Product product;
    private int amount;


    public int price(){
        return amount * product.price();
    }
}
