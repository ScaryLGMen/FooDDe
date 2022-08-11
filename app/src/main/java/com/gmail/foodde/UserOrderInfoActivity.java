package com.gmail.foodde;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.foodde.adapter.MultiViewAdapter;
import com.gmail.foodde.adapter.MultiViewOrderInfoAdapter;
import com.gmail.foodde.model.MultiViewItem;

import java.util.ArrayList;

public class UserOrderInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private String user_mail, user_phone, address, address_add_info,
            delivery_start_time, dishes, dishes_comment, status, token;
    private Double address_latitude, address_longitude;
    private float price;
    private int delivery_id, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_info);

        user_mail = getIntent().getStringExtra("item_user_mail");
        user_phone = getIntent().getStringExtra("item_user_phone");
        address = getIntent().getStringExtra("item_address");
        address_add_info = getIntent().getStringExtra("item_address_add_info");
        delivery_start_time = getIntent().getStringExtra("item_delivery_start_time");
        dishes = getIntent().getStringExtra("item_dishes");
        dishes_comment = getIntent().getStringExtra("item_dishes_comment");
        status = getIntent().getStringExtra("item_status");
        token = getIntent().getStringExtra("item_token");
        address_latitude = getIntent().getDoubleExtra("item_address_latitude",-1);
        address_longitude = getIntent().getDoubleExtra("item_address_longitude",-1);
        price = getIntent().getFloatExtra("item_price",-1);
        delivery_id = getIntent().getIntExtra("item_delivery_id",-1);
        id = getIntent().getIntExtra("item_id",-1);

        recyclerView = findViewById(R.id.user_order_info);

        ArrayList<MultiViewItem> items = new ArrayList<>();
        items.add(new MultiViewItem(0,0));
        items.add(new MultiViewItem(1,1));
        items.add(new MultiViewItem(2,2));
        items.add(new MultiViewItem(-1,-1));

        setAdapter(items, recyclerView);

        ImageView back = (ImageView) findViewById(R.id.user_order_info_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView idTV = (TextView) findViewById(R.id.user_order_info_id);
        idTV.setText("Заказ №"+id);
        ImageView copy = (ImageView) findViewById(R.id.user_order_info_copy);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void setAdapter(ArrayList<MultiViewItem> items, RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(UserOrderInfoActivity.this, RecyclerView.VERTICAL,
                        false );
        recyclerView.setLayoutManager(layoutManager);
        MultiViewOrderInfoAdapter adapter =
                new MultiViewOrderInfoAdapter(UserOrderInfoActivity.this, items,
                        recyclerView, UserOrderInfoActivity.this, address, delivery_start_time,
                        dishes, price, delivery_id);
        recyclerView.setAdapter(adapter);
    }
}