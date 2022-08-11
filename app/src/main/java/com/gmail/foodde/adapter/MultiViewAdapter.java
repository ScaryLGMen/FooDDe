package com.gmail.foodde.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gmail.foodde.GetCategories;
import com.gmail.foodde.GetDishes;
import com.gmail.foodde.GetNews;
import com.gmail.foodde.R;
import com.gmail.foodde.model.Category;
import com.gmail.foodde.model.Item;
import com.gmail.foodde.model.MultiViewItem;

import java.util.ArrayList;
import java.util.List;

public class MultiViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<MultiViewItem> items;

    private RecyclerView categoryRecyclerView, dishRecyclerView, promRecyclerView, promCirclesRecyclerView, catDishRecyclerView;

    public MultiViewAdapter(Context context, ArrayList<MultiViewItem> items){
     this.items = items;
     this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:{
                View categoryItems = LayoutInflater.from(context).inflate(R.layout.main_category_element, parent, false);
                return new ViewHolder0(categoryItems);
            }
            case 1:{
                View promItems = LayoutInflater.from(context).inflate(R.layout.main_prom_element, parent, false);
                return new ViewHolder1(promItems);
            }
            case 2:{
                View dishItems = LayoutInflater.from(context).inflate(R.layout.main_dish_element, parent, false);
                return new ViewHolder2(dishItems);
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        List<Item> itemList = new ArrayList<>();
        List<Category> categoryList = new ArrayList<>();


        switch (holder.getItemViewType()) {
            case 0:
            {
                ViewHolder0 viewHolder0 = (ViewHolder0) holder;
                categoryRecyclerView = viewHolder0.recyclerViewCategory;
                break;
            }
            case 1:{
                ViewHolder1 viewHolder1 = (ViewHolder1) holder;
                promRecyclerView = viewHolder1.recyclerViewProm;
                promCirclesRecyclerView = viewHolder1.recyclerViewCircles;
                break;
            }
            case 2:
            {
                ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                dishRecyclerView = viewHolder2.recyclerViewDish;
                break;
            }
            case -1:{
                new GetNews(context, promRecyclerView, promCirclesRecyclerView).execute();
                new GetCategories(context, categoryList, categoryRecyclerView, dishRecyclerView).execute();
                new GetDishes(context, itemList, dishRecyclerView).execute();
                break;
            }

        }
    }


    class ViewHolder0 extends RecyclerView.ViewHolder {
        private RecyclerView recyclerViewCategory;
        public ViewHolder0(View itemView){
            super(itemView);
            recyclerViewCategory = itemView.findViewById(R.id.main_category_element_recyclerview);
        }
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        RecyclerView recyclerViewProm, recyclerViewCircles;
        public ViewHolder1(View itemView) {
            super(itemView);
            recyclerViewCircles = itemView.findViewById(R.id.main_prom_circles_recyclerView);
            recyclerViewProm = itemView.findViewById(R.id.main_prom_recyclerView);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        private RecyclerView recyclerViewDish;
        public ViewHolder2(View itemView) {
            super(itemView);
            recyclerViewDish = itemView.findViewById(R.id.main_dish_element_recyclerview);
        }
    }

    class ViewHolder01 extends RecyclerView.ViewHolder {
        public ViewHolder01(View itemView) {
            super(itemView);
        }
    }



    @Override
    public int getItemCount() {
        return items.size();
    }
}
