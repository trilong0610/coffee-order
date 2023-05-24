package com.example.coffeeorder.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorder.R;
import com.example.coffeeorder.data.OrderDetailsAdapter;
import com.example.coffeeorder.model.OrderDetailModel;
import com.example.coffeeorder.model.OrderModel;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.ncorti.slidetoact.SlideToActView;

import java.util.ArrayList;

public class PaymentOrderActivity extends AppCompatActivity {
    String idOrder = "";
    String idTable = "";
    String fromActivity = "main";
    OrderModel orderModel; // don hang hien tai cua ban
    ArrayList<OrderDetailModel> listOrderDetails;

    RecyclerView rvPaymentMain;
    OrderDetailsAdapter adapter;

    MaterialTextView txtBill;
    SlideToActView slideComplete;

    ImageView btnDeleteOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_order);

        // Nhan idOrder tu intent
        idTable = getIntent().getExtras().getString("id_table");
        idOrder = getIntent().getExtras().getString("id_order");
        fromActivity = getIntent().getExtras().getString("from_activity");
        Log.e("orders", String.valueOf(idOrder));

        initView();
        initEvent();
        loadData();
    }



    private void initView() {
        rvPaymentMain = findViewById(R.id.rv_payment_main);
        slideComplete = findViewById(R.id.slide_order_complete);
        txtBill = findViewById(R.id.txt_order_bill);
        btnDeleteOrder = findViewById(R.id.btn_payment_delete);
        orderModel = new OrderModel();
        listOrderDetails = new ArrayList<>();
        adapter = new OrderDetailsAdapter(this, listOrderDetails, orderModel.statusOrder);

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
                    Toast.makeText(getBaseContext(),"Cập nhật đơn thành công",Toast.LENGTH_SHORT).show();

                }

                if (orderModel.statusOrder == 1){
                    // thanh toan don
                    completeBill(idOrder, idTable);
                    Toast.makeText(getBaseContext(),"Cập nhật đơn thành công",Toast.LENGTH_SHORT).show();

                }

            }
        });
        btnDeleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PaymentOrderActivity.this)
                        .setMessage("Bạn muốn xóa đơn hàng này ?")
                        .setCancelable(true)
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // Cap nhat ban
                                MainActivity.mDatabase.child("Table")
                                                .child(idTable)
                                                        .child("idOrder").setValue("");
                                MainActivity.mDatabase.child("Table")
                                        .child(idTable)
                                        .child("status").setValue(true);

                                // Xoa don hang
                                MainActivity.mDatabase.child("Order")
                                        .child(idOrder)
                                        .removeValue();
                                Toast.makeText(getApplicationContext(), "Xóa đơn hàng thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
    }

    private void loadData(){
        // lay du lieu order
        MainActivity.mDatabase.child("Order")
                .orderByChild("idOrder")
                .equalTo(idOrder)
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                orderModel = snapshot.getValue(OrderModel.class);
                Log.d("TAG_ORDER", String.valueOf(orderModel.orderDetails));
                if (orderModel == null)
                    return;
                adapter.setOrderStatus(orderModel.statusOrder);
                adapter.setOrderId(orderModel.idOrder);
                // Lay danh sach san pham tu db
                orderModel.orderDetails.forEach((details) ->
                        {
                            if (details != null) {
                                listOrderDetails.add(details);
                            }

                        }
                );
//                Log.d("TAG_ORDER", String.valueOf(listOrderDetails));
                adapter.notifyDataSetChanged();
                rvPaymentMain.scrollToPosition(listOrderDetails.size() - 1);
                // Tinh tong tien
                txtBill.setText(getBill(listOrderDetails));

                if (orderModel.statusOrder == 0 && (MainActivity.permission == 0 || MainActivity.permission == 1)){
                    // Neu don chua hoan thanhf va login la admin hoac phuc vu
                    // Cho phep xoa
                        btnDeleteOrder.setVisibility(View.VISIBLE);
                    }
                else {
                    btnDeleteOrder.setVisibility(View.INVISIBLE);
                }


                // Thay doi thong tin slide
                if (orderModel.statusOrder == 0){
                    // Chua pha che
                    // gan thanh truot = hoan thanh
                    if (MainActivity.permission == 0 || MainActivity.permission == 2){
                        // Neu la account admin hoac pha che -> cho cap nhat pha che
                        slideComplete.setVisibility(View.VISIBLE);
                        slideComplete.setText("Đã pha chế");


                    }
                    else {
                        // Neu la account phuc vu -> khong cho cap nhat pha che
                        slideComplete.setVisibility(View.INVISIBLE);
                    }

                }

                if (orderModel.statusOrder == 1){
                    // Da pha che
                    // gan thanh truot = thanh toan
                    if (MainActivity.permission == 0 || MainActivity.permission == 1){
                        // Neu la account admin hoac phuc vu -> cho hoan thanh don
                        slideComplete.setVisibility(View.VISIBLE);
                        slideComplete.setText("Thanh toán");
                    }
                    else {
                        // Neu la account pha che -> khong cho hoan thanh don
                        slideComplete.setVisibility(View.INVISIBLE);
                    }

                }
                if (orderModel.statusOrder == 2){
                    // da thanh toan
                    slideComplete.setVisibility(View.INVISIBLE);
                }

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