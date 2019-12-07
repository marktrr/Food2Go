package com.food2go.ViewHolder;

import android.view.View;
import android.widget.TextView;
import com.food2go.R;
import androidx.annotation.NonNull;
import com.food2go.Interface.ItemClickListener;
import androidx.recyclerview.widget.RecyclerView;
/**
 * Created by Hy Minh Tran (Mark) on 12/07/2019
 */
public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView order_id, order_address, order_phone, order_status;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        order_id = itemView.findViewById(R.id.order_id);
        order_address = itemView.findViewById(R.id.order_address);
        order_phone = itemView.findViewById(R.id.order_phone);
        order_status = itemView.findViewById(R.id.order_status);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(), false);
    }
}
