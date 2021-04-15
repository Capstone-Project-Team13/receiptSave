package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DatabaseFiles.ReceiptDatabase;
import com.example.myapplication.DatabaseFiles.ImagetoDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Receipt extends AppCompatActivity {

    private ImageView imageView;
    private final int CAPTURE_IMAGE = 1000;
    private final int CAMERA_REQUEST = 100;
    private String cameraPermission[];
    private String writePermission[];

    private RecyclerView recyclerView;
    private ArrayList<ImagetoDatabase> list;
    private MyAdapter adapter;
    static String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("ReceiptImages").child(user);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        writePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
       //imageView = findViewById(R.id.imageId);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new MyAdapter(this,list);
        recyclerView.setAdapter(adapter);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ImagetoDatabase model = dataSnapshot.getValue(ImagetoDatabase.class);
                    list.add(model);
                }
                Collections.reverse(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //Actionbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    //Handle actionbar click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_camera:
                //Toast.makeText(this,"Camera", Toast.LENGTH_SHORT).show();
                captureReceipt(imageView);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // CaptureReceipt Button
    public void captureReceipt(View view) {
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST);
        }
        else {

            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Start camera
            if (pictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(pictureIntent, CAPTURE_IMAGE);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap imageBitmap = null;
        Uri imageUri = null;
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == CAPTURE_IMAGE) {
            Bundle bundle = data.getExtras();
            imageBitmap = (Bitmap) bundle.get("data");
            //imageView.setImageBitmap(imageBitmap);
            ReceiptDatabase.uploadToFirebase(imageBitmap);
//            if (data != null) {
//                imageUri = data.getData();
//                //imageView.setImageURI(imageUri);
//                if (imageUri == null) {
//                    System.out.println("to the database");
//                   // ReceiptDatabase.uploadToFirebase(imageUri);
//                }
//            }
            //System.out.println("here here");
        }

//        else if (resultCode == RESULT_CANCELED) {
//            return;
//        }
    }

}