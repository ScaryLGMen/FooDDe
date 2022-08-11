package com.gmail.foodde;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class UserLogin extends AsyncTask<String, Void, String> {

    private Context context;
    private String mail;
    private String distribution, token;
    private Activity activity;
    private boolean start = true;

    public UserLogin(Context context, String mail,
                     String distribution, Activity activity, String token){
        this.context = context;
        this.mail = mail;
        this.distribution = distribution;
        this.activity = activity;
        this.token = token;
    }
    public UserLogin(Context context, String mail,
                     String distribution, Activity activity, String token, boolean start){
        this.context = context;
        this.mail = mail;
        this.distribution = distribution;
        this.activity = activity;
        this.token = token;
        this.start = start;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            String link = "https://myfirstdbproject.000webhostapp.com/loginUserAccount.php";
            String data = URLEncoder.encode("data","UTF-8") + "= '" + mail + "', '" + distribution + "', '" + token + "'" ;

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

        }
        catch (Exception e){
            return new String("Exeption " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {
        if(result.matches(".*\\d+.*")){

            String[] subStr = result.split(",");
            DatabaseHelper databaseHelper;
            SQLiteDatabase db;
            databaseHelper = new DatabaseHelper(context);
            databaseHelper.create_db();
            db = databaseHelper.open();

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_USER_ID, subStr[0]);
            values.put(DatabaseHelper.COLUMN_USER_NAME, subStr[1]);
            values.put(DatabaseHelper.COLUMN_USER_BONUS,subStr[2]);
            values.put(DatabaseHelper.COLUMN_USER_LIKED,subStr[3]);
            values.put(DatabaseHelper.COLUMN_USER_MAIL, subStr[4]);
            values.put(DatabaseHelper.COLUMN_USER_DISTRIBUTION, subStr[5]);
            db.insert(DatabaseHelper.TABLE_OF_USER, null, values);
            db.close();
            //Toast.makeText(context, "Активити с профилем ещё не готово", Toast.LENGTH_LONG).show();
            if(start){
                Intent intent = new Intent(activity, UserActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }

        }else {
            Toast.makeText(context, "Упс, кажется случилась какая-то ошибка..", Toast.LENGTH_LONG).show();
        }

    }
}
