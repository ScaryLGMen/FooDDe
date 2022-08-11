package com.gmail.foodde.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.gmail.foodde.DatabaseHelper;
import com.gmail.foodde.MapsActivity;
import com.gmail.foodde.R;
import com.gmail.foodde.TopSheetBehavior;
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

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    Context context;
    List<Place> places;
    GoogleMap map;
    TopSheetBehavior topSheetPanel, topSheetBehavior, topSheetBack;
    BottomSheetBehavior bottomSheetBehavior, bottomSheetConfirmation;
    TextView address, timeOfDelivery;
    Button confrim;
    RecyclerView recyclerView;
    Activity activity;

    public PlaceAdapter(Context context, List<Place> places, GoogleMap map, TopSheetBehavior topSheetPanel ,
                        TopSheetBehavior topSheetBehavior, BottomSheetBehavior bottomSheetBehavior,
                        BottomSheetBehavior bottomSheetConfirmation, TextView address,
                        TextView timeOfDelivery, Button confrirm, TopSheetBehavior topSheetBack,
                        RecyclerView recyclerView, Activity activity) {
        this.context = context;
        this.places = places;
        this.map = map;
        this.topSheetPanel = topSheetPanel;
        this.topSheetBehavior = topSheetBehavior;
        this.bottomSheetBehavior = bottomSheetBehavior;
        this.bottomSheetConfirmation = bottomSheetConfirmation;
        this.address = address;
        this.timeOfDelivery = timeOfDelivery;
        this.confrim = confrirm;
        this.topSheetBack = topSheetBack;
        this.recyclerView = recyclerView;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View placeItems = LayoutInflater.from(context).inflate(R.layout.place_item, parent, false);
        return  new PlaceViewHolder(placeItems);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                topSheetBehavior.setHideable(true);
                bottomSheetBehavior.setHideable(true);
                topSheetPanel.setHideable(true);

                topSheetBehavior.setState(TopSheetBehavior.STATE_HIDDEN);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                topSheetPanel.setState(TopSheetBehavior.STATE_HIDDEN);

                LatLng point = new LatLng(places.get(position).getLatitude(), places.get(position).getLongitude());

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

                address.setText(places.get(position).getText());
                confrim.setOnClickListener(new View.OnClickListener() {
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
                            //Добавление старых
                            db.delete(DatabaseHelper.TABLE_OF_ADDRESSES,
                                    "addressLine = ?", new String[]{places.get(position).getText()});
                            db.delete(DatabaseHelper.TABLE_OF_ADDRESSES,
                                    "latitude = ?", new String[]{String.valueOf(places.get(position).getLatitude())});
                            db.delete(DatabaseHelper.TABLE_OF_ADDRESSES,
                                    "longitude = ?", new String[]{String.valueOf(places.get(position).getLongitude())});

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



                        placeList.add(0, new Place(0, places.get(position).getText(), true, true,
                                places.get(position).getLatitude(), places.get(position).getLongitude()));


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

                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.COLUMN_ADDRESS_LATITUDE, places.get(position).getLatitude());
                        values.put(DatabaseHelper.COLUMN_ADDRESS_LONGITUDE, places.get(position).getLongitude());
                        values.put(DatabaseHelper.COLUMN_ADDRESS_LINE, places.get(position).getText());
                        values.put(DatabaseHelper.COLUMN_ADDRESS_CHOSEN, "true");
                        db.insert(DatabaseHelper.TABLE_OF_ADDRESSES, null, values);

                        //Toast toast = Toast.makeText(context, "На этом моменте Activity закрывается,\n открывается главная страница с указаным адресом доставки" ,Toast.LENGTH_LONG);
                        //toast.show();
                        db.close();
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
                            address.setText(addresses.get(0).getAddressLine(0));
                        } catch (IOException e) {
                            Log.e("PlaceAdapter", "onMarkerDragEnd: " + e.getMessage());
                        }
                    }
                });

            }
        });
        holder.text.setText(places.get(position).getText());


        if(places.get(position).getCheck()){
            holder.check.setVisibility(View.VISIBLE);
        }


        if(!places.get(position).getFound()){
            holder.image.setImageResource(R.drawable.ic_edit_location_lg); }

        if(places.get(position).getID()==-1){
        holder.image.setImageResource(R.drawable.ic_restaurant_lg);}


    }

    private void setPlaceRecyclar(List<Place> placeList) {
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, 1);
        recyclerView.setLayoutManager(layoutManager);
        PlaceAdapter adapter = new PlaceAdapter(context, placeList, map, topSheetPanel, topSheetBehavior, bottomSheetBehavior, bottomSheetConfirmation, address, timeOfDelivery, confrim, topSheetBack, recyclerView, activity);
        //System.out.println(placeList.get(0).getLatitude()+ " "+ placeList.get(0).getLongitude()+ " "+ placeList.get(0).getText());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static final class PlaceViewHolder extends RecyclerView.ViewHolder{
        CardView item;
        TextView text;
        ImageView image, check;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
                item = itemView.findViewById(R.id.place_item);
                text = itemView.findViewById(R.id.place_item_text);
                image = itemView.findViewById(R.id.place_item_image);
                check = itemView.findViewById(R.id.place_item_check);
        }
    }
}

