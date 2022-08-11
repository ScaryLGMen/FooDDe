package com.gmail.foodde;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.foodde.adapter.PromAdapter;
import com.gmail.foodde.model.Key;
import com.gmail.foodde.model.PromItem;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SendMail extends AsyncTask<String, Void, String> {


    private Context context;
    private String mail;
    private Key key;

    public SendMail(Context context, String mail, Key key){
        this.context = context;
        this.mail = mail;
        this.key = key;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            String link = "https://myfirstdbproject.000webhostapp.com/web/php/sendMail.php?mail="+mail;
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
        key.setText(result);
        System.out.println(result);
    }
}
