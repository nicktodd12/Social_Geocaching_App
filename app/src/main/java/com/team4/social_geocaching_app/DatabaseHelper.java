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
    private static final String NEW_GEOCACHE_QUERY = "insert into " + GEOCACHE_TABLE + "(username, points, latitude, longitude, cacheName, description) " +
            "values (?, ?, ?, ?, ?, ?)";
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

    public long insertGeocache(String username, int points, double latitude, double longitude, String cacheName, String description) {
        this.currentStmt = this.db.compileStatement(NEW_GEOCACHE_QUERY);
        this.currentStmt.bindString(1, username);
        this.currentStmt.bindLong(2, points);
        this.currentStmt.bindDouble(3, latitude);
        this.currentStmt.bindDouble(4, longitude);
        this.currentStmt.bindString(5, cacheName);
        this.currentStmt.bindString(6, description);
        return this.currentStmt.executeInsert();
    }

    public long insertAction(String username, String action, int cacheNum) {
        this.currentStmt = this.db.compileStatement(NEW_ACTION_QUERY);
        this.currentStmt.bindString(1, username);
        this.currentStmt.bindString(2, action);
        this.currentStmt.bindLong(3, cacheNum);
        return this.currentStmt.executeInsert();
    }

    public List<String> selectUsernames(String username, String password) {
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

    public boolean firstTime() {
        List<String> list = new ArrayList<>();
        //Cursor cursor = this.db.query(ACCOUNT_TABLE, new String[] {"username"}, "username = '"+ username +"'", null, null, null, "username desc");
        Cursor cursor = db.rawQuery("SELECT * FROM Accounts", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list.size() == 0;
    }

    public List<Geocache> selectGeocaches() {
        List<Geocache> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Geocaches ORDER BY cacheNum DESC", null);
        if (cursor.moveToFirst()) {
            do {
                Geocache g = new Geocache();
                g.setCachenum(Integer.parseInt(cursor.getString(0)));
                g.setUsername(cursor.getString(1));
                g.setPoints(Integer.parseInt(cursor.getString(2)));
                g.setLatitude(Double.parseDouble(cursor.getString(3)));
                g.setLongitude(Double.parseDouble(cursor.getString(4)));
                g.setCacheName(cursor.getString(5));
                g.setDescription(cursor.getString(6));
                g.setDate(cursor.getString(7));
                list.add(g);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    public List<Action> selectActionsByUser(String username) {
        List<Action> list = new ArrayList<>();
        String query = "SELECT * FROM Actions LEFT OUTER JOIN Geocaches ON Actions.cacheNum=Geocaches.cacheNum";
        if(!username.equals("")){
            query += " WHERE Actions.username='"+username+"'";
        }
        query+=" ORDER BY Timestamp DESC";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Action a = new Action();
                a.setUsername(cursor.getString(0));
                a.setAction(cursor.getString(1));
                a.setCacheNum(Integer.parseInt(cursor.getString(2)));
                a.setDate(cursor.getString(3));
                a.setPoints(Integer.parseInt(cursor.getString(6)));
                list.add(a);
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
            db.execSQL("CREATE TABLE " + GEOCACHE_TABLE + "(cacheNum INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, points INTEGER, " +
                    "latitude REAL, longitude REAL, cacheName TEXT, description TEXT, Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
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

