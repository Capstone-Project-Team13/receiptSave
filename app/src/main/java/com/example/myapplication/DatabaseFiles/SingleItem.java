package com.example.myapplication.DatabaseFiles;

public class SingleItem {
    String item;
    String expiration_date;




    public SingleItem(String item, String expiration_date) {

        this.item = item;
        this.expiration_date = expiration_date;
    }

    public String getItem() {
        return item;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }
}
