package com.gmail.foodde;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class UserGetData extends AsyncTask<String, Void, String> {

    private Context context;
    private int id = -1;
    private int operation = -1;
    private String token = "";
    private Activity activity;

    public UserGetData(Context context, int id, int operation){
        this.context = context;
        this.id = id;
        this.operation = operation;
    }

    public UserGetData(Context context, int id, int operation, String token, Activity activity){
        this.context = context;
        this.id = id;
        this.operation = operation;
        this.token = token;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            String link = "https://myfirstdbproject.000webhostapp.com/getUserData.php";
            String data = URLEncoder.encode("id","UTF-8") + "=" + id;

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
        if(operation == 0){
            if(result.equals("nothing")){
                DatabaseHelper databaseHelper;
                SQLiteDatabase db;
                databaseHelper = new DatabaseHelper(context);
                databaseHelper.create_db();
                db = databaseHelper.open();
                db.delete(DatabaseHelper.TABLE_OF_USER, "id = ?", new String[]{String.valueOf(id)});
                db.close();

                Toast.makeText(context, "Катежся данные вашего аккаунта устарели,\nпожалуйста, войдите снова", Toast.LENGTH_LONG).show();
            }else {
                System.out.println(result);
                String[] subStr = result.split(",");
                new UserLogin(context, subStr[4], subStr[5],activity, token, false).execute();
            }
        }
    }
}
