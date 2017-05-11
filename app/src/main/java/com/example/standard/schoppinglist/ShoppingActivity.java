package com.example.standard.schoppinglist;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.standard.schoppinglist.data.ShoppingContract;
import com.example.standard.schoppinglist.data.ShoppingDbHelper;

import com.example.standard.schoppinglist.data.ShoppingContract.ShoppingEntry;

import static android.R.attr.bitmap;
import static android.R.attr.data;

public class ShoppingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText text;
    private TextView emptyView;
    private ListView shoppingList;

    private ShoppingCursorAdapter mCursorAdapter;

    private boolean isClicked;

    private static final int SHOPPING_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_activity);

        mCursorAdapter = new ShoppingCursorAdapter(this,null);

        text = (EditText) findViewById(R.id.editText);
        emptyView = (TextView) findViewById(R.id.empty_text);
        //checkIt = (CheckBox) findViewById(R.id.checkbox);

        shoppingList = (ListView) findViewById(R.id.list);
        //list.getCheckedItemPosition();
        shoppingList.setAdapter(mCursorAdapter);

        //Set Start Text if no Items in List
        shoppingList.setEmptyView(emptyView);

        getSupportLoaderManager().initLoader(SHOPPING_LOADER, null,this);
    }

    // This Button inserts an article into the database
    public void saveButton(View view){
        insertData();

        text.setText("");
    }
    private void insertData(){

        ContentValues values = new ContentValues();

        String artikel = text.getText().toString().trim();

        values.put(ShoppingEntry.COLUMN_PRODUCT_NAME, artikel);

        Uri shoppingUri = getContentResolver().insert(ShoppingEntry.CONTENT_URI, values);

        if (shoppingUri == null){
            Toast.makeText(this, "Artikel nicht in Datenbank abgespeichert", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Artikel wurde in Datenbank abgespeichert", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shopping_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_help:
                helpMessage();
                return true;

            case R.id.action_delete_item:
                Intent intent = new Intent(ShoppingActivity.this, ShoppingActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_delete_list:
                deleteMessage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        deleteBackground();
        return;
    }

    private void deleteBackground (){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_title_delete))
                .setMessage(getString(R.string.dialog_message_back_pressed))
                .setPositiveButton(getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ShoppingActivity.this, ShoppingActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getString(R.string.dialog_negative_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null){
                            dialog.dismiss();
                        }
                    }
                })
                .setIcon(R.drawable.totenkopf)
                .show();
    }

    private void deleteMessage(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_title_delete))
                .setMessage(getString(R.string.dialog_message_delete_list))
                .setPositiveButton(getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_negative_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null){
                            dialog.dismiss();
                        }
                    }
                })
                .setIcon(R.drawable.totenkopf)
                .show();
    }

    private void helpMessage(){

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_title_help))
                .setMessage(getString(R.string.dialog_message_delete_help))
                .setPositiveButton(getString(R.string.dialog_help_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.totenkopf)
                .show();
    }

    private void deleteData(){

        int rowsDeleted = getContentResolver().delete(ShoppingEntry.CONTENT_URI, null, null);

        if (rowsDeleted == 0) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.delete_failed_edit_activity), Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.delete_succed_edit_activity), Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this, ShoppingActivity.class);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                ShoppingEntry._ID,
                ShoppingEntry.COLUMN_PRODUCT_NAME
        };

        return new CursorLoader(this, ShoppingEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@InventoryAdapter} with this new cursor containing updated product data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
