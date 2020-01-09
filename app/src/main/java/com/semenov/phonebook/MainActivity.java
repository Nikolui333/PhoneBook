package com.semenov.phonebook;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnRead, btnClear, btnUpd, btnDel;
    EditText etName, etEmail, etId;

    ListView userList;
    TextView header;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        header = (TextView)findViewById(R.id.header);
        userList = (ListView)findViewById(R.id.list);

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
        etEmail = (EditText) findViewById(R.id.etEmail);
        etId = (EditText) findViewById(R.id.etId);

        databaseHelper = new DatabaseHelper(getApplicationContext()); //создание объекта DatabaseHelper
    }

    public void onClick(View v) {

        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String id = etId.getText().toString();

        SQLiteDatabase database = databaseHelper.getWritableDatabase(); //???

        ContentValues contentValues = new ContentValues();


        switch (v.getId()) {

            case R.id.btnAdd:
                contentValues.put(DatabaseHelper.COLUMN_NAME, name);
                contentValues.put(DatabaseHelper.COLUMN_YEAR, email);

                database.insert(DatabaseHelper.TABLE, null, contentValues);
                onResume();
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
                                ", email = " + cursor.getString(emailIndex));
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
        db.close();
    }


    @Override
    public void onResume() {
        super.onResume();

        // открываем подключение
        db = databaseHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора
        userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[] {DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_YEAR};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        header.setText("Найдено элементов: " + String.valueOf(userCursor.getCount()));
        userList.setAdapter(userAdapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}