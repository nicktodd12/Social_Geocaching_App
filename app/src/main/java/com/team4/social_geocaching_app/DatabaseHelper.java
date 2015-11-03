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
    private static final String ACCOUNT_TABLE = "Accounts";
    private static final String GEOCACHE_TABLE = "Geocaches";
    private static final String ACTION_TABLE = "Actions";
    private static String Username, Password;
    private Context context;
    private SQLiteDatabase db;
    private SQLiteStatement currentStmt;
    private static final String NEW_USER_QUERY = "insert into " + ACCOUNT_TABLE + "(name, password) values (?, ?)";
    private static final String NEW_GEOCACHE_QUERY = "insert into " + GEOCACHE_TABLE + "(cacheNum, creator, points, latitude, longitude) values (?, ?, ?, ?, ?)";
    private static final String NEW_ACTION_QUERY = "insert into " + ACTION_TABLE + "(username, action, cacheNum) values (?, ?, ?)";

    public DatabaseHelper(Context context) {
        this.context = context;
        GeocacheOpenHelper openHelper = new GeocacheOpenHelper(this.context);
        this.db = openHelper.getWritableDatabase();
    }

    public long insertAccount(String name, String password) {
        this.currentStmt = this.db.compileStatement(NEW_USER_QUERY);
        this.currentStmt.bindString(1, name);
        this.currentStmt.bindString(2, password);
        return this.currentStmt.executeInsert();
    }

    public List<String> selectAll(String username, String password) {
        Username = username;
        Password = password;
        List<String> list = new ArrayList<>();
        Cursor cursor = this.db.query(ACCOUNT_TABLE, new String[] { "name", "password" }, "name = '"+ username +"' AND password= '"+ password+"'", null, null, null, "name desc");
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
            db.execSQL("CREATE TABLE " + ACCOUNT_TABLE + "(id INTEGER PRIMARY KEY, name TEXT, password TEXT)");
            db.execSQL("CREATE TABLE " + GEOCACHE_TABLE + "(cacheNum INTEGER PRIMARY KEY, creator TEXT, points INTEGER, latitude REAL, longitude REAL)");
            db.execSQL("CREATE TABLE " + ACTION_TABLE + "(username TEXT, action TEXT, cacheNum INTEGER, Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "PRIMARY KEY(username, action, cacheNUM))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.w("Example", "Upgrading database; this will drop and recreate the tables.");
            db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE);
            onCreate(db);
        }
    }
}

