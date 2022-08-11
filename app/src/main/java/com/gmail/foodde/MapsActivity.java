package com.gmail.foodde;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import com.gmail.foodde.adapter.PlaceAdapter;
import com.gmail.foodde.adapter.PlacePickupAdapter;
import com.gmail.foodde.model.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gmail.foodde.databinding.ActivityMapsBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private final String TAG = "MapsActivity";
    private CardView sample1, sample2, sample3;
    private TimePicker mTimePicker;
    private Spinner spinner;
    private TextView time;

    private String dayOfWeek = "";
    private String timeOfDel = "";

    private boolean isOpen = false;

    private LocationManager locationManager;
    public static boolean geolocationEnabled = false;

    private RecyclerView placesRV, placePickupRV;
    private List<Place> placeList = new ArrayList<>();

    private BottomSheetBehavior bottomSheetBehavior = new BottomSheetBehavior();
    private BottomSheetBehavior bottomSheetBehaviorConfirmation = new BottomSheetBehavior();
    private BottomSheetBehavior bottomSheetBehaviorSampleLocation = new BottomSheetBehavior();
    private BottomSheetBehavior bottomSheetBehaviorPickup = new BottomSheetBehavior();
    private TopSheetBehavior topSheetBehavior = new TopSheetBehavior();
    private TopSheetBehavior topSheetPanel = new TopSheetBehavior();
    private TopSheetBehavior topSheetBack = new TopSheetBehavior();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final int category = getIntent().getIntExtra("category", -1);
        showAllContent(category);
        Spinner spinner = findViewById(R.id.spinnerpanel);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                showAllContent(i);
                if(i==1){
                    setPlacePickupRecyclar();
                }
                if(i==0){
                    mMap.clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private void showAllContent(int category){

        time = findViewById(R.id.mapsbottcontime);

        placesRV = findViewById(R.id.mapsAddressesRV);
        placePickupRV = findViewById(R.id.pickup_recycler_view);
        placeList.clear();

        ConstraintLayout llBottomSheet = (ConstraintLayout) findViewById(R.id.bottom_sheet_address);
        ConstraintLayout clBottomSheetSampleLocation = (ConstraintLayout) findViewById(R.id.bottom_sheet_sample_location);
        ConstraintLayout llTopSheet = (ConstraintLayout) findViewById(R.id.top_sheet_address);
        LinearLayout llTopPanel = (LinearLayout) findViewById(R.id.top_sheet_panel);
        LinearLayout llTopBack = (LinearLayout) findViewById(R.id.top_sheet_back);
        ConstraintLayout llBottomConfirmation = (ConstraintLayout) findViewById(R.id.bottom_sheet_confirmation);
        LinearLayout llBottomPickup = (LinearLayout) findViewById(R.id.bottom_sheet_pickup);

        bottomSheetBehaviorPickup = BottomSheetBehavior.from(llBottomPickup);
        bottomSheetBehaviorConfirmation = BottomSheetBehavior.from(llBottomConfirmation);
        bottomSheetBehaviorSampleLocation = BottomSheetBehavior.from(clBottomSheetSampleLocation);
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        topSheetBehavior = TopSheetBehavior.from(llTopSheet);
        topSheetPanel = TopSheetBehavior.from(llTopPanel);
        topSheetBack = TopSheetBehavior.from(llTopBack);

        if(category==0){
            topSheetBack.setState(TopSheetBehavior.STATE_HIDDEN);
            bottomSheetBehaviorConfirmation.setState(BottomSheetBehavior.STATE_HIDDEN);
            bottomSheetBehaviorSampleLocation.setState(BottomSheetBehavior.STATE_HIDDEN);
            topSheetBehavior.setState(TopSheetBehavior.STATE_HIDDEN);
            topSheetPanel.setState(TopSheetBehavior.STATE_EXPANDED);
            bottomSheetBehaviorPickup.setState(BottomSheetBehavior.STATE_HIDDEN);

            topSheetPanel.setHideable(false);
            bottomSheetBehavior.setHideable(false);

            binding.mapsRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    binding.mapsRoot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int height =  binding.mapsRoot.getHeight();
                    CoordinatorLayout.LayoutParams lpt = (CoordinatorLayout.LayoutParams) llTopSheet.getLayoutParams();
                    CoordinatorLayout.LayoutParams lpb = (CoordinatorLayout.LayoutParams) llBottomSheet.getLayoutParams();
                    lpt.height = (int) (Math.ceil(height*0.3));
                    lpb.height = (int) (Math.ceil(height*0.8));
                    if(lpt.height+lpb.height!=height){
                        lpt.height++;
                    }

                    llTopSheet.setLayoutParams(lpt);
                    llBottomSheet.setLayoutParams(lpb);

                    bottomSheetBehavior.setPeekHeight((int) (Math.ceil(height*0.1)));

                    LinearLayout ll = findViewById(R.id.bottom_sheet_panel);

                    FrameLayout.LayoutParams lpll = (FrameLayout.LayoutParams) ll.getLayoutParams();
                    lpll.height = (int) (Math.ceil(height*0.1));
                    ll.setLayoutParams(lpll);


                    LinearLayout addresses = findViewById(R.id.mapsaddresses);
                    FrameLayout.LayoutParams lprv = (FrameLayout.LayoutParams) addresses.getLayoutParams();
                    lprv.height = (int) (Math.ceil(height*0.55));
                    addresses.setLayoutParams(lprv);


                    LinearLayout time = findViewById(R.id.mapstime);
                    FrameLayout.LayoutParams lpcv = (FrameLayout.LayoutParams) time.getLayoutParams();
                    lpcv.height = (int) (Math.ceil(height*0.1));
                    time.setLayoutParams(lpcv);

                    LinearLayout timehelp = findViewById(R.id.mapstimehelp);
                    LinearLayout.LayoutParams lpcvh = (LinearLayout.LayoutParams) timehelp.getLayoutParams();
                    lpcvh.height = (int) (Math.ceil(height*0.1));
                    timehelp.setLayoutParams(lpcvh);

                    LinearLayout search = findViewById(R.id.mapsSearch);
                    FrameLayout.LayoutParams lpsrch = (FrameLayout.LayoutParams) search.getLayoutParams();
                    lpsrch.height = (int) (Math.ceil(height*0.11));
                    search.setLayoutParams(lpsrch);

                    LinearLayout sample = findViewById(R.id.mapsSample);
                    LinearLayout.LayoutParams lpspl = (LinearLayout.LayoutParams) sample.getLayoutParams();
                    lpspl.height = (int) (Math.ceil(height*0.065));
                    sample.setLayoutParams(lpspl);

                    CardView specifyLocation = findViewById(R.id.mapsspecifiaddress);
                    specifyLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            topSheetPanel.setHideable(true);
                            topSheetPanel.setState(TopSheetBehavior.STATE_HIDDEN);
                            topSheetBehavior.setState(TopSheetBehavior.STATE_EXPANDED);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    });

                    bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {

                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                            if(slideOffset>0.5){
                                if(topSheetPanel.getState()!=TopSheetBehavior.STATE_HIDDEN){
                                    topSheetPanel.setHideable(true);
                                    topSheetPanel.setState(TopSheetBehavior.STATE_HIDDEN);
                                }
                                topSheetBehavior.setState(TopSheetBehavior.STATE_EXPANDED);
                                CoordinatorLayout.LayoutParams lpt = (CoordinatorLayout.LayoutParams) llTopSheet.getLayoutParams();
                                lpt.height = (int) (Math.ceil(height*0.3*(slideOffset)));
                                llTopSheet.setLayoutParams(lpt);
                            }else {
                                topSheetBehavior.setState(TopSheetBehavior.STATE_HIDDEN);
                                if(topSheetPanel.getState()==TopSheetBehavior.STATE_HIDDEN&&topSheetBack.getState()==TopSheetBehavior.STATE_HIDDEN){
                                    topSheetPanel.setState(TopSheetBehavior.STATE_EXPANDED);
                                }

                            }

                        }
                    });



                }
            });

            ImageView cansel = findViewById(R.id.mapstopcansel);
            cansel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    topSheetPanel.setHideable(false);
                    topSheetPanel.setState(TopSheetBehavior.STATE_EXPANDED);
                    topSheetBehavior.setState(TopSheetBehavior.STATE_HIDDEN);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            });

            ImageView close = findViewById(R.id.mapBackFromConfirm);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    topSheetPanel.setHideable(true);
                    topSheetBehavior.setHideable(true);
                    topSheetBack.setHideable(true);
                    bottomSheetBehavior.setHideable(true);
                    bottomSheetBehaviorSampleLocation.setHideable(true);

                    bottomSheetBehaviorConfirmation.setState(BottomSheetBehavior.STATE_HIDDEN);
                    topSheetBack.setState(TopSheetBehavior.STATE_HIDDEN);
                    topSheetBehavior.setState(TopSheetBehavior.STATE_HIDDEN);
                    topSheetPanel.setState(TopSheetBehavior.STATE_EXPANDED);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    bottomSheetBehaviorSampleLocation.setState(BottomSheetBehavior.STATE_HIDDEN);

                    mMap.clear();
                }
            });

            CardView findLocation = findViewById(R.id.mapsbottlocation);
            final Animation animAlpha = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.alpha);
            findLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkFineAndCoarseLocation()){
                        view.startAnimation(animAlpha);
                        getFastLocation(MapsActivity.this);
                    }
                }
            });

            //Нужно реализвать изменение времени
            mTimePicker = findViewById(R.id.timePicker);
            mTimePicker.setIs24HourView(true);

            time = findViewById(R.id.mapsbottcontime);
            time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TimeParameterChanges(MapsActivity.this, time, mTimePicker, spinner).show();
                }
            });

            spinner = findViewById(R.id.mapsdateofdelivety);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_WEEK);
                    if(i == 0){
                        mTimePicker.setVisibility(View.GONE);
                        mTimePicker.setEnabled(false);
                        dayOfWeek = "Cейчас";
                        timeOfDel = "";

                        DatabaseHelper databaseHelper;
                        SQLiteDatabase db;

                        databaseHelper = new DatabaseHelper(MapsActivity.this);
                        databaseHelper.create_db();
                        db = databaseHelper.open();

                        Cursor userCursor;
                        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);

                        if(userCursor.moveToFirst()){
                            ContentValues values = new ContentValues();

                            values.put(DatabaseHelper.COLUMN_TIME, "сейчас");
                            values.put(DatabaseHelper.COLUMN_DATE, "Cейчас");
                            userCursor.moveToFirst();
                            String where = DatabaseHelper.COLUMN_TIME + "=" + '"'+userCursor.getString(0)+'"';
                            //System.out.println(""+userCursor.getString(0));
                            db.update(DatabaseHelper.TABLE_OF_TIME, values, where, null);
                            time.setText(dayOfWeek);
                        }else {

                            ContentValues values = new ContentValues();
                            values.put(DatabaseHelper.COLUMN_TIME, "сейчас");
                            values.put(DatabaseHelper.COLUMN_DATE, "Cейчас");
                            db.insert(DatabaseHelper.TABLE_OF_TIME, null, values);
                            time.setText(dayOfWeek);
                        }
                        db.close();
                    }
                    if(i == 1){
                        mTimePicker.setVisibility(View.VISIBLE);
                        mTimePicker.setEnabled(true);
                        switch (day){
                            case Calendar.MONDAY:
                                dayOfWeek = "Понедельник";
                                break;
                            case Calendar.TUESDAY:
                                dayOfWeek = "Вторник";
                                break;
                            case Calendar.WEDNESDAY:
                                dayOfWeek = "Среда";
                                break;
                            case Calendar.THURSDAY:
                                dayOfWeek = "Четверг";
                                break;
                            case Calendar.FRIDAY:
                                dayOfWeek = "Пятница";
                                break;
                            case Calendar.SATURDAY:
                                dayOfWeek = "Суббота";
                                break;
                            case Calendar.SUNDAY:
                                dayOfWeek = "Воскресенье";
                                break;
                        }
                        String hour = "";
                        String minutes = "";
                        if(mTimePicker.getHour()<10){
                            hour = "0"+mTimePicker.getHour();
                        }else {
                            hour = ""+mTimePicker.getHour();
                        }
                        if(mTimePicker.getMinute()<10){
                            minutes = "0"+mTimePicker.getMinute();
                        }else {
                            minutes = ""+mTimePicker.getMinute();
                        }
                        timeOfDel = hour+":"+minutes;


                        DatabaseHelper databaseHelper;
                        SQLiteDatabase db;

                        databaseHelper = new DatabaseHelper(MapsActivity.this);
                        databaseHelper.create_db();
                        db = databaseHelper.open();

                        Cursor userCursor;
                        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);

                        if(userCursor.moveToFirst()){
                            ContentValues values = new ContentValues();

                            values.put(DatabaseHelper.COLUMN_TIME, timeOfDel);
                            values.put(DatabaseHelper.COLUMN_DATE, dayOfWeek);
                            userCursor.moveToFirst();
                            String where = DatabaseHelper.COLUMN_TIME + "=" + '"'+userCursor.getString(0)+'"';
                            //System.out.println(""+userCursor.getString(0));
                            db.update(DatabaseHelper.TABLE_OF_TIME, values, where, null);
                            time.setText(dayOfWeek+ ", "+timeOfDel);
                        }else {

                            ContentValues values = new ContentValues();
                            values.put(DatabaseHelper.COLUMN_TIME, timeOfDel);
                            values.put(DatabaseHelper.COLUMN_DATE, dayOfWeek);
                            db.insert(DatabaseHelper.TABLE_OF_TIME, null, values);
                            time.setText(dayOfWeek+ ", "+timeOfDel);
                        }

                    }
                    if(i == 2){
                        mTimePicker.setVisibility(View.VISIBLE);
                        mTimePicker.setEnabled(true);
                        switch (day){
                            case Calendar.MONDAY:
                                dayOfWeek = "Вторник";
                                break;
                            case Calendar.TUESDAY:
                                dayOfWeek = "Среда";
                                break;
                            case Calendar.WEDNESDAY:
                                dayOfWeek = "Четверг";
                                break;
                            case Calendar.THURSDAY:
                                dayOfWeek = "Пятница";
                                break;
                            case Calendar.FRIDAY:
                                dayOfWeek = "Суббота";
                                break;
                            case Calendar.SATURDAY:
                                dayOfWeek = "Воскресенье";
                                break;
                            case Calendar.SUNDAY:
                                dayOfWeek = "Понедельник";
                                break;
                        }
                        String hour = "";
                        String minutes = "";
                        if(mTimePicker.getHour()<10){
                            hour = "0"+mTimePicker.getHour();
                        }else {
                            hour = ""+mTimePicker.getHour();
                        }
                        if(mTimePicker.getMinute()<10){
                            minutes = "0"+mTimePicker.getMinute();
                        }else {
                            minutes = ""+mTimePicker.getMinute();
                        }

                        timeOfDel = hour+":"+minutes;

                        DatabaseHelper databaseHelper;
                        SQLiteDatabase db;

                        databaseHelper = new DatabaseHelper(MapsActivity.this);
                        databaseHelper.create_db();
                        db = databaseHelper.open();

                        Cursor userCursor;
                        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);

                        if(userCursor.moveToFirst()){
                            ContentValues values = new ContentValues();

                            values.put(DatabaseHelper.COLUMN_TIME, timeOfDel);
                            values.put(DatabaseHelper.COLUMN_DATE, dayOfWeek);
                            userCursor.moveToFirst();
                            String where = DatabaseHelper.COLUMN_TIME + "=" + '"'+userCursor.getString(0)+'"';
                            //System.out.println(""+userCursor.getString(0));
                            db.update(DatabaseHelper.TABLE_OF_TIME, values, where, null);
                            time.setText(dayOfWeek+ ", "+timeOfDel);
                        }else {

                            ContentValues values = new ContentValues();
                            values.put(DatabaseHelper.COLUMN_TIME, timeOfDel);
                            values.put(DatabaseHelper.COLUMN_DATE, dayOfWeek);
                            db.insert(DatabaseHelper.TABLE_OF_TIME, null, values);
                            time.setText(dayOfWeek+ ", "+timeOfDel);
                        }
                        db.close();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            DatabaseHelper databaseHelper;
            SQLiteDatabase db;

            databaseHelper = new DatabaseHelper(MapsActivity.this);
            databaseHelper.create_db();
            db = databaseHelper.open();

            Cursor userCursor;
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);
            if(userCursor.moveToFirst()){
                userCursor.moveToFirst();
                String oldTime = userCursor.getString(0);
                String oldDate = userCursor.getString(1);
                System.out.println(oldTime);
                if(!oldTime.equals("сейчас")){
                    int oldDayOfWeek = 0;
                    Calendar calendar = Calendar.getInstance();
                    int presentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    switch (oldDate){
                        case "Понедельник":
                            oldDayOfWeek = Calendar.MONDAY;
                            break;
                        case "Вторник":
                            oldDayOfWeek = Calendar.TUESDAY;
                            break;
                        case "Среда":
                            oldDayOfWeek = Calendar.WEDNESDAY;
                            break;
                        case "Четверг":
                            oldDayOfWeek = Calendar.THURSDAY;
                            break;
                        case "Пятница":
                            oldDayOfWeek = Calendar.FRIDAY;
                            break;
                        case "Суббота":
                            oldDayOfWeek = Calendar.SATURDAY;
                            break;
                        case "Воскресенье":
                            oldDayOfWeek = Calendar.SUNDAY;
                            break;
                    }
                    if(presentDayOfWeek-oldDayOfWeek==-1){
                        spinner.setSelection(2);
                    }
                    if(presentDayOfWeek-oldDayOfWeek==0){
                        spinner.setSelection(1);
                    }

                    String subTimeHour = oldTime.substring(0,2);
                    String subTimeMinutes = oldTime.substring(3,5);
                    mTimePicker.setHour(Integer.parseInt(subTimeHour));
                    mTimePicker.setMinute(Integer.parseInt(subTimeMinutes));
                }
            }
            db.close();

            mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                    String hour = "";
                    String minutes = "";
                    if(timePicker.getHour()<10){
                        hour = "0"+timePicker.getHour();
                    }else {
                        hour = ""+timePicker.getHour();
                    }
                    if(timePicker.getMinute()<10){
                        minutes = "0"+timePicker.getMinute();
                    }else {
                        minutes = ""+timePicker.getMinute();
                    }
                    timeOfDel = hour+":"+minutes;

                    DatabaseHelper databaseHelper;
                    SQLiteDatabase db;

                    databaseHelper = new DatabaseHelper(MapsActivity.this);
                    databaseHelper.create_db();
                    db = databaseHelper.open();

                    Cursor userCursor;
                    userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);

                    if(userCursor.moveToFirst()){
                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.COLUMN_TIME, timeOfDel);
                        values.put(DatabaseHelper.COLUMN_DATE, dayOfWeek);
                        userCursor.moveToFirst();
                        String where = DatabaseHelper.COLUMN_TIME + "=" + '"'+userCursor.getString(0)+'"';
                        System.out.println(""+userCursor.getString(0));
                        db.update(DatabaseHelper.TABLE_OF_TIME, values, where, null);
                        time.setText(dayOfWeek+ ", "+timeOfDel);
                    }else {
                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.COLUMN_TIME, timeOfDel);
                        values.put(DatabaseHelper.COLUMN_DATE, dayOfWeek);
                        db.insert(DatabaseHelper.TABLE_OF_TIME, null, values);
                        time.setText(dayOfWeek+ ", "+timeOfDel);
                    }
                    db.close();

                }
            });


            EditText searchAddress = findViewById(R.id.mapstopsearchaddress);

            searchAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    placeList.clear();
                    String value = searchAddress.getText().toString();
                    Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.ROOT);
                    try {
                        List<Address> addresses = geoCoder.getFromLocationName(
                                value, 3, 45.26831, 28.12139, 48.27241, 31.27530);
                        if (addresses.size() > 0) {
                            for(int y = 0; y< addresses.size();y++){
                                placeList.add(new Place(y,"" +addresses.get(y).getAddressLine(0) ,
                                        false,false,
                                        addresses.get(y).getLatitude(), addresses.get(y).getLongitude()));
                            }
                            //
                            DatabaseHelper databaseHelper;
                            SQLiteDatabase db;

                            databaseHelper = new DatabaseHelper(MapsActivity.this);
                            databaseHelper.create_db();
                            db = databaseHelper.open();

                            Cursor userCursor;
                            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);
                            if(userCursor.moveToFirst()){
                                time.setText(userCursor.getString(1)+", "+userCursor.getString(0));
                            }
                            db.close();

                            databaseHelper = new DatabaseHelper(MapsActivity.this);
                            databaseHelper.create_db();
                            db = databaseHelper.open();

                            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_ADDRESSES +
                                    " where " + DatabaseHelper.COLUMN_ADDRESS_CHOSEN + " = " + "\"true\"", null);
                            if(userCursor.moveToFirst()){
                                userCursor.moveToFirst();
                                placeList.add(new Place(0, userCursor.getString(2), true, true, userCursor.getDouble(0), userCursor.getDouble(1) ));
                            }

                            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_ADDRESSES+
                                    " where " + DatabaseHelper.COLUMN_ADDRESS_CHOSEN + " = " + "\"false\"", null);
                            if(userCursor.moveToFirst()){
                                userCursor.moveToFirst();
                                //Добавление false
                                do{
                                    //System.out.println("\n"+userCursor.getString(2));
                                    placeList.add(new Place(0, userCursor.getString(2), false, true, userCursor.getDouble(0), userCursor.getDouble(1) ));
                                } while (userCursor.moveToNext());
                                //Новый
                            }
                            //

                            placeList.add(new Place(-1,"ул. Академика Вильямса, 71,\nРесторан FooDDe", false, false,
                                    46.39092815919191, 30.72517058102887));
                            placeList.add(new Place(-1,"ул. Посметного, 20,\nРесторан FooDDe", false, false,
                                    46.42729658551131, 30.75354588893678));
                            placeList.add(new Place(-1,"ул. Иоганна Гена, 11,\nРесторан FooDDe", false, false,
                                    46.46919224735501, 30.679065805455334));
                            placeList.add(new Place(-1,"ул. Ростовская, 17,\nРесторан FooDDe", false, false,
                                    46.56041614010818, 30.776388563015868));
                            placeList.add(new Place(-1,"Соборная прощадь, 4,\nРесторан FooDDe", false, false,
                                    46.48406076972199, 30.73056700396722));

                            setPlaceRecyclar(placeList);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });

            ImageView back = findViewById(R.id.mapBack);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            sample1 = findViewById(R.id.sample1);
            sample2 = findViewById(R.id.sample2);
            sample3 = findViewById(R.id.sample3);

            TextView address = findViewById(R.id.mapsbottconaddress);
            TextView time = findViewById(R.id.mapsbottcontime);
            Button confirm = findViewById(R.id.mapsbottconbutton);

            TextView sample1Text = findViewById(R.id.sample1Text);
            ImageView sample1Picture = findViewById(R.id.sample1Picture);
            sample1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView textView = findViewById(R.id.bott_sample_address_textView);
                    Button button = findViewById(R.id.bott_sample_address_Button);
                    new MapsSample(MapsActivity.this,1, sample1Text, sample1Picture,
                            topSheetBehavior, topSheetPanel, bottomSheetBehavior, topSheetBack,
                            bottomSheetBehaviorSampleLocation, mMap, button, textView,address,time,confirm,
                            bottomSheetBehaviorConfirmation, mTimePicker, MapsActivity.this, placesRV).show();
                }
            });
            TextView sample2Text = findViewById(R.id.sample2Text);
            ImageView sample2Picture = findViewById(R.id.sample2Picture);
            sample2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button button = findViewById(R.id.bott_sample_address_Button);
                    TextView textView = findViewById(R.id.bott_sample_address_textView);
                    new MapsSample(MapsActivity.this,2, sample2Text, sample2Picture,
                            topSheetBehavior, topSheetPanel, bottomSheetBehavior, topSheetBack,
                            bottomSheetBehaviorSampleLocation, mMap, button, textView,address,time,confirm,
                            bottomSheetBehaviorConfirmation, mTimePicker, MapsActivity.this, placesRV).show();
                }
            });
            TextView sample3Text = findViewById(R.id.sample3Text);
            ImageView sample3Picture = findViewById(R.id.sample3Picture);
            sample3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button button = findViewById(R.id.bott_sample_address_Button);
                    TextView textView = findViewById(R.id.bott_sample_address_textView);
                    new MapsSample(MapsActivity.this,3, sample3Text, sample3Picture,
                            topSheetBehavior, topSheetPanel, bottomSheetBehavior, topSheetBack,
                            bottomSheetBehaviorSampleLocation, mMap, button, textView,address,time,confirm,
                            bottomSheetBehaviorConfirmation, mTimePicker, MapsActivity.this, placesRV).show();
                }
            });

            DatabaseHelper databaseHelper2;
            SQLiteDatabase db2;
            databaseHelper2 = new DatabaseHelper(MapsActivity.this);
            databaseHelper2.create_db();
            db2 = databaseHelper2.open();

            Cursor userCursor2;
            for(int i = 1; i < 4; i++){
                userCursor2 = db2.rawQuery("select * from " + DatabaseHelper.TABLE_OF_SAMPLES +" where " +DatabaseHelper.COLUMN_SAMPLE_ID +" = "+ i , null);
                if(userCursor2.moveToFirst()){
                    userCursor2.moveToFirst();
                    switch (userCursor2.getInt(0)){
                        case 1:
                            sample1Text.setText(userCursor2.getString(2));
                            sample1Picture.setImageResource(Integer.parseInt(userCursor2.getString(1)));
                            break;
                        case 2:
                            sample2Text.setText(userCursor2.getString(2));
                            sample2Picture.setImageResource(Integer.parseInt(userCursor2.getString(1)));
                            break;
                        case 3:
                            sample3Text.setText(userCursor2.getString(2));
                            sample3Picture.setImageResource(Integer.parseInt(userCursor2.getString(1)));
                            break;
                    }

                }
            }
            db2.close();

            placeList.clear();

            databaseHelper = new DatabaseHelper(MapsActivity.this);
            databaseHelper.create_db();
            db = databaseHelper.open();

            //Cursor userCursor;
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);
            if(userCursor.moveToFirst()){
                time.setText(userCursor.getString(1)+", "+userCursor.getString(0));
            }
            db.close();

            databaseHelper = new DatabaseHelper(MapsActivity.this);
            databaseHelper.create_db();
            db = databaseHelper.open();

            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_ADDRESSES +
                    " where " + DatabaseHelper.COLUMN_ADDRESS_CHOSEN + " = " + "\"true\"", null);
            if(userCursor.moveToFirst()){
                userCursor.moveToFirst();
                placeList.add(new Place(0, userCursor.getString(2), true, true, userCursor.getDouble(0), userCursor.getDouble(1) ));
            }

            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_ADDRESSES+
                    " where " + DatabaseHelper.COLUMN_ADDRESS_CHOSEN + " = " + "\"false\"", null);
            if(userCursor.moveToFirst()){
                userCursor.moveToFirst();
                //Добавление false
                do{
                    //System.out.println("\n"+userCursor.getString(2));
                    placeList.add(new Place(0, userCursor.getString(2), false, true, userCursor.getDouble(0), userCursor.getDouble(1) ));
                } while (userCursor.moveToNext());
                //Новый
            }
            placeList.add(new Place(-1,"ул. Академика Вильямса, 71,\nРесторан FooDDe", false, false,
                    46.39092815919191, 30.72517058102887));
            placeList.add(new Place(-1,"ул. Посметного, 20,\nРесторан FooDDe", false, false,
                    46.42729658551131, 30.75354588893678));
            placeList.add(new Place(-1,"ул. Иоганна Гена, 11,\nРесторан FooDDe", false, false,
                    46.46919224735501, 30.679065805455334));
            placeList.add(new Place(-1,"ул. Ростовская, 17,\nРесторан FooDDe", false, false,
                    46.56041614010818, 30.776388563015868));
            placeList.add(new Place(-1,"Соборная прощадь, 4,\nРесторан FooDDe", false, false,
                    46.48406076972199, 30.73056700396722));

            setPlaceRecyclar(placeList);
            db.close();


        }if(category==1){

            bottomSheetBehavior.setHideable(true);
            topSheetBack.setState(TopSheetBehavior.STATE_HIDDEN);
            bottomSheetBehaviorConfirmation.setState(BottomSheetBehavior.STATE_HIDDEN);
            bottomSheetBehaviorSampleLocation.setState(BottomSheetBehavior.STATE_HIDDEN);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            topSheetBehavior.setState(TopSheetBehavior.STATE_HIDDEN);
            topSheetPanel.setState(TopSheetBehavior.STATE_EXPANDED);
            bottomSheetBehaviorPickup.setState(BottomSheetBehavior.STATE_EXPANDED);

            bottomSheetBehaviorPickup.setDraggable(true);
            Spinner spinner = findViewById(R.id.spinnerpanel);
            spinner.setSelection(1);

            ImageView back = findViewById(R.id.mapBack);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            //Самомвывоз


        }

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        try{
            mMap = googleMap;
            LatLng home = new LatLng(46.48424075343196, 30.722766322108644);
            mMap.clear();
            mMap.setMinZoomPreference(12.0f);
            mMap.setMaxZoomPreference(18.0f);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(home));
        } catch (Exception e){
         Log.e(TAG, "onMapReady: " + e.getMessage());
        }

        final int category = getIntent().getIntExtra("category", -1);

        if(category==0){
            placeList.clear();
            DatabaseHelper databaseHelper;
            SQLiteDatabase db;

            databaseHelper = new DatabaseHelper(MapsActivity.this);
            databaseHelper.create_db();
            db = databaseHelper.open();

            Cursor userCursor;
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);
            if(userCursor.moveToFirst()){
                time.setText(userCursor.getString(1)+", "+userCursor.getString(0));
            }
            db.close();

            databaseHelper = new DatabaseHelper(MapsActivity.this);
            databaseHelper.create_db();
            db = databaseHelper.open();

            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_ADDRESSES +
                    " where " + DatabaseHelper.COLUMN_ADDRESS_CHOSEN + " = " + "\"true\"", null);
            if(userCursor.moveToFirst()){
                userCursor.moveToFirst();
                placeList.add(new Place(0, userCursor.getString(2), true, true, userCursor.getDouble(0), userCursor.getDouble(1) ));
            }

            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_ADDRESSES+
                    " where " + DatabaseHelper.COLUMN_ADDRESS_CHOSEN + " = " + "\"false\"", null);
            if(userCursor.moveToFirst()){
                userCursor.moveToFirst();
                //Добавление false
                do{
                    //System.out.println("\n"+userCursor.getString(2));
                    placeList.add(new Place(0, userCursor.getString(2), false, true, userCursor.getDouble(0), userCursor.getDouble(1) ));
                } while (userCursor.moveToNext());
                //Новый
            }


            placeList.add(new Place(-1,"ул. Академика Вильямса, 71,\nРесторан FooDDe", false, false,
                    46.39092815919191, 30.72517058102887));
            placeList.add(new Place(-1,"ул. Посметного, 20,\nРесторан FooDDe", false, false,
                    46.42729658551131, 30.75354588893678));
            placeList.add(new Place(-1,"ул. Иоганна Гена, 11,\nРесторан FooDDe", false, false,
                    46.46919224735501, 30.679065805455334));
            placeList.add(new Place(-1,"ул. Ростовская, 17,\nРесторан FooDDe", false, false,
                    46.56041614010818, 30.776388563015868));
            placeList.add(new Place(-1,"Соборная прощадь, 4,\nРесторан FooDDe", false, false,
                    46.48406076972199, 30.73056700396722));
            setPlaceRecyclar(placeList);
            db.close();

        }if(category==1){

            setPlacePickupRecyclar();

            //Самовывоз
        }

    }

    public class LinearLayoutManagerWrapper extends LinearLayoutManager {

        public LinearLayoutManagerWrapper(Context context) {
            super(context);
        }

        public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }


    public void getFastLocation(Context context) {
        SingleShotLocationProvider singleShotLocationProvider = new SingleShotLocationProvider(context);
        singleShotLocationProvider.requestSingleUpdate(context,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        try {
                            Geocoder geocoder = new Geocoder(MapsActivity.this,
                                    Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.latitude,
                                    location.longitude,
                                    1
                            );
                            LatLng home = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                            mMap.clear();
                            mMap.setMinZoomPreference(12.0f);
                            mMap.setMaxZoomPreference(18.0f);
                            mMap.addMarker(new MarkerOptions()
                                    .position(home)
                                    .title("Сlick to confirm")
                                    .draggable(true))
                                    .showInfoWindow();

                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(@NonNull Marker marker) {
                                    mMap.clear();

                                    topSheetBehavior.setHideable(true);
                                    bottomSheetBehavior.setHideable(true);
                                    topSheetPanel.setHideable(true);
                                    String addressLine = "";

                                    TextView address = findViewById(R.id.mapsbottconaddress);
                                    TextView time = findViewById(R.id.mapsbottcontime);
                                    Button confirm = findViewById(R.id.mapsbottconbutton);

                                    topSheetBehavior.setState(TopSheetBehavior.STATE_HIDDEN);
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                                    topSheetPanel.setState(TopSheetBehavior.STATE_HIDDEN);

                                    LatLng point = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);

                                    Geocoder geocoder = new Geocoder(MapsActivity.this,
                                            Locale.getDefault());
                                    try {
                                        List<Address> addresses = geocoder.getFromLocation(
                                                marker.getPosition().latitude,
                                                marker.getPosition().longitude,
                                                1
                                        );
                                        addressLine = addresses.get(0).getAddressLine(0);
                                        address.setText(addresses.get(0).getAddressLine(0));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    mMap.setMinZoomPreference(15.0f);
                                    mMap.setMaxZoomPreference(18.0f);
                                    mMap.addMarker(new MarkerOptions()
                                            .position(point)
                                            .title("You Address")
                                            .draggable(true));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
                                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(@NonNull Marker marker) {
                                            return false;
                                        }
                                    });

                                    time.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            new TimeParameterChanges(MapsActivity.this, time, mTimePicker, spinner).show();
                                        }
                                    });

                                    final String[] finalAddressLine = {addressLine};
                                    final LatLng[] finalAddressll = {marker.getPosition()};
                                    confirm.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            placeList.clear();

                                            DatabaseHelper databaseHelper;
                                            SQLiteDatabase db;
                                            databaseHelper = new DatabaseHelper(MapsActivity.this);
                                            databaseHelper.create_db();
                                            db = databaseHelper.open();

                                            Cursor userCursor;
                                            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_ADDRESSES , null);
                                            if(userCursor.moveToFirst()){
                                                userCursor.moveToFirst();

                                                ContentValues values = new ContentValues();
                                                values.put(DatabaseHelper.COLUMN_ADDRESS_CHOSEN, "false");
                                                String where = DatabaseHelper.COLUMN_ADDRESS_CHOSEN + " = " + "\"true\"";
                                                db.update(DatabaseHelper.TABLE_OF_ADDRESSES, values, where, null);

                                                db.delete(DatabaseHelper.TABLE_OF_ADDRESSES,
                                                        "addressLine = ?", new String[]{finalAddressLine[0]});
                                                db.delete(DatabaseHelper.TABLE_OF_ADDRESSES,
                                                        "latitude = ?", new String[]{String.valueOf(marker.getPosition().latitude)});
                                                db.delete(DatabaseHelper.TABLE_OF_ADDRESSES,
                                                        "longitude = ?", new String[]{String.valueOf(marker.getPosition().longitude)});

                                                userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_ADDRESSES , null);
                                                if(userCursor.moveToFirst()){
                                                    userCursor.moveToFirst();
                                                    //Добавление старых
                                                    do{
                                                        //System.out.println("\n"+userCursor.getString(2));
                                                        placeList.add(new Place(1, userCursor.getString(2), false, true, userCursor.getDouble(0), userCursor.getDouble(1) ));

                                                    } while (userCursor.moveToNext());
                                                    //Новый

                                                }

                                            }

                                            placeList.add(new Place(-1,"ул. Академика Вильямса, 71,\nРесторан FooDDe", false, false,
                                                    46.39092815919191, 30.72517058102887));
                                            placeList.add(new Place(-1,"ул. Посметного, 20,\nРесторан FooDDe", false, false,
                                                    46.42729658551131, 30.75354588893678));
                                            placeList.add(new Place(-1,"ул. Иоганна Гена, 11,\nРесторан FooDDe", false, false,
                                                    46.46919224735501, 30.679065805455334));
                                            placeList.add(new Place(-1,"ул. Ростовская, 17,\nРесторан FooDDe", false, false,
                                                    46.56041614010818, 30.776388563015868));
                                            placeList.add(new Place(-1,"Соборная прощадь, 4,\nРесторан FooDDe", false, false,
                                                    46.48406076972199, 30.73056700396722));

                                            placeList.add(0, new Place(0, finalAddressLine[0], true, true,
                                                    marker.getPosition().latitude, marker.getPosition().longitude));

                                            setPlaceRecyclar(placeList);


                                            ContentValues values = new ContentValues();
                                            values.put(DatabaseHelper.COLUMN_ADDRESS_LATITUDE, finalAddressll[0].latitude);
                                            values.put(DatabaseHelper.COLUMN_ADDRESS_LONGITUDE, finalAddressll[0].longitude);
                                            values.put(DatabaseHelper.COLUMN_ADDRESS_LINE, finalAddressLine[0]);
                                            values.put(DatabaseHelper.COLUMN_ADDRESS_CHOSEN, "true");
                                            db.insert(DatabaseHelper.TABLE_OF_ADDRESSES, null, values);

                                            db.close();
                                            finish();
                                        }
                                    });

                                    topSheetBack.setState(TopSheetBehavior.STATE_EXPANDED);
                                    topSheetBack.setHideable(false);
                                    bottomSheetBehaviorConfirmation.setState(BottomSheetBehavior.STATE_EXPANDED);
                                    bottomSheetBehaviorConfirmation.setDraggable(false);

                                    mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                        @Override
                                        public void onMarkerDragStart(@NonNull Marker marker) {
                                        }

                                        @Override
                                        public void onMarkerDrag(@NonNull Marker marker) {
                                        }

                                        @Override
                                        public void onMarkerDragEnd(@NonNull Marker marker) {
                                            finalAddressll[0] = marker.getPosition();
                                            try {
                                                LatLng markerPosition =  marker.getPosition();
                                                Geocoder geocoder = new Geocoder(MapsActivity.this,
                                                        Locale.getDefault());
                                                List<Address> addresses = geocoder.getFromLocation(
                                                        markerPosition.latitude,
                                                        markerPosition.longitude,
                                                        1
                                                );
                                                finalAddressLine[0] = addresses.get(0).getAddressLine(0);
                                                address.setText(addresses.get(0).getAddressLine(0));

                                            } catch (IOException e) {
                                                Log.e("PlaceAdapter", "onMarkerDragEnd: " + e.getMessage());
                                            }
                                        }
                                    });
                                    return false;
                                }
                            });
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(home));
                        } catch (IOException e) {
                            Log.e(TAG, "getFastLocation: " + e.getMessage());
                        }
                    }
                });
    }


    private boolean checkLocationServiceEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            geolocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        return buildAlertMessageNoLocationService(geolocationEnabled);
    }

    private  boolean checkFineAndCoarseLocation(){

        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return false;
        }

    }


    private boolean buildAlertMessageNoLocationService(boolean network_enabled) {
        if(!isOpen){
            isOpen = true;
            if (!network_enabled) {
                String msg = "Чтобы продолжить, включите на\nустройстве геолокацию Google.";
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false)
                        .setMessage(msg)
                        .setPositiveButton("Включить", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.cancel();
                                dialog.dismiss();
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                isOpen = false;
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
            return false;
        } return false;
    }

    private void setPlaceRecyclar(List<Place> placeList) {
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, 1);
        placesRV.setLayoutManager(layoutManager);

        TextView address = findViewById(R.id.mapsbottconaddress);
        TextView time = findViewById(R.id.mapsbottcontime);
        Button confirm = findViewById(R.id.mapsbottconbutton);

        PlaceAdapter adapter = new PlaceAdapter(MapsActivity.this, placeList, mMap, topSheetPanel, topSheetBehavior, bottomSheetBehavior, bottomSheetBehaviorConfirmation, address, time, confirm, topSheetBack, placesRV, MapsActivity.this);
        placesRV.setAdapter(adapter);
    }

    private void setPlacePickupRecyclar() {
        TextView date = findViewById(R.id.bottom_sheet_pickup_date);

        DatabaseHelper databaseHelper;
        SQLiteDatabase db;

        databaseHelper = new DatabaseHelper(MapsActivity.this);
        databaseHelper.create_db();
        db = databaseHelper.open();

        Cursor userCursor;
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);
        if(userCursor.moveToFirst()){
            date.setText(userCursor.getString(1)+", "+userCursor.getString(0));
        }
        db.close();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManagerWrapper(MapsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        placePickupRV.setLayoutManager(layoutManager);
        PlacePickupAdapter adapter = new PlacePickupAdapter(MapsActivity.this,
                mMap, placePickupRV, date, MapsActivity.this);
        placePickupRV.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        time = findViewById(R.id.mapsbottcontime);

        checkFineAndCoarseLocation();
        checkLocationServiceEnabled();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}