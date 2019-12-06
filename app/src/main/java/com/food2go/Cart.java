package com.food2go;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.icu.text.NumberFormat;
import com.food2go.DB.OrderDB;
import com.food2go.Model.Order;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.food2go.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase db;
    DatabaseReference cart;
    TextView txtTotalPrice;
    Button btnPlace;
    OrderDB orderDB;
    List<Order> order = new ArrayList<>();
    CartAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Firebase
        db = FirebaseDatabase.getInstance();
        cart = db.getReference("Cart");

        //Init
        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);
        orderDB = new OrderDB(this);
        loadListFood();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadListFood() {
        order = new OrderDB(this).getCart();
        adapter = new CartAdapter(order, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //calculating total price
        double total = 0;
        for (Order item : order)
            total += item.getPrice() * item.getQuantity();
        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));
    }
}
