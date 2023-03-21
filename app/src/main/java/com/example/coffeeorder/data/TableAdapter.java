package com.example.coffeeorder.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorder.R;
import com.example.coffeeorder.activity.MainActivity;
import com.example.coffeeorder.fragment.HomeFragment;
import com.example.coffeeorder.fragment.ProductFragment;
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
        holder.txtItemTableId.setText(tableModels.get(position).idTable);
        holder.txtItemTableQuantity.setText(String.valueOf(tableModels.get(position).quantity));
        holder.txtItemTableStatus.setText(String.valueOf(tableModels.get(position).status));
        holder.txtItemTableIdOrder.setText(tableModels.get(position).idOrder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ProductFragment();
                ((MainActivity) view.getContext()).addFragment(new ProductFragment(), true);
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
        public TextView txtItemTableIdOrder;
        public TableItemViewHolder(@NonNull View itemView) {
            super(itemView);

            txtItemTableId = itemView.findViewById(R.id.txt_item_table_id);
            txtItemTableQuantity = itemView.findViewById(R.id.txt_item_table_quantity);
            txtItemTableStatus = itemView.findViewById(R.id.txt_item_table_status);
            txtItemTableIdOrder = itemView.findViewById(R.id.txt_item_table_idOrder);
        }
    }
}
