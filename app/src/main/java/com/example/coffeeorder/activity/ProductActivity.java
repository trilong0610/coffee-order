package com.example.coffeeorder.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorder.R;
import com.example.coffeeorder.data.ProductAdapter;
import com.example.coffeeorder.model.OrderModel;
import com.example.coffeeorder.model.ProductModel;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.ncorti.slidetoact.SlideToActView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView rv_product_main;
    private ArrayList<ProductModel> listProduct;
    private ProductAdapter adapter;
    private String id_table;
    private MaterialTextView txt_bill;
    private SlideToActView slideToActView;

    private ArrayList<ProductModel> listProductOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        initView();
        initEvent();
        loadData();
        id_table = getIntent().getExtras().getString("id_table");
    }

    private void initEvent() {

        slideToActView.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                // ------------------tao don-------------------------
                // push de tao 1 note moi voi id ngau nhien
                // getKey de lay id cua note vua tao
                String key = MainActivity.mDatabase.child("Order").push().getKey();

                long currentTime = System.currentTimeMillis()/1000;

                OrderModel orderModel = new OrderModel(key,currentTime,0,Long.parseLong(getBill(listProductOrder)),"a","detail","table", listProductOrder);
                // toMap de tao ra model dung format de update len firebase
                Map<String, Object> postValues = orderModel.toMap();

                Map<String, Object> childUpdates = new HashMap<>();

                // update thong tin len note vua tao
                childUpdates.put("/Order/" +key, postValues);

                MainActivity.mDatabase.updateChildren(childUpdates);

                // ------------------chinh sua trang thai ban-------------------------
                Map<String, Object> tableUpdate = new HashMap<>();
                tableUpdate.put("/"+ id_table + "/idOrder/", key );
                tableUpdate.put("/"+ id_table + "/status/", false );
                MainActivity.mDatabase.child("Table").updateChildren(tableUpdate);

                // Dong activity
                finish();
            }
        });
    }

    private void initView(){
        rv_product_main = findViewById(R.id.rv_product_main);
        listProduct = new ArrayList<ProductModel>();
        listProductOrder = new ArrayList<ProductModel>();
        slideToActView = findViewById(R.id.slide_product_order);
        txt_bill = findViewById(R.id.txt_product_bill);
        //        Set layout de hien thi thong tin trong recycle view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);

//        Đảo ngược chiều thêm item từ dưới lên
        gridLayoutManager.setReverseLayout(true);

//        linearLayoutManager.setStackFromEnd(true);
//        rvUsers.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rv_product_main.setLayoutManager(gridLayoutManager);

        adapter = new ProductAdapter(listProduct, this, new ProductAdapter.OnItemChangeListener() {
            @Override
            public void onItemAdd(ProductModel item, int number) {
                for (int i = 0; i < number; i++) {
                    listProductOrder.add(item);
                }

                txt_bill.setText(String.format("%,d", Long.parseLong(getBill(listProductOrder))));
                Log.e("listProductOrder", String.valueOf(listProductOrder));
            }

            @Override
            public void onItemDelete(ProductModel item, int number) {
                for (int i = 0; i < number; i++) {
                    listProductOrder.remove(item);
                }
                txt_bill.setText(String.format("%,d", Long.parseLong(getBill(listProductOrder))));
                Log.e("listProductOrder", String.valueOf(listProductOrder));
            }

        });

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

    private String getBill(ArrayList<ProductModel> productModels){
        int total = 0;
        for (ProductModel productModel: productModels) {
            total += productModel.priceProduct;
        }
        return String.valueOf(total);

    };
}