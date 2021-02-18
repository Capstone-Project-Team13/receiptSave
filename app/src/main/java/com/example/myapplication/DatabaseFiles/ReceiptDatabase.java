package com.example.myapplication.DatabaseFiles;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ReceiptDatabase {
    public static void Builddatabase(ArrayList<String> Items, ArrayList<String> prices){
        System.out.println("walalalalalalalalalalalalaall");
        DatabaseReference element = FirebaseDatabase.getInstance().getReference().child("ReceiptItems");
        int i = Items.size();
        int j = prices.size();
        int groceries =0;
        int cost =0;
//        SingleItem Theitem = new SingleItem(Items.get(0), prices.get(0));
//        element.push().setValue(Theitem);
//        System.out.println(Items.get(0));
        while(groceries < i && cost < j){
            SingleItem Theitem = new SingleItem(Items.get(groceries), prices.get(cost));
            element.push().setValue(Theitem);
            groceries++;
            cost++;
            //Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_SHORT).show();
        }
    }
}
