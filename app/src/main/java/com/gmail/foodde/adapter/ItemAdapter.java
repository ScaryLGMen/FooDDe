package com.gmail.foodde.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.foodde.DatabaseHelper;
import com.gmail.foodde.Discount;
import com.gmail.foodde.GetImage;
import com.gmail.foodde.ItemActivity;
import com.gmail.foodde.R;
import com.gmail.foodde.model.DiscountItem;
import com.gmail.foodde.model.Item;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    Context context;
    List<Item> items;
    List<Bitmap> bitmaps = new ArrayList<>();

    public ItemAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemItems = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return  new ItemAdapter.ItemViewHolder(itemItems);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder holder, int position) {
        new GetImage(holder.itemImage, items.get(position).getLink(), bitmaps, items.get(position).getLink()).execute();
        if(items.get(position).getCategory()==4){
            holder.itemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        DiscountItem discountItem = new Discount(items.get(position), context).isExists("Товар");

        if(discountItem.isExists()){
            holder.Discount.setVisibility(View.VISIBLE);
            holder.itemDiscountPercent.setText("-"+discountItem.getPercent()+"%");
            holder.itemPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.itemPriceDiscount.setVisibility(View.VISIBLE);
            DecimalFormat decimalFormat = new DecimalFormat("#");
            String priceDiscount = decimalFormat.format(items.get(position).getPrice()-(items.get(position).getPrice()/100)*discountItem.getPercent());
            holder.itemPriceDiscount.setText(priceDiscount+"₴");
        }

        holder.itemTitle.setText(items.get(position).getName());
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        String price = decimalFormat.format(items.get(position).getPrice());
        holder.itemPrice.setText(price+"₴");
        holder.itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseHelper databaseHelper;
                SQLiteDatabase db;

                databaseHelper = new DatabaseHelper(context);
                databaseHelper.create_db();
                db = databaseHelper.open();

                Cursor userCursor;
                userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where ID = " + items.get(position).getID() , null);

                if(userCursor.moveToFirst()){
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_ID, items.get(position).getID());
                    values.put(DatabaseHelper.COLUMN_QUANTITY, userCursor.getInt(1)+1);
                    values.put(DatabaseHelper.COLUMN_NAME, items.get(position).getName());
                    values.put(DatabaseHelper.COLUMN_DESCRIPTION, items.get(position).getDescription());
                    values.put(DatabaseHelper.COLUMN_LINK, items.get(position).getLink());
                    values.put(DatabaseHelper.COLUMN_PRICE, items.get(position).getPrice());
                    values.put(DatabaseHelper.COLUMN_CATEGORY, items.get(position).getCategory());
                    values.put(DatabaseHelper.COLUMN_GRAMS, items.get(position).getGrams());

                    String where = DatabaseHelper.COLUMN_ID + "=" + items.get(position).getID();
                    db.update(DatabaseHelper.TABLE, values, where, null);

                }else {

                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_ID, items.get(position).getID());
                    values.put(DatabaseHelper.COLUMN_QUANTITY, 1);
                    values.put(DatabaseHelper.COLUMN_NAME, items.get(position).getName());
                    values.put(DatabaseHelper.COLUMN_DESCRIPTION, items.get(position).getDescription());
                    values.put(DatabaseHelper.COLUMN_LINK, items.get(position).getLink());
                    values.put(DatabaseHelper.COLUMN_PRICE, items.get(position).getPrice());
                    values.put(DatabaseHelper.COLUMN_CATEGORY, items.get(position).getCategory());
                    values.put(DatabaseHelper.COLUMN_GRAMS, items.get(position).getGrams());
                    db.insert(DatabaseHelper.TABLE, null, values);

                }

                db.close();
                Toast toast = Toast.makeText(context, "Блюдо было добавлено в корзину",Toast.LENGTH_LONG);
                toast.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("itemID", items.get(position).getID());
                intent.putExtra("itemName", items.get(position).getName());
                intent.putExtra("itemDescription", items.get(position).getDescription());
                intent.putExtra("itemCategory", items.get(position).getCategory());
                intent.putExtra("itemPrice", items.get(position).getPrice());
                intent.putExtra("itemLink", items.get(position).getLink());
                intent.putExtra("itemGrams", items.get(position).getGrams());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmaps.get(position).compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                intent.putExtra("image",byteArray);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static final class ItemViewHolder extends RecyclerView.ViewHolder{

        CardView Discount;
        ImageView itemImage;
        TextView itemTitle, itemPrice, itemDiscountPercent, itemPriceDiscount;
        ImageButton itemButton;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemButton = itemView.findViewById(R.id.itemButton);
            Discount = itemView.findViewById(R.id.itemDiscount);
            itemDiscountPercent = itemView.findViewById(R.id.itemDiscountPercent);
            itemPriceDiscount = itemView.findViewById(R.id.itemPriceDiscount);

        }
    }
}
