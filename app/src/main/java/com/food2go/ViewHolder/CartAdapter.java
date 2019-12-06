package com.food2go.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.food2go.Common.Common;
import com.food2go.Interface.ItemClickListener;
import com.food2go.Model.Order;
import com.food2go.R;
import com.google.firebase.database.collection.LLRBNode;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hy Minh Tran (Mark) on 12/05/2019
 */

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView item_name, item_price;
    public ImageView quantity;


    public CartViewHolder(View itemView) {
        super(itemView);
        item_name = itemView.findViewById(R.id.cart_item_name);
        item_price = itemView.findViewById(R.id.cart_item_price);
        quantity = itemView.findViewById(R.id.cart_item_count);
    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        TextDrawable drawable = TextDrawable.builder().beginConfig().fontSize(40).bold().endConfig().buildRound("" + listData.get(position).getQuantity(), Color.RED);
        holder.quantity.setImageDrawable(drawable);

        double price = listData.get(position).getPrice() * listData.get(position).getQuantity();
        holder.item_price.setText(String.valueOf(price));
        holder.item_name.setText(listData.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}