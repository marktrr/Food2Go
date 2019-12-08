package com.food2go;
/**
 * Created by Hy Minh Tran (Mark) on 12/05/2019
 */

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.food2go.Common.Common;
import com.food2go.DB.OrderDB;
import com.food2go.Model.Order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.food2go.Model.Reports;
import com.food2go.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView totalPrice;
    Button btnNext;
    List<Order> cart = new ArrayList<Order>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Init
        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        totalPrice = findViewById(R.id.total);
        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        loadListFood();
    }

    private void loadListFood() {
        cart = new OrderDB(this).getCart();
        adapter = new CartAdapter(cart, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //calculating total price
        double total = 0;
        for (Order item : cart)
            total += item.getPrice() * item.getQuantity();

        totalPrice.setText("$" + total);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                Intent address = new Intent(Cart.this, Address.class);
                address.putExtra("totalPrice", totalPrice.getText().toString()); // get price
                Bundle args = new Bundle();
                args.putSerializable("arrayList",(Serializable)cart);
                address.putExtra("listFoods",args);
                startActivity(address);
                break;
        }
    }
}
