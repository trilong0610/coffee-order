package com.example.coffeeorder.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorder.R;
import com.example.coffeeorder.model.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductItemViewHolder>{
    private ArrayList<ProductModel> productModels;
    private Context context;
    public ProductAdapter(ArrayList<ProductModel> productModels, Context c) {
        this.productModels = productModels;
        this.context = c;
    }
    @NonNull
    @Override
    public ProductAdapter.ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        parent.scrollTo(0,0);
        return new ProductItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder holder, int position) {
        ProductModel productModel = productModels.get(position);
        holder.txtItemProductName.setText(productModel.nameProduct);
        holder.txtItemProductPrice.setText(String.valueOf(productModel.priceProduct));
        Picasso.get().load(productModel.imgProduct).into(holder.imgItemProductImage);

    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class ProductItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgItemProductImage;
        public TextView txtItemProductName;
        public TextView txtItemProductPrice;
        public TextView edtItemProductQuantity;
        public Button btn_item_product_plus;
        public Button btn_item_product_minus;
        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imgItemProductImage = itemView.findViewById(R.id.img_item_product_image);
            txtItemProductName = itemView.findViewById(R.id.txt_item_product_name);
            txtItemProductPrice = itemView.findViewById(R.id.txt_item_product_price);
            edtItemProductQuantity = itemView.findViewById(R.id.edt_item_product_quantity);
            btn_item_product_plus = itemView.findViewById(R.id.btn_item_product_plus);
            btn_item_product_minus = itemView.findViewById(R.id.btn_item_product_minus);
        }
    }
}
