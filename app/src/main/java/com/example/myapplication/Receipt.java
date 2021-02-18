package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Receipt extends AppCompatActivity {

    private ImageView imageView;
    private final int CAPTURE_IMAGE = 1000;
    private final int CAMERA_REQUEST = 100;
    private String cameraPermission[];
    private String writePermission[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        writePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        imageView = findViewById(R.id.imageId);
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
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == CAPTURE_IMAGE) {
            Bundle bundle = data.getExtras();
            imageBitmap = (Bitmap) bundle.get("data");
            imageView.setImageBitmap(imageBitmap);
        }

//        else if (resultCode == RESULT_CANCELED) {
//            return;
//        }
    }

}