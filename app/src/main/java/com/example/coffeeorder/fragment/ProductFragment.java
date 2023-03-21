package com.example.coffeeorder.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffeeorder.R;
import com.example.coffeeorder.activity.MainActivity;
import com.example.coffeeorder.data.ProductAdapter;
import com.example.coffeeorder.data.TableAdapter;
import com.example.coffeeorder.model.ProductModel;
import com.example.coffeeorder.model.TableModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;


public class ProductFragment extends Fragment {

    private RecyclerView rv_product_main;
    private ArrayList<ProductModel> listProduct;
    private ProductAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        initView(view);
        loadData();
        initEvent(view);


        return view;
    }

    private void initEvent(View view) {

    }

    private void initView(View view){
        rv_product_main = view.findViewById(R.id.rv_product_main);
        listProduct = new ArrayList<ProductModel>();
        //        Set layout de hien thi thong tin trong recycle view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);

//        Đảo ngược chiều thêm item từ dưới lên
        gridLayoutManager.setReverseLayout(true);

//        linearLayoutManager.setStackFromEnd(true);
//        rvUsers.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rv_product_main.setLayoutManager(gridLayoutManager);

        adapter = new ProductAdapter(listProduct, getContext());

        rv_product_main.setAdapter(adapter);

        rv_product_main.scrollToPosition(listProduct.size() - 1);

    }

    private void loadData(){
        MainActivity.mDatabase.child("Product").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ProductModel productModel = snapshot.getValue(ProductModel.class);

                listProduct.add(productModel);

                adapter.notifyDataSetChanged();

//                ---------------Cuộn đến item trên cùng khi thêm---------------------------
                rv_product_main.scrollToPosition(listProduct.size() - 1);
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

}