package com.example.nicolai.shoppinglist.model;

/**
 * Created by Nicolai on 22-02-2018.
 */

public class Product {

    private int imageID;
    private int price;
    private String name;
    private Deal deal;
    private Store store;

    public Product(int imageID, int price, String name, Store store) {
        this.imageID = imageID;
        this.price = price;
        this.name = name;
        this.store = store;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    public int price(){
        if (deal != null){
            return deal.price();
        }
        else return price;
    }
}
