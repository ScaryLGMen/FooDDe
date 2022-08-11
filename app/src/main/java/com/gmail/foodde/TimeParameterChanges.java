package com.gmail.foodde;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeParameterChanges {

    private Context context;
    TextView timeView;
    private String dayOfWeek = "";
    private String time = "";
    private TimePicker staticTimePicker;
    private Spinner staticSpinner;

    public TimeParameterChanges(Context context, TextView timeView,
                                TimePicker staticTimePicker, Spinner staticSpinner){
        this.context = context; this.timeView = timeView; this.staticTimePicker = staticTimePicker; this.staticSpinner = staticSpinner;
    }
    public TimeParameterChanges(Context context, TextView timeView){
        this.context = context;
        this.timeView = timeView;
    }




    public void show(boolean a){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.time_parameter_changes_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        final TimePicker timePicker = (TimePicker) promptsView.findViewById(R.id.time_param_picker);
        timePicker.setIs24HourView(true);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        final Spinner spinner = (Spinner) promptsView.findViewById(R.id.time_param_spinner);
        final Button button = (Button) promptsView.findViewById(R.id.time_param_button);
        ///
        DatabaseHelper databaseHelper;
        SQLiteDatabase db;

        databaseHelper = new DatabaseHelper(context);
        databaseHelper.create_db();
        db = databaseHelper.open();

        Cursor userCursor;
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_ADDRESSES +
                " where " + DatabaseHelper.COLUMN_ADDRESS_CHOSEN + " = " + "\"true\"", null);
        if(!userCursor.moveToFirst()) {
            Toast toast = Toast.makeText(context, "Сначала укажите адрес доставки!",Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        databaseHelper = new DatabaseHelper(context);
        databaseHelper.create_db();
        db = databaseHelper.open();

        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);
        if(userCursor.moveToFirst()){
            userCursor.moveToFirst();
            String oldTime = userCursor.getString(0);
            String oldDate = userCursor.getString(1);
            System.out.println(oldTime);
            if(!oldTime.equals("сейчас")){
                int oldDayOfWeek = 0;
                Calendar calendar = Calendar.getInstance();
                int presentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                switch (oldDate){
                    case "Понедельник":
                        oldDayOfWeek = Calendar.MONDAY;
                        break;
                    case "Вторник":
                        oldDayOfWeek = Calendar.TUESDAY;
                        break;
                    case "Среда":
                        oldDayOfWeek = Calendar.WEDNESDAY;
                        break;
                    case "Четверг":
                        oldDayOfWeek = Calendar.THURSDAY;
                        break;
                    case "Пятница":
                        oldDayOfWeek = Calendar.FRIDAY;
                        break;
                    case "Суббота":
                        oldDayOfWeek = Calendar.SATURDAY;
                        break;
                    case "Воскресенье":
                        oldDayOfWeek = Calendar.SUNDAY;
                        break;
                }
                if(presentDayOfWeek-oldDayOfWeek==-1){
                    spinner.setSelection(2);
                }
                if(presentDayOfWeek-oldDayOfWeek==0){
                    spinner.setSelection(1);
                }

                String subTimeHour = oldTime.substring(0,2);
                String subTimeMinutes = oldTime.substring(3,5);
                timePicker.setHour(Integer.parseInt(subTimeHour));
                timePicker.setMinute(Integer.parseInt(subTimeMinutes));
            }
        }
        db.close();


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                String hour = "";
                String minutes = "";
                if(timePicker.getHour()<10){
                    hour = "0"+timePicker.getHour();
                }else {
                    hour = ""+timePicker.getHour();
                }
                if(timePicker.getMinute()<10){
                    minutes = "0"+timePicker.getMinute();
                }else {
                    minutes = ""+timePicker.getMinute();
                }
                time = hour+":"+minutes;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                if(i == 0){
                    timePicker.setVisibility(View.GONE);
                    timePicker.setEnabled(false);
                    dayOfWeek  = "Сейчас";
                    time = "сейчас";
                }
                if(i == 1){
                    timePicker.setVisibility(View.VISIBLE);
                    timePicker.setEnabled(true);
                    switch (day){
                        case Calendar.MONDAY:
                            dayOfWeek = "Понедельник";
                            break;
                        case Calendar.TUESDAY:
                            dayOfWeek = "Вторник";
                            break;
                        case Calendar.WEDNESDAY:
                            dayOfWeek = "Среда";
                            break;
                        case Calendar.THURSDAY:
                            dayOfWeek = "Четверг";
                            break;
                        case Calendar.FRIDAY:
                            dayOfWeek = "Пятница";
                            break;
                        case Calendar.SATURDAY:
                            dayOfWeek = "Суббота";
                            break;
                        case Calendar.SUNDAY:
                            dayOfWeek = "Воскресенье";
                            break;
                    }
                    String hour = "";
                    String minutes = "";
                    if(timePicker.getHour()<10){
                        hour = "0"+timePicker.getHour();
                    }else {
                        hour = ""+timePicker.getHour();
                    }
                    if(timePicker.getMinute()<10){
                        minutes = "0"+timePicker.getMinute();
                    }else {
                        minutes = ""+timePicker.getMinute();
                    }
                    time = hour+":"+minutes;
                }
                if(i == 2){
                    timePicker.setVisibility(View.VISIBLE);
                    timePicker.setEnabled(true);
                    switch (day){
                        case Calendar.MONDAY:
                            dayOfWeek = "Вторник";
                            break;
                        case Calendar.TUESDAY:
                            dayOfWeek = "Среда";
                            break;
                        case Calendar.WEDNESDAY:
                            dayOfWeek = "Четверг";
                            break;
                        case Calendar.THURSDAY:
                            dayOfWeek = "Пятница";
                            break;
                        case Calendar.FRIDAY:
                            dayOfWeek = "Суббота";
                            break;
                        case Calendar.SATURDAY:
                            dayOfWeek = "Воскресенье";
                            break;
                        case Calendar.SUNDAY:
                            dayOfWeek = "Понедельник";
                            break;
                    }
                    String hour = "";
                    String minutes = "";
                    if(timePicker.getHour()<10){
                        hour = "0"+timePicker.getHour();
                    }else {
                        hour = ""+timePicker.getHour();
                    }
                    if(timePicker.getMinute()<10){
                        minutes = "0"+timePicker.getMinute();
                    }else {
                        minutes = ""+timePicker.getMinute();
                    }
                    time = hour+":"+minutes;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseHelper databaseHelper;
                SQLiteDatabase db;

                databaseHelper = new DatabaseHelper(context);
                databaseHelper.create_db();
                db = databaseHelper.open();

                Cursor userCursor;
                timeView.setText("");
                //timeView.setText(userCursor.getString(2) + "; ");
                //timeView.setTextSize(10);

                userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);

                if(userCursor.moveToFirst()){
                    userCursor.moveToFirst();

                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_TIME, time);
                    values.put(DatabaseHelper.COLUMN_DATE, dayOfWeek);

                    String where = DatabaseHelper.COLUMN_TIME + "=" + '"'+userCursor.getString(0)+'"';
                    db.update(DatabaseHelper.TABLE_OF_TIME, values, where, null);
                    Toast toast = Toast.makeText(context, "Время доставки изменено",Toast.LENGTH_LONG);
                    toast.show();

                }else {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_TIME, time);
                    values.put(DatabaseHelper.COLUMN_DATE, dayOfWeek);
                    db.insert(DatabaseHelper.TABLE_OF_TIME, null, values);

                    Toast toast = Toast.makeText(context, "Время доставки записано",Toast.LENGTH_LONG);
                    toast.show();

                }

                if(dayOfWeek!="Сейчас"){
                    timeView.setText(timeView.getText()+dayOfWeek+ ", "+time);
                }else {
                    timeView.setText(timeView.getText()+dayOfWeek);
                }

                db.close();
                alertDialog.cancel();
            }
        });
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }



    public void sh0w(){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.time_parameter_changes_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        final TimePicker timePicker = (TimePicker) promptsView.findViewById(R.id.time_param_picker);
        timePicker.setIs24HourView(true);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        final Spinner spinner = (Spinner) promptsView.findViewById(R.id.time_param_spinner);
        final Button button = (Button) promptsView.findViewById(R.id.time_param_button);
        ///
        DatabaseHelper databaseHelper;
        SQLiteDatabase db;

        databaseHelper = new DatabaseHelper(context);
        databaseHelper.create_db();
        db = databaseHelper.open();

        Cursor userCursor;
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_ADDRESSES +
                " where " + DatabaseHelper.COLUMN_ADDRESS_CHOSEN + " = " + "\"true\"", null);
        if(!userCursor.moveToFirst()) {
            Toast toast = Toast.makeText(context, "Сначала укажите адрес доставки!",Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        databaseHelper = new DatabaseHelper(context);
        databaseHelper.create_db();
        db = databaseHelper.open();

        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);
        if(userCursor.moveToFirst()){
            userCursor.moveToFirst();
            String oldTime = userCursor.getString(0);
            String oldDate = userCursor.getString(1);
            System.out.println(oldTime);
            if(!oldTime.equals("сейчас")){
                int oldDayOfWeek = 0;
                Calendar calendar = Calendar.getInstance();
                int presentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                switch (oldDate){
                    case "Понедельник":
                        oldDayOfWeek = Calendar.MONDAY;
                        break;
                    case "Вторник":
                        oldDayOfWeek = Calendar.TUESDAY;
                        break;
                    case "Среда":
                        oldDayOfWeek = Calendar.WEDNESDAY;
                        break;
                    case "Четверг":
                        oldDayOfWeek = Calendar.THURSDAY;
                        break;
                    case "Пятница":
                        oldDayOfWeek = Calendar.FRIDAY;
                        break;
                    case "Суббота":
                        oldDayOfWeek = Calendar.SATURDAY;
                        break;
                    case "Воскресенье":
                        oldDayOfWeek = Calendar.SUNDAY;
                        break;
                }
                if(presentDayOfWeek-oldDayOfWeek==-1){
                    spinner.setSelection(2);
                }
                if(presentDayOfWeek-oldDayOfWeek==0){
                    spinner.setSelection(1);
                }

                String subTimeHour = oldTime.substring(0,2);
                String subTimeMinutes = oldTime.substring(3,5);
                timePicker.setHour(Integer.parseInt(subTimeHour));
                timePicker.setMinute(Integer.parseInt(subTimeMinutes));
            }
        }
        db.close();


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                String hour = "";
                String minutes = "";
                if(timePicker.getHour()<10){
                    hour = "0"+timePicker.getHour();
                }else {
                    hour = ""+timePicker.getHour();
                }
                if(timePicker.getMinute()<10){
                    minutes = "0"+timePicker.getMinute();
                }else {
                    minutes = ""+timePicker.getMinute();
                }
                time = hour+":"+minutes;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                if(i == 0){
                    timePicker.setVisibility(View.GONE);
                    timePicker.setEnabled(false);
                    dayOfWeek  = "Сейчас";
                    time = "сейчас";
                }
                if(i == 1){
                    timePicker.setVisibility(View.VISIBLE);
                    timePicker.setEnabled(true);
                    switch (day){
                        case Calendar.MONDAY:
                            dayOfWeek = "Понедельник";
                            break;
                        case Calendar.TUESDAY:
                            dayOfWeek = "Вторник";
                            break;
                        case Calendar.WEDNESDAY:
                            dayOfWeek = "Среда";
                            break;
                        case Calendar.THURSDAY:
                            dayOfWeek = "Четверг";
                            break;
                        case Calendar.FRIDAY:
                            dayOfWeek = "Пятница";
                            break;
                        case Calendar.SATURDAY:
                            dayOfWeek = "Суббота";
                            break;
                        case Calendar.SUNDAY:
                            dayOfWeek = "Воскресенье";
                            break;
                    }
                    String hour = "";
                    String minutes = "";
                    if(timePicker.getHour()<10){
                        hour = "0"+timePicker.getHour();
                    }else {
                        hour = ""+timePicker.getHour();
                    }
                    if(timePicker.getMinute()<10){
                        minutes = "0"+timePicker.getMinute();
                    }else {
                        minutes = ""+timePicker.getMinute();
                    }
                    time = hour+":"+minutes;
                }
                if(i == 2){
                    timePicker.setVisibility(View.VISIBLE);
                    timePicker.setEnabled(true);
                    switch (day){
                        case Calendar.MONDAY:
                            dayOfWeek = "Вторник";
                            break;
                        case Calendar.TUESDAY:
                            dayOfWeek = "Среда";
                            break;
                        case Calendar.WEDNESDAY:
                            dayOfWeek = "Четверг";
                            break;
                        case Calendar.THURSDAY:
                            dayOfWeek = "Пятница";
                            break;
                        case Calendar.FRIDAY:
                            dayOfWeek = "Суббота";
                            break;
                        case Calendar.SATURDAY:
                            dayOfWeek = "Воскресенье";
                            break;
                        case Calendar.SUNDAY:
                            dayOfWeek = "Понедельник";
                            break;
                    }
                    String hour = "";
                    String minutes = "";
                    if(timePicker.getHour()<10){
                        hour = "0"+timePicker.getHour();
                    }else {
                        hour = ""+timePicker.getHour();
                    }
                    if(timePicker.getMinute()<10){
                        minutes = "0"+timePicker.getMinute();
                    }else {
                        minutes = ""+timePicker.getMinute();
                    }
                    time = hour+":"+minutes;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseHelper databaseHelper;
                SQLiteDatabase db;

                databaseHelper = new DatabaseHelper(context);
                databaseHelper.create_db();
                db = databaseHelper.open();

                Cursor userCursor;
                userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_ADDRESSES +
                        " where " + DatabaseHelper.COLUMN_ADDRESS_CHOSEN + " = " + "\"true\"", null);
                if(userCursor.moveToFirst()) {
                    userCursor.moveToFirst();
                    timeView.setText(userCursor.getString(2) + "; ");
                    timeView.setTextSize(10);

                    userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);

                    if(userCursor.moveToFirst()){
                        userCursor.moveToFirst();

                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.COLUMN_TIME, time);
                        values.put(DatabaseHelper.COLUMN_DATE, dayOfWeek);

                        String where = DatabaseHelper.COLUMN_TIME + "=" + '"'+userCursor.getString(0)+'"';
                        db.update(DatabaseHelper.TABLE_OF_TIME, values, where, null);
                        Toast toast = Toast.makeText(context, "Время доставки изменено",Toast.LENGTH_LONG);
                        toast.show();

                    }else {
                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.COLUMN_TIME, time);
                        values.put(DatabaseHelper.COLUMN_DATE, dayOfWeek);
                        db.insert(DatabaseHelper.TABLE_OF_TIME, null, values);

                        Toast toast = Toast.makeText(context, "Время доставки записано",Toast.LENGTH_LONG);
                        toast.show();

                    }

                    if(dayOfWeek!="Сейчас"){
                        timeView.setText(timeView.getText()+dayOfWeek+ ", "+time);
                    }else {
                        timeView.setText(timeView.getText()+dayOfWeek);
                    }

                }else {
                    Toast toast = Toast.makeText(context, "Сначала укажите адрес доставки!",Toast.LENGTH_LONG);
                    toast.show();
                }

                db.close();
                alertDialog.cancel();
            }
        });
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    public void show(){

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.time_parameter_changes_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        final TimePicker timePicker = (TimePicker) promptsView.findViewById(R.id.time_param_picker);
        timePicker.setIs24HourView(true);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        final Spinner spinner = (Spinner) promptsView.findViewById(R.id.time_param_spinner);
        final Button button = (Button) promptsView.findViewById(R.id.time_param_button);
        ///
        DatabaseHelper databaseHelper;
        SQLiteDatabase db;

        databaseHelper = new DatabaseHelper(context);
        databaseHelper.create_db();
        db = databaseHelper.open();

        Cursor userCursor;
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);
        if(userCursor.moveToFirst()){
            userCursor.moveToFirst();
            String oldTime = userCursor.getString(0);
            String oldDate = userCursor.getString(1);
            System.out.println(oldTime);
            if(!oldTime.equals("сейчас")){
                int oldDayOfWeek = 0;
                Calendar calendar = Calendar.getInstance();
                int presentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                switch (oldDate){
                    case "Понедельник":
                        oldDayOfWeek = Calendar.MONDAY;
                        break;
                    case "Вторник":
                        oldDayOfWeek = Calendar.TUESDAY;
                        break;
                    case "Среда":
                        oldDayOfWeek = Calendar.WEDNESDAY;
                        break;
                    case "Четверг":
                        oldDayOfWeek = Calendar.THURSDAY;
                        break;
                    case "Пятница":
                        oldDayOfWeek = Calendar.FRIDAY;
                        break;
                    case "Суббота":
                        oldDayOfWeek = Calendar.SATURDAY;
                        break;
                    case "Воскресенье":
                        oldDayOfWeek = Calendar.SUNDAY;
                        break;
                }
                if(presentDayOfWeek-oldDayOfWeek==-1){
                    spinner.setSelection(2);

                }
                if(presentDayOfWeek-oldDayOfWeek==0){
                    spinner.setSelection(1);

                }

                String subTimeHour = oldTime.substring(0,2);
                String subTimeMinutes = oldTime.substring(3,5);
                timePicker.setHour(Integer.parseInt(subTimeHour));
                timePicker.setMinute(Integer.parseInt(subTimeMinutes));
            }
        }
        db.close();

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                String hour = "";
                String minutes = "";
                if(timePicker.getHour()<10){
                    hour = "0"+timePicker.getHour();
                }else {
                    hour = ""+timePicker.getHour();
                }
                if(timePicker.getMinute()<10){
                    minutes = "0"+timePicker.getMinute();
                }else {
                    minutes = ""+timePicker.getMinute();
                }
                time = hour+":"+minutes;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                if(i == 0){
                    timePicker.setVisibility(View.GONE);
                    timePicker.setEnabled(false);
                    switch (day){
                        case Calendar.MONDAY:
                            dayOfWeek = "Понедельник";
                            break;
                        case Calendar.TUESDAY:
                            dayOfWeek = "Вторник";
                            break;
                        case Calendar.WEDNESDAY:
                            dayOfWeek = "Среда";
                            break;
                        case Calendar.THURSDAY:
                            dayOfWeek = "Четверг";
                            break;
                        case Calendar.FRIDAY:
                            dayOfWeek = "Пятница";
                            break;
                        case Calendar.SATURDAY:
                            dayOfWeek = "Суббота";
                            break;
                        case Calendar.SUNDAY:
                            dayOfWeek = "Воскресенье";
                            break;
                    }
                    time = "сейчас";
                }
                if(i == 1){
                    timePicker.setVisibility(View.VISIBLE);
                    timePicker.setEnabled(true);
                    switch (day){
                        case Calendar.MONDAY:
                            dayOfWeek = "Понедельник";
                            break;
                        case Calendar.TUESDAY:
                            dayOfWeek = "Вторник";
                            break;
                        case Calendar.WEDNESDAY:
                            dayOfWeek = "Среда";
                            break;
                        case Calendar.THURSDAY:
                            dayOfWeek = "Четверг";
                            break;
                        case Calendar.FRIDAY:
                            dayOfWeek = "Пятница";
                            break;
                        case Calendar.SATURDAY:
                            dayOfWeek = "Суббота";
                            break;
                        case Calendar.SUNDAY:
                            dayOfWeek = "Воскресенье";
                            break;
                    }
                    String hour = "";
                    String minutes = "";
                    if(timePicker.getHour()<10){
                        hour = "0"+timePicker.getHour();
                    }else {
                        hour = ""+timePicker.getHour();
                    }
                    if(timePicker.getMinute()<10){
                        minutes = "0"+timePicker.getMinute();
                    }else {
                        minutes = ""+timePicker.getMinute();
                    }
                    time = hour+":"+minutes;
                }
                if(i == 2){
                    timePicker.setVisibility(View.VISIBLE);
                    timePicker.setEnabled(true);
                    switch (day){
                        case Calendar.MONDAY:
                            dayOfWeek = "Вторник";
                            break;
                        case Calendar.TUESDAY:
                            dayOfWeek = "Среда";
                            break;
                        case Calendar.WEDNESDAY:
                            dayOfWeek = "Четверг";
                            break;
                        case Calendar.THURSDAY:
                            dayOfWeek = "Пятница";
                            break;
                        case Calendar.FRIDAY:
                            dayOfWeek = "Суббота";
                            break;
                        case Calendar.SATURDAY:
                            dayOfWeek = "Воскресенье";
                            break;
                        case Calendar.SUNDAY:
                            dayOfWeek = "Понедельник";
                            break;
                    }
                    String hour = "";
                    String minutes = "";
                    if(timePicker.getHour()<10){
                        hour = "0"+timePicker.getHour();
                    }else {
                        hour = ""+timePicker.getHour();
                    }
                    if(timePicker.getMinute()<10){
                        minutes = "0"+timePicker.getMinute();
                    }else {
                        minutes = ""+timePicker.getMinute();
                    }

                    time = hour+":"+minutes;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseHelper databaseHelper;
                SQLiteDatabase db;

                databaseHelper = new DatabaseHelper(context);
                databaseHelper.create_db();
                db = databaseHelper.open();

                Cursor userCursor;
                userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_TIME , null);

                if(userCursor.moveToFirst()){
                    ContentValues values = new ContentValues();

                    values.put(DatabaseHelper.COLUMN_TIME, time);
                    values.put(DatabaseHelper.COLUMN_DATE, dayOfWeek);
                    userCursor.moveToFirst();
                    String where = DatabaseHelper.COLUMN_TIME + "=" + '"'+userCursor.getString(0)+'"';
                    System.out.println(""+userCursor.getString(0));
                    db.update(DatabaseHelper.TABLE_OF_TIME, values, where, null);
                    Toast toast = Toast.makeText(context, "Время доставки изменено",Toast.LENGTH_LONG);
                    toast.show();

                    timeView.setText(dayOfWeek+ ", "+time);
                }else {

                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_TIME, time);
                    values.put(DatabaseHelper.COLUMN_DATE, dayOfWeek);
                    db.insert(DatabaseHelper.TABLE_OF_TIME, null, values);

                    Toast toast = Toast.makeText(context, "Время доставки записано",Toast.LENGTH_LONG);
                    toast.show();

                    timeView.setText(dayOfWeek+ ", "+time);
                }
                db.close();

                staticTimePicker.setHour(timePicker.getHour());
                staticTimePicker.setMinute(timePicker.getMinute());
                staticSpinner.setSelection(spinner.getSelectedItemPosition());

                alertDialog.cancel();
            }
        });

        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

}
