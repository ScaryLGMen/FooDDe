package com.gmail.foodde;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.room.util.StringUtil;

import com.gmail.foodde.model.DiscountItem;
import com.gmail.foodde.model.Item;

import java.util.ArrayList;
import java.util.List;

public class Discount {
    private Item item;
    private Context context;
    private List<DiscountItem> discountItems = new ArrayList<>();

    public Discount(Item item, Context context){
        this.item = item;
        this.context = context;
        this.setProperties();
    }
    /*public Discount(Context context){
        this.context = context;
        getProperties();
    }*/
    public double calculate(){
        double res = 0;

        return res;
    }
    public void setProperties(){
        DatabaseHelper databaseHelper;
        SQLiteDatabase db;
        databaseHelper = new DatabaseHelper(context);
        databaseHelper.create_db();
        db = databaseHelper.open();
        Cursor userCursor;
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_DISCOUNT , null);
        if(userCursor.moveToFirst()){
            userCursor.moveToFirst();
            for(int i = 0; i < userCursor.getCount(); userCursor.moveToNext(), i++){
                //Toast.makeText(context, userCursor.getString(0), Toast.LENGTH_LONG).show();
                discountItems.add(new DiscountItem(userCursor.getString(1),userCursor.getString(0).split(" ")));
            }
        }
    }
    public DiscountItem isExists(String separator){
        DiscountItem discountItem = new DiscountItem();
        if(separator.equals("Товар")){
            for(int i = 0; i < discountItems.size(); i++){
                String[] discount = discountItems.get(i).getProperties();
                if(discount[0].equals("discount")){
                    switch (discount[1]){
                        case "item":{
                            if(item.getID()==Integer.parseInt(discount[2])){
                                discountItem.setExists(true);
                                discountItem.setPercent(discountItem.getPercent()+Integer.parseInt(discount[3].substring(0, discount[3].length()-1)));
                                discountItem.setPromText(discountItem.getPromText() + discountItems.get(i).getPromText()+"\n");
                            }
                            break;
                        }
                        case "items":{
                            for(int y = 2; y < discount.length-1;y++){
                                if(item.getID()==Integer.parseInt(discount[y])){
                                    discountItem.setExists(true);
                                    discountItem.setPercent(discountItem.getPercent()+Integer.parseInt(discount[discount.length-1].substring(0, discount[discount.length-1].length()-1)));
                                    discountItem.setPromText(discountItem.getPromText() + discountItems.get(i).getPromText()+"\n");
                                }
                            }
                            break;
                        }
                        case "cat":{
                            if(item.getCategory()==Integer.parseInt(discount[2])){
                                discountItem.setExists(true);
                                discountItem.setPercent(discountItem.getPercent()+Integer.parseInt(discount[3].substring(0, discount[3].length()-1)));
                                discountItem.setPromText(discountItem.getPromText() + discountItems.get(i).getPromText()+"\n");
                            }
                            break;
                        }
                        case "cats":{
                            for(int y = 2; y < discount.length-1;y++){
                                if(item.getCategory()==Integer.parseInt(discount[y])){
                                    discountItem.setExists(true);
                                    discountItem.setPercent(discountItem.getPercent()+Integer.parseInt(discount[discount.length-1].substring(0, discount[discount.length-1].length()-1)));
                                    discountItem.setPromText(discountItem.getPromText() + discountItems.get(i).getPromText()+"\n");
                                }
                            }
                            break;
                        }
                        case "all":{
                            discountItem.setExists(true);
                            discountItem.setPercent(discountItem.getPercent()+Integer.parseInt(discount[2].substring(0, discount[2].length()-1)));
                            discountItem.setPromText(discountItem.getPromText() + discountItems.get(i).getPromText()+"\n");
                            break;
                        }
                    }
                }
            }
        }
        if(separator.equals("Доставка")){

        }
        return discountItem;
    }

}
