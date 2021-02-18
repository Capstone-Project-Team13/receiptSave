package com.example.myapplication.DatabaseFiles;

public class SingleItem {
    String item;
    String price;

    public SingleItem(String item, String price) {
        this.item = item;
        this.price = price;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItem() {
        return item;
    }

    public String getPrice() {
        return price;
    }
}
