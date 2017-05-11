package com.example.standard.schoppinglist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.standard.schoppinglist.data.ShoppingContract.ShoppingEntry;

import static android.R.attr.version;

/**
 * Created by vince on 19.04.2017.
 */

public class ShoppingDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shopping.db";

    private static final int DATABASE_VERSION = 1;

    public ShoppingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_SHOPPING_TABLE = "CREATE TABLE " + ShoppingEntry.TABLE_NAME + " ("
                + ShoppingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ShoppingEntry.COLUMN_PRODUCT_NAME + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_SHOPPING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
