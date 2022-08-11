package com.gmail.foodde;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.foodde.adapter.MultiViewAdapter;
import com.gmail.foodde.model.MultiViewItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private int catOfDel = 0;
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView cardViewDelivery = findViewById(R.id.mainCardViewDelivery);
        final Animation animAlpha = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
        cardViewDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                showBottomSheetDialog();
            }
        });

        ArrayList<MultiViewItem> items = new ArrayList<>();
        items.add(new MultiViewItem(0,0));
        items.add(new MultiViewItem(1,1));
        items.add(new MultiViewItem(2,2));
        items.add(new MultiViewItem(-1,-1));


        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeRefresh);
        final RecyclerView mainRecyclerView = (RecyclerView) findViewById(R.id.mainRecView);
        setMainRecycler(items, mainRecyclerView);

        CardView basket  = (CardView) findViewById(R.id.mainShoppingCart);
        LinearLayout home, categories, profile, settings;
        home = (LinearLayout) findViewById(R.id.mainHome);
        categories = (LinearLayout) findViewById(R.id.mainCategory);
        profile = (LinearLayout) findViewById(R.id.mainProfile);
        settings = (LinearLayout) findViewById(R.id.mainMenu);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);

                ArrayList<MultiViewItem> items = new ArrayList<>();
                items.add(new MultiViewItem(0,0));
                items.add(new MultiViewItem(1,1));
                items.add(new MultiViewItem(2,2));
                items.add(new MultiViewItem(-1,-1));

                final RecyclerView mainRecyclerView = (RecyclerView) findViewById(R.id.mainRecView);
                setMainRecycler(items, mainRecyclerView);


            }
        });
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
                startActivity(intent);
            }
        });
        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                Intent intent = new Intent(MainActivity.this, BasketActivity.class);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);

                DatabaseHelper databaseHelper;
                SQLiteDatabase db;
                databaseHelper = new DatabaseHelper(MainActivity.this);
                databaseHelper.create_db();
                db = databaseHelper.open();

                Cursor userCursor;
                userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_USER , null);
                if(userCursor.moveToFirst()){
                    db.close();
                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                    startActivity(intent);
                }else {
                    db.close();
                    Intent intent = new Intent(MainActivity.this, UserRegistrationActivity.class);
                    startActivity(intent);
                }
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                Toast.makeText(MainActivity.this, "Settings is Clicked", Toast.LENGTH_LONG).show();
            }
        });
        TextView search = (TextView) findViewById(R.id.mainTextViewSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                Toast.makeText(MainActivity.this, "Search is Clicked", Toast.LENGTH_LONG).show();
            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("user")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfully connected topic \"User\" ";
                        if (!task.isSuccessful()) {
                            msg = "Topic \"User\" was not successfully connected";
                        }
                        Log.e("Main", msg);
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("all")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfully connected topic \"All\" ";
                        if (!task.isSuccessful()) {
                            msg = "Topic \"All\" was not successfully connected";
                        }
                        Log.e("Main", msg);
                    }
                });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Main", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        token = task.getResult();
                        System.out.println("TOKEN: "+token);
                        DatabaseHelper databaseHelper;
                        SQLiteDatabase db;
                        databaseHelper = new DatabaseHelper(MainActivity.this);
                        databaseHelper.create_db();
                        db = databaseHelper.open();

                        Cursor userCursor;
                        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_USER , null);
                        if(userCursor.moveToFirst()){
                            userCursor.moveToFirst();
                            new UserGetData(MainActivity.this, userCursor.getInt(0),0, token, MainActivity.this).execute();
                        }
                        db.close();
                    }
                });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ArrayList<MultiViewItem> items = new ArrayList<>();
                items.add(new MultiViewItem(0,0));
                items.add(new MultiViewItem(1,1));
                items.add(new MultiViewItem(2,2));
                items.add(new MultiViewItem(-1,-1));

                setMainRecycler(items, mainRecyclerView);

                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.orange,
                R.color.orange,
                R.color.orange,
                android.R.color.holo_orange_light);

        swipeRefreshLayout.setProgressViewOffset(false , 50, 180);

    }

    private void setMainRecycler(ArrayList<MultiViewItem> items, RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false );
        recyclerView.setLayoutManager(layoutManager);
        MultiViewAdapter adapter = new MultiViewAdapter(MainActivity.this, items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseHelper databaseHelper;
        SQLiteDatabase db;
        databaseHelper = new DatabaseHelper(MainActivity.this);
        databaseHelper.create_db();
        db = databaseHelper.open();

        Cursor userCursor;
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_ADDRESSES +
                " where " + DatabaseHelper.COLUMN_ADDRESS_CHOSEN + " = " + "\"true\"", null);
        if(userCursor.moveToFirst()){
            userCursor.moveToFirst();
            TextView textView = findViewById(R.id.mainTextViewAddress);

            textView.setText(userCursor.getString(2)+"; ");
            textView.setTextSize(10);

            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);
            if(userCursor.moveToFirst()){
                userCursor.moveToFirst();
                if(!userCursor.getString(0).equals("сейчас")){
                    textView.setText(textView.getText()+""+userCursor.getString(1)+", "+userCursor.getString(0));
                }
                else {
                    textView.setText(textView.getText()+"Cейчас");
                }
            }
        }
        db.close();
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_delivery_layout);
        LinearLayout map = (LinearLayout) bottomSheetDialog.findViewById(R.id.mapLinearLayout);
        LinearLayout time = (LinearLayout) bottomSheetDialog.findViewById(R.id.timeLinearLayout);
        LinearLayout cat = (LinearLayout) bottomSheetDialog.findViewById(R.id.categoryLinearLayout);

        Spinner spinner = (Spinner) cat.findViewById(R.id.spinner);
        TextView tv = (TextView) map.findViewById(R.id.mapText);
        bottomSheetDialog.getBehavior().setPeekHeight(600);
        bottomSheetDialog.getBehavior().setMaxWidth(-2);
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        switch (catOfDel){
            case 0:
                spinner.setSelection(0);
                tv.setText("Укажите адрес доставки");
                catOfDel = 0;
                break;
            case 1:
                spinner.setSelection(1);
                tv.setText("Укажите место самовывоза");
                catOfDel = 1;
                break;
        }

        bottomSheetDialog.show();

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("category", catOfDel);
                startActivity(intent);
                bottomSheetDialog.dismiss();

            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.mainTextViewAddress);
                new TimeParameterChanges(MainActivity.this,textView).sh0w();
                bottomSheetDialog.dismiss();

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        spinner.setSelection(0);
                        tv.setText("Укажите адрес доставки");
                        catOfDel = 0;
                        break;
                    case 1:
                        spinner.setSelection(1);
                        tv.setText("Укажите место самовывоза");
                        catOfDel = 1;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }
}

