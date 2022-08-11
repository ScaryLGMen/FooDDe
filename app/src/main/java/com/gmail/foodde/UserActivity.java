package com.gmail.foodde;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        LinearLayout myOrders = (LinearLayout) findViewById(R.id.user_my_orders);
        LinearLayout myDelMethods = (LinearLayout) findViewById(R.id.user_shipping_methods);
        LinearLayout myDistribution = (LinearLayout) findViewById(R.id.user_distribution);
        LinearLayout myLanguage= (LinearLayout) findViewById(R.id.user_language);

        LinearLayout Payment = (LinearLayout) findViewById(R.id.user_payment_and_delivery);
        LinearLayout FAQ = (LinearLayout) findViewById(R.id.user_aoq);
        LinearLayout Contacts = (LinearLayout) findViewById(R.id.user_contacts);

        TextView mail = (TextView) findViewById(R.id.activity_user_mail_tv);

        DatabaseHelper databaseHelper;
        SQLiteDatabase db;
        Cursor userCursor;

        databaseHelper = new DatabaseHelper(UserActivity.this);
        databaseHelper.create_db();

        db = databaseHelper.open();
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_USER, null);
        userCursor.moveToFirst();

        mail.setText(userCursor.getString(2));


        Button exit = (Button) findViewById(R.id.activity_user_exit_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper;
                SQLiteDatabase db;
                databaseHelper = new DatabaseHelper(UserActivity.this);
                databaseHelper.create_db();
                db = databaseHelper.open();
                db.delete(DatabaseHelper.TABLE_OF_USER,"id = ?", new String[]{String.valueOf(userCursor.getInt(0))});

                db.close();
                finish();
            }
        });

        myOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, UserOrdersActivity.class);
                intent.putExtra("mail", userCursor.getString(2));
                startActivity(intent);
            }
        });
        myDelMethods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, UserDeliveryMethods.class);
                //intent.putExtra("mail", userCursor.getString(2));
                startActivity(intent);
            }
        });
        myDistribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, UserDistributions.class);
                //intent.putExtra("mail", userCursor.getString(2));
                startActivity(intent);
            }
        });

        myLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserActivity.this, "Nigmod", Toast.LENGTH_LONG).show();
            }
        });

        Payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        FAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        db.close();
    }
}