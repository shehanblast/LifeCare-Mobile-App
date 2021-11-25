package com.example.lifecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lifecare.model.Product;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class NewProduct extends AppCompatActivity {

    private EditText pName,pQty;
    private String sPname,sQty,downloadImageUrl;
    private int iQty;
    private DatabaseReference itemRef;
    private StorageReference ProductImagesRef;
    private Button btnAdd;
    private Uri ImageUri;
    private ImageView InputProductImage;
    private static final int GalleryPick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        pName = findViewById(R.id.Product_name);
        pQty = findViewById(R.id.Product_qty);
        itemRef = FirebaseDatabase.getInstance().getReference().child("Product");
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);

        btnAdd = findViewById(R.id.product_addButton);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addingToCartList();
            }
        });

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    private void addingToCartList() {

        sPname = pName.getText().toString();
        sQty = pQty.getText().toString();

        iQty=((Integer.valueOf(sQty)));




        //get values related to that ID
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    Product product = dataSnapshot.getValue(Product.class);

                    String saveCurrentTime, saveCurrentDate,productRandomKey;

                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                    saveCurrentDate = currentDate.format(calForDate.getTime());

                    Calendar calForTime = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                    saveCurrentTime = currentTime.format(calForTime.getTime());

                    productRandomKey = saveCurrentDate + saveCurrentTime;

                    final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

                    final UploadTask uploadTask = filePath.putFile(ImageUri);


                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String message = e.toString();
                            Toast.makeText(NewProduct.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(NewProduct.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                    downloadImageUrl = filePath.getDownloadUrl().toString();
                                    return filePath.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        downloadImageUrl = task.getResult().toString();

                                        Toast.makeText(NewProduct.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                                        ////////////////////
                                        //Put details in the Map
                                        final HashMap<String,Object> cartMap = new HashMap<>();

                                        cartMap.put("image",downloadImageUrl);
                                        cartMap.put("pName",sPname);
                                        cartMap.put("pQty",iQty);
                                        cartMap.put("pid",productRandomKey);

                                        itemRef.child(productRandomKey).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(NewProduct.this, "Added to Cart!.", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(NewProduct.this, MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


}