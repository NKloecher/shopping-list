package com.example.nicolai.shoppinglist.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Nicolai on 22-02-2018.
 */

public class Store {
    private long id;
    private String name;
    private String address;
    private String website;

    public Store(long id, String name, String address, String website) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.website = website;
    }


    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsite() {
        return website;
    }

    public long getId() {
        return id;
    }
}
