package com.gmail.foodde;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gmail.foodde.databinding.ActivityUserOrderMapsBinding;

import java.util.Timer;
import java.util.TimerTask;

public class UserOrderMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityUserOrderMapsBinding binding;
    /*private String user_mail, user_phone, address, address_add_info,
            delivery_start_time, dishes, dishes_comment, status, token;
    private Double address_latitude, address_longitude;
    */private float price;
    private int delivery_id;

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;
    private CountDownTimer waitTimer;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserOrderMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        delivery_id = getIntent().getIntExtra("item_delivery_id",-1);
        //id = getIntent().getIntExtra("item_id",-1);*/

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_order);
        mapFragment.getMapAsync(this);


        container = findViewById(R.id.map_container);
        /*Button button = findViewById(R.id.user_order_maps_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer = null;
                    mMyTimerTask.cancel();
                    mMyTimerTask = null;
                }else {
                    mTimer = new Timer();
                    mMyTimerTask = new MyTimerTask();

                    mTimer.schedule(mMyTimerTask, 1000, 5000);
                }



            }
        });

        */

        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();

        mTimer.schedule(mMyTimerTask, 1000, 5000);
    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //System.out.println("da");
                    new GetOrderLocation(UserOrderMapsActivity.this, mMap, delivery_id).execute();
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if(delivery_id<=0){
            container.setVisibility(View.INVISIBLE);
        }
    }
}