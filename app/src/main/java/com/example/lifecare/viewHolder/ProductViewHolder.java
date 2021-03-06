package com.example.lifecare.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifecare.Interface.ItemClickListner;
import com.example.lifecare.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtProductName, txtProductQuantity;
    public ItemClickListner itemClickListner;
    public ImageView cartImage;
    public ImageView removePro,addProduct,choice;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        cartImage = (ImageView) itemView.findViewById(R.id.cart_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductQuantity = (TextView) itemView.findViewById(R.id.product_qty);
        addProduct = (ImageView) itemView.findViewById(R.id.addNewProduct);
        choice = (ImageView) itemView.findViewById(R.id.choice);
        removePro = (ImageView) itemView.findViewById(R.id.close_product);
    }

    @Override
    public void onClick(View view) {

    }
}
