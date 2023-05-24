package com.example.coffeeorder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coffeeorder.R;
import com.example.coffeeorder.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountFragment extends Fragment implements View.OnClickListener {
    private DatabaseReference mDatabase;
    private Button btnAddLogout;
    private TextView txtEmail;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initView(view);
        initEvent(view);

        return view;
    }

    private void initView(View view){
        btnAddLogout = view.findViewById(R.id.btn_admin_logout);
        txtEmail = view.findViewById(R.id.txt_account_email);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null){
            txtEmail.setText("Chưa đăng nhập");
            btnAddLogout.setVisibility(View.INVISIBLE);
        }
        else{
            txtEmail.setText(mAuth.getCurrentUser().getEmail());
            btnAddLogout.setVisibility(View.VISIBLE);
        }

    }

    private void initEvent(View view){
        btnAddLogout.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {


        if (view == btnAddLogout){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }
}