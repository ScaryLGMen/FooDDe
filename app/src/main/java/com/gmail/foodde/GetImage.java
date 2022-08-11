package com.gmail.foodde;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetImage extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;
    private String link, key;

    public GetImage(ImageView imageView, String link, String key) {
        this.imageView = imageView;
        this.link = link;
        this.key = key;
    }

    private List<Bitmap> bitmaps = new ArrayList<>();

    public GetImage(ImageView imageView, String link, List<Bitmap> bitmaps, String key) {
        this.imageView = imageView;
        this.link = link;
        this.bitmaps = bitmaps;
        this.key = key;
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            DatabaseHelper databaseHelper;
            SQLiteDatabase db;
            databaseHelper = new DatabaseHelper(imageView.getContext());
            databaseHelper.create_db();
            db = databaseHelper.open();

            Cursor userCursor;
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_OF_IMAGES +
                    " where " + DatabaseHelper.COLUMN_IMAGE_KEY + " = " + "\""+key+ "\"" , null);
            if(userCursor.moveToFirst()){
                userCursor.moveToFirst();
                db.close();
                return getImageDataInBitmap(userCursor.getBlob(1));
            }else {
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);

                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_IMAGE_KEY, key);
                values.put(DatabaseHelper.COLUMN_IMAGE_DATA, bitmapToByte(bitmap));
                db.insert(DatabaseHelper.TABLE_OF_IMAGES, null, values);
                db.close();
                return bitmap;
            }
        }catch (Exception e){
            Log.d("TAG",e.getMessage());
        }
        return null;
    }

    public Bitmap getImageDataInBitmap(byte[] imageData) {
        if (imageData != null) {
            return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        }
        return null;
    }

    public byte[] bitmapToByte(Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            stream.close();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
        bitmaps.add(result);
    }

}