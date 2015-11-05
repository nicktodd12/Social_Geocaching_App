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
    private static final String NEW_USER_QUERY = "insert into " + ACCOUNT_TABLE + "(username, password) values (?, ?)";
    private static final String NEW_GEOCACHE_QUERY = "insert into " + GEOCACHE_TABLE + "(cacheNum, username, points, latitude, longitude) values (?, ?, ?, ?, ?)";
    private static final String NEW_ACTION_QUERY = "insert into " + ACTION_TABLE + "(username, action, cacheNum) values (?, ?, ?)";

    public DatabaseHelper(Context context) {
        this.context = context;
        GeocacheOpenHelper openHelper = new GeocacheOpenHelper(this.context);
        this.db = openHelper.getWritableDatabase();
    }

    public long insertAccount(String username, String password) {
        this.currentStmt = this.db.compileStatement(NEW_USER_QUERY);
        this.currentStmt.bindString(1, username);
        this.currentStmt.bindString(2, password);
        return this.currentStmt.executeInsert();
    }

    public long insertGeocache(int cacheNum, String username, int points, double latitude, double longitude) {
        this.currentStmt = this.db.compileStatement(NEW_GEOCACHE_QUERY);
        this.currentStmt.bindLong(1, cacheNum);
        this.currentStmt.bindString(2, username);
        this.currentStmt.bindLong(3, points);
        this.currentStmt.bindDouble(4, latitude);
        this.currentStmt.bindDouble(5, longitude);
        return this.currentStmt.executeInsert();
    }

    public long insertAction(String username, String action, int cacheNum) {
        this.currentStmt = this.db.compileStatement(NEW_ACTION_QUERY);
        this.currentStmt.bindString(1, username);
        this.currentStmt.bindString(2, action);
        this.currentStmt.bindLong(3, cacheNum);
        return this.currentStmt.executeInsert();
    }

    public List<String> selectAll(String username, String password) {
        List<String> list = new ArrayList<>();
        Cursor cursor = this.db.query(ACCOUNT_TABLE, new String[] { "username", "password" }, "username = '"+ username +"' " +
                "AND password= '"+ password+"'", null, null, null, "username desc");
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

    public boolean usernameTaken(String username) {
        List<String> list = new ArrayList<>();
        Cursor cursor = this.db.query(ACCOUNT_TABLE, new String[] {"username"}, "username = '"+ username +"'", null, null, null, "username desc");
        //Cursor cursor = db.rawQuery("SELECT * FROM Accounts WHERE username = ?", new String[] {username});
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list.size() != 0;
    }

    public List<String> selectGeocaches(int cacheNum) {
        List<String> list = new ArrayList<>();
        Cursor cursor = this.db.query(GEOCACHE_TABLE, new String[] { "cacheNum", "username", "points", "latitude", "longitude" }, "cacheNum = '"+ cacheNum +"'"
                , null, null, null, "cacheNum desc");
        if (cursor.moveToFirst()) {
            do {
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
            db.execSQL("CREATE TABLE " + ACCOUNT_TABLE + "(username TEXT PRIMARY KEY, password TEXT)");
            db.execSQL("CREATE TABLE " + GEOCACHE_TABLE + "(cacheNum INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, points INTEGER, latitude REAL, longitude REAL, cacheName TEXT)");
            db.execSQL("CREATE TABLE " + ACTION_TABLE + "(username TEXT, action TEXT, cacheNum INTEGER, Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "PRIMARY KEY(username, action, cacheNUM))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.w("Example", "Upgrading database; this will drop and recreate the tables.");
            db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + GEOCACHE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ACTION_TABLE);
            onCreate(db);
        }
    }
}

