package com.example.nicolai.shoppinglist.model;

/**
 * Created by Nicolai on 22-02-2018.
 */

public class ListItem {
    private int id;
    private Product product;
    private int amount;
    private boolean done;

    public ListItem(int id, Product product, int amount, boolean done) {
        this.id = id;
        this.product = product;
        this.amount = amount;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public boolean getDone() { return done; }

    public int price(){
        return amount * product.price();
    }
    public int normalPrice() { return amount * product.getPrice(); }
}
