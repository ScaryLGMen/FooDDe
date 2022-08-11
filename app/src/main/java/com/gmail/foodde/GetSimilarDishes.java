package com.gmail.foodde;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.gmail.foodde.adapter.ItemAdapter;
import com.gmail.foodde.adapter.MultiViewItemAdapter;
import com.gmail.foodde.adapter.SimilarDishesAdapter;
import com.gmail.foodde.model.Item;
import com.gmail.foodde.model.MultiViewItem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GetSimilarDishes extends AsyncTask<String, Void, String> {

    private int ID, Dish;
    private Context context;
    private List<Item> itemList = new ArrayList<>();
    private RecyclerView recyclerView;

    public GetSimilarDishes(Context context, RecyclerView recyclerView, int ID, int Dish){
        this.context = context;
        this.recyclerView = recyclerView;
        this.ID = ID;
        this.Dish = Dish;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            String link = "https://myfirstdbproject.000webhostapp.com/getDishes.php";
            String data = URLEncoder.encode("id","UTF-8") + "=" + ID;

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
        if(result!=""){
            String[] subStr = result.split("~");
            for(int y = 0; y < subStr.length; y++){
                String[] subStr1 = subStr[y].split(";");
                itemList.add(new Item(Integer.parseInt(subStr1[0]), subStr1[1], subStr1[2],Integer.parseInt(subStr1[3]),Float.parseFloat(subStr1[4]),subStr1[5],Integer.parseInt(subStr1[6]))); }

            for(int y = 0; y < itemList.size();y++){
                if(Dish==itemList.get(y).getID()){
                    itemList.remove(y);
                }
            }

            setRecycler(itemList);
        }
        else {
            Toast toast = Toast.makeText(context, "Упс, что-то пошло не так.." ,Toast.LENGTH_LONG);
            toast.show();
        }

    }

    private void setRecycler(List<Item> itemList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false );
        recyclerView.setLayoutManager(layoutManager);
        SimilarDishesAdapter adapter = new SimilarDishesAdapter(context, itemList);
        recyclerView.setAdapter(adapter);
    }

}
