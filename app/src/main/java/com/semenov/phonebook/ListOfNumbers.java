package com.semenov.phonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static com.semenov.phonebook.DatabaseHelper.COLUMN_NAME;

public class ListOfNumbers extends AppCompatActivity {

    ListView userList;
    TextView header;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_numbers);

        header = (TextView)findViewById(R.id.header);
        userList = (ListView)findViewById(R.id.list);

        databaseHelper = new DatabaseHelper(getApplicationContext()); //создание объекта DatabaseHelper
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            // открываем подключение
            db = databaseHelper.getReadableDatabase();

            //получаем данные из бд в виде курсора
            userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE, null);
            // определяем, какие столбцы из курсора будут выводиться в ListView
            String[] headers = new String[] {DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_NUMBER};
            // создаем адаптер, передаем в него курсор
            userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                    userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
            header.setText("Номеров в списке: " + String.valueOf(userCursor.getCount()));
            userList.setAdapter(userAdapter);
        }catch (Exception ex){

        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }

    public void newNumber(View view){
        Intent intent = new Intent(this,NewNumber.class);
        startActivity(intent);
    }
}
