package com.example.coffeeorder.fragment;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
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
    private ArrayList<OrderModel> filteredList;
    private ArrayList<OrderDetailModel> orderDetails;

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

        //Xử lý search
        EditText searchEditText = view.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString().trim();
                filter(keyword);
            }
        });
        //Xử lý spinner
        Spinner filterSpinner = view.findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.filter_options, // Tạo một mảng string trong strings.xml với các tùy chọn lọc
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(spinnerAdapter);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                filterStatus(selectedOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        prepareMovieData();
        return view;
    }


    private void filter(String keyword) {
        filteredList.clear();

        for (OrderModel item : dataList) {
            if (item.getIdUser().toLowerCase().contains(keyword.toLowerCase())||item.getIdTable().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(item);
            }
        }

        mAdapter.filterList(filteredList);
    }
    private void filterStatus(String selectedOption) {
        filteredList.clear();

        // Lọc dữ liệu dựa trên tùy chọn được chọn
        if (selectedOption.equals("Tất cả")) {
            // Lọc dữ liệu cho Option 1
            for (OrderModel item : dataList) {

                // Kiểm tra và thêm item vào danh sách lọc
                if (item.getStatusOrder() == 0 || item.getStatusOrder() == 1 || item.getStatusOrder() == 2) {
                    filteredList.add(item);
                }
            }
        }
        if (selectedOption.equals("Chưa hoàn thành (0)")) {
            // Lọc dữ liệu cho Option 1
            for (OrderModel item : dataList) {

                // Kiểm tra và thêm item vào danh sách lọc
                if (item.getStatusOrder() == 0) {
                    filteredList.add(item);
                }
            }
        } else if (selectedOption.equals("Đã hoàn thành (1)")) {
            // Lọc dữ liệu cho Option 2
            for (OrderModel item : dataList) {
                // Kiểm tra và thêm item vào danh sách lọc
                if (item.getStatusOrder() == 1) {
                    filteredList.add(item);
                }
            }
        } else if (selectedOption.equals("Đã thanh toán (2)")) {
            // Lọc dữ liệu cho Option 3
            for (OrderModel item : dataList) {
                // Kiểm tra và thêm item vào danh sách lọc
                if (item.getStatusOrder() == 2) {
                    filteredList.add(item);
                }
            }
        }

        mAdapter.filterList(filteredList);
    }

    private void prepareMovieData()
    {
        loadData();
        /*OrderModel orderModel = new OrderModel("1", 1, 20000, "User", "Detail Order", "1", orderDetails);
        dataList.add(orderModel);
        orderModel = new OrderModel("2", 2, 20000, "User", "Detail Order", "1", orderDetails);
        dataList.add(orderModel);*/
        mAdapter.notifyDataSetChanged();
    }
    private void initEvent(View view) {
        //loadData();

    }

    private void initView(View view) {
        dataList = new ArrayList<OrderModel>();
        filteredList = new ArrayList<>(dataList);
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