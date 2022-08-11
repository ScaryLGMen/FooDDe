package com.gmail.foodde;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gmail.foodde.adapter.MultiViewItemAdapter;
import com.gmail.foodde.model.Item;
import com.gmail.foodde.model.MultiViewItem;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);

        final int itemID = getIntent().getIntExtra("itemID", -1);
        final String itemName = getIntent().getStringExtra("itemName");
        final String itemDescription = getIntent().getStringExtra("itemDescription");
        final int itemCategory = getIntent().getIntExtra("itemCategory", -1);
        final Float itemPrice = getIntent().getFloatExtra("itemPrice", -1);
        final String itemLink = getIntent().getStringExtra("itemLink");
        final int itemGrams = getIntent().getIntExtra("itemGrams", -1);

        final byte[] byteArray = getIntent().getByteArrayExtra("image");
        final Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        ArrayList<MultiViewItem> items = new ArrayList<>();
        /*items.add(new MultiViewItem(0,0, itemID, itemGrams,
                itemCategory, itemName, itemDescription, itemLink, itemPrice));*/
        items.add(new MultiViewItem(0,0,new Item(itemID, itemName, itemDescription, itemCategory, itemPrice, itemLink, itemGrams)));
        items.add(new MultiViewItem(1,1));
        items.add(new MultiViewItem(-1,-1));

        final  RecyclerView recyclerView = findViewById(R.id.item_page_recyclerView);
        setRecycler(items, recyclerView, bitmap);

        final ImageView close = findViewById(R.id.item_page_bottom_panel_close);
        final Button add = findViewById(R.id.item_page_bottom_panel_add);
        final LinearLayout basket = findViewById(R.id.item_page_bottom_panel_basket);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper;
                SQLiteDatabase db;

                databaseHelper = new DatabaseHelper(ItemActivity.this);
                databaseHelper.create_db();
                db = databaseHelper.open();

                Cursor userCursor;
                userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where ID = " + itemID , null);

                if(userCursor.moveToFirst()){
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_ID, itemID);
                    values.put(DatabaseHelper.COLUMN_QUANTITY, userCursor.getInt(1)+1);
                    values.put(DatabaseHelper.COLUMN_NAME, itemName);
                    values.put(DatabaseHelper.COLUMN_DESCRIPTION, itemDescription);
                    values.put(DatabaseHelper.COLUMN_LINK, itemLink);
                    values.put(DatabaseHelper.COLUMN_PRICE, itemPrice);
                    values.put(DatabaseHelper.COLUMN_CATEGORY, itemCategory);
                    values.put(DatabaseHelper.COLUMN_GRAMS, itemGrams);
                    String where = DatabaseHelper.COLUMN_ID + "=" + itemID;
                    db.update(DatabaseHelper.TABLE, values, where, null);

                }else {

                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_ID, itemID);
                    values.put(DatabaseHelper.COLUMN_QUANTITY, 1);
                    values.put(DatabaseHelper.COLUMN_NAME, itemName);
                    values.put(DatabaseHelper.COLUMN_DESCRIPTION, itemDescription);
                    values.put(DatabaseHelper.COLUMN_LINK, itemLink);
                    values.put(DatabaseHelper.COLUMN_PRICE, itemPrice);
                    values.put(DatabaseHelper.COLUMN_CATEGORY, itemCategory);
                    values.put(DatabaseHelper.COLUMN_GRAMS, itemGrams);
                    db.insert(DatabaseHelper.TABLE, null, values);

                }

                db.close();
                Toast toast = Toast.makeText(ItemActivity.this, "Блюдо было добавлено в корзину",Toast.LENGTH_LONG);
                toast.show();
            }
        });

        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemActivity.this, BasketActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void setRecycler(ArrayList<MultiViewItem> items, RecyclerView recyclerView, Bitmap bitmap) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ItemActivity.this, RecyclerView.VERTICAL, false );
        recyclerView.setLayoutManager(layoutManager);
        MultiViewItemAdapter adapter = new MultiViewItemAdapter(ItemActivity.this, items, bitmap);
        recyclerView.setAdapter(adapter);
    }
}