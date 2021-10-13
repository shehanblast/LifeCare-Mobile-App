package com.example.lifecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lifecare.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditActivity2 extends AppCompatActivity {

    private TextView productQty, productName;
    private EditText eQty,eQty2;
    private Button change;
    private DatabaseReference itemRef;
    private String productID,pqq;
    private int current,abc,addQty;
    private int lol = 3;
    private String ab,bb,q;
    private ImageView removeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit2);

        productID = getIntent().getStringExtra("pid");
        pqq = getIntent().getStringExtra("pqq");
        itemRef = FirebaseDatabase.getInstance().getReference().child("Product");

        productName = findViewById(R.id.product_name_details2);
        productQty = findViewById(R.id.product_qty_details2);
        change = findViewById(R.id.change);
        eQty2 = findViewById(R.id.editQty2);
        removeImage = findViewById(R.id.product_image_details);


        Toast.makeText(EditActivity2.this, productID, Toast.LENGTH_SHORT).show();

        getProductDetails(productID);

        current=((Integer.valueOf(pqq)));


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeQty();
            }
        });


    }


    private void changeQty() {

        q= eQty2.getText().toString();
        addQty=((Integer.valueOf(q)));

        lol = current - addQty;


        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Product");

        //get values related to that ID
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    Product product = dataSnapshot.getValue(Product.class);

//                    productName.setText(product.getpName());
//                    productQty.setText(product.getpQty());
//
//                    String q3= productQty.getText().toString();
//                    current=((Integer.valueOf(q3)));
//
//                    String q= eQty.getText().toString();
//                    int addQty=((Integer.valueOf(q)));

//                    String q2= eQty2.getText().toString();
//                    int removeQty=((Integer.valueOf(q2)));

//                    int y = current + addQty;
//                    String tem2 = String.valueOf(y);

//                    Toast.makeText(EditProduct.this, "d" + current, Toast.LENGTH_SHORT).show();

                    itemRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){


                                //get values related to that ID
                                itemRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()){
                                            Product product = dataSnapshot.getValue(Product.class);



                                            //create ref in db
                                            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

                                            //Put details in the Map
                                            final HashMap<String,Object> cartMap = new HashMap<>();



                                            cartMap.put("image","e");
//                                            cartMap.put("pName","ppp");
                                            cartMap.put("pQty",lol);
//                                            cartMap.put("pid",productID);
//                                cartMap.put("ptime",saveCurrentTime);
//                                cartMap.put("pquantity",totQty);
//                                cartMap.put("pimage",Iimage);
//                                cartMap.put("totPrice",tottot);
//

                                            itemRef.child(productID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(EditActivity2.this, "Added to Cart!.", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(EditActivity2.this, MainActivity.class);
                                                        startActivity(intent);
                                                    }
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

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });






                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }




    private void getProductDetails(String productID) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Product");

        //get values related to that ID
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    Product product = dataSnapshot.getValue(Product.class);

                    productName.setText(product.getpName());
                    productQty.setText(String.valueOf(product.getpQty()));
                    Picasso.get().load(product.getImage()).into(removeImage);

//                    ab= productQty.getText().toString();
//                    abc=((Integer.valueOf(ab)));





                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}