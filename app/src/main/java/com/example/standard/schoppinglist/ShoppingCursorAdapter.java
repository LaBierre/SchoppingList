package com.example.standard.schoppinglist;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.standard.schoppinglist.data.ShoppingContract;
import com.example.standard.schoppinglist.data.ShoppingContract.ShoppingEntry;

import java.util.ArrayList;

import static android.R.attr.id;

/**
 * Created by vince on 19.04.2017.
 */

public class ShoppingCursorAdapter extends CursorAdapter {
    public ShoppingCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        int id = view.getId();
        final TextView artikel = (TextView) view.findViewById(R.id.entry_text);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);

        int nameColumnIndex = cursor.getColumnIndex(ShoppingEntry.COLUMN_PRODUCT_NAME);

        final String productName = cursor.getString(nameColumnIndex);

        artikel.setText(productName);

//        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "angelina.TTF");
//        artikel.setTypeface(myTypeface);

        final long idCurrent = getItemId(cursor.getPosition());

        final Uri currentItemUri = ContentUris.withAppendedId(ShoppingEntry.CONTENT_URI, idCurrent);
        //cursor.moveToFirst();



        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = currentItemUri;

                if (checkBox.isChecked()){

                    context.getContentResolver().delete(uri, null, null);
                }
            }
        });
    }
}
