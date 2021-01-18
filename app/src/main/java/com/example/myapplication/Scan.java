package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;


public class Scan extends AppCompatActivity {

    private Button captureImage, extractText;
    private TextView textView;
    private TessBaseAPI tessBaseAPI;
    private Uri resultImageDir;
    private static final String TAG = Scan.class.getSimpleName();
    private static final String TESS_DATA = "/Tessdata";
    private static final String DATA_PATCH = Environment.getExternalStorageDirectory().toString()+"/Tess";

//    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        captureImage = findViewById(R.id.captureImage);
        extractText = findViewById(R.id.extractText);
        textView = findViewById(R.id.textDisplay);

        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        try {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        } catch (ActivityNotFoundException e) {
//            // display error state to the user
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
//        }
//    }
}