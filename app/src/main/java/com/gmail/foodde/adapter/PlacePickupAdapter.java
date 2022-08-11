package com.gmail.foodde.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.foodde.DatabaseHelper;
import com.gmail.foodde.MapsActivity;
import com.gmail.foodde.R;
import com.gmail.foodde.TimeParameterChanges;
import com.gmail.foodde.model.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class PlacePickupAdapter extends RecyclerView.Adapter<PlacePickupAdapter.PlacePickupViewHolder>{

    private Context context;
    private List<Place> places;
    private GoogleMap map;
    private RecyclerView recyclerView;
    private ArrayList<Marker> markerArray = new ArrayList<Marker>();
    private TextView date;
    private Activity activity;

    public PlacePickupAdapter(Context context, GoogleMap map, RecyclerView recyclerView, TextView date, Activity activity){
        this.context = context;
        this.places = new ArrayList<>();
        this.date = date;
        this.places.add(new Place(0,"ул. Академика Вильямса, 71,\nРесторан FooDDe", false, false,
                46.39092815919191, 30.72517058102887));
        this.places.add(new Place(1,"ул. Посметного, 20,\nРесторан FooDDe", false, false,
                46.42729658551131, 30.75354588893678));
        this.places.add(new Place(2,"ул. Иоганна Гена, 11,\nРесторан FooDDe", false, false,
                46.46919224735501, 30.679065805455334));
        this.places.add(new Place(3,"ул. Ростовская, 17,\nРесторан FooDDe", false, false,
                46.56041614010818, 30.776388563015868));
        this.places.add(new Place(4,"Соборная прощадь, 4,\nРесторан FooDDe", false, false,
                46.48406076972199, 30.73056700396722));
        this.map = map;
        this.recyclerView = recyclerView;
        this.activity = activity;

        for(int i = 0; i < places.size(); i++){
            LatLng point = new LatLng(places.get(i).getLatitude(), places.get(i).getLongitude());
            Marker marker = map.addMarker(new MarkerOptions().position(point)
                    .title(places.get(i).getText())
                    .draggable(false));
            markerArray.add(marker);
        }
    }


    @NonNull
    @Override
    public PlacePickupAdapter.PlacePickupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View placeItems = LayoutInflater.from(context).inflate(R.layout.place_item_pickup, parent, false);
        return  new PlacePickupAdapter.PlacePickupViewHolder(placeItems);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacePickupViewHolder holder, int position) {
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i <markerArray.size(); i++){
                    if(places.get(position).getText().equals(markerArray.get(i).getTitle())){
                        LatLng point = new LatLng(markerArray.get(i).getPosition().latitude, markerArray.get(i).getPosition().longitude);
                        map.animateCamera(CameraUpdateFactory.newLatLng(point));
                        markerArray.get(i).showInfoWindow();
                    }
                }
            }
        });
        holder.address.setText(places.get(position).getText());
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                for(int i = 0; i< places.size();i++){
                    if(places.get(i).getText().equals(marker.getTitle())){
                        //System.out.println("\n\n\n"+places.get(i).getID());
                        recyclerView.smoothScrollToPosition(places.get(i).getID());
                    }
                }
                return false;
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimeParameterChanges(context, date).show(false);
            }
        });

        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                            "addressLine = ?", new String[]{places.get(position).getText()});
                    db.delete(DatabaseHelper.TABLE_OF_ADDRESSES,
                            "latitude = ?", new String[]{String.valueOf(places.get(position).getLatitude())});
                    db.delete(DatabaseHelper.TABLE_OF_ADDRESSES,
                            "longitude = ?", new String[]{String.valueOf(places.get(position).getLongitude())});

                }

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
    }

    @Override
    public int getItemCount() { return places.size(); }

    public static final class PlacePickupViewHolder extends RecyclerView.ViewHolder{
        CardView item;
        TextView address;
        Button confirm;
        public PlacePickupViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.place_item_pickup);
            address = itemView.findViewById(R.id.place_item_pickup_address);
            //time = itemView.findViewById(R.id.place_item_pickup_time);
            confirm = itemView.findViewById(R.id.place_item_pickup_confirm);
        }
    }
}
