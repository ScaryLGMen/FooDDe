package com.gmail.foodde;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gmail.foodde.adapter.BasketAdapter;
import com.gmail.foodde.model.BasketItem;
import com.gmail.foodde.model.DiscountItem;
import com.gmail.foodde.model.Item;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Basket {

    private DecimalFormat decimalFormat = new DecimalFormat("#");

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor userCursor;

    private List<BasketItem> basketItems = new ArrayList<>();
    private RecyclerView recyclerView, mainRecyclerView;
    private TextView itemsP, delP, totalP;
    private LinearLayout background;

    public Basket(RecyclerView recyclerView, TextView itemsP,TextView delP,TextView totalP, RecyclerView mainRecyclerView, LinearLayout background){
        this.recyclerView = recyclerView;
        this.itemsP = itemsP;
        this.delP = delP;
        this.totalP = totalP;
        this.mainRecyclerView = mainRecyclerView;
        this.background = background;
    }

    public void show(){

        int quantity = 0;
        float price = 0;

        databaseHelper = new DatabaseHelper(recyclerView.getContext());
        databaseHelper.create_db();

        db = databaseHelper.open();
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
        userCursor.moveToFirst();

        basketItems.clear();
        for(int i = 0; i < userCursor.getCount(); i++){
            float tempPrice = 0;
            DiscountItem discountItem = new Discount(new Item(userCursor.getInt(0),
                    userCursor.getString(2),
                    userCursor.getString(3),
                    userCursor.getInt(6),
                    userCursor.getFloat(5),
                    userCursor.getString(4),
                    userCursor.getInt(7)), recyclerView.getContext()).isExists("Товар");
            if(discountItem.isExists()){
                tempPrice = userCursor.getFloat(5)-(userCursor.getFloat(5)/100)*discountItem.getPercent();
            }

            basketItems.add(new BasketItem(
                    userCursor.getInt(0),
                    userCursor.getInt(1),
                    userCursor.getString(2),
                    userCursor.getString(3),
                    userCursor.getString(4),
                    userCursor.getFloat(5)));
            if(tempPrice!=0){
                price += tempPrice * userCursor.getInt(1);
            }else {
                price += userCursor.getFloat(5)*userCursor.getInt(1);
            }

            quantity += userCursor.getInt(1);
            userCursor.moveToNext();
        }
        db.close();
        setItemRecycler(basketItems);

        if(quantity==0){
            mainRecyclerView.setVisibility(View.GONE);
            background.setVisibility(View.VISIBLE);
        }

        itemsP.setText(decimalFormat.format(price));
        delP.setText(""+(50+(quantity*10)));
        totalP.setText(decimalFormat.format(price+50+(quantity*10)));
    }


    private void setItemRecycler(List<BasketItem> itemList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false );
        recyclerView.setLayoutManager(layoutManager);
        BasketAdapter adapter = new BasketAdapter(recyclerView.getContext(), itemList, recyclerView, itemsP, delP, totalP, mainRecyclerView, background);
        recyclerView.setAdapter(adapter);
    }


}
