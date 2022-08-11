package com.gmail.foodde;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.gmail.foodde.adapter.PlaceAdapter;
import com.gmail.foodde.model.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsSample {
    private int id;
    private TextView text, SampleAddressTextView, FINALaddress, FINALtime;
    private ImageView picture;
    private Context context;
    private Button SampleAddressButton, FINALconfirm;
    private RecyclerView placesRV;


    double latitude;
    double longitude;

    private TopSheetBehavior topSheetBehavior, topSheetPanel, topSheetBack;
    private BottomSheetBehavior bottomSheetBehavior, bottomSheetBehaviorSampleLocation,  bottomSheetConfirmation;

    private GoogleMap map;
    private TimePicker timePicker;
    Activity activity;

    String[] sample_titles = {"Дом","Работа","Клуб","Маркер"};
    int sample_pictures[] = {R.drawable.ic_home_black,R.drawable.ic_work_black,
            R.drawable.ic_bar_black,R.drawable.ic_location_black};


    public MapsSample(Context context, int id ,
                      TextView text, ImageView picture,
                      TopSheetBehavior topSheetBehavior,
                      TopSheetBehavior topSheetPanel,
                      BottomSheetBehavior bottomSheetBehavior,
                      TopSheetBehavior topSheetBack,
                      BottomSheetBehavior bottomSheetBehaviorSampleLocation,
                      GoogleMap map, Button button, TextView sampleAddressTextView,
                      TextView FIANLaddress, TextView FINALtime, Button FINALconfirm,
                      BottomSheetBehavior bottomSheetConfirmation, TimePicker timePicker, Activity activity,
                      RecyclerView placesRV){
        this.context = context;
        this.id = id;
        this.text = text;
        this.picture = picture;
        this.topSheetBehavior = topSheetBehavior;
        this.topSheetPanel = topSheetPanel;
        this.bottomSheetBehavior = bottomSheetBehavior;
        this.topSheetBack = topSheetBack;
        this.bottomSheetBehaviorSampleLocation = bottomSheetBehaviorSampleLocation;
        this.map = map;
        this.SampleAddressButton = button;
        this.SampleAddressTextView = sampleAddressTextView;
        this.FINALaddress = FIANLaddress;
        this.FINALtime = FINALtime;
        this.FINALconfirm = FINALconfirm;
        this.bottomSheetConfirmation = bottomSheetConfirmation;
        this.timePicker = timePicker;
        this.activity = activity;
        this.placesRV = placesRV;
    }

    public void show(){

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptsView = layoutInflater.inflate(R.layout.sample_maps, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);


        //создание/редактирование
        final AlertDialog alertDialog = alertDialogBuilder.create();
        final LinearLayout editLL = (LinearLayout) promptsView.findViewById(R.id.sample_item_ll_editing);
        final Button buttonConfirm = (Button) promptsView.findViewById(R.id.sample_item_confirmation_button);
        final Button buttonLocation = (Button) promptsView.findViewById(R.id.sample_item_getLocation);
        final EditText editText = (EditText) promptsView.findViewById(R.id.sample_item_editText);
        final TextView address = (TextView) promptsView.findViewById(R.id.sample_item_textView);
        final Spinner spinner = (Spinner) promptsView.findViewById(R.id.sample_item_spinner);

        //Подтверждение
        final LinearLayout conLL = (LinearLayout) promptsView.findViewById(R.id.sample_item_ll_con);
        final ImageView conPicture = (ImageView) promptsView.findViewById(R.id.sample_item_ll_con_picture);
        final TextView conText = (TextView) promptsView.findViewById(R.id.sample_item_ll_con_text);
        final TextView conAddress = (TextView) promptsView.findViewById(R.id.sample_item_ll_con_address);
        final Button conDelete = (Button) promptsView.findViewById(R.id.sample_item_ll_con_delete);
        final Button conEdit = (Button) promptsView.findViewById(R.id.sample_item_ll_con_edit);
        final Button conSelect = (Button) promptsView.findViewById(R.id.sample_item_ll_con_select);
        //final

        address.setVisibility(View.GONE);

        DatabaseHelper databaseHelper;
        SQLiteDatabase db;
        databaseHelper = new DatabaseHelper(context);
        databaseHelper.create_db();
        db = databaseHelper.open();

        Cursor userCursor;
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_SAMPLES +" where " +DatabaseHelper.COLUMN_SAMPLE_ID +" = "+ id , null);
        if(userCursor.moveToFirst()){
            userCursor.moveToFirst();
            editLL.setVisibility(View.GONE);
            conLL.setVisibility(View.VISIBLE);

            conText.setText(userCursor.getString(2));
            conPicture.setImageResource(Integer.parseInt(userCursor.getString(1)));

            Geocoder geocoder = new Geocoder(context,
                    Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(
                        userCursor.getDouble(3),
                        userCursor.getDouble(4),
                        1
                );

                conAddress.setText(addresses.get(0).getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }

            conDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHelper databaseHelper;
                    SQLiteDatabase db;
                    databaseHelper = new DatabaseHelper(context);
                    databaseHelper.create_db();
                    db = databaseHelper.open();
                    db.delete(DatabaseHelper.TABLE_OF_SAMPLES, "id = ?", new String[]{String.valueOf(id)});
                    text.setText("Шаблон");
                    picture.setImageResource(R.drawable.ic_plus_lg);
                    alertDialog.cancel();
                    db.close();
                }
            });
            conEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    address.setVisibility(View.VISIBLE);
                    address.setText(conAddress.getText());
                    editText.setText(conText.getText());
                    latitude = userCursor.getDouble(3);
                    longitude = userCursor.getDouble(4);

                    buttonConfirm.setText("Редактировать");

                    editLL.setVisibility(View.VISIBLE);
                    conLL.setVisibility(View.GONE);
                }
            });

            conSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertDialog.cancel();

                    topSheetBehavior.setHideable(true);
                    bottomSheetBehavior.setHideable(true);
                    topSheetPanel.setHideable(true);

                    topSheetBehavior.setState(TopSheetBehavior.STATE_HIDDEN);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    topSheetPanel.setState(TopSheetBehavior.STATE_HIDDEN);

                    LatLng point = new LatLng(userCursor.getDouble(3), userCursor.getDouble(4));

                    map.clear();
                    map.setMinZoomPreference(15.0f);
                    map.setMaxZoomPreference(18.0f);
                    map.addMarker(new MarkerOptions()
                            .position(point)
                            .title("You Address")
                            .draggable(true));
                    map.moveCamera(CameraUpdateFactory.newLatLng(point));
                    map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            return false;
                        }
                    });
                    //map.animateCamera(CameraUpdateFactory.newLatLng(point));

                    FINALtime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new TimeParameterChanges(context, FINALtime, timePicker, spinner).show();
                        }
                    });

                    Geocoder geocoder = new Geocoder(context,
                            Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(
                                userCursor.getDouble(3),
                                userCursor.getDouble(4),
                                1
                        );
                        FINALaddress.setText(addresses.get(0).getAddressLine(0));
                        latitude = addresses.get(0).getLatitude();
                        longitude = addresses.get(0).getLongitude();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    FINALconfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            List<Place> placeList = new ArrayList<>();

                            DatabaseHelper databaseHelper;
                            SQLiteDatabase db;
                            databaseHelper = new DatabaseHelper(context);
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
                                        "addressLine = ?", new String[]{FINALaddress.getText().toString()});
                                db.delete(DatabaseHelper.TABLE_OF_ADDRESSES,
                                        "latitude = ?", new String[]{String.valueOf(latitude)});
                                db.delete(DatabaseHelper.TABLE_OF_ADDRESSES,
                                        "longitude = ?", new String[]{String.valueOf(longitude)});

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

     //fooodddeee

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


                            placeList.add(0, new Place(0, FINALaddress.getText().toString(), true, true,
                                    latitude, longitude));

                            setPlaceRecyclar(placeList);

                            ContentValues values = new ContentValues();
                            values.put(DatabaseHelper.COLUMN_ADDRESS_LATITUDE, latitude);
                            values.put(DatabaseHelper.COLUMN_ADDRESS_LONGITUDE, longitude);
                            values.put(DatabaseHelper.COLUMN_ADDRESS_LINE, FINALaddress.getText().toString());
                            values.put(DatabaseHelper.COLUMN_ADDRESS_CHOSEN, "true");
                            db.insert(DatabaseHelper.TABLE_OF_ADDRESSES, null, values);

                            //Toast toast = Toast.makeText(context, "На этом моменте Activity закрывается,\n открывается главная страница с указаным адресом доставки" ,Toast.LENGTH_LONG);
                            //toast.show();
                            activity.finish();
                        }
                    });

                    topSheetBack.setState(TopSheetBehavior.STATE_EXPANDED);
                    topSheetBack.setHideable(false);

                    bottomSheetConfirmation.setState(BottomSheetBehavior.STATE_EXPANDED);
                    bottomSheetConfirmation.setDraggable(false);

                    map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(@NonNull Marker marker) {
                        }

                        @Override
                        public void onMarkerDrag(@NonNull Marker marker) {
                        }

                        @Override
                        public void onMarkerDragEnd(@NonNull Marker marker) {
                            try {
                                LatLng markerPosition =  marker.getPosition();
                                Geocoder geocoder = new Geocoder(context,
                                        Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(
                                        markerPosition.latitude,
                                        markerPosition.longitude,
                                        1
                                );
                                FINALaddress.setText(addresses.get(0).getAddressLine(0));
                            } catch (IOException e) {
                                Log.e("PlaceAdapter", "onMarkerDragEnd: " + e.getMessage());
                            }
                        }
                    });
                    //
                }
            });
            //System.out.println("Da"+ userCursor.getString(1));
        }else {
            editLL.setVisibility(View.VISIBLE);
            conLL.setVisibility(View.GONE);
            //System.out.println("Net");
        }

        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();

                //hideKeyboard(activity);

                map.clear();
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        map.clear();
                        map.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title("You point")
                                .draggable(true));
                        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                        Geocoder geocoder = new Geocoder(context,
                                Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(
                                    latLng.latitude,
                                    latLng.longitude,
                                    1
                            );

                            latitude = addresses.get(0).getLatitude();
                            longitude = addresses.get(0).getLongitude();

                            SampleAddressTextView.setText(addresses.get(0).getAddressLine(0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                            @Override
                            public void onMarkerDragStart(@NonNull Marker marker) { }
                            @Override
                            public void onMarkerDrag(@NonNull Marker marker) { }

                            @Override
                            public void onMarkerDragEnd(@NonNull Marker marker) {
                                Geocoder geocoder = new Geocoder(context,
                                        Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(
                                            marker.getPosition().latitude,
                                            marker.getPosition().longitude,
                                            1
                                    );

                                    latitude = addresses.get(0).getLatitude();
                                    longitude = addresses.get(0).getLongitude();

                                    SampleAddressTextView.setText(addresses.get(0).getAddressLine(0));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                });


                SampleAddressButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        address.setVisibility(View.VISIBLE);
                        address.setText(SampleAddressTextView.getText());
                        alertDialog.show();


                        topSheetBack.setHideable(true);
                        topSheetBack.setState(TopSheetBehavior.STATE_HIDDEN);
                        bottomSheetBehaviorSampleLocation.setHideable(true);
                        bottomSheetBehaviorSampleLocation.setState(BottomSheetBehavior.STATE_HIDDEN);

                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        topSheetBehavior.setState(TopSheetBehavior.STATE_EXPANDED);
                        topSheetPanel.setState(TopSheetBehavior.STATE_HIDDEN);

                        map.clear();
                        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(@NonNull LatLng latLng) {
                            }
                        });
                    }
                });



                topSheetBehavior.setHideable(true);
                bottomSheetBehavior.setHideable(true);
                topSheetPanel.setHideable(true);

                topSheetBehavior.setState(TopSheetBehavior.STATE_HIDDEN);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                topSheetPanel.setState(TopSheetBehavior.STATE_HIDDEN);

                //bottomSheetBehavior.setMaxWidth(0);
                //
                bottomSheetBehaviorSampleLocation.setState(BottomSheetBehavior.STATE_EXPANDED);
                topSheetBack.setState(TopSheetBehavior.STATE_EXPANDED);
                topSheetBack.setHideable(false);
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editText.getText().length()!=0&&address.getText().length()!=0){
                    int pos = spinner.getSelectedItemPosition();

                    DatabaseHelper databaseHelper;
                    SQLiteDatabase db;
                    databaseHelper = new DatabaseHelper(context);
                    databaseHelper.create_db();
                    db = databaseHelper.open();

                    Cursor userCursor;
                    userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_SAMPLES +" where " +DatabaseHelper.COLUMN_SAMPLE_ID +" = "+ id , null);
                    if(userCursor.moveToFirst()){
                        text.setText(editText.getText());
                        picture.setImageResource(sample_pictures[pos]);

                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.COLUMN_SAMPLE_ID, id);
                        values.put(DatabaseHelper.COLUMN_SAMPLE_PICTURE, sample_pictures[pos]);
                        values.put(DatabaseHelper.COLUMN_SAMPLE_TEXT, String.valueOf(text.getText()));
                        values.put(DatabaseHelper.COLUMN_SAMPLE_LATITUDE, latitude);
                        values.put(DatabaseHelper.COLUMN_SAMPLE_LONGITUDE, longitude);
                        String where = DatabaseHelper.COLUMN_SAMPLE_ID + "=" + id;
                        db.update(DatabaseHelper.TABLE_OF_SAMPLES, values, where, null);

                    }else {
                        text.setText(editText.getText());
                        picture.setImageResource(sample_pictures[pos]);

                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.COLUMN_SAMPLE_ID, id);
                        values.put(DatabaseHelper.COLUMN_SAMPLE_PICTURE, sample_pictures[pos]);
                        values.put(DatabaseHelper.COLUMN_SAMPLE_TEXT, String.valueOf(text.getText()));
                        values.put(DatabaseHelper.COLUMN_SAMPLE_LATITUDE, latitude);
                        values.put(DatabaseHelper.COLUMN_SAMPLE_LONGITUDE, longitude);
                        db.insert(DatabaseHelper.TABLE_OF_SAMPLES, null, values);
                    }
                    db.close();

                    if(conAddress.length()>0){

                        editLL.setVisibility(View.GONE);
                        conLL.setVisibility(View.VISIBLE);
                        Toast toast = Toast.makeText(context, "Шаблон успешно редактирован" ,Toast.LENGTH_LONG);
                        toast.show();
                    }else {

                        alertDialog.cancel();
                    }

                }else {
                    Toast toast = Toast.makeText(context, "Не указано название\n или координаты шаблона " ,Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        CustomAdapter customAdapter = new CustomAdapter(context, sample_pictures, sample_titles);
        spinner.setAdapter(customAdapter);

        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    private void setPlaceRecyclar(List<Place> placeList) {
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, 1);
        placesRV.setLayoutManager(layoutManager);


        PlaceAdapter adapter = new PlaceAdapter(context, placeList, map, topSheetPanel, topSheetBehavior, bottomSheetBehavior, bottomSheetConfirmation, FINALaddress, FINALtime, FINALconfirm, topSheetBack, placesRV, activity);
        placesRV.setAdapter(adapter);
    }

    private class CustomAdapter extends BaseAdapter {
        private Context context;
        private int pictures[];
        private String[] titles;
        private LayoutInflater inflater;

        public CustomAdapter(Context context, int[] pictures, String[] titles) {
            this.context = context;
            this.pictures = pictures;
            this.titles = titles;
            this.inflater = (LayoutInflater.from(context));
        }

        @Override
        public int getCount() {
            return pictures.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.custom_row, null);
            ImageView picture = (ImageView) view.findViewById(R.id.custom_row_image);
            //TextView title = (TextView) view.findViewById(R.id.custom_row_text);
            picture.setImageResource(pictures[i]);
            //title.setText(titles[i]);
            return view;
        }
    }


}
