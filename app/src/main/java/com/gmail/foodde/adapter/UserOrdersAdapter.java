package com.gmail.foodde.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.foodde.R;
import com.gmail.foodde.UserOrderInfoActivity;
import com.gmail.foodde.UserOrderMapsActivity;
import com.gmail.foodde.model.OrderItem;

import java.text.DecimalFormat;
import java.util.List;

public class UserOrdersAdapter extends RecyclerView.Adapter<UserOrdersAdapter.UserOrderViewHolder> {
    private Context context;
    private List<OrderItem> items;
    private RecyclerView recyclerView;

    public UserOrdersAdapter(Context context, List<OrderItem> items, RecyclerView recyclerView) {
        this.context = context;
        this.items = items;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public UserOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderItems = LayoutInflater.from(context).inflate(R.layout.user_orders_item, parent, false);
        return  new UserOrdersAdapter.UserOrderViewHolder(orderItems);
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrderViewHolder holder, int position) {
        holder.title.setText("№"+items.get(position).getId());
        holder.status.setText(items.get(position).getStatus());
        DecimalFormat decimalFormat = new DecimalFormat("#");
        String price = decimalFormat.format(items.get(position).getPrice());
        holder.price.setText(price+"₴");
        holder.date.setText(items.get(position).getDelivery_start_time());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserOrderInfoActivity.class);

                intent.putExtra("item_user_mail", items.get(position).getUser_mail());
                intent.putExtra("item_user_phone", items.get(position).getUser_phone());
                intent.putExtra("item_address", items.get(position).getAddress());
                intent.putExtra("item_address_add_info", items.get(position).getAddress_add_info());
                intent.putExtra("item_delivery_start_time", items.get(position).getDelivery_start_time());
                intent.putExtra("item_dishes", items.get(position).getDishes());
                intent.putExtra("item_dishes_comment", items.get(position).getDishes_comment());
                intent.putExtra("item_status", items.get(position).getStatus());
                intent.putExtra("item_token", items.get(position).getToken());
                intent.putExtra("item_address_latitude", items.get(position).getAddress_latitude());
                intent.putExtra("item_address_longitude", items.get(position).getAddress_longitude());
                intent.putExtra("item_price", items.get(position).getPrice());
                intent.putExtra("item_delivery_id", items.get(position).getDelivery_id());
                intent.putExtra("item_id", items.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static final class UserOrderViewHolder extends RecyclerView.ViewHolder{
        CardView item;
        TextView title, status, price, date;
        public UserOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item_order);
            title = itemView.findViewById(R.id.title_order);
            status = itemView.findViewById(R.id.status_order);
            price = itemView.findViewById(R.id.price_order);
            date = itemView.findViewById(R.id.date_order);
        }
    }
}
