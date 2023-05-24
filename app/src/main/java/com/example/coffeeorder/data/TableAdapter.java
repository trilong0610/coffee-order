package com.example.coffeeorder.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorder.R;
import com.example.coffeeorder.activity.PaymentOrderActivity;
import com.example.coffeeorder.activity.ProductActivity;
import com.example.coffeeorder.model.TableModel;

import java.util.ArrayList;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableItemViewHolder>{
    private ArrayList<TableModel> tableModels;
    private Context context;
    public TableAdapter(ArrayList<TableModel> tableModels, Context c) {
        this.tableModels = tableModels;
        this.context = c;
    }
    @NonNull
    @Override
    public TableAdapter.TableItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_table, parent, false);
        parent.scrollTo(0,0);
        return new TableItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TableAdapter.TableItemViewHolder holder, int position) {
        TableModel tableModel = tableModels.get(position);
        holder.txtItemTableId.setText(tableModel.idTable);
        holder.txtItemTableQuantity.setText(String.valueOf(tableModels.get(position).quantity));
        holder.txtItemTableStatus.setText(String.valueOf(tableModels.get(position).status));
        // Gan mau cho item
        if (tableModel.status == true) { // san sang
            holder.layout.setBackground(new ColorDrawable(ContextCompat.getColor(context, R.color.green)));
        }
        else {
            holder.layout.setBackground(new ColorDrawable(ContextCompat.getColor(context, R.color.red)));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tableModel.status == true) {
                    // san sang
                    Intent intent = new Intent(context, ProductActivity.class);
                    intent.putExtra("id_table", tableModel.idTable);
                    Log.d("TAG_TABLE", String.valueOf(tableModel.idTable));
                    context.startActivity(intent);
                }
                else { // dang co khach

                    // mo activity thanh toan
                    Intent intent = new Intent(context, PaymentOrderActivity.class);
                    intent.putExtra("id_table", tableModel.idTable);
                    intent.putExtra("id_order", tableModel.idOrder);
                    intent.putExtra("from_activity", "main");
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return tableModels.size();
    }

    public class TableItemViewHolder extends RecyclerView.ViewHolder {
        public TextView txtItemTableId;
        public TextView txtItemTableQuantity;
        public TextView txtItemTableStatus;

        public LinearLayout layout;
        public TableItemViewHolder(@NonNull View itemView) {
            super(itemView);

            txtItemTableId = itemView.findViewById(R.id.txt_item_table_id);
            txtItemTableQuantity = itemView.findViewById(R.id.txt_item_table_quantity);
            txtItemTableStatus = itemView.findViewById(R.id.txt_item_table_status);
            layout = itemView.findViewById(R.id.layout_item_table);
        }
    }
}
