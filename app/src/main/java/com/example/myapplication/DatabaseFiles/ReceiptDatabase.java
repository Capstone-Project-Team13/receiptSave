package com.example.myapplication.DatabaseFiles;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class ReceiptDatabase {
    public static void Builddatabase(ArrayList<String> Items, ArrayList<String> expiration_date){

        DatabaseReference element = FirebaseDatabase.getInstance().getReference().child("ReceiptItems");
        int i = Items.size();
        int j = expiration_date.size();
        int groceries =0;
        int exdate =0;
//        SingleItem Theitem = new SingleItem(Items.get(0), prices.get(0));
//        element.push().setValue(Theitem);
//        System.out.println(Items.get(0));
        while(groceries < i && exdate < j){
            SingleItem Theitem = new SingleItem(Items.get(groceries), expiration_date.get(exdate));
            element.push().setValue(Theitem);
            groceries++;
            exdate++;
            //Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_SHORT).show();
        }
    }
    public static  void uploadToFirebase(Bitmap image){
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        final String randomKey = UUID.randomUUID().toString();
        StorageReference fileRef = reference.child("images/" + randomKey);

        //database access
         DatabaseReference root = FirebaseDatabase.getInstance().getReference("ReceiptImages");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] b = stream.toByteArray();
        fileRef.putBytes(b)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //Uri downloadUri = uri;
                                ImagetoDatabase theimage = new ImagetoDatabase(uri.toString());
//                        //String modelId = root.push().getKey();  create the key (for future reference)
                                root.push().setValue(theimage);
                            }
                        });

                       // Toast.makeText(MainActivity.this, "Photo Uploaded", Toast.LENGTH_SHORT).show();
                    }
                });

//        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        ImagetoDatabase theimage = new ImagetoDatabase(uri.toString());
//                        //String modelId = root.push().getKey();  create the key (for future reference)
//                        root.push().setValue(theimage);
//
//
//                    }
//                });
//            }
//        });
    }

    
}
