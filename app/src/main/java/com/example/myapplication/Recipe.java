package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Recipe extends AppCompatActivity {

    private ListView listView;
    //private ArrayAdapter adapter;
    private ListViewAdapter adapter;
    private DatabaseReference mDatabase;
    private ArrayList<String> itemList = new ArrayList<String>();
    private ArrayList<String> searchList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        listView = findViewById(R.id.listId);
        //Create adapter
        adapter = new ListViewAdapter();

        mDatabase = FirebaseDatabase.getInstance().getReference("ReceiptItems");

        getData();




    }

    private void getData(){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get data from DB
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String item = snapshot.child("item").getValue().toString();
                    itemList.add(item);
                }
                listView.setAdapter(adapter);
                addAdapters();



                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (!searchList.contains(itemList.get(position))) {
                            //listView.setSelector(new PaintDrawable(0xfff0000));
                            searchList.add(itemList.get(position));
                        }
                        else {
                            searchList.remove(itemList.get(position));
                        }
                    }

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
    // Recipe search from google.
    public void searchRecipe(View view) {
        String searchString = " ";
        // Get clicked item
        for (int i=0; i<searchList.size(); i++){
            searchString = searchString + " " +searchList.get(i);
        }
        searchString = searchString + " food recipe";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/search?q="+searchString));
        startActivity(browserIntent);
    }

    // Print images and texts.
    private void addAdapters() {
        for (int i=0; i<itemList.size();i++){

            if (itemList.get(i).toLowerCase().equals("potatoes")){
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.potatoes), itemList.get(i)) ;
            }
            else if(itemList.get(i).toLowerCase().equals("zucchini")){
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.zucchini), itemList.get(i)) ;
            }
            else if(itemList.get(i).toLowerCase().equals("green onions")){
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.greenonions), itemList.get(i)) ;
            }
            else if(itemList.get(i).toLowerCase().equals("sweet onions")){
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.sweet_onion), itemList.get(i)) ;
            }
            else if(itemList.get(i).toLowerCase().equals("mushrooms")){
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.mushrooms), itemList.get(i)) ;
            }
            else if(itemList.get(i).toLowerCase().equals("apples")){
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.apples), itemList.get(i)) ;
            }
            else if(itemList.get(i).toLowerCase().equals("jalapenos")){
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.jalapeno), itemList.get(i)) ;
            }
            else {
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.smile), itemList.get(i)) ;
            }
        }

    }
}