package com.gmail.foodde.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.foodde.GetImage;
import com.gmail.foodde.GetNewDishes;
import com.gmail.foodde.R;
//import com.gmail.foodde.databinding.FragmentHomeBinding;
import com.gmail.foodde.model.Category;
import com.gmail.foodde.model.Item;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    List<Category> categories;
    //FragmentHomeBinding binding;
    RecyclerView itemsView;

    public CategoryAdapter(Context context, List<Category> categories, RecyclerView itemsView) {
        this.context = context;
        this.categories = categories;
        this.itemsView = itemsView;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View categoryItems = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return  new CategoryAdapter.CategoryViewHolder(categoryItems);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        new GetImage(holder.categoryImage, categories.get(position).getLink(), categories.get(position).getLink()).execute();
        holder.categoryTitle.setText(categories.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    public static final class CategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView categoryImage;
        TextView categoryTitle;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
        }
    }
}
