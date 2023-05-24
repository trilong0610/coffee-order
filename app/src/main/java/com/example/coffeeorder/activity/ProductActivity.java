package com.example.coffeeorder.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorder.R;
import com.example.coffeeorder.data.ProductAdapter;
import com.example.coffeeorder.model.OrderDetailModel;
import com.example.coffeeorder.model.OrderModel;
import com.example.coffeeorder.model.ProductModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.ncorti.slidetoact.SlideToActView;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView rv_product_main;
    private ArrayList<OrderDetailModel> dataList;
    private ProductAdapter adapter;
    private String id_table;
    private MaterialTextView txt_bill;
    private SlideToActView slideToActView;

    private ImageView btnSearch;

    private ArrayList<OrderDetailModel> listOrderDetail;

    private TextInputLayout edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        initView();
        initEvent();
        loadData();
        id_table = getIntent().getExtras().getString("id_table");
    }

    private void initView(){
        rv_product_main = findViewById(R.id.rv_product_main);
        dataList = new ArrayList<OrderDetailModel>();
        listOrderDetail = new ArrayList<OrderDetailModel>();
        slideToActView = findViewById(R.id.slide_product_order);
        txt_bill = findViewById(R.id.txt_product_bill);
        edtSearch = findViewById(R.id.edt_product_search);
        btnSearch = findViewById(R.id.btn_product_search);

        //        Set layout de hien thi thong tin trong recycle view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);

//        Đảo ngược chiều thêm item từ dưới lên
        gridLayoutManager.setReverseLayout(true);

//        linearLayoutManager.setStackFromEnd(true);
//        rvUsers.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rv_product_main.setLayoutManager(gridLayoutManager);

        adapter = new ProductAdapter(dataList, this, new ProductAdapter.OnItemChangeListener() {
            @Override
            public void updateList(ArrayList<OrderDetailModel> orderDetailModels) {
                dataList = orderDetailModels;
                txt_bill.setText(getBill(dataList));
            }

        });

        rv_product_main.setAdapter(adapter);


        // Kiem tra quyen
        // Chi cho phuc vu hoac admin them
        Log.d("TAG_D", String.valueOf(MainActivity.permission));
        if (MainActivity.permission == 0 || MainActivity.permission == 1){
            slideToActView.setVisibility(View.VISIBLE);
        }
        else{
            slideToActView.setVisibility(View.INVISIBLE);
        }

    }
    private void initEvent() {

        slideToActView.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NonNull SlideToActView slideToActView) {

//                Kiem tra da them san pham vao gio hang chua
                if (listOrderDetail.size() <= 0){
                    Toast.makeText(getBaseContext(),"Vui lòng thêm sản phẩm",Toast.LENGTH_SHORT).show();
                    slideToActView.resetSlider();
                    return;
                }
                // ------------------tao don-------------------------
                // push de tao 1 note moi voi id ngau nhien
                // getKey de lay id cua note vua tao
                String key = MainActivity.mDatabase.child("Order").push().getKey();

                long currentTime = System.currentTimeMillis()/1000;

                long total = 0;
                for (OrderDetailModel orderDetailModel: listOrderDetail) {
                    total += orderDetailModel.product.priceProduct * orderDetailModel.quantity;
                }

                OrderModel orderModel = new OrderModel(key,0,total,MainActivity.email,"detail",id_table, listOrderDetail);
                // toMap de tao ra model dung format de update len firebase
//                Map<String, Object> postValues = orderModel.toMap();
//
//                Map<String, Object> childUpdates = new HashMap<>();

                // update thong tin len note vua tao
//                childUpdates.put("/Order/" +key, postValues);
                MainActivity.mDatabase.child("Order").child(key).setValue(orderModel);


//                MainActivity.mDatabase.updateChildren(childUpdates);

                // ------------------chinh sua trang thai ban-------------------------
                MainActivity.mDatabase.child("Table").child(id_table).child("status").setValue(false);
                MainActivity.mDatabase.child("Table").child(id_table).child("idOrder").setValue(key);
                // Gui data len note thong bao
                String notifyKey = MainActivity.mDatabase.child("Notify").push().getKey();
                MainActivity.mDatabase.child("Notify").child(notifyKey).setValue(orderModel);
                // Xoa data
                MainActivity.mDatabase.child("Notify").child(notifyKey).removeValue();
                Toast.makeText(getBaseContext(),"Tạo đơn thành công",Toast.LENGTH_SHORT).show();

                // Dong activity
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSearch.getVisibility() == View.INVISIBLE){
                    // neu input an => Hien input
                    edtSearch.setVisibility(View.VISIBLE);
                }
                else {
                    // Tim kiem
                    // Kiem tra input
                    Log.d("Tag_searcg", edtSearch.getEditText().getText().toString());
                    if (edtSearch.getEditText().getText().toString().equalsIgnoreCase("")){
                        // ng dung xoa keyword
                        // Tra ve full product
                        adapter.updateAdapter(dataList);
                    }
                    else {
                        String key = edtSearch.getEditText().getText().toString();
                        Log.d("Tag_searcg", key);
                        ArrayList<OrderDetailModel> listTemp = new ArrayList<>();

                        // lay danh sach nhung sp co ten = key
                        dataList.forEach((detail) -> {
                            if (detail.product.nameProduct.toLowerCase().contains(key.toLowerCase())) {
                                listTemp.add(detail);
                            }
                        });

                        adapter.updateAdapter(listTemp);
                    }
                    // An search
                    edtSearch.setVisibility(View.INVISIBLE);
                }
            }
        });


    }

    private void loadData(){
        MainActivity.mDatabase.child("Product").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ProductModel productModel = snapshot.getValue(ProductModel.class);

                dataList.add(new OrderDetailModel(productModel, 0));

                adapter.notifyDataSetChanged();

                rv_product_main.scrollToPosition(dataList.size() - 1);
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

    private String getBill(ArrayList<OrderDetailModel> orderDetailModels){
        int total = 0;
        for (OrderDetailModel orderDetailModel: orderDetailModels) {
            total += orderDetailModel.product.priceProduct * orderDetailModel.quantity;
        }
        return String.format("%,d", Long.parseLong(String.valueOf(total)));
    };
}