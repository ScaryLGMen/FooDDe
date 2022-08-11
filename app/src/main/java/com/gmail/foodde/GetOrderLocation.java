package com.gmail.foodde;

import android.content.Context;
import android.os.AsyncTask;

import com.gmail.foodde.model.Item;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class GetOrderLocation extends AsyncTask<String, Void, String> {


    private Context context;
    private GoogleMap map;
    int orderID;

    public GetOrderLocation(Context context, GoogleMap map, int orderID){
        this.context = context;
        this.map = map;
        this.orderID = orderID;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            String link = "https://myfirstdbproject.000webhostapp.com/getOrderLocation.php";
            String data = URLEncoder.encode("id","UTF-8") + "=" + orderID;

            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while(line != null){
                sb.append(line);
                break;
            }
            return sb.toString();

        }catch (Exception e){
            return new String("Exeption" + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //System.out.println("!!!!"+result);
        String[] subStr = result.split("~");

        LatLng point = new LatLng(Double.parseDouble(subStr[3]), Double.parseDouble(subStr[4]));

        map.clear();
        map.setMinZoomPreference(15.0f);
        map.setMaxZoomPreference(18.0f);
        map.addMarker(new MarkerOptions()
                .position(point)
                .title("Courier")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.courier64))
                .draggable(false));

        map.animateCamera(CameraUpdateFactory.newLatLng(point));
    }

}