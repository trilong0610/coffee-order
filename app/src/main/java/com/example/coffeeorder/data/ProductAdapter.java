package com.example.coffeeorder.data;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorder.R;
import com.example.coffeeorder.model.OrderDetailModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductItemViewHolder>{
    public ArrayList<OrderDetailModel> orderDetailModels;
    private Context context;
    @NonNull
    private OnItemChangeListener onItemCheckListener;
    public interface OnItemChangeListener {
        // item: san pham can tuong tac
        void updateList(ArrayList<OrderDetailModel> orderDetailModels);
    }

    public ProductAdapter(ArrayList<OrderDetailModel> orderDetailModels, Context c, @NonNull OnItemChangeListener onItemCheckListener) {
        this.orderDetailModels = orderDetailModels;
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
        OrderDetailModel orderDetailModel = orderDetailModels.get(position);
        holder.txtItemProductName.setText(orderDetailModel.product.nameProduct);
        holder.txtItemProductPrice.setText(String.valueOf(orderDetailModel.product.priceProduct));
        holder.edtItemProductQuantity.setText(String.valueOf(orderDetailModel.quantity));
        Picasso.get().load(orderDetailModel.product.imgProduct).into(holder.imgItemProductImage);
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                try {
                    int input = Integer.parseInt(dest.toString() + source.toString());
                    if (input >= 0)
                        return null;
                } catch (NumberFormatException e) {
                    // do nothing
                }
                if (holder.edtItemProductQuantity.getText().toString().isEmpty())
                    return "0";
                else
                    return "";
            }
        };
        holder.edtItemProductQuantity.setFilters(new InputFilter[]{filter});
        // event

        // tang so luong sp
        holder.btn_item_product_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderDetailModels.get(holder.getAdapterPosition()).quantity += 1;
                holder.edtItemProductQuantity.setText(String.valueOf(orderDetailModels.get(holder.getAdapterPosition()).quantity));
            }
        });

        holder.btn_item_product_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderDetailModels.get(holder.getAdapterPosition()).quantity > 0){
                    orderDetailModels.get(holder.getAdapterPosition()).quantity -= 1;
                    holder.edtItemProductQuantity.setText(String.valueOf(orderDetailModels.get(holder.getAdapterPosition()).quantity));
                }

            }
        });

        // Kiem tra neu so luong sp >0 -> khach hang dat
        // Them vao danh sach sp can order

        holder.edtItemProductQuantity.addTextChangedListener(new TextWatcher() {
            int oldQuantity = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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

                orderDetailModels.get(holder.getAdapterPosition()).quantity = numQuantity;


                onItemCheckListener.updateList(orderDetailModels);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderDetailModels.size();
    }


    public static class ProductItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgItemProductImage;
        public TextView txtItemProductName;
        public TextView txtItemProductPrice;
        public TextView edtItemProductQuantity;
        public ImageView btn_item_product_plus;
        public ImageView btn_item_product_minus;
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

    public void updateAdapter(ArrayList<OrderDetailModel> orderDetailModels){
        this.orderDetailModels = orderDetailModels;
        notifyDataSetChanged();
    }
}
