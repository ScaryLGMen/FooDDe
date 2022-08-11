package com.gmail.foodde;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.gmail.foodde.adapter.ItemAdapter;
import com.gmail.foodde.adapter.PromAdapter;
import com.gmail.foodde.model.Item;
import com.gmail.foodde.model.PromItem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GetNews extends AsyncTask<String, Void, String> {


    private Context context;
    private List<PromItem> itemList = new ArrayList<>();
    private RecyclerView recyclerView, circlesRecyclerView;

    public GetNews(Context context, RecyclerView recyclerView, RecyclerView circlesRecyclerView){
        this.context = context;
        this.recyclerView = recyclerView;
        this.circlesRecyclerView = circlesRecyclerView;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            String link = "https://myfirstdbproject.000webhostapp.com/getNews.php";
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

        DatabaseHelper databaseHelper;
        SQLiteDatabase db;

        databaseHelper = new DatabaseHelper(context);
        databaseHelper.create_db();
        db = databaseHelper.open();

        db.delete(DatabaseHelper.TABLE_OF_DISCOUNT,null,null);
        ///нужно очистить таблицу

        // код в цикл

        String[] subStr = result.split("~");
        for(int y = 0; y < subStr.length; y++){
            String[] subStr1 = subStr[y].split(";");
            System.out.println(subStr1[0]);
            itemList.add(new PromItem(Integer.parseInt(subStr1[0]), subStr1[1], subStr1[2],subStr1[3],subStr1[4],subStr1[5]));
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_DISCOUNT_PROPERTIES, subStr1[5]);
            values.put(DatabaseHelper.COLUMN_DISCOUNT_DESCRIPTION, subStr1[2]);
            db.insert(DatabaseHelper.TABLE_OF_DISCOUNT, null, values);}

        setItemRecycler(itemList);

        /*Cursor userCursor;
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_DISCOUNT , null);
        if(userCursor.moveToFirst()){
            userCursor.moveToFirst();
            for(int i = 0; i < userCursor.getCount(); userCursor.moveToNext(), i++){
                Toast.makeText(context, userCursor.getString(0), Toast.LENGTH_LONG).show();
            }
        }
        db.close();*/
    }

    int selected = 0;

    private void setItemRecycler(List<PromItem> itemList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false );
        recyclerView.setLayoutManager(layoutManager);
        PromAdapter adapter = new PromAdapter(context, itemList, recyclerView, circlesRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new CirclePagerIndicatorDecoration());
        recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                if(velocityX>0){
                    if(selected<itemList.size()-1){
                        recyclerView.smoothScrollToPosition(selected+1);
                        selected++;
                    }
                }else {
                    if(selected>0){
                        recyclerView.smoothScrollToPosition(selected-1);
                        selected--;
                    }
                }
                return false;
            }
        });
    }

}
