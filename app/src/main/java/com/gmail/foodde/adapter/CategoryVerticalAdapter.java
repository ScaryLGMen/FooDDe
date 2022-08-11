package com.gmail.foodde.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.foodde.GetImage;
import com.gmail.foodde.GetNewDishes;
import com.gmail.foodde.R;
//import com.gmail.foodde.databinding.FragmentHomeBinding;
import com.gmail.foodde.model.Category;
import com.gmail.foodde.model.Item;

import java.util.ArrayList;
import java.util.List;

public class CategoryVerticalAdapter extends RecyclerView.Adapter<CategoryVerticalAdapter.CategoryVerticalViewHolder> {

    Context context;
    List<Category> categories;
    List<CardView> cardViews = new ArrayList<>();
    //FragmentHomeBinding binding;
    RecyclerView itemsView;

    public CategoryVerticalAdapter(Context context, List<Category> categories, RecyclerView itemsView) {
        this.context = context;
        this.categories = categories;
        this.itemsView = itemsView;
    }

    @NonNull
    @Override
    public CategoryVerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View categoryItems = LayoutInflater.from(context).inflate(R.layout.category_vertical_item, parent, false);
        return  new CategoryVerticalAdapter.CategoryVerticalViewHolder(categoryItems);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVerticalViewHolder holder, int position) {
        cardViews.add(holder.cardView);
        if(categories.get(position).getID()==1){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#D0D0D0"));
        }
        new GetImage(holder.categoryImage, categories.get(position).getLink(), categories.get(position).getLink()).execute();
        holder.categoryTitle.setText(categories.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < cardViews.size(); i++){
                    cardViews.get(i).setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
                }
                holder.cardView.setCardBackgroundColor(Color.parseColor("#D0D0D0"));

                try {
                    List<Item> itemList = new ArrayList<>();
                    new GetNewDishes(context, itemList, itemsView, categories.get(position).getID()).execute();

                }
                catch (Exception e){
                    Toast toast = Toast.makeText(context, "Упс, что-то пошло не так.." ,Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static final class CategoryVerticalViewHolder extends RecyclerView.ViewHolder{
        ImageView categoryImage;
        TextView categoryTitle;
        CardView cardView;
        public CategoryVerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            cardView = itemView.findViewById(R.id.categoryCardView);
        }
    }
}
