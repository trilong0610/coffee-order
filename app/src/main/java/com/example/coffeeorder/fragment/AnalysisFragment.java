package com.example.coffeeorder.fragment;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.example.coffeeorder.R;
import com.example.coffeeorder.activity.MainActivity;
import com.example.coffeeorder.model.OrderDetailModel;
import com.example.coffeeorder.model.OrderModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AnalysisFragment extends Fragment implements View.OnClickListener {
    TextInputLayout txtLayoutFromDate;
    TextInputLayout txtLayoutToDate;
    TextInputEditText edtFromDate;
    TextInputEditText edtToDate;
    TextView txtTotalBill;
    TextView txtTotalProduct;
    TextView txtTotalOrder;
    MaterialDatePicker datePicker;
    Calendar calendarFromTime = Calendar.getInstance();
    Calendar calendarToTime = Calendar.getInstance();

    ArrayList<OrderDetailModel> listOrderDetails;
    ArrayList<OrderModel> listOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);
        init(view);
        initEvent(view);
        return view;
    }
    private void init(View view) {
        txtLayoutFromDate = view.findViewById(R.id.txt_layout_analysis_from_date);
        txtLayoutToDate = view.findViewById(R.id.txt_layout_analysis_to_date);
        edtFromDate = view.findViewById(R.id.edt_analysis_from_date);
        edtToDate = view.findViewById(R.id.edt_analysis_to_date);

        listOrderDetails = new ArrayList<>();
        listOrders = new ArrayList<>();

        //gan gia tri mac dinh cho ngay
        // Ngay bat dau = ngay hien tai - 30 ngay
        Date dt = new Date();
        calendarFromTime.setTime(dt);
        calendarFromTime.add(Calendar.DATE, -30);
        updateTimeToEditText(edtFromDate, calendarFromTime);
        // Ngay ket thuc
        updateTimeToEditText(edtToDate, calendarToTime);

        txtTotalBill = view.findViewById(R.id.txt_analysis_total_bill);
        txtTotalProduct = view.findViewById(R.id.txt_analysis_total_product);
        txtTotalOrder = view.findViewById(R.id.txt_analysis_total_order);
        datePicker = MaterialDatePicker.Builder
                .dateRangePicker()
                .setTitleText("Chọn thời gian")
                .setSelection(new Pair<>(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                ))
                .build();

        // load data
        loadDataFromFirebase(calendarFromTime, calendarToTime);

    }

    private void initEvent(View view) {
        txtLayoutFromDate.setOnClickListener(this::onClick);
        edtFromDate.setOnClickListener(this::onClick);
        edtToDate.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        if (v == edtFromDate){
            DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    calendarFromTime.set(Calendar.YEAR, year);
                    calendarFromTime.set(Calendar.MONTH,month);
                    calendarFromTime.set(Calendar.DAY_OF_MONTH,day);
                    // Gan gia tri cho text
                    updateTimeToEditText(edtFromDate, calendarFromTime);
                    // load du lieu theo ngay
                    loadDataFromFirebase(calendarFromTime, calendarToTime);
                }
            };
            new DatePickerDialog(
                    getContext(),
                    date,
                    calendarFromTime.get(Calendar.YEAR),
                    calendarFromTime.get(Calendar.MONTH),
                    calendarFromTime.get(Calendar.DAY_OF_MONTH)
            ).show();
        }
        if (v == edtToDate){
            DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    calendarToTime.set(Calendar.YEAR, year);
                    calendarToTime.set(Calendar.MONTH,month);
                    calendarToTime.set(Calendar.DAY_OF_MONTH,day);

                    // Gan gia tri cho text
                    updateTimeToEditText(edtToDate, calendarToTime);

                    // load du lieu theo ngay
                    loadDataFromFirebase(calendarFromTime, calendarToTime);
                }
            };
            new DatePickerDialog(
                    getContext(),
                    date,
                    calendarToTime.get(Calendar.YEAR),
                    calendarToTime.get(Calendar.MONTH),
                    calendarToTime.get(Calendar.DAY_OF_MONTH)
            ).show();
        }
    }

    private void loadDataFromFirebase(Calendar calendarFromTime, Calendar calendarToTime){
            // San pham
        long fromTime = calendarFromTime.getTimeInMillis() / 1000;
        long toTime = calendarToTime.getTimeInMillis() / 1000;

        Log.d("TAG_ANALYSIS", String.valueOf(toTime));
        listOrders = new ArrayList<>();
        listOrderDetails = new ArrayList<>();
        MainActivity.mDatabase.child("Order").orderByChild("timeOrder").startAt(fromTime).endAt(toTime).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                OrderModel orderModel = snapshot.getValue(OrderModel.class);

                // Lay danh sach don hang
                listOrders.add(orderModel);
                // Lay danh sach chi tiet don hang trong don
//                orderModel.orderDetails.forEach((item) -> listOrderDetails.add(item));
                Log.d("TAG_ANALYSIS", String.valueOf(listOrders.size()));
//                Log.d("TAG_ANALYSIS", String.valueOf(orderModel.getOrderDetails()));
                updateTotalBill();
                updateTotalProduct();
                updateTotalOrder();
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
    private void updateTotalProduct(){
        int total = 0;
        for (OrderDetailModel orderDetailModel: listOrderDetails) {
                total += orderDetailModel.quantity;
        }
        txtTotalProduct.setText(String.valueOf(total));
    }
    private void updateTotalOrder(){
        int total = listOrders.size();
        txtTotalOrder.setText(String.valueOf(total));
    }
    private void updateTotalBill(){
        int total = 0;
        for (OrderModel orderModel: listOrders) {
            total += orderModel.totalOrder;
        }

        txtTotalBill.setText(String.format("%,d", Long.parseLong(String.valueOf(total))) + "đ");
    }
    private void updateTimeToEditText(EditText editText, Calendar calendar){
        String format = "dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(format, Locale.US);
        editText.setText(dateFormat.format(calendar.getTime()));
    }
}