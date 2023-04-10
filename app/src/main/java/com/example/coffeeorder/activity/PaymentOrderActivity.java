package com.example.coffeeorder.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorder.R;
import com.example.coffeeorder.data.ProductAdapter;
import com.example.coffeeorder.model.OrderModel;
import com.example.coffeeorder.model.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentOrderActivity extends AppCompatActivity {
    String idOrder = "";
    String idTable = "";
    OrderModel orderModel; // don hang hien tai cua ban
    ArrayList<ProductModel> productModels;

    RecyclerView rvMain;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_order);

        // Nhan idTable tu itent
        idTable = getIntent().getExtras().getString("id_table");
        idOrder = getIntent().getExtras().getString("id_order");
        Log.e("orders", String.valueOf(idOrder));
        initView();
        initEvent();
    }

    private void initEvent() {
        // lay du lieu order
        MainActivity.mDatabase.child("Order").child(idOrder).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    orderModel = task.getResult().getValue(OrderModel.class);
                    productModels = orderModel.productOrders;
                    for (ProductModel productModel: productModels
                         ) {
                        Log.e("orders", productModel.nameProduct);

                    }
                    // Tao adapter
                    adapter.notifyDataSetChanged();
                }
                else {
                    orderModel = null;
                }
            }
        });

    }

    private void initView() {
        rvMain = findViewById(R.id.rv_payment_main);
        orderModel = new OrderModel();
        productModels = new ArrayList<ProductModel>();
        adapter = new ProductAdapter(productModels, this, new ProductAdapter.OnItemChangeListener() {
            @Override
            public void onItemAdd(ProductModel item, int number) {

            }

            @Override
            public void onItemDelete(ProductModel item, int number) {

            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);

//        Đảo ngược chiều thêm item từ dưới lên
        gridLayoutManager.setReverseLayout(true);
        rvMain.setLayoutManager(gridLayoutManager);
        rvMain.setAdapter(adapter);
        rvMain.scrollToPosition(productModels.size() - 1);

    }

    // khi ng dung thanh toan thi goi ham nay
    private void completeBill(String idOrder, String idTable){
        // ------------------chinh sua trang thai ban-------------------------
        Map<String, Object> tableUpdate = new HashMap<>();
        tableUpdate.put("/"+ idTable + "/idOrder/", "" );
        tableUpdate.put("/"+ idTable + "/status/", true );
        MainActivity.mDatabase.child("Table").updateChildren(tableUpdate);
    // ------------------chinh sua trang thai don hang-------------------------
        Map<String, Object> orderUpdate = new HashMap<>();
        orderUpdate.put("/"+ idOrder + "/statusOrder/", 2 );
        MainActivity.mDatabase.child("Order").updateChildren(orderUpdate);
        // Dong activity
        finish();
    }
}