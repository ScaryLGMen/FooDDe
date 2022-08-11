package com.gmail.foodde.adapter;

import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.foodde.GetImage;
import com.gmail.foodde.R;
import com.gmail.foodde.model.PromItem;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PromAdapter extends RecyclerView.Adapter<PromAdapter.PromViewHolder> {
    private Context context;
    private List<PromItem> items;
    private RecyclerView circles, recyclerView;
    private int SelectedItem , MaxItems;

    public PromAdapter(Context context, List<PromItem> items, RecyclerView recyclerView ,RecyclerView circles) {
        this.context = context;
        this.items = items;
        this.circles = circles;
        this.recyclerView = recyclerView;
        this.MaxItems = items.size();
        this.SelectedItem = 1;
    }

    @NonNull
    @Override
    public PromViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View promItems = LayoutInflater.from(context).inflate(R.layout.prom_item, parent, false);
        return  new PromAdapter.PromViewHolder(promItems);
    }

    @Override
    public void onBindViewHolder(@NonNull PromViewHolder holder, int position) {
        new GetImage(holder.background, items.get(position).getLink(), items.get(position).getLink()).execute();
        holder.background.setImageResource(R.drawable.restaurant);
        holder.title.setText(items.get(position).getText());

        String[] subStr = items.get(position).getExpiratoin_date().split(" ");
        switch (subStr[1]){
            case "Январь":{
                subStr[1] = String.valueOf(0);
                break;
            }
            case "Февраль":{
                subStr[1] = String.valueOf(1);
                break;
            }
            case "Март":{
                subStr[1] = String.valueOf(2);
                break;
            }
            case "Апрель":{
                subStr[1] = String.valueOf(3);
                break;
            }
            case "Май":{
                subStr[1] = String.valueOf(4);
                break;
            }
            case "Июнь":{
                subStr[1] = String.valueOf(5);
                break;
            }
            case "Июль":{
                subStr[1] = String.valueOf(6);
                break;
            }
            case "Август":{
                subStr[1] = String.valueOf(7);
                break;
            }
            case "Сентябрь":{
                subStr[1] = String.valueOf(8);
                break;
            }
            case "Октябрь":{
                subStr[1] = String.valueOf(9);
                break;
            }
            case "Ноябрь":{
                subStr[1] = String.valueOf(10);
                break;
            }
            case "Декабрь":{
                subStr[1] = String.valueOf(11);
                break;
            }
        }
        Calendar date = Calendar.getInstance();
        date.set(Calendar.getInstance().get(Calendar.YEAR), Integer.parseInt(subStr[1]), Integer.parseInt(subStr[0]));
        Calendar now = Calendar.getInstance();
        long diffMillis = Math.abs(now.getTimeInMillis() - date.getTimeInMillis());
        long differenceInDays = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);
        holder.time.setText(""+(differenceInDays+1));

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptsView = layoutInflater.inflate(R.layout.prom_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);

                //создание/редактирование
                final AlertDialog alertDialog = alertDialogBuilder.create();
                final TextView title = (TextView) promptsView.findViewById(R.id.prom_dialog_title);
                final TextView body = (TextView) promptsView.findViewById(R.id.prom_dialog_body);

                title.setText(items.get(position).getText());
                body.setText(items.get(position).getDescription());

                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static final class PromViewHolder extends RecyclerView.ViewHolder{
        CardView item;
        ImageView background;
        TextView title, time;
        public PromViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.prom_item);
            background = itemView.findViewById(R.id.prom_item_background);
            title = itemView.findViewById(R.id.prom_item_title);
            time = itemView.findViewById(R.id.prom_item_time);
        }
    }
}
