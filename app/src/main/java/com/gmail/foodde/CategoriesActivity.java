package com.gmail.foodde;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.gmail.foodde.model.Category;
import com.gmail.foodde.model.Item;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {
    private RecyclerView category, items;
    List<Item> itemList = new ArrayList<>();
    List<Category> categoryList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        category = findViewById(R.id.category_element_category);
        items = findViewById(R.id.category_element_items);

        new GetVerticalCategories(CategoriesActivity.this, categoryList, category, items).execute();
        //new GetDishes(CategoriesActivity.this, itemList, items).execute();
        new GetNewDishes(CategoriesActivity.this, itemList, items, 1).execute();

    }
}