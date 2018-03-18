package com.example.nicolai.shoppinglist.model;

/**
 * Created by Nicolai on 22-02-2018.
 */

public class Deal {
    private long id;
    private int dealPrice;
    private Store store;

    public Deal(long id, int dealPrice, Store store) {
        this.id = id;
        this.dealPrice = dealPrice;
        this.store = store;
    }

    public long getId() {
        return id;
    }

    public Store getStore() {
        return store;
    }

    public int price() {
        return dealPrice;
    }

    public void setDealPrice(int dealPrice) {
        this.dealPrice = dealPrice;
    }

    @Override
    public String toString() {
        return ""+dealPrice;
    }
}
