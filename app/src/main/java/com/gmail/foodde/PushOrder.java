package com.gmail.foodde;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.gmail.foodde.adapter.ItemAdapter;
import com.gmail.foodde.model.Item;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class PushOrder extends AsyncTask<String, Void, String> {


    private Context context;
    String user_mail, user_phone, address, address_add_info,
            delivery_start_time, dishes, dishes_comment, status, token;
    Double address_latitude, address_longitude;
    float price;
    int delivery_id = -1;

    public PushOrder (Context context, String user_mail, String user_phone,
                      String address, Double address_latitude, Double address_longitude,
                      String address_add_info, String delivery_start_time, String dishes,
                      String dishes_comment, float price, String status, String token){
        this.context = context;
        this.user_mail = user_mail;
        this.user_phone = user_phone;
        this.address = address;
        this.address_latitude = address_latitude;
        this.address_longitude = address_longitude;
        this.address_add_info = address_add_info;
        this.delivery_start_time = delivery_start_time;
        this.dishes = dishes;
        this.dishes_comment = dishes_comment;
        this.price = price;
        this.status = status;
        this.token = token;

    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            String link = "https://myfirstdbproject.000webhostapp.com/pushOrder.php";
            String data = URLEncoder.encode("data","UTF-8") + "='"+user_mail+"','"+user_phone+"','"+address+"','"+address_latitude+"','"+address_longitude+"','"+address_add_info+"','"+delivery_start_time+"','"+delivery_id+"','"+dishes+"','"+dishes_comment+"','"+price+"','"+status+"','"+token+"'";

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
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
       /* String[] subStr = result.split("~");
        for(int y = 0; y < subStr.length; y++){
            String[] subStr1 = subStr[y].split(";");
            itemList.add(new Item(Integer.parseInt(subStr1[0]), subStr1[1], subStr1[2],Integer.parseInt(subStr1[3]),Float.parseFloat(subStr1[4]),subStr1[5],Integer.parseInt(subStr1[6]))); }
        setItemRecycler(itemList);*/
    }
}
