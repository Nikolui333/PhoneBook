package com.semenov.phonebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "userstore.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "users"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_YEAR = "year";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA); //создание безы данных и передача ей названия и номера версии
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // создание таблицы и добавление в неё столбцов
        db.execSQL("CREATE TABLE users (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," //создание id и обеспечение уникальности каждого id
                + COLUMN_NAME + " TEXT, " //создание текстового столбца
                + COLUMN_YEAR + " INTEGER);"); //создание числового столбца
        // добавление начальных данных
        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME
                + ", " + COLUMN_YEAR  + ") VALUES ('Том Смит', 1981);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE); //обновление таблицы
        onCreate(db);
    }
}
