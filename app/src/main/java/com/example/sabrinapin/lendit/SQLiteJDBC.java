package com.example.sabrinapin.lendit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SQLiteJDBC extends SQLiteOpenHelper{


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LendIt.db";
    private static final String KEY_ID = "ID";
    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_DATE = "DATE";
    private static final String TABLE_NAME = "EVENTS";
    private static final int GLOBAL_ID = 1;


    private static final String CREATE_TABLE = "CREATE TABLE EVENTS " +
            "(ID INTEGER PRIMARY KEY NOT NULL," +
            " TITLE           TEXT    NOT NULL," +
            " DATE           TEXT    NOT NULL)";

    private static final String SQL_CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE
            + " TEXT," + KEY_DATE + " TEXT" + ")";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + DATABASE_NAME;

    public SQLiteJDBC(Context c) {
        // don't pass in a SQLiteCursorFactory
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    public void addEvent(EventObject event) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).


            ContentValues values = new ContentValues();
//            values.put(KEY_ID, event.getId());
            values.put(KEY_TITLE, event.getTitle());
            values.put(KEY_DATE, event.getDate());


            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insert(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            System.out.println("FUCK");
            System.out.println("FUCK");
            System.out.println("FUCK");
            System.out.println("FUCK");
            System.out.println("FUCK");
            System.out.println("FUCK");
            System.out.println("FUCK");
            System.out.println("FUCK");
            System.out.println("FUCK");
            System.out.println("FUCK");
            System.out.println("FUCK");
            System.out.println("FUCK");
            System.out.println("FUCK");
            System.out.println("FUCK");
            System.out.println("FUCK");
            System.out.println("FUCK");
            System.out.println("FUCK");
            System.out.println("FUCK");
            System.out.println(e.getMessage());
        } finally {
            db.endTransaction();
        }
    }


    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /*
     * getting all todos
     * */
    public List<EventObject> getAllEvents() {
        List<EventObject> todos = new ArrayList<EventObject>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

//        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z", Locale.ENGLISH);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                EventObject td = new EventObject();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setTitle((c.getString(c.getColumnIndex(KEY_TITLE))));
                td.setDate(c.getString(c.getColumnIndex(KEY_DATE)));

                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }



}
