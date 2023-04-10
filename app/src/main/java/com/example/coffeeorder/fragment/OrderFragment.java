package com.example.coffeeorder.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coffeeorder.R;
import com.example.coffeeorder.activity.MainActivity;
import com.example.coffeeorder.model.OrderModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class OrderFragment extends Fragment {
    private ArrayList<OrderModel> orderModels;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_order, container, false);
        initView(view);
        initEvent(view);
        return view;
    }

    private void initEvent(View view) {
        // load danh sach order tu db
        MainActivity.mDatabase.child("Order").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                orderModels.add(snapshot.getValue(OrderModel.class));

                // Gan data vao adapter

//                <ten adapter>.notifyDataSetChanged();

//                ---------------Cuộn đến item trên cùng khi thêm---------------------------
//                <ten recycleview>.scrollToPosition(listProduct.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

    private void initView(View view) {
        orderModels = new ArrayList<OrderModel>();
    }
}