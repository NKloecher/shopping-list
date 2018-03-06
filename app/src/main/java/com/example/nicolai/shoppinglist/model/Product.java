package com.example.nicolai.shoppinglist.model;

/**
 * Created by Nicolai on 22-02-2018.
 */

public class Product {

    private long id;
    private int price;
    private String name;
    private Deal deal;
    private Store store;

    public Product(long id, int price, String name, Store store, Deal deal) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.store = store;
        this.deal = deal;
    }

    public long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public Deal getDeal() {
        return deal;
    }

    public Store getStore() {
        return store;
    }

    public int price(){
        if (deal != null){
            return deal.price();
        }
        else return price;
    }
}
