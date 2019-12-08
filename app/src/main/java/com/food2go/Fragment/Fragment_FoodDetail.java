package com.food2go.Fragment;

/**
 * Created by Hy Minh Tran (Mark) on 12/08/2019
 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.food2go.Common.Common;
import com.food2go.DB.OrderDB;
import com.food2go.Model.Food;
import com.food2go.Model.Order;
import com.food2go.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Fragment_FoodDetail extends Fragment implements View.OnClickListener {

    TextView item_name, item_price, item_description;
    ImageView item_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton btnQuantity;
    FirebaseDatabase db;
    DatabaseReference item;
    String foodId = "";
    Food currentItem;

    public Fragment_FoodDetail() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_detail, container, false);

        // Init Firebase
        db= FirebaseDatabase.getInstance();
        item = db.getReference("FoodList");

        btnQuantity = view.findViewById(R.id.btnQuantity);
        btnCart = view.findViewById(R.id.btnCart);
        btnCart.setOnClickListener(this);

        item_name = view.findViewById(R.id.food_name);
        item_image = view.findViewById(R.id.img_food);
        item_description = view.findViewById(R.id.food_description);
        item_price = view.findViewById(R.id.food_price);

        collapsingToolbarLayout = view.findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        // get food id from intent
        if (getActivity().getIntent() != null) {
            foodId = getActivity().getIntent().getStringExtra("foodId");
        }
        if (!foodId.isEmpty() && foodId != null) {
            foodDetail(foodId);
        }

        return(view);
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
            new OrderDB(getContext()).addToCart(new Order(
                    foodId,
                    currentItem.getName(),
                    Integer.parseInt(btnQuantity.getNumber()),
                    Double.parseDouble(currentItem.getPrice()),
                    Common.currentUser.getId()
            ));
            Toast.makeText(getContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
        }
    }
}
