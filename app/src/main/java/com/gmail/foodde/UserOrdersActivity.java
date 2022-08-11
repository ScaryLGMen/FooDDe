package com.gmail.foodde;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.gmail.foodde.adapter.UserOrdersAdapter;
import com.gmail.foodde.model.BasketItem;

import java.util.List;

public class UserOrdersActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);
        recyclerView = (RecyclerView) findViewById(R.id.activity_user_orders_recycler_view);
        new GetUserOrders(UserOrdersActivity.this, recyclerView, getIntent().getStringExtra("mail")).execute();
    }


}