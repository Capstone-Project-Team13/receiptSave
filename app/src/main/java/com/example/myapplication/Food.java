package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ListView.ListViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Food extends AppCompatActivity {
    private ListView listView;
    private ListViewAdapter adapter;
    private Button deleteBtn;
    private ArrayList<String> listDB = new ArrayList<String>();
    private ArrayList<String> keyDB = new ArrayList<String>();
    private DatabaseReference mDatabase;
    private ArrayList<String> itemList = new ArrayList<String>();
    private ArrayList<String> dateList = new ArrayList<String>();
    private static final String TAG = "Food";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        adapter = new ListViewAdapter();
        listView = findViewById(R.id.foodListId);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getData();
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                for(int i=0; i<listDB.size(); i++) {
                    if(keyDB.contains(listDB.get(i))) {
                        int index = keyDB.indexOf(listDB.get(i));
                        itemList.remove(index);
                        dateList.remove(index);
                    }
                    mDatabase.child("ReceiptItems").child(user).child(listDB.get(i)).removeValue();
                }
//                for(int j=0; j<itemList.size(); j++) {
//                    String s =itemList.get(j);
//                    Log.d(TAG, s +" !!!!!!!!!!!!!!!");
//                }
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                finish();
                startActivity(intent);
            }

        });
    }

    private void getData(){
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("ReceiptItems").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get data from DB
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String keys = snapshot.getKey();
                    String item = snapshot.child("item").getValue().toString();
                    String expDate = snapshot.child("expiration_date").getValue().toString();

                    itemList.add(item);
                    dateList.add(expDate);
//                    listDB.add(snapshot.toString());
                    keyDB.add(keys);
                }
                listView.setAdapter(adapter);
                for (int i = 0; i < itemList.size(); i++) {
                    adapter.addItem(itemList.get(i), dateList.get(i));
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(listDB.contains(keyDB.get(position))) {
                            listDB.remove(keyDB.get(position));
                        }
                        else {
                            listDB.add(keyDB.get(position));
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