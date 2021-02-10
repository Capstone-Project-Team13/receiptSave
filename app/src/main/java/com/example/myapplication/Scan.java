package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;


public class Scan extends AppCompatActivity {

    private ImageView imageView;
    private LinearLayout items;
    private LinearLayout price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        imageView = findViewById(R.id.imageId);
        items = findViewById(R.id.items);
        price = findViewById(R.id.price);

        //Check permission for Camera
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1000);
        }
    }

    public void captureReceipt(View view) {
        // Accessing the camera
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Clear the view before taking pictures
        items.removeAllViews();
        price.removeAllViews();

        // Start camera
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent, 1000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();

        // From bundle, extract the image
        Bitmap imageBitmap  = (Bitmap) bundle.get("data");
        // Set image in imageView
        imageView.setImageBitmap(imageBitmap);

        // 1. To create a FirebaseVisionImage object from a Bitmap object
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        // 2. Get an instance of FirebaseVisionTextRecognizer
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        // 3. Pass the image to the processImage method
        Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {

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
                                }
                                // Items
                                else {
                                    lineText = lineText.replaceAll("[0-9]","");
                                    text.setText(lineText);
                                    items.addView(text);
                                }

//                                for (Text.Element element : line.getElements()) {
//                                    String elementText = element.getText();//
//                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}