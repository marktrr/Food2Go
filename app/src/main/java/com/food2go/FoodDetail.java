package com.food2go;
/**
 * Created by Hy Minh Tran (Mark) on 12/05/2019
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.food2go.DB.OrderDB;
import com.food2go.Model.Food;
import com.food2go.Model.Order;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity implements View.OnClickListener {

    TextView item_name, item_price, item_description;
    ImageView item_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton btnQuantity;
    FirebaseDatabase db;
    DatabaseReference item;
    String foodId = "";
    Food currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        // Init Firebase
        db= FirebaseDatabase.getInstance();
        item = db.getReference("FoodList");

        btnQuantity = findViewById(R.id.btnQuantity);
        btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(this);

        item_name = findViewById(R.id.food_name);
        item_image = findViewById(R.id.img_food);
        item_description = findViewById(R.id.food_description);
        item_price = findViewById(R.id.food_price);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        // get food id from intent
        if (getIntent() != null) {
            foodId = getIntent().getStringExtra("foodId");
        }
        if (!foodId.isEmpty() && foodId != null) {
            foodDetail(foodId);
        }
    }

    private void foodDetail(String foodId) {
        item.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentItem = dataSnapshot.getValue(Food.class);

                // get detail of item
                Picasso.get().load(currentItem.getImage()).into(item_image);
                //set title in appbar
                collapsingToolbarLayout.setTitle(currentItem.getName());

                item_price.setText(currentItem.getPrice());
                item_description.setText(currentItem.getDescription());
                item_name.setText(currentItem.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCart) {
            new OrderDB(getBaseContext()).addToCart(new Order(
                    foodId,
                    currentItem.getName(),
                    Integer.parseInt(btnQuantity.getNumber()),
                    Double.parseDouble(currentItem.getPrice()),
                    currentItem.getDiscount()
            ));
            Toast.makeText(FoodDetail.this, "Added to Cart", Toast.LENGTH_SHORT).show();
        }
    }
}
