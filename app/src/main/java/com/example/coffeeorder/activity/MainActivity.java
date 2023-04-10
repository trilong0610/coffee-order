package com.example.coffeeorder.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.coffeeorder.R;
import com.example.coffeeorder.fragment.AdminFragment;
import com.example.coffeeorder.fragment.AnalysisFragment;
import com.example.coffeeorder.fragment.HomeFragment;
import com.example.coffeeorder.fragment.OrderFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    public static DatabaseReference mDatabase;
    private View mainContainer;
    private ChipNavigationBar chipNavigationBar;
    private FragmentManager fragmentManager;

    private boolean endData = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }


    private void initView(){
        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        mainContainer = findViewById(R.id.main_container);
        // Chon item dau tien trong menu
        chipNavigationBar.setItemSelected(R.id.mnu_item_home,true);
        fragmentManager = this.getSupportFragmentManager();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Add fragment home vao View
        addFragment(new HomeFragment(), false);

    }

    private void initEvent(){
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.mnu_item_home:
                        addFragment(new HomeFragment(), true);
                        break;
                    case R.id.mnu_item_order:
                        addFragment(new OrderFragment(), true);
                        break;
                    case R.id.mnu_item_analysis:
                        addFragment(new AnalysisFragment(), true);
                        break;
                    case R.id.mnu_item_admin:
                        addFragment(new AdminFragment(), true);
                        break;

                }
            }
        });

        // thong bao neu co don moi
        mDatabase.child("Order").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
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

    public DatabaseReference getDBInstance(){
        return this.mDatabase;
    }

    public void addFragment(Fragment fragment, boolean addToBackstack){
        FragmentTransaction ft_add = fragmentManager.beginTransaction();
        ft_add.replace(R.id.main_container, fragment);

        // Them fragment vao backstack
        if (addToBackstack){
            ft_add.addToBackStack("");
        }
        ft_add.commit();
    }
}