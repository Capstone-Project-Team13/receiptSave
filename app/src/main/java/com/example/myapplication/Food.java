package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Food extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter adapter;
    private DatabaseReference mDatabase;
    private ArrayList<String> itemList = new ArrayList<String>();
    private ArrayList<String> listDB = new ArrayList<String>();
    private ArrayList<String> keyDB = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        listView = findViewById(R.id.listId);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getData();
    }

    private void getData(){
        mDatabase.child("ReceiptItems").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    listDB.add(snapshot.toString());
                    String item = snapshot.child("item").getValue().toString();
                    itemList.add(item);

                    String keys = snapshot.getKey();
                    keyDB.add(keys);

                }
                for (int i=0;i<itemList.size();i++)
                   // Log.d("Recipe", "what???????????: " + itemList.get(i));

                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, itemList);

                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String deleteItem = itemList.get(position);

                        if (listDB.get(position).contains(deleteItem)) {

                            mDatabase.child("ReceiptItems").child(keyDB.get(position)).removeValue();
                            //listView.removeView(parent);
                            //adapter.notifyDataSetChanged();
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

}