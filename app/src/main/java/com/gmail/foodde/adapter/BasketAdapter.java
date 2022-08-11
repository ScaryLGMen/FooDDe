package com.gmail.foodde.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.foodde.Basket;
import com.gmail.foodde.DatabaseHelper;
import com.gmail.foodde.Discount;
import com.gmail.foodde.GetImage;
import com.gmail.foodde.R;
import com.gmail.foodde.model.BasketItem;
import com.gmail.foodde.model.DiscountItem;
import com.gmail.foodde.model.Item;

import java.text.DecimalFormat;
import java.util.List;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.BasketViewHolder> {

    private DecimalFormat decimalFormat = new DecimalFormat("#");
    private Context context;
    private List<BasketItem> basketItems;
    private RecyclerView recyclerView, mainRecyclerView;
    private TextView itemsP, delP, totalP;
    private LinearLayout background;

    public BasketAdapter(Context context, List<BasketItem> basketItems, RecyclerView recyclerView,TextView itemsP,TextView delP,TextView totalP, RecyclerView mainRecyclerView, LinearLayout background) {
        this.context = context;
        this.basketItems = basketItems;
        this.recyclerView = recyclerView;
        this.itemsP = itemsP;
        this.delP = delP;
        this.totalP = totalP;
        this.mainRecyclerView = mainRecyclerView;
        this.background = background;
    }



    @NonNull
    @Override
    public BasketAdapter.BasketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View basketItems = LayoutInflater.from(context).inflate(R.layout.backet_item, parent, false);
        return  new BasketAdapter.BasketViewHolder(basketItems);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketAdapter.BasketViewHolder holder, int position) {
        float discountPrice = 0;
        new GetImage(holder.basketImage, basketItems.get(position).getLink(), basketItems.get(position).getLink()).execute();
        holder.basketTitle.setText(basketItems.get(position).getName());

        DatabaseHelper databaseHelper;
        SQLiteDatabase db;
        databaseHelper = new DatabaseHelper(context);
        databaseHelper.create_db();
        db = databaseHelper.open();

        Cursor userCursor;
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where ID = " + basketItems.get(position).getID() , null);
        userCursor.moveToFirst();

        final int[] quantity = {userCursor.getInt(1)};
            if(quantity[0] ==1){ holder.leftButton.setEnabled(false); }


        //holder.basketPrice.setText(String.valueOf(basketItems.get(position).getPrice()));

        DiscountItem discountItem = new Discount(new Item(userCursor.getInt(0),
                userCursor.getString(2),
                userCursor.getString(3),
                userCursor.getInt(6),
                userCursor.getFloat(5),
                userCursor.getString(4),
                userCursor.getInt(7)), recyclerView.getContext()).isExists("Товар");
        if(discountItem.isExists()){
            //DecimalFormat decimalFormat = new DecimalFormat("#");
            holder.discountView.setVisibility(View.VISIBLE);
            holder.discountPercent.setText("-"+discountItem.getPercent()+"%");
            discountPrice = basketItems.get(position).getPrice()-(basketItems.get(position).getPrice()/100)*discountItem.getPercent();
            String priceDiscount = decimalFormat.format(discountPrice*quantity[0]);
            holder.basketPrice.setText(priceDiscount);
        }else {
            String price = decimalFormat.format(basketItems.get(position).getPrice()*quantity[0]);
            holder.basketPrice.setText(price);
        }



        //holder.basketPrice.setText(""+(basketItems.get(position).getPrice()*quantity[0]));

        holder.basketQuantity.setText(String.valueOf(basketItems.get(position).getQuantity()));
        holder.basketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper;
                SQLiteDatabase db;
                databaseHelper = new DatabaseHelper(recyclerView.getContext());
                databaseHelper.create_db();
                db = databaseHelper.open();
                db.delete(DatabaseHelper.TABLE, "ID = ?", new String[]{String.valueOf(basketItems.get(position).getID())});
                new Basket(recyclerView, itemsP, delP, totalP, mainRecyclerView, background).show();
                db.close();
            }
        });
        float finalDiscountPrice = discountPrice;
        holder.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_ID, basketItems.get(position).getID());
                values.put(DatabaseHelper.COLUMN_QUANTITY, quantity[0] -1);
                values.put(DatabaseHelper.COLUMN_NAME, basketItems.get(position).getName());
                values.put(DatabaseHelper.COLUMN_DESCRIPTION, basketItems.get(position).getDescription());
                values.put(DatabaseHelper.COLUMN_LINK, basketItems.get(position).getLink());
                values.put(DatabaseHelper.COLUMN_PRICE, basketItems.get(position).getPrice());
                String where = DatabaseHelper.COLUMN_ID + "=" + basketItems.get(position).getID();
                db.update(DatabaseHelper.TABLE, values, where, null);

                holder.basketQuantity.setText(""+(Integer.parseInt(String.valueOf(holder.basketQuantity.getText()))-1));
                if(Integer.parseInt(String.valueOf(holder.basketQuantity.getText()))==1){ holder.leftButton.setEnabled(false); }
                quantity[0]--;

                //holder.basketPrice.setText(""+(basketItems.get(position).getPrice()*quantity[0]));
                if(finalDiscountPrice !=0){
                    String price = decimalFormat.format(finalDiscountPrice *quantity[0]);
                    holder.basketPrice.setText(price);
                    itemsP.setText(decimalFormat.format(Float.parseFloat(itemsP.getText().toString())-finalDiscountPrice));
                    delP.setText(""+(Integer.parseInt(delP.getText().toString())-10));
                    totalP.setText(""+(Integer.parseInt(itemsP.getText().toString())+Integer.parseInt(delP.getText().toString())));
                }else {
                    String price = decimalFormat.format(basketItems.get(position).getPrice()*quantity[0]);
                    holder.basketPrice.setText(price);
                    itemsP.setText(decimalFormat.format(Float.parseFloat(itemsP.getText().toString())-basketItems.get(position).getPrice()));
                    delP.setText(""+(Integer.parseInt(delP.getText().toString())-10));
                    totalP.setText(""+(Integer.parseInt(itemsP.getText().toString())+Integer.parseInt(delP.getText().toString())));
                }
            }
        });
        float finalDiscountPrice1 = discountPrice;
        holder.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_ID, basketItems.get(position).getID());
                values.put(DatabaseHelper.COLUMN_QUANTITY, quantity[0] +1);
                values.put(DatabaseHelper.COLUMN_NAME, basketItems.get(position).getName());
                values.put(DatabaseHelper.COLUMN_DESCRIPTION, basketItems.get(position).getDescription());
                values.put(DatabaseHelper.COLUMN_LINK, basketItems.get(position).getLink());
                values.put(DatabaseHelper.COLUMN_PRICE, basketItems.get(position).getPrice());
                String where = DatabaseHelper.COLUMN_ID + "=" + basketItems.get(position).getID();
                db.update(DatabaseHelper.TABLE, values, where, null);

                holder.basketQuantity.setText(""+(Integer.parseInt(String.valueOf(holder.basketQuantity.getText()))+1));
                if(!holder.leftButton.isEnabled()){ holder.leftButton.setEnabled(true); }
                quantity[0]++;


                //holder.basketPrice.setText(""+(basketItems.get(position).getPrice()*quantity[0]));
                //DecimalFormat decimalFormat = new DecimalFormat("#");
                //String price= decimalFormat.format(basketItems.get(position).getPrice()*quantity[0]);
                //holder.basketPrice.setText(price);

                if(finalDiscountPrice1 !=0){
                    String price = decimalFormat.format(finalDiscountPrice1 *quantity[0]);
                    holder.basketPrice.setText(price);
                    itemsP.setText(decimalFormat.format(Float.parseFloat(itemsP.getText().toString())+finalDiscountPrice1));
                    delP.setText(""+(Integer.parseInt(delP.getText().toString())+10));
                    totalP.setText(""+(Integer.parseInt(itemsP.getText().toString())+Integer.parseInt(delP.getText().toString())));
                }else {
                    String price = decimalFormat.format(basketItems.get(position).getPrice()*quantity[0]);
                    holder.basketPrice.setText(price);
                    itemsP.setText(decimalFormat.format(Float.parseFloat(itemsP.getText().toString())+basketItems.get(position).getPrice()));
                    delP.setText(""+(Integer.parseInt(delP.getText().toString())+10));
                    totalP.setText(""+(Integer.parseInt(itemsP.getText().toString())+Integer.parseInt(delP.getText().toString())));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return basketItems.size();
    }

    public static final class BasketViewHolder extends RecyclerView.ViewHolder{

        ImageView basketImage;
        TextView basketTitle, basketPrice, basketQuantity, discountPercent;
        ImageButton basketButton, leftButton, rightButton;
        CardView discountView;


        public BasketViewHolder(@NonNull View itemView) {
            super(itemView);

            basketImage = itemView.findViewById(R.id.basketImage);
            basketTitle = itemView.findViewById(R.id.basketItemTitle);
            basketPrice = itemView.findViewById(R.id.basketItemPrice);
            basketButton = itemView.findViewById(R.id.exitBasketImage);
            basketQuantity = itemView.findViewById(R.id.quantityTextView);
            leftButton = itemView.findViewById(R.id.leftBasketImage);
            rightButton = itemView.findViewById(R.id.rightBasketImage);
            discountPercent = itemView.findViewById(R.id.basketItem_discount_percent);
            discountView = itemView.findViewById(R.id.basketItem_discount_view);

        }
    }
}

