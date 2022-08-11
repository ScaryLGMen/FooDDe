package com.gmail.foodde;

import android.content.Context;
import android.os.AsyncTask;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.foodde.adapter.UserOrdersAdapter;
import com.gmail.foodde.model.OrderItem;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GetUserOrders extends AsyncTask<String, Void, String> {

    private Context context;
    private List<OrderItem> items = new ArrayList<>();
    private RecyclerView recyclerView;
    private String mail;

    public GetUserOrders(Context context, RecyclerView recyclerView, String mail){
        this.context = context;
        this.recyclerView = recyclerView;
        this.mail = mail;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            String link = "https://myfirstdbproject.000webhostapp.com/getOrders.php";
            String data = URLEncoder.encode("mail","UTF-8") + "=" + mail;

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
        System.out.println("!!!!!!!!!"+result);

        String[] line = result.split(Pattern.quote("}"));
        System.out.println("!!!!!!!!!"+line.length);
        if(!result.equals("")){
            for(int i = 0; i < line.length;i++){
                String orderLine = line[i].toString().substring(1);
                System.out.println(orderLine);
                String[] elements = orderLine.split(Pattern.quote("]"));

                items.add(new OrderItem(Integer.parseInt(elements[0].substring(1)), elements[1].substring(1),
                        elements[2].substring(1),elements[3].substring(1),Double.parseDouble(elements[4].substring(1)),
                        Double.parseDouble(elements[5].substring(1)),elements[6].substring(1),elements[7].substring(1),
                        Integer.parseInt(elements[8].substring(1)),elements[9].substring(1),elements[10].substring(1),
                        Float.parseFloat(elements[11].substring(1)),elements[12].substring(1),elements[13].substring(1)));
            }

            setItemRecycler(items);
        }

    }

    private void setItemRecycler(List<OrderItem> items) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false );
        recyclerView.setLayoutManager(layoutManager);

        UserOrdersAdapter adapter = new UserOrdersAdapter(context, items, recyclerView);
        recyclerView.setAdapter(adapter);
    }
}
