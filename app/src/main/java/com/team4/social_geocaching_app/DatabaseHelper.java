package com.team4.social_geocaching_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String DATABASE_NAME = "Geocache.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_1 = "Account";
    private Context context;
    private SQLiteDatabase db;
    private SQLiteStatement currentStmt;
    private static final String NEW_USER_QUERY = "insert into " + TABLE_NAME_1 + "(name, password) values (?, ?)";

    public DatabaseHelper(Context context) {
        this.context = context;
        GeocacheOpenHelper openHelper = new GeocacheOpenHelper(this.context);
        this.db = openHelper.getWritableDatabase();
    }

    public long insert(String name, String password) {
        this.currentStmt = this.db.compileStatement(NEW_USER_QUERY);
        this.currentStmt.bindString(1, name);
        this.currentStmt.bindString(2, password);
        return this.currentStmt.executeInsert();
    }

    public List<String> selectAll(String username, String password) {
        List<String> list = new ArrayList<>();
        Cursor cursor = this.db.query(TABLE_NAME_1, new String[] { "name", "password" }, "name = '"+ username +"' AND password= '"+ password+"'", null, null, null, "name desc");
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
                list.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    private static class GeocacheOpenHelper extends SQLiteOpenHelper {
        GeocacheOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME_1 + "(id INTEGER PRIMARY KEY, name TEXT, password TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.w("Example", "Upgrading database; this will drop and recreate the tables.");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
            onCreate(db);
        }
    }
}

