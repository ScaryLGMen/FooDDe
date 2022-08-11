package com.gmail.foodde.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.foodde.Discount;
import com.gmail.foodde.GetSimilarDishes;
import com.gmail.foodde.R;
import com.gmail.foodde.model.DiscountItem;
import com.gmail.foodde.model.MultiViewItem;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MultiViewItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

private Context context;
private ArrayList<MultiViewItem> items;
private RecyclerView similar;
private Bitmap bitmap;

public MultiViewItemAdapter(Context context, ArrayList<MultiViewItem> items, Bitmap bitmap){
        this.items = items;
        this.context = context;
        this.bitmap = bitmap;
        }

@NonNull
@Override
public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
        case 0:{
        View DishItem = LayoutInflater.from(context).inflate(R.layout.item_dish_element, parent, false);
        return new ViewHolder0(DishItem);
        }
        case 1:{
        View promItems = LayoutInflater.from(context).inflate(R.layout.item_similar_element, parent, false);
        return new ViewHolder1(promItems);
        }
        case -1:{
        View start = LayoutInflater.from(context).inflate(R.layout.main_start_element, parent, false);
        return new ViewHolder01(start);
        }
default: return null;
        }
        }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

@Override
public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
        case 0:
        {
        ViewHolder0 viewHolder0 = (ViewHolder0) holder;
        viewHolder0.title.setText(items.get(position).getItem().getName());

        viewHolder0.image.setImageBitmap(bitmap);

        DiscountItem discountItem = new Discount(items.get(position).getItem(), context).isExists("Товар");
        if(discountItem.isExists()){
            viewHolder0.promText.setVisibility(View.VISIBLE);
            viewHolder0.promText.setText(discountItem.getPromText());
            viewHolder0.discount.setVisibility(View.VISIBLE);
            viewHolder0.percents.setText("-"+discountItem.getPercent()+"%");
            viewHolder0.price1.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder0.price2.setVisibility(View.VISIBLE);
            DecimalFormat decimalFormat = new DecimalFormat("#");
            String priceDiscount = decimalFormat.format(items.get(position).getItemPrice()-(items.get(position).getItemPrice()/100)*discountItem.getPercent());
            viewHolder0.price2.setText(priceDiscount+"₴");
        }

        DecimalFormat decimalFormat = new DecimalFormat("#");
        String price = decimalFormat.format(items.get(position).getItemPrice());
        viewHolder0.price1.setText(price+"₴");
        viewHolder0.grams.setText(items.get(position).getItemGrams()+"г");
        viewHolder0.description.setText(items.get(position).getItemDescription());

        break;
        }
        case 1:{
        ViewHolder1 viewHolder1 = (ViewHolder1) holder;
        similar = viewHolder1.similarRecyclerView;
        break;
        }
        case -1:{
        new GetSimilarDishes(context, similar, items.get(0).getItemCategory(), items.get(0).getItemID()).execute();
        break;
        }

        }
        }

class ViewHolder0 extends RecyclerView.ViewHolder {
    ImageView image;
    TextView title, price1, price2, grams, promText, description, percents;
    CardView discount;
    //EditText comment;
    public ViewHolder0(View itemView){
        super(itemView);
        image = itemView.findViewById(R.id.item_dish_element_image);
        title = itemView.findViewById(R.id.item_dish_element_title);
        price1 = itemView.findViewById(R.id.item_dish_element_price1);
        price2 = itemView.findViewById(R.id.item_dish_element_price2);
        grams = itemView.findViewById(R.id.item_dish_element_grams);
        promText = itemView.findViewById(R.id.item_dish_element_prom_text);
        description = itemView.findViewById(R.id.item_dish_element_description);
        percents = itemView.findViewById(R.id.item_dish_element_discount_percent);
        discount = itemView.findViewById(R.id.item_dish_element_discount_card_view);
        //comment = itemView.findViewById(R.id.item_dish_element_comment);

    }
}

class ViewHolder1 extends RecyclerView.ViewHolder {
    RecyclerView similarRecyclerView;
    public ViewHolder1(View itemView) {
        super(itemView);
       similarRecyclerView = itemView.findViewById(R.id.item_similar_element_recyclerview);
    }
}

class ViewHolder01 extends RecyclerView.ViewHolder {
    public ViewHolder01(View itemView) {
        super(itemView);
    }
}

}