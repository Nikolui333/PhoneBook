package com.semenov.phonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewNumber extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnRead, btnClear, btnUpd, btnDel;
    EditText etName, etNumber, etId;

    SQLiteDatabase db;
    DatabaseHelper databaseHelper; //возможно стоит не создавать эти переменные заново, а взять из главной активности

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_number);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnUpd = (Button) findViewById(R.id.btnUpd);
        btnUpd.setOnClickListener(this);

        btnDel = (Button) findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);
        etNumber = (EditText) findViewById(R.id.etEmail);
        etId = (EditText) findViewById(R.id.etId);

        databaseHelper = new DatabaseHelper(getApplicationContext()); //создание объекта DatabaseHelper
    }

    public void onClick(View v) {

        String name = etName.getText().toString();
        String email = etNumber.getText().toString();
        String id = etId.getText().toString();

        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        try {
            switch (v.getId()) {

                case R.id.btnAdd:
                    contentValues.put(DatabaseHelper.COLUMN_NAME, name);
                    contentValues.put(DatabaseHelper.COLUMN_YEAR, email);

                    database.insert(DatabaseHelper.TABLE, null, contentValues);
                    /*new MainActivity().*/onResume();
                    break;

                case R.id.btnRead:
                    Cursor cursor = database.query(DatabaseHelper.TABLE, null, null, null, null, null, null);

                    if (cursor.moveToFirst()) {
                        int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                        int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
                        int emailIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_YEAR);
                        do {
                            Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                                    ", name = " + cursor.getString(nameIndex) +
                                    ", number = " + cursor.getString(emailIndex)); //поменять название
                        } while (cursor.moveToNext());
                    } else
                        Log.d("mLog","0 rows");

                    cursor.close();
                    break;

                case R.id.btnClear:
                    database.delete(DatabaseHelper.TABLE, null, null);
                    break;

                case R.id.btnUpd:
                    if (id.equalsIgnoreCase("")){
                        break;
                    }
                    contentValues.put(DatabaseHelper.COLUMN_YEAR, email);
                    contentValues.put(DatabaseHelper.COLUMN_NAME, name);
                    int updCount = database.update(DatabaseHelper.TABLE, contentValues, DatabaseHelper.COLUMN_ID + "= ?", new String[] {id});

                    Log.d("mLog", "updates rows count = " + updCount);

                case R.id.btnDel:
                    if (id.equalsIgnoreCase("")){
                        break;
                    }
                    int delCount = database.delete(DatabaseHelper.TABLE, DatabaseHelper.COLUMN_ID + "=" + id, null);

                    Log.d("mLog", "deleted rows count = " + delCount);
            }
            databaseHelper.close();
        } catch (Exception ex){

        }
    }
}
