package com.gmail.foodde;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.gmail.foodde.adapter.MultiViewOrderAdapter;
import com.gmail.foodde.model.MultiViewItem;
import java.util.ArrayList;

public class BasketActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ImageView back = (ImageView) findViewById(R.id.order_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.order_recycler_view);

        ArrayList<MultiViewItem> items = new ArrayList<>();
        items.add(new MultiViewItem(0,0));
        items.add(new MultiViewItem(1,1));
        items.add(new MultiViewItem(2,2));
        items.add(new MultiViewItem(-1,-1));
        setMainRecycler(items, recyclerView);
    }

    private void setMainRecycler(ArrayList<MultiViewItem> items, RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BasketActivity.this, RecyclerView.VERTICAL, false );
        recyclerView.setLayoutManager(layoutManager);

        final LinearLayout background = (LinearLayout) findViewById(R.id.order_background);

        MultiViewOrderAdapter adapter = new MultiViewOrderAdapter(BasketActivity.this, items, recyclerView, background, BasketActivity.this);
        recyclerView.setAdapter(adapter);
    }

}