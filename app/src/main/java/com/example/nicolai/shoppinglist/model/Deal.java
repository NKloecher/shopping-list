package com.example.nicolai.shoppinglist.model;

/**
 * Created by Nicolai on 22-02-2018.
 */

public class Deal {
    private int id;
    private int dealPrice;
    private Store store;

    public Deal(int id, int dealPrice, Store store) {
        this.id = id;
        this.dealPrice = dealPrice;
        this.store = store;
    }

    public int getId() {
        return id;
    }

    public Store getStore() {
        return store;
    }

    public int price() {
        return dealPrice;
    }
}
