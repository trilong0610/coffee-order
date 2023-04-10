package com.example.coffeeorder.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorder.R;
import com.example.coffeeorder.activity.MainActivity;
import com.example.coffeeorder.data.TableAdapter;
import com.example.coffeeorder.model.TableModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private RecyclerView recycleViewTableMain;
    private ArrayList<TableModel> listTable;
    private TableAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listTable = new ArrayList<TableModel>();

        initView(view);
        loadData();
        initEvent(view);

        return view;
    }

    private void initView(View view){
        recycleViewTableMain = view.findViewById(R.id.rv_table_main);

        //        Set layout de hien thi thong tin trong recycle view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

//        Đảo ngược chiều thêm item từ dưới lên
        gridLayoutManager.setReverseLayout(true);

//        linearLayoutManager.setStackFromEnd(true);
//        rvUsers.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recycleViewTableMain.setLayoutManager(gridLayoutManager);

        adapter = new TableAdapter(listTable, getContext());

        recycleViewTableMain.setAdapter(adapter);

        recycleViewTableMain.scrollToPosition(listTable.size() - 1);
    }

    private void initEvent(View view){
    }

    private void loadData(){
        MainActivity.mDatabase.child("Table").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TableModel tableModel = snapshot.getValue(TableModel.class);

                listTable.add(tableModel);

                adapter.notifyDataSetChanged();

//                ---------------Cuộn đến item trên cùng khi thêm---------------------------
                recycleViewTableMain.scrollToPosition(listTable.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Lay thong tin ban thay doi
                TableModel tableChange = snapshot.getValue(TableModel.class);
                for (TableModel tableModel: listTable) {
                    if (tableChange.idTable == tableModel.idTable){
                        tableModel.status = tableChange.status;
                        tableModel.idOrder = tableChange.idOrder;
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
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
}