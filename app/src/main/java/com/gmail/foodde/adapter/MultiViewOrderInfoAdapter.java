package com.gmail.foodde.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.foodde.BasketActivity;
import com.gmail.foodde.MainActivity;
import com.gmail.foodde.R;
import com.gmail.foodde.UserOrderMapsActivity;
import com.gmail.foodde.model.MultiViewItem;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class MultiViewOrderInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<MultiViewItem> items;
    private RecyclerView mainRecyclerView;
    private Activity activity;
    private String address, time, dishes;
    private float price;
    private int delID;


    public MultiViewOrderInfoAdapter(Context context,
                                     ArrayList<MultiViewItem> items, RecyclerView mainRecyclerView,
                                     Activity activity, String address, String time,
                                     String dishes, float price, int delID){
        this.items = items;
        this.context = context;
        this.mainRecyclerView = mainRecyclerView;
        this.activity = activity;
        this.address = address;
        this.time = time;
        this.dishes = dishes;
        this.price = price;
        this.delID = delID;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:{
                View categoryItems = LayoutInflater.from(context).inflate(R.layout.user_order_info_address_element, parent, false);
                return new MultiViewOrderInfoAdapter.ViewHolder0(categoryItems);
            }
            case 1:{
                View promItems = LayoutInflater.from(context).inflate(R.layout.user_order_info_dishes_element, parent, false);
                return new MultiViewOrderInfoAdapter.ViewHolder1(promItems);
            }
            case 2:{
                View dishItems = LayoutInflater.from(context).inflate(R.layout.user_order_info_dpayment_element, parent, false);
                return new MultiViewOrderInfoAdapter.ViewHolder2(dishItems);
            }
            case -1:{
                View start = LayoutInflater.from(context).inflate(R.layout.user_order_info_start_element, parent, false);
                return new MultiViewOrderInfoAdapter.ViewHolder01(start);
            }
            default: return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:{
                ViewHolder0 viewHolder = (ViewHolder0) holder;
                viewHolder.address.setText(address);
                viewHolder.time.setText(time);

                if(delID==-1){
                    viewHolder.view.setVisibility(View.GONE);
                }

                viewHolder.check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, UserOrderMapsActivity.class);
                        intent.putExtra("item_delivery_id", delID);
                        context.startActivity(intent);
                    }
                });

                break;
            }
            case 1:{
                ViewHolder1 viewHolder = (ViewHolder1) holder;
                viewHolder.dishes.setText("");
                String[] dish = dishes.split("\\|");
                for(int i = 0; i < dish.length; i++){
                    viewHolder.dishes.setText(viewHolder.dishes.getText()+dish[i]+"\n");
                }
                break;
            }
            case 2:{
                ViewHolder2 viewHolder = (ViewHolder2) holder;
                DecimalFormat decimalFormat = new DecimalFormat("#");
                String price1 = decimalFormat.format(price);

                viewHolder.price.setText(price1);
                break;
            }
            case -1:{

                break;
            }

        }
    }


    class ViewHolder0 extends RecyclerView.ViewHolder {
        TextView address, time;
        CardView view;
        Button check;

        public ViewHolder0(View itemView){
            super(itemView);
            address = itemView.findViewById(R.id.order_info_address_text_view);
            time = itemView.findViewById(R.id.order_info_address_time_text_view);
            view = itemView.findViewById(R.id.order_info_delivery_view);
            check = itemView.findViewById(R.id.order_info_check_delivery);
        }
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView dishes;
        public ViewHolder1(View itemView) {
            super(itemView);
            dishes = itemView.findViewById(R.id.order_info_dishes_);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView price;
        public ViewHolder2(View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.order_info_price);
        }
    }

    class ViewHolder01 extends RecyclerView.ViewHolder {
        public ViewHolder01(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
