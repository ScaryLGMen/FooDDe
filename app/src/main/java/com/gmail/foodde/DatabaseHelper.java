package com.gmail.foodde;

import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH; // полный путь к базе данных
    private static String DB_NAME = "basket.db";
    private static final int SCHEMA = 1; // версия базы данных
    public static final String TABLE = "basket"; // название таблицы в бд
    public static final String TABLE_OF_ADDRESSES = "address";
    public static final String TABLE_OF_TIME = "time";
    public static final String TABLE_OF_SAMPLES = "samples";
    public static final String TABLE_OF_IMAGES = "images";
    public static final String TABLE_OF_USER = "user";
    public static final String TABLE_OF_DISCOUNT = "discount";

    // названия столбцов
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_GRAMS = "grams";
    public static final String COLUMN_DOPING = "doping";
    public static final String COLUMN_COMMENT = "comment";

    public static final String COLUMN_ADDRESS_LATITUDE = "latitude";
    public static final String COLUMN_ADDRESS_LONGITUDE = "longitude";
    public static final String COLUMN_ADDRESS_LINE = "addressLine";
    public static final String COLUMN_ADDRESS_CHOSEN = "chosen";

    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATE = "date";

    public static final String COLUMN_SAMPLE_ID = "id";
    public static final String COLUMN_SAMPLE_PICTURE = "picture";
    public static final String COLUMN_SAMPLE_TEXT = "text";
    public static final String COLUMN_SAMPLE_LATITUDE = "latitude";
    public static final String COLUMN_SAMPLE_LONGITUDE = "longitude";

    public static final String COLUMN_IMAGE_KEY = "key";
    public static final String COLUMN_IMAGE_DATA = "image_data";

    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_MAIL = "mail";
    public static final String COLUMN_USER_BONUS = "bonus";
    public static final String COLUMN_USER_LIKED = "liked";
    public static final String COLUMN_USER_DISTRIBUTION = "distribution";

    public static final String COLUMN_DISCOUNT_PROPERTIES = "properties";
    public static final String COLUMN_DISCOUNT_DESCRIPTION = "description";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.context = context;
        DB_PATH = context.getFilesDir().getPath() + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {

    }

    public void create_db(){

        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            File file = new File(DB_PATH);
            if (!file.exists()) {

                myInput = context.getAssets().open(DB_NAME);
                String outFileName = DB_PATH;

                myOutput = new FileOutputStream(outFileName);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
            }
        }
        catch(IOException ex){
            Log.d("DatabaseHelper", ex.getMessage());
        }
        finally {
            try{
                if(myOutput!=null) myOutput.close();
                if(myInput!=null) myInput.close();
            }
            catch(IOException ex){
                Log.d("DatabaseHelper", ex.getMessage());
            }
        }
    }
    public SQLiteDatabase open()throws SQLException {

        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

}