package com.example.coffeeorder.data;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorder.R;
import com.example.coffeeorder.activity.PaymentOrderActivity;
import com.example.coffeeorder.model.OrderModel;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private ArrayList<OrderModel> dataList;
    private ArrayList<OrderModel> filteredList;
    public class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView id_order, time_order, time_complete, status_order,total_order,id_user,id_table,detail_order;
        public View layoutMain;
        public OrderViewHolder(View view) {
            super(view);

            id_order = (TextView) view.findViewById(R.id.id_order);
            status_order = (TextView) view.findViewById(R.id.status_order);
            total_order = (TextView) view.findViewById(R.id.total_order);
            id_user = (TextView) view.findViewById(R.id.id_user);
            id_table = (TextView) view.findViewById(R.id.id_table);
            detail_order = (TextView) view.findViewById(R.id.detail_order);
            layoutMain = view.findViewById(R.id.layout_item_order_main);

        }
    }
    public OrderAdapter(ArrayList<OrderModel> dataList) {
        this.dataList = dataList;
        this.filteredList = new ArrayList<>(dataList);
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int
            viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        OrderModel data = dataList.get(position);
        holder.id_order.setText(data.getIdOrder());

        String status = "";

        holder.total_order.setText(String.valueOf(data.getTotalOrder()));
        holder.id_user.setText(data.getIdUser());
        holder.id_table.setText(data.getIdTable());
        holder.detail_order.setText(data.getDetailOrder());

        // gan trang thai va mau cho don
        if(data.statusOrder == 0) {
            holder.layoutMain.setBackgroundColor(Color.parseColor("#E76161"));
            status = "Chưa pha chế";
        }
            else if(data.statusOrder == 1){
            holder.layoutMain.setBackgroundColor(Color.parseColor("#d47f00"));
            status = "Đã pha chế";
        }
        else {
            holder.layoutMain.setBackgroundColor(Color.parseColor("#47A992"));
            status = "Đã thanh toán";
        }
        holder.status_order.setText(status);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PaymentOrderActivity.class);
                intent.putExtra("id_order", data.getIdOrder());
                intent.putExtra("status_order", data.getStatusOrder());
                intent.putExtra("total_order", data.getTotalOrder());
                intent.putExtra("id_user", data.getIdUser());
                intent.putExtra("id_table", data.getIdTable());
                intent.putExtra("detail_order", data.getDetailOrder());
                intent.putExtra("from_activity", "order");
                view.getContext().startActivity(intent);
            }
        }
        );
    }
    public void filterList(ArrayList<OrderModel> filterList) {
        dataList = filterList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

