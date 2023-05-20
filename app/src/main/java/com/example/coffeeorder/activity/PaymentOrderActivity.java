package com.example.coffeeorder.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorder.R;
import com.example.coffeeorder.data.OrderDetailsAdapter;
import com.example.coffeeorder.model.OrderDetailModel;
import com.example.coffeeorder.model.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.ncorti.slidetoact.SlideToActView;

import java.util.ArrayList;

public class PaymentOrderActivity extends AppCompatActivity {
    String idOrder = "";
    String idTable = "";
    OrderModel orderModel; // don hang hien tai cua ban
    ArrayList<OrderDetailModel> listOrderDetails;

    RecyclerView rvPaymentMain;
    OrderDetailsAdapter adapter;

    MaterialTextView txtBill;
    SlideToActView slideComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_order);

        // Nhan idOrder tu intent
        idTable = getIntent().getExtras().getString("id_table");
        idOrder = getIntent().getExtras().getString("id_order");
        Log.e("orders", String.valueOf(idOrder));

        initView();
        initEvent();
        loadData();
    }



    private void initView() {
        rvPaymentMain = findViewById(R.id.rv_payment_main);
        slideComplete = findViewById(R.id.slide_order_complete);
        txtBill = findViewById(R.id.txt_order_bill);
        orderModel = new OrderModel();
        listOrderDetails = new ArrayList<>();
        adapter = new OrderDetailsAdapter(this, listOrderDetails);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);

//        Đảo ngược chiều thêm item từ dưới lên
        gridLayoutManager.setReverseLayout(true);
        rvPaymentMain.setLayoutManager(gridLayoutManager);
        rvPaymentMain.setAdapter(adapter);



    }

    private void initEvent() {
        slideComplete.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                if (orderModel.statusOrder == 0){
                    // Hoan thanh pha che
                    completeBartending(idOrder);

                }

                if (orderModel.statusOrder == 1){
                    // thanh toan don
                    completeBill(idOrder, idTable);

                }

            }
        });

    }

    private void loadData(){
        // lay du lieu order
        MainActivity.mDatabase.child("Order").child(idOrder).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                orderModel = task.getResult().getValue(OrderModel.class);
                // Lay danh sach san pham tu db
                orderModel.orderDetails.forEach((details) -> listOrderDetails.add(details));
                Log.d("TAG_ORDER", String.valueOf(listOrderDetails));
                adapter.notifyDataSetChanged();
                rvPaymentMain.scrollToPosition(listOrderDetails.size() - 1);
                // Tinh tong tien
                txtBill.setText(getBill(listOrderDetails));
                // Thay doi thong tin slide
                if (orderModel.statusOrder == 0){
                    // Chua pha che
                    // gan thanh truot = hoan thanh
                    slideComplete.setText("Pha chế hoàn thành");
                }
                if (orderModel.statusOrder == 1){
                    // Da pha che
                    // gan thanh truot = thanh toan
                    slideComplete.setText("Thanh toán");
                }
                if (orderModel.statusOrder == 2){
                    // da thanh toan
                    slideComplete.setEnabled(false);
                }
            }
        });
    }

    // khi ng dung thanh toan thi goi ham nay
    private void completeBill(String idOrder, String idTable){
        // ------------------chinh sua trang thai ban-------------------------
        MainActivity.mDatabase.child("Table").child(idTable).child("status").setValue(true);
        MainActivity.mDatabase.child("Table").child(idTable).child("idOrder").setValue("");
    // ------------------chinh sua trang thai don hang-------------------------
        MainActivity.mDatabase.child("Order").child(idOrder).child("statusOrder").setValue(2);
        MainActivity.mDatabase.child("Order").child(idOrder).child("timeComplete").setValue(System.currentTimeMillis()/1000);
        // Dong activity
        finish();
    }

    private void completeBartending(String idOrder){
        // ------------------chinh sua trang thai don hang-------------------------
        MainActivity.mDatabase.child("Order").child(idOrder).child("statusOrder").setValue(1);
        // Dong activity
        finish();
    }

    private String getBill(ArrayList<OrderDetailModel> orderDetailModels){
        int total = 0;
        for (OrderDetailModel orderDetailModel: orderDetailModels) {
            total += orderDetailModel.product.priceProduct * orderDetailModel.quantity;
        }
        return String.format("%,d", Long.parseLong(String.valueOf(total)));
    };
}