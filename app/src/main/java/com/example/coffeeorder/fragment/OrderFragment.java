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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;

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


    TextInputEditText edtFromDate;
    TextInputEditText edtToDate;

    TextInputLayout layoutMenuFilter;

    Calendar calendarFromTime = Calendar.getInstance();
    Calendar calendarToTime = Calendar.getInstance();

    AutoCompleteTextView menuType;

    int currentPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.fragment_order, container, false);
        initView(view);
        initEvent(view);



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(currentPosition);
    }

    private void initView(View view) {
        dataList = new ArrayList<OrderModel>();
        filteredList = new ArrayList<>(dataList);
        recyclerView = view.findViewById(R.id.recyclerView);

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
    }

    private void initEvent(View view) {
        menuType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", String.valueOf(position));
                currentPosition = position;
                loadData(currentPosition);
            }
        });

    }
    // load danh sach order tu db
    private void loadData(int status){
        dataList.clear();
        mAdapter.notifyDataSetChanged();
        MainActivity.mDatabase.child("Order")
                .orderByChild("statusOrder")
                .equalTo(status)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        for (DataSnapshot snapshot: task.getResult().getChildren()) {
                            OrderModel orderModel = snapshot.getValue(OrderModel.class);
                            Log.d("TAG", String.valueOf(orderModel.idOrder));
                            dataList.add(orderModel);
                            // cap nhat adapter
                            mAdapter.notifyDataSetChanged();
//                ---------------Cuộn đến item trên cùng khi thêm---------------------------
                            recyclerView.scrollToPosition(dataList.size() - 1);
                        }

                    }
                });

    }
    private void updateTimeToEditText(EditText editText, Calendar calendar){
        String format = "dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(format, Locale.US);
        editText.setText(dateFormat.format(calendar.getTime()));
    }

}