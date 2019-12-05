package com.food2go.ViewHolder;

import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import com.food2go.R;
import androidx.recyclerview.widget.RecyclerView;

import com.food2go.Interface.ItemClickListener;

/**
 * Created by Hy Minh Tran (Mark) on 12/05/2019
 */
public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView food_name;
    public ImageView food_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(View itemView) {
        super(itemView);
        food_image = itemView.findViewById(R.id.food_image);
        food_name = itemView.findViewById(R.id.food_name);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
