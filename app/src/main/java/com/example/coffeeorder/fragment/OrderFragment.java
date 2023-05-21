package com.example.coffeeorder.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorder.R;
import com.example.coffeeorder.activity.MainActivity;
import com.example.coffeeorder.data.OrderAdapter;
import com.example.coffeeorder.model.OrderDetailModel;
import com.example.coffeeorder.model.OrderModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class OrderFragment extends Fragment {
    //private ArrayList<OrderModel> listOrders;
    private ArrayList<OrderModel> dataList;
    ArrayList<OrderDetailModel> orderDetails;
    private RecyclerView recyclerView;
    private OrderAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.fragment_order, container, false);
        initView(view);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mAdapter = new OrderAdapter(dataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareMovieData();
        return view;

    }

    private void prepareMovieData()
    {
        loadData();
        OrderModel orderModel = new OrderModel("1", 1, 20000, "User", "Detail Order", "1", orderDetails);
        dataList.add(orderModel);
        orderModel = new OrderModel("2", 2, 20000, "User", "Detail Order", "1", orderDetails);
        dataList.add(orderModel);
        mAdapter.notifyDataSetChanged();
    }
    private void initEvent(View view) {
        //loadData();

    }

    private void initView(View view) {
        dataList = new ArrayList<OrderModel>();
    }

    // load danh sach order tu db
    private void loadData(){
        MainActivity.mDatabase.child("Order").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                OrderModel orderModel = snapshot.getValue(OrderModel.class);
                Log.d("TAG", String.valueOf(orderModel.idOrder));
                dataList.add(orderModel);


                // cap nhat adapter
                mAdapter.notifyDataSetChanged();
//                ---------------Cuộn đến item trên cùng khi thêm---------------------------
                recyclerView.scrollToPosition(dataList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Lay thong tin don hang thay doi
                OrderModel orderModel = snapshot.getValue(OrderModel.class);
                // Tim don hang trong list
                for (OrderModel item : dataList) {
                    if (item.idOrder == orderModel.idOrder){
                        item.statusOrder = orderModel.statusOrder;
                        mAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(dataList.size() - 1);
                        return;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}