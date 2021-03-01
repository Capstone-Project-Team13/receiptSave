package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.myapplication.DatabaseFiles.ReceiptDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Scan extends AppCompatActivity {

    private ImageView imageView;
    private LinearLayout itemsLayout;
    private LinearLayout expDateLayout;
    private final int CAPTURE_IMAGE = 1000;
    private final int PICK_GALLERY_IMAGE = 2000;
    private final int CAMERA_REQUEST = 100;
    private final int WRITE_REQUEST = 200;
    private String cameraPermission[];
    private String writePermission[];
    private Bitmap imageBitmap = null;
    private ArrayList<EditText> itemList = new ArrayList<>();
    private ArrayList<Spinner> expList = new ArrayList<>();
    private ArrayList<String> expArray = new ArrayList<>();
    private ArrayList<String> itemArray = new ArrayList<>();
    private HashMap expHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        imageView = findViewById(R.id.imageId);
        itemsLayout = findViewById(R.id.items);
        expDateLayout = findViewById(R.id.expirationDate);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        writePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    // Actionbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_receipt, menu);
        return true;
    }

    // Handle SAVE btn when it's clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:
                Toast.makeText(this,"Save Button is Clicked", Toast.LENGTH_SHORT).show();
                saveReceiptToDatabase();
                saveGroceryItemsAndExpDate();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Save receipt to database
    public void saveReceiptToDatabase() {
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, writePermission, WRITE_REQUEST);
        }
        else {
            if(imageBitmap != null) {
                // TODO: SAVE THE RECEIPT IMAGE TO DATABASE


            }
            else {
                Toast.makeText(this,"Image is empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Save Grocery Items & Exp Date to database
    private void saveGroceryItemsAndExpDate() {
        String itemsString = "";
        String expString = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        for(int i = 0; i < itemList.size();  i ++) {
            itemsString = itemList.get(i).getText().toString();
            expString = expList.get(i).getSelectedItem().toString();
            itemArray.add(itemsString);
            if(expString.contains("3 Days")){
                calendar.add(Calendar.DATE, 3);
                expString = simpleDateFormat.format(calendar.getTime());
            }
            else if(expString.contains("1 Week")){
                calendar.add(Calendar.DATE, 7);
                expString = simpleDateFormat.format(calendar.getTime());
            }
            else if(expString.contains("2 Week")){
                calendar.add(Calendar.DATE, 14);
                expString = simpleDateFormat.format(calendar.getTime());
            }
            else if(expString.contains("3 Week")){
                calendar.add(Calendar.DATE, 21);
                expString = simpleDateFormat.format(calendar.getTime());
            }
            else if(expString.contains("1 Months")){
                calendar.add(Calendar.MONTH, 1);
                expString = simpleDateFormat.format(calendar.getTime());
            }
            else if(expString.contains("3 Months")){
                calendar.add(Calendar.MONTH, 3);
                expString = simpleDateFormat.format(calendar.getTime());
            }
            else if(expString.contains("6 Months")){
                calendar.add(Calendar.MONTH, 6);
                expString = simpleDateFormat.format(calendar.getTime());
            }
            else {
                expString = expHashMap.get(i).toString();
            }
            expArray.add(expString);
            calendar.setTime(new Date());
        }
        ReceiptDatabase.Builddatabase(itemArray, expArray);
    }

    // CaptureReceipt Button
    public void captureReceipt(View view) {
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST);
        }
        else {
            // Clear the view before taking pictures
            itemsLayout.removeAllViews();
            expDateLayout.removeAllViews();

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
            itemsLayout.removeAllViews();
            expDateLayout.removeAllViews();

            // Open gallery
            Intent pictureIntent = new Intent();
            pictureIntent.setType("image/*");
            pictureIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(pictureIntent, PICK_GALLERY_IMAGE);
        }
    }

    private void createTextView() {
        TextView itemTextView = new TextView((getApplicationContext()));
        TextView expTextView = new TextView((getApplicationContext()));
        itemTextView.setText("Items");
        itemTextView.setGravity(Gravity.CENTER);
        itemsLayout.addView(itemTextView);
        expTextView.setGravity(Gravity.CENTER);
        expTextView.setText("Expiration Date");
        expDateLayout.addView(expTextView);
    }
    // Set dropdown menu on EXP column
    private void setSpinnerField(Spinner expSpinner) {
        String[] expDateArray = {"3 Days", "1 Week", "2 Weeks", "3 Weeks", "1 Months",
                "3 Months", "6 Months", "Date from Calender"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_spinner_item,
                expDateArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.RIGHT;
        params.height = 120;
        expSpinner.setLayoutParams(params);
        expSpinner.setAdapter(arrayAdapter);
    }
    private void pickDateFromCalendar(Spinner expSpinner) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        expSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMenu = parent.getItemAtPosition(position).toString();
                if(selectedMenu.contains("Calender")) {
                    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            String dateFromCalendar = dateFormat.format(calendar.getTime());
                            int expSpinnerId = expSpinner.getId();
                            expHashMap.put(expSpinnerId, dateFromCalendar);
                            Toast.makeText(Scan.this, "" + dateFromCalendar, Toast.LENGTH_LONG).show();
                        }
                    };
                    DatePickerDialog dialog = new DatePickerDialog(Scan.this,
                            android.R.style.Theme_Dialog,
                            dateListener,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            expSpinner.setSelection(0);
                        }
                    });
                    dialog.show();

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // Perform OCR
    private void convertImageToText(Bitmap imageBitmap) {
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
                        // Prompt error message that no text found after scan a receipt
                        if(resultBlocks.isEmpty()) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "No Text Found. Please Try Again!",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Extract text
                        int idNum =0;
                        for (FirebaseVisionText.TextBlock block : resultBlocks) {
                            if((block.getText().toUpperCase().contains("DEBIT"))||
                                    (block.getText().toUpperCase().contains("SUBTOTAL"))) {
                                break;
                            }
                            if(!((block.getText().toUpperCase().contains("ALDI"))||
                                    (block.getText().toUpperCase().contains("CASH")))) {

                                for (FirebaseVisionText.Line line : block.getLines()) {
                                    String lineText = line.getText();
                                    EditText editText = new EditText(getApplicationContext());
                                    Spinner expSpinner = new Spinner(getApplicationContext());
                                    setSpinnerField(expSpinner);
                                    expSpinner.setId(idNum);
                                    pickDateFromCalendar(expSpinner);
                                    idNum++;

                                    // price
                                    if (lineText.contains(".")) {
                                        lineText = lineText.replaceAll("[a-zA-Z]", "");
                                        editText.setText(lineText);
//                                    price.addView(text);
                                    }
                                    // Items & Exp Date
                                    else {
                                        lineText = lineText.replaceAll("[0-9]", "");
                                        if(lineText.length() <= 2) {
                                            lineText = "";
                                        }
                                        if(!lineText.isEmpty()) {
                                            editText.setText(lineText);
                                            itemList.add(editText);
                                            itemsLayout.addView(editText);
                                            expList.add(expSpinner);
                                            expDateLayout.addView(expSpinner);
                                        }
                                    }
                                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (resultCode == RESULT_CANCELED){
            return;
        }
        // Make the table names(items and exp date) visible
        createTextView();

        // Perform OCR
        convertImageToText(imageBitmap);
    }
}