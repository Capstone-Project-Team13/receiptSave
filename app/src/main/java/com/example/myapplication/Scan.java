package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class Scan extends AppCompatActivity {

    private ImageView imageView;
    private LinearLayout items;
    private LinearLayout price;
    private final int CAPTURE_IMAGE = 1000;
    private final int PICK_GALLERY_IMAGE = 2000;
    private final int CAMERA_REQUEST = 100;
    private final int WRITE_REQUEST = 200;

    private String cameraPermission[];
    private String writePermission[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        imageView = findViewById(R.id.imageId);
        items = findViewById(R.id.items);
        price = findViewById(R.id.price);

        //Check permission for Camera
//        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
//        }
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        writePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    // CaptureReceipt Button
    public void captureReceipt(View view) {
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST);
        }
        else {
            // Clear the view before taking pictures
            items.removeAllViews();
            price.removeAllViews();

            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Start camera
            if (pictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(pictureIntent, CAPTURE_IMAGE);
            }
        }
    }

    // Pick Receipts from Gallery Button
    public void pickReceipt(View view) {
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, writePermission, WRITE_REQUEST);
        }
        else {
            // Clear the view
            items.removeAllViews();
            price.removeAllViews();

//            Intent pictureIntent = new Intent(Intent.ACTION_PICK);
//            pictureIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//            startActivityForResult(pictureIntent, PICK_GALLERY_IMAGE);
            Intent pictureIntent = new Intent();
            pictureIntent.setType("image/*");
            pictureIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(pictureIntent, PICK_GALLERY_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap imageBitmap = null;
        if (resultCode != RESULT_OK){
            return;
        }
        if (requestCode == CAPTURE_IMAGE) {
            Bundle bundle = data.getExtras();
            imageBitmap = (Bitmap) bundle.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
        else if (requestCode == PICK_GALLERY_IMAGE){
            InputStream inputStream;
            try {
                inputStream = getContentResolver().openInputStream(data.getData());
                imageBitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                Uri uri = data.getData();
                Glide.with(this).load(uri).override(150,150).into(imageView);
             //   imageView.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (resultCode == RESULT_CANCELED){
            return;
        }

        // 1. To create a FirebaseVisionImage object from a Bitmap object
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        // 2. Get an instance of FirebaseVisionTextRecognizer
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        // 3. Pass the image to the processImage method
        Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        ArrayList<String> prices = new ArrayList<>();
                        ArrayList<String> Items = new ArrayList<>();
                        List<FirebaseVisionText.TextBlock> resultBlocks = firebaseVisionText.getTextBlocks();
                        for (FirebaseVisionText.TextBlock block : resultBlocks) {
                            for (FirebaseVisionText.Line line : block.getLines()) {
                                String lineText = line.getText();
                                EditText text = new EditText(getApplicationContext());

                                // price
                                if(lineText.contains(".")){
                                    lineText = lineText.replaceAll("[a-zA-Z]","");
                                    text.setText(lineText);
                                    price.addView(text);
                                    prices.add(text.getText().toString());
                                }
                                // Items
                                else {
                                    lineText = lineText.replaceAll("[0-9]","");
                                    text.setText(lineText);
                                    items.addView(text);
                                    Items.add(text.getText().toString());
                                }

//                                for (Text.Element element : line.getElements()) {
//                                    String elementText = element.getText();//
//                                }
                            }
                        }
                        Builddatabase(Items, prices);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void Builddatabase(ArrayList<String> Items, ArrayList<String> prices){
        System.out.println("am here");
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