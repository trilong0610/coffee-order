package com.example.coffeeorder.data;

import android.content.Context;
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

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderItemViewHolder> {
    public Context c;
    public ArrayList<OrderDetailModel> orderDetailModels;

    public int orderStatus;

    public String idOrder;



    public OrderDetailsAdapter(Context c, ArrayList<OrderDetailModel> orderDetailModels, int orderStatus){
        this.c = c;
        this.orderDetailModels = orderDetailModels;
        this.orderStatus = orderStatus;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
        parent.scrollTo(0,0);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderDetailModel orderDetailModel = orderDetailModels.get(position);
        holder.txtItemOrderName.setText(orderDetailModel.product.nameProduct);
        holder.txtItemOrderPrice.setText(String.valueOf(orderDetailModel.product.priceProduct));
        holder.txtItemOrderQuantity.setText(String.valueOf(orderDetailModel.quantity));
        Picasso.get().load(orderDetailModel.product.imgProduct).into(holder.imgItemOrderImage);
        holder.btnDelete.setVisibility(View.INVISIBLE);
//        if (orderStatus == 0){
//            holder.btnDelete.setVisibility(View.VISIBLE);
//        }
//        else {
//            holder.btnDelete.setVisibility(View.INVISIBLE);
//        }

//        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//// Hien thi thong bao
//                new AlertDialog.Builder(c)
//                        .setMessage("Bạn muốn xóa sản phẩm " + orderDetailModel.product.nameProduct + " ?")
//                        .setCancelable(true)
//                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                // Doc dánh sasch san pham trong detail order
//                                MainActivity.mDatabase.child("Order")
//                                        .child(idOrder)
//                                        .child("orderDetails")
//                                        .orderByChild("product/idProduct")
//                                        .equalTo(orderDetailModel.product.idProduct)
//                                        .get()
//                                        .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                                                for (DataSnapshot dataSnapshot: task.getResult().getChildren()) {
//                                                    OrderDetailModel model = dataSnapshot.getValue(OrderDetailModel.class);
//                                                    Log.d("DELETE_PRODUCT", dataSnapshot.getValue(OrderDetailModel.class).product.nameProduct);
//                                                    Log.d("DELETE_PRODUCT", dataSnapshot.getRef().toString());
//                                                    dataSnapshot.getRef().removeValue();
//                                                }
//
//                                            }
//                                        });
//                            }
//                        })
//                        .setNegativeButton("Hủy", null)
//                        .show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return orderDetailModels.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgItemOrderImage;
        public TextView txtItemOrderName;
        public TextView txtItemOrderPrice;
        public TextView txtItemOrderQuantity;

        public ImageView btnDelete;
        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemOrderImage = itemView.findViewById(R.id.img_item_order_image);
            txtItemOrderName = itemView.findViewById(R.id.txt_item_order_name);
            txtItemOrderPrice = itemView.findViewById(R.id.txt_item_order_price);
            txtItemOrderQuantity = itemView.findViewById(R.id.txt_item_order_quantity);
            btnDelete = itemView.findViewById(R.id.btn_item_order_delete);
        }
    }

    public void setOrderStatus(int status){
        this.orderStatus = status;
        notifyDataSetChanged();
    }
    public void setOrderId(String id){
        this.idOrder = id;
        notifyDataSetChanged();
    }

    public void updateAdapter(ArrayList<OrderDetailModel> orderDetailModels){
        this.orderDetailModels = orderDetailModels;
        notifyDataSetChanged();
    }
}
