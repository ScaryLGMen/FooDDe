package com.gmail.foodde;

import android.content.Context;
import android.os.AsyncTask;

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

public class GetDishes extends AsyncTask<String, Void, String> {


private Context context;
private List<Item> itemList;
private RecyclerView recyclerView;

public GetDishes(Context context,  List<Item> itemList, RecyclerView recyclerView){
        this.context = context;
        this.itemList = itemList;
        this.recyclerView = recyclerView;
        }

@Override
protected String doInBackground(String... strings) {

        try {
        String link = "https://myfirstdbproject.000webhostapp.com/getAllDishes.php";
        String data = URLEncoder.encode("id","UTF-8") + "=" + 1;

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
        String[] subStr = result.split("~");
        for(int y = 0; y < subStr.length; y++){
        String[] subStr1 = subStr[y].split(";");
        itemList.add(new Item(Integer.parseInt(subStr1[0]), subStr1[1], subStr1[2],Integer.parseInt(subStr1[3]),Float.parseFloat(subStr1[4]),subStr1[5],Integer.parseInt(subStr1[6]))); }
        setItemRecycler(itemList);
        }

private void setItemRecycler(List<Item> itemList) {
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(layoutManager);
        ItemAdapter categoryAdapter = new ItemAdapter(context, itemList);
        recyclerView.setAdapter(categoryAdapter);
        }

        }
