package com.example.coffeeorder.fragment;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OrderFragment extends Fragment {
    //private ArrayList<OrderModel> listOrders;
    private ArrayList<OrderModel> dataList;
    private ArrayList<OrderModel> filteredList;
    private ArrayList<OrderDetailModel> orderDetails;

    private RecyclerView recyclerView;
    private OrderAdapter mAdapter;

    private FloatingActionButton btnOpenFilter;
    private BottomSheetDialog bottomSheetDialog;

    TextInputEditText edtFromDate;
    TextInputEditText edtToDate;

    TextInputLayout layoutMenuFilter;

    MaterialDatePicker datePicker;
    Calendar calendarFromTime = Calendar.getInstance();
    Calendar calendarToTime = Calendar.getInstance();

    AutoCompleteTextView menuType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.fragment_order, container, false);
        initView(view);
        initEvent(view);

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
    private void initView(View view) {
        dataList = new ArrayList<OrderModel>();
        filteredList = new ArrayList<>(dataList);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnOpenFilter = view.findViewById(R.id.floatbtn_order_open_filter);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mAdapter = new OrderAdapter(dataList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        edtFromDate = view.findViewById(R.id.edt_order_from_date);
        edtToDate = view.findViewById(R.id.edt_order_to_date);
        layoutMenuFilter = view.findViewById(R.id.menu_order_filter);
        //gan gia tri mac dinh cho ngay
        // Ngay bat dau = ngay hien tai - 30 ngay
        Date dt = new Date();
        calendarFromTime.setTime(dt);
        calendarFromTime.add(Calendar.DATE, -30);
        updateTimeToEditText(edtFromDate, calendarFromTime);
        // Ngay ket thuc
        updateTimeToEditText(edtToDate, calendarToTime);

        // gan item cho menu dropdown
        ArrayList<String> items = new ArrayList<>();
        items.add("Chưa pha chế");
        items.add("Đã pha chế");
        items.add("Đã thanh toán");
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.item_menu_type, items);
        menuType = (AutoCompleteTextView) layoutMenuFilter.getEditText();
        menuType.setAdapter(arrayAdapter);
        menuType.setText("Chưa pha chế", false);

        loadData(0);
    }
    private void initEvent(View view) {
        menuType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", String.valueOf(position));
                loadData(position);
            }
        });
    }
    // load danh sach order tu db
    private void loadData(int status){
        dataList.clear();
        mAdapter.notifyDataSetChanged();
        MainActivity.mDatabase.child("Order").orderByChild("statusOrder").equalTo(status).addChildEventListener(new ChildEventListener() {
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
    private void updateTimeToEditText(EditText editText, Calendar calendar){
        String format = "dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(format, Locale.US);
        editText.setText(dateFormat.format(calendar.getTime()));
    }
}