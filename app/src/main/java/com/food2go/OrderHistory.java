package com.food2go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.food2go.Common.Common;
import com.food2go.Interface.ItemClickListener;
import  com.food2go.ViewHolder.OrderViewHolder;
import com.food2go.Model.Reports;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderHistory extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Reports, OrderViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        //firebase
        database = FirebaseDatabase.getInstance();
        reports = database.getReference("Reports");

        recyclerView = findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //display order list
        loadOrders(Common.currentUser.getPhoneNumber());
    }

    private void loadOrders(String phone) {

        FirebaseRecyclerOptions<Reports> options = new FirebaseRecyclerOptions.Builder<Reports>()
                .setQuery(reports.orderByChild("phoneNumber").equalTo(phone), Reports.class).build();

        adapter = new FirebaseRecyclerAdapter<Reports, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(OrderViewHolder orderViewHolder, int i, Reports report) {
                orderViewHolder.order_id.setText(adapter.getRef(i).getKey());
                orderViewHolder.order_address.setText(report.getAddress());
                orderViewHolder.order_phone.setText(report.getPhoneNumber());
                orderViewHolder.order_status.setText(getStatus(report.getStatus()));
                orderViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClik) {
                        //
                    }
                });
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new OrderViewHolder(view);
            }
        };
        //set adapter
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private String getStatus(String input) {
        if(input.equals("0")) {
            return "Processing..";
        }
        else if(input.equals("1")){
            return "Out of delivery..";
        }
        else {
            return "Shipped";
        }
    }
}