package com.example.nicolai.shoppinglist.model;

/**
 * Created by Nicolai on 22-02-2018.
 */

public class Deal {
    private int dealPrice;
    private Store store;

    public Deal(int dealPrice, Store store) {
        this.dealPrice = dealPrice;
        this.store = store;
    }

    public int price() {
        return dealPrice;
    }
}
