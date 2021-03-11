package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Recipe extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter adapter;
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

        getData();
    }

    private void getData(){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String item = snapshot.child("item").getValue().toString();
                    itemList.add(item);
                }
                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, itemList);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (!searchList.contains(itemList.get(position))) {
                            searchList.add(itemList.get(position));
                        }
                        //Log.d("Recipe", "what?????: " + searchList.size());
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

    public void searchRecipe(View view) {
        String searchString = " ";
        for (int i=0; i<searchList.size(); i++){
            searchString = searchString + " " +searchList.get(i);
        }
        searchString = searchString + " food recipe";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/search?q="+searchString));
        startActivity(browserIntent);
    }
}