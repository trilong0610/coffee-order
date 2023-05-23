package com.example.coffeeorder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coffeeorder.R;
import com.example.coffeeorder.activity.LoginActivity;
import com.example.coffeeorder.model.TableModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AdminFragment extends Fragment implements View.OnClickListener {
    private DatabaseReference mDatabase;
    private Button btnAddTable;
    private Button btnAddOrder;
    private Button btnAddLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        initView(view);
        initEvent(view);

        return view;
    }

    private void initView(View view){
        btnAddTable = view.findViewById(R.id.btn_admin_add_table);
        btnAddOrder = view.findViewById(R.id.btn_admin_add_order);
        btnAddLogout = view.findViewById(R.id.btn_admin_logout);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void initEvent(View view){
        btnAddTable.setOnClickListener(this::onClick);
        btnAddOrder.setOnClickListener(this::onClick);
        btnAddLogout.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (view == btnAddTable){
            // push de tao 1 note moi voi id ngau nhien
            // getKey de lay id cua note vua tao
            String key = mDatabase.child("Table").push().getKey();

            TableModel table = new TableModel(key,0,true,"");
            // toMap de tao ra model dung format de update len firebase
            Map<String, Object> postValues = table.toMap();

            Map<String, Object> childUpdates = new HashMap<>();

            // update thong tin len note vua tao
            childUpdates.put("/Table/" +key, postValues);

            mDatabase.updateChildren(childUpdates);
        }
        if (view == btnAddOrder){

        }
        if (view == btnAddLogout){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }
}