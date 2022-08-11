package com.gmail.foodde.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.foodde.Basket;
import com.gmail.foodde.DatabaseHelper;
import com.gmail.foodde.Discount;
import com.gmail.foodde.MainActivity;
import com.gmail.foodde.MapsActivity;
import com.gmail.foodde.PushOrder;
import com.gmail.foodde.R;
import com.gmail.foodde.TimeParameterChanges;
import com.gmail.foodde.UserRegistrationActivity;
import com.gmail.foodde.model.BasketItem;
import com.gmail.foodde.model.DiscountItem;
import com.gmail.foodde.model.Item;
import com.gmail.foodde.model.MultiViewItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MultiViewOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<MultiViewItem> items;

    private RecyclerView recyclerViewItems, mainRecyclerView;
    private TextView itemsP, delP, totalP;
    private LinearLayout background;
    private TextView address;
    private Activity activity;
    private EditText pFrontDoorET, pFloorET, pApartmentET, pCommentET, pCommentDish;
    private String token;
    //private Button conf;

    public MultiViewOrderAdapter(Context context, ArrayList<MultiViewItem> items, RecyclerView mainRecyclerView, LinearLayout background, Activity activity){
        this.items = items;
        this.context = context;
        this.mainRecyclerView = mainRecyclerView;
        this.background = background;
        this.activity = activity;

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("Main", "Fetching FCM registration token failed", task.getException());
                    token = "";
                    return;
                }

                // Get new FCM registration token
                token = task.getResult();
                System.out.println("TOKEN: "+token);

            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:{
                View categoryItems = LayoutInflater.from(context).inflate(R.layout.order_address_element, parent, false);
                return new ViewHolder0(categoryItems);
            }
            case 1:{
                View promItems = LayoutInflater.from(context).inflate(R.layout.order_items_element, parent, false);
                return new ViewHolder1(promItems);
            }
            case 2:{
                View dishItems = LayoutInflater.from(context).inflate(R.layout.order_price_element, parent, false);
                return new ViewHolder2(dishItems);
            }
            case -1:{
                View start = LayoutInflater.from(context).inflate(R.layout.order_start_element, parent, false);
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
        switch (holder.getItemViewType()) {
            case 0:
            {
                ViewHolder0 viewHolder0 = (ViewHolder0) holder;
                address = viewHolder0.addressTV;

                DatabaseHelper databaseHelper;
                SQLiteDatabase db;
                databaseHelper = new DatabaseHelper(context);
                databaseHelper.create_db();
                db = databaseHelper.open();

                Cursor userCursor;
                userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_ADDRESSES +
                        " where " + DatabaseHelper.COLUMN_ADDRESS_CHOSEN + " = " + "\"true\"", null);
                if(userCursor.moveToFirst()){
                    userCursor.moveToFirst();
                    viewHolder0.addressTV.setText(userCursor.getString(2));

                    userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);
                    if(userCursor.moveToFirst()){
                        userCursor.moveToFirst();
                        if(!userCursor.getString(0).equals("сейчас")){
                            viewHolder0.timeTV.setText(userCursor.getString(1)+", "+userCursor.getString(0));
                        }
                        else {
                            viewHolder0.timeTV.setText("Сейчас");
                        }
                    }
                }
                db.close();

                viewHolder0.timeLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new TimeParameterChanges(context, viewHolder0.timeTV).show(false);
                    }
                });

                viewHolder0.addressLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, MapsActivity.class);
                        intent.putExtra("category", 0);
                        context.startActivity(intent);
                    }
                });

                pFrontDoorET = viewHolder0.frontDoorET;
                pFloorET = viewHolder0.floorET;
                pApartmentET = viewHolder0.apartmentET;
                pCommentET = viewHolder0.commentET;


                break;
            }
            case 1:{
                ViewHolder1 viewHolder1 = (ViewHolder1) holder;
                recyclerViewItems = viewHolder1.recyclerViewItems;
                pCommentDish = viewHolder1.commentDish;
                //promRecyclerView = viewHolder1.recyclerViewProm;
                //promCirclesRecyclerView = viewHolder1.recyclerViewCircles;
                break;
            }

            case 2:
            {
                ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                itemsP = viewHolder2.priceOfItems;
                delP = viewHolder2.priceOfDelivery;
                totalP = viewHolder2.priceOfTotal;

                DatabaseHelper databaseHelper;
                SQLiteDatabase db;
                databaseHelper = new DatabaseHelper(context);
                databaseHelper.create_db();
                db = databaseHelper.open();
                Cursor userCursor;

                userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_USER, null);
                if(!userCursor.moveToFirst()){
                    viewHolder2.mail.setVisibility(View.VISIBLE);
                    viewHolder2.emailContext.setVisibility(View.VISIBLE);
                }

                viewHolder2.phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(viewHolder2.phone.isChecked()){
                            viewHolder2.phoneNumber.setVisibility(View.VISIBLE);
                        }else{
                            viewHolder2.phoneNumber.setVisibility(View.GONE);
                        }
                    }
                });

                viewHolder2.checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    View promptsView = layoutInflater.inflate(R.layout.clipboard_layout, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setView(promptsView);
                    final AlertDialog alertDialog = alertDialogBuilder.create();

                    CardView reg_info = promptsView.findViewById(R.id.clipboard_registration_info);
                    Button reg = promptsView.findViewById(R.id.clipboard_registration);
                    TextView phone_info = promptsView.findViewById(R.id.clipboard_phone);

                    reg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, UserRegistrationActivity.class);
                            activity.startActivity(intent);
                            activity.finish();

                        }
                    });

                    DatabaseHelper databaseHelper;
                    SQLiteDatabase db;
                    databaseHelper = new DatabaseHelper(context);
                    databaseHelper.create_db();
                    db = databaseHelper.open();
                    Cursor userCursor;

                    userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_ADDRESSES +
                            " where " + DatabaseHelper.COLUMN_ADDRESS_CHOSEN + " = " + "\"true\"", null);
                    if(userCursor.moveToFirst()){

                        String user_mail, user_phone, address, address_add_info,
                                delivery_start_time, dishes, dishes_comment, status;
                        Double address_latitude, address_longitude;
                        float price;

                        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_USER, null);
                        if(userCursor.moveToFirst()){
                            userCursor.moveToFirst();
                            user_mail = userCursor.getString(2);
                        }else {
                            reg_info.setVisibility(View.VISIBLE);
                            if(isEmail(viewHolder2.mail.getText().toString())){
                                user_mail = viewHolder2.mail.getText().toString();
                            }else {
                                String msg = "Вы ввели некорректный адрес электронной почты";
                                if(viewHolder2.mail.getText().toString().equals("")){
                                    msg = "Укажите адрес электронной почты";
                                }
                                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                                builder.setCancelable(true)
                                        .setMessage(msg)
                                        .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                                dialog.cancel();
                                                dialog.dismiss();
                                            }
                                        });
                                final android.app.AlertDialog alert = builder.create();
                                alert.show();
                                return;
                            }

                        }
                        if(viewHolder2.phone.isChecked()){
                            phone_info.setVisibility(View.VISIBLE);
                            user_phone = viewHolder2.phoneNumber.getText().toString();
                        }else {
                            user_phone = "";
                        }

                        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_ADDRESSES +
                                " where " + DatabaseHelper.COLUMN_ADDRESS_CHOSEN + " = " + "\"true\"", null);
                        userCursor.moveToFirst();
                        address = userCursor.getString(2);
                        address_latitude = userCursor.getDouble(0);
                        address_longitude = userCursor.getDouble(1);

                        address_add_info = pFrontDoorET.getText().toString()+", "
                                +pFloorET.getText().toString()+", "+pApartmentET.getText().toString()+
                                ", "+pCommentET.getText().toString();

                        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);
                        userCursor.moveToFirst();

                        System.out.println(userCursor.getString(0).toString());

                        if(!userCursor.getString(0).toString().equals("сейчас")){
                            System.out.println("Попало");
                            Calendar calendar = Calendar.getInstance();
                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;


                            if(dayOfWeek==0){
                                dayOfWeek=7;
                            }

                            System.out.println(dayOfWeek);

                            int dayOfOrder = 0;
                            switch (userCursor.getString(1)){
                                case "Понедельник":
                                    dayOfOrder = 1;
                                    break;
                                case "Вторник":
                                    dayOfOrder = 2;
                                    break;
                                case "Среда":
                                    dayOfOrder = 3;
                                    break;
                                case "Четверг":
                                    dayOfOrder = 4;
                                    break;
                                case "Пятница":
                                    dayOfOrder = 5;
                                    break;
                                case "Суббота":
                                    dayOfOrder = 6;
                                    break;
                                case "Воскресенье":
                                    dayOfOrder = 7;
                                    break;
                            }
                            String date = "";

                            System.out.println(dayOfOrder-dayOfWeek);


                            if(dayOfOrder-dayOfWeek==0){
                                Date currentDate = new Date();
                                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                                date = dateFormat.format(currentDate);
                            }else {
                                Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.DATE, +1);
                                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                                date = format.format(cal.getTime());
                            }

                            delivery_start_time = date+", "+userCursor.getString(0);
                        }
                        else {
                            //System.out.println("Не Попало");
                            String date = "";
                            Date currentDate = new Date();
                            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                            date = dateFormat.format(currentDate);
                            delivery_start_time = date;
                        }

                        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
                        userCursor.moveToFirst();
                        dishes = "";
                        for(int i = 0; i < userCursor.getCount(); i++){
                            dishes += userCursor.getString(2)+" x "+ userCursor.getInt(1)+"|";
                            userCursor.moveToNext();
                        }
                        dishes_comment = pCommentDish.getText().toString();
                        status = "На рассмотрении";

                        //String temp = .substring(0, totalP.getText().toString().length()-2);
                        price = Float.parseFloat(totalP.getText().toString());

                        new PushOrder(context, user_mail, user_phone, address, address_latitude,
                                        address_longitude, address_add_info, delivery_start_time,
                                        dishes, dishes_comment, price, status, token).execute();




                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(true);
                        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                activity.finish();
                            }
                        });
                        //alertDialog.getWindow().getDecorView().getBackground().setAlpha(2);
                        //alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.background_dark);

                    }else {
                        final Animation animAlpha = AnimationUtils.loadAnimation(context, R.anim.alpha);
                        address.startAnimation(animAlpha);
                        Toast.makeText(context, address.getText(), Toast.LENGTH_LONG).show();
                        mainRecyclerView.smoothScrollToPosition(0);
                    }
                    db.close();
                }
            });
                break;
            }
            case -1:{
                new Basket(recyclerViewItems, itemsP, delP, totalP, mainRecyclerView, background).show();
                break;
            }

        }
    }


    class ViewHolder0 extends RecyclerView.ViewHolder {
        LinearLayout addressLL, timeLL;
        TextView addressTV, timeTV;
        EditText frontDoorET, floorET, apartmentET, commentET;
        public ViewHolder0(View itemView){
            super(itemView);
            addressLL = itemView.findViewById(R.id.order_address_linear_layout);
            timeLL = itemView.findViewById(R.id.order_address_time_linear_layout);
            addressTV = itemView.findViewById(R.id.order_address_text_view);
            timeTV = itemView.findViewById(R.id.order_address_time_text_view);
            frontDoorET = itemView.findViewById(R.id.order_address_edit_text_front_door);
            floorET = itemView.findViewById(R.id.order_address_edit_text_floor);
            apartmentET = itemView.findViewById(R.id.order_address_edit_text_apartment);
            commentET = itemView.findViewById(R.id.order_address_edit_text_comment);
        }
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        RecyclerView recyclerViewItems;
        EditText commentDish;
        public ViewHolder1(View itemView) {
            super(itemView);
            recyclerViewItems = itemView.findViewById(R.id.order_items_recycler_view);
            commentDish = itemView.findViewById(R.id.order_items_comment);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView priceOfItems, priceOfDelivery, priceOfTotal, emailContext;
        Button checkout;
        EditText mail, phoneNumber;
        CheckBox phone;
        public ViewHolder2(View itemView) {
            super(itemView);
            priceOfItems = itemView.findViewById(R.id.order_price_items);
            priceOfDelivery = itemView.findViewById(R.id.order_price_delivery);
            priceOfTotal = itemView.findViewById(R.id.order_price_total);
            checkout = itemView.findViewById(R.id.order_price_checkout);
            mail = itemView.findViewById(R.id.order_price_mail_address);
            phoneNumber = itemView.findViewById(R.id.order_price_phone_number);
            phone = itemView.findViewById(R.id.order_price_phone);
            emailContext = itemView.findViewById(R.id.order_price_mail_context);
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

    private boolean isEmail(String em){
        if(em.contains("@")){
            int pos = em.indexOf("@");
            String domainName = em.substring(pos, em.length());
            if(domainName.contains(".")){
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }
}
