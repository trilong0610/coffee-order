package com.example.coffeeorder.data;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
    @NonNull
    private OnItemChangeListener onItemCheckListener;
    public interface OnItemChangeListener {
        // item: san pham can tuong tac
        // number: so luong thay doi
        void onItemAdd(ProductModel item, int number);
        void onItemDelete(ProductModel item, int number);
    }

    public ProductAdapter(ArrayList<ProductModel> productModels, Context c, @NonNull OnItemChangeListener onItemCheckListener) {
        this.productModels = productModels;
        this.context = c;
        this.onItemCheckListener = onItemCheckListener;
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

        // event

        // tang so luong sp
        holder.btn_item_product_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(holder.edtItemProductQuantity.getText().toString());
                holder.edtItemProductQuantity.setText(String.valueOf(quantity + 1));
            }
        });

        holder.btn_item_product_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(holder.edtItemProductQuantity.getText().toString());
                if (quantity > 0)
                    holder.edtItemProductQuantity.setText(String.valueOf(quantity - 1));
            }
        });

        // Kiem tra neu so luong sp >0 -> khach hang dat
        // Them vao danh sach sp can order

        holder.edtItemProductQuantity.addTextChangedListener(new TextWatcher() {
            int oldQuantity = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                oldQuantity = Integer.parseInt(holder.edtItemProductQuantity.getText().toString());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String quantity = holder.edtItemProductQuantity.getText().toString();
                if (quantity.isEmpty()){
                    return;
                }
                int numQuantity = Integer.parseInt(quantity);
                if (numQuantity > oldQuantity){
                    // tang so luong
                    onItemCheckListener.onItemAdd(productModel, numQuantity - oldQuantity);
                }
                else{
                    // giam so luong
                    onItemCheckListener.onItemDelete(productModel, oldQuantity - numQuantity);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }


    public static class ProductItemViewHolder extends RecyclerView.ViewHolder {
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
