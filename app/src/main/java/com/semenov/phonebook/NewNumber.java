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

    Button btnAdd, btnClear, btnDel;
    EditText etName, etNumber;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_number);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnDel = (Button) findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);
        etNumber = (EditText) findViewById(R.id.etEmail);

        databaseHelper = new DatabaseHelper(getApplicationContext()); //создание объекта DatabaseHelper
    }

    public void onClick(View v) {

        String name = etName.getText().toString();
        String number = etNumber.getText().toString();

        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        try {
            switch (v.getId()) {

                case R.id.btnAdd: // кнопка "сохранить"
                    contentValues.put(DatabaseHelper.COLUMN_NAME, name);
                    contentValues.put(DatabaseHelper.COLUMN_NUMBER, number);

                    database.insert(DatabaseHelper.TABLE, null, contentValues);
                    onResume();
                    break;

                case R.id.btnClear: //кнопка "удалить всё"
                    database.delete(DatabaseHelper.TABLE, null, null);
                    break;

                case R.id.btnDel:

                    int delCount = database.delete(DatabaseHelper.TABLE, DatabaseHelper.COLUMN_NAME + " = ?", new String[] {name});

                    Log.d("mLog", "deleted rows count = " + delCount);
            }
            databaseHelper.close();
        } catch (Exception ex){

        }
    }
}
