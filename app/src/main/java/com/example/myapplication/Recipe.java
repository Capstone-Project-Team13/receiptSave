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

import com.example.myapplication.ListView.ListViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Recipe extends AppCompatActivity {

    private ListView listView;
    private ListViewAdapter adapter;
    private DatabaseReference mDatabase;
    private ArrayList<String> itemList = new ArrayList<String>();
    private ArrayList<String> searchList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        listView = findViewById(R.id.listId);
        mDatabase = FirebaseDatabase.getInstance().getReference("ReceiptItems").child(user);
        //Create adapter
        adapter = new ListViewAdapter();
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

            if (itemList.get(i).toLowerCase().contains("potato")){
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.potatoes), itemList.get(i)) ;
            }
            else if(itemList.get(i).toLowerCase().contains("zucchini")){
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.zucchini), itemList.get(i)) ;
            }
            else if(itemList.get(i).toLowerCase().contains("green onion")){
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.greenonions), itemList.get(i)) ;
            }
            else if(itemList.get(i).toLowerCase().contains("sweet onion")){
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.sweet_onion), itemList.get(i)) ;
            }
            else if(itemList.get(i).toLowerCase().contains("mushroom")){
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.mushrooms), itemList.get(i)) ;
            }
            else if(itemList.get(i).toLowerCase().contains("apple")){
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.apples), itemList.get(i)) ;
            }
            else if(itemList.get(i).toLowerCase().contains("jalapeno")){
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.jalapeno), itemList.get(i)) ;
            }
            else {
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.smile), itemList.get(i)) ;
            }
        }

    }
}