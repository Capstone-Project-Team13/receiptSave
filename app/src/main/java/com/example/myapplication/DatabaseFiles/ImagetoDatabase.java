package com.example.myapplication.DatabaseFiles;

public class ImagetoDatabase {
    String Imageurl;

    public ImagetoDatabase(String imageurl) {
        Imageurl = imageurl;
    }

    public String getImageurl() {
        return Imageurl;
    }

    public void setImageurl(String imageurl) {
        Imageurl = imageurl;
    }
}
