package com.gmail.foodde;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.foodde.adapter.CategoryAdapter;
import com.gmail.foodde.model.Category;
import com.gmail.foodde.model.Item;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class GetCategories extends AsyncTask<String, Void, String> {


private Context context;
private List<Category> categoryList;
private  RecyclerView recyclerView, itemsView;

public GetCategories(Context context, List<Category> categoryList, RecyclerView recyclerView, RecyclerView itemsView){
        this.context = context;
        this.categoryList = categoryList;
        this.recyclerView = recyclerView;
        this.itemsView = itemsView;
        }

@Override
protected String doInBackground(String... strings) {

        try {
        String link = "https://myfirstdbproject.000webhostapp.com/getCategories.php";
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
                 categoryList.add(new Category(Integer.parseInt(subStr1[0]), subStr1[1], subStr1[2])); }
        setCategoryRecycler(categoryList);
}

private void setCategoryRecycler(List<Category> categoryList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false );
        recyclerView.setLayoutManager(layoutManager);
        CategoryAdapter categoryAdapter = new CategoryAdapter(context, categoryList, itemsView);
        recyclerView.setAdapter(categoryAdapter);
}

}