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
    private static final String NEW_USER_QUERY = "insert into " + ACCOUNT_TABLE + "(username, password, points) values (?, ?, ?)";
    private static final String NEW_GEOCACHE_QUERY = "insert into " + GEOCACHE_TABLE + "(username, points, latitude, longitude, cacheName, description, image) " +
            "values (?, ?, ?, ?, ?, ?, ?)";
    private static final String NEW_ACTION_QUERY = "insert into " + ACTION_TABLE + "(username, action, cacheNum, comment) values (?, ?, ?, ?)";

    public DatabaseHelper(Context context) {
        this.context = context;
        GeocacheOpenHelper openHelper = new GeocacheOpenHelper(this.context);
        this.db = openHelper.getWritableDatabase();
    }

    public void fillAppWithData(){
        this.insertAccount("A", "aaaaaa");
        this.insertAccount("B", "bbbbbb");
        this.insertAccount("C", "cccccc");
        this.insertAccount("D", "dddddd");
        byte[] pic = new byte[0];
        this.insertGeocache("A", 12, 41.40338, 2.17403, "A1", "This is a cache", pic);
        this.insertGeocache("A", 2, 41.40338, 2.17403, "A2", "This is a cache", pic);
        this.insertGeocache("B",4,4.40338,21.17403,"B","This is a cache", pic);
        this.insertGeocache("C", 2, 2.41, 1.7541, "C1", "This is a cache", pic);
        this.insertGeocache("C", 1, 4.0338, 2.7403, "C2", "This is a cache", pic);
        this.insertGeocache("C", 2, 23.338, 23.34, "C3", "This is a cache", pic);
        this.insertGeocache("D", 1, 1.4238, 5.67, "D", "This is a cache", pic);
        this.insertAction("A", "created", 1, "comment");
        this.insertAction("A", "created", 2, "comment2");
        this.insertAction("B", "created", 3, "comment3");
        this.insertAction("C", "created", 4, "comment4");
        this.insertAction("C", "created", 5, "comment");
        this.insertAction("C", "created", 6, "comment2");
        this.insertAction("D", "created", 7, "comment3");
        this.insertAction("D", "checkin", 1, "comment3");
        this.insertAction("D", "checkin", 3, "comment3");
        this.insertAction("A", "checkin", 5, "comment3");
        this.insertAction("B", "checkin", 1, "comment3");
    }

    public long insertAccount(String username, String password) {
        this.currentStmt = this.db.compileStatement(NEW_USER_QUERY);
        this.currentStmt.bindString(1, username);
        this.currentStmt.bindString(2, password);
        this.currentStmt.bindLong(3, 0);
        return this.currentStmt.executeInsert();
    }

    public long insertGeocache(String username, int points, double latitude, double longitude, String cacheName, String description, byte[] image) {
        this.currentStmt = this.db.compileStatement(NEW_GEOCACHE_QUERY);
        this.currentStmt.bindString(1, username);
        this.currentStmt.bindLong(2, points);
        this.currentStmt.bindDouble(3, latitude);
        this.currentStmt.bindDouble(4, longitude);
        this.currentStmt.bindString(5, cacheName);
        this.currentStmt.bindString(6, description);
        this.currentStmt.bindBlob(7, image);
        return this.currentStmt.executeInsert();
    }

    public long insertAction(String username, String action, int cacheNum, String comment) {
        this.currentStmt = this.db.compileStatement(NEW_ACTION_QUERY);
        this.currentStmt.bindString(1, username);
        this.currentStmt.bindString(2, action);
        this.currentStmt.bindLong(3, cacheNum);
        this.currentStmt.bindString(4, comment);
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

    public List<Geocache> selectGeocaches(int cacheNum) {
        List<Geocache> list = new ArrayList<>();
        String query = "SELECT * FROM Geocaches";
        if(cacheNum>0){
            query += " WHERE cacheNum="+cacheNum;
        }
        query+=" ORDER BY cacheNum DESC";
        Cursor cursor = db.rawQuery(query, null);
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
                g.setImage(cursor.getBlob(8));
                list.add(g);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    public List<Action> selectActions(String username, int cacheNum) {
        List<Action> list = new ArrayList<>();
        String query = "SELECT * FROM Actions LEFT OUTER JOIN Geocaches ON Actions.cacheNum=Geocaches.cacheNum";
        if(!username.equals("") && cacheNum>0){
            query += " WHERE Actions.username='"+username+"' AND Actions.cacheNum="+cacheNum;
        }else if(cacheNum>0) {
            query += " WHERE Actions.cacheNum="+cacheNum;
        }else if(!username.equals("")){
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
                a.setComment(cursor.getColumnName(3));
                a.setDate(cursor.getString(4));
                a.setPoints(Integer.parseInt(cursor.getString(7)));
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
            db.execSQL("CREATE TABLE " + ACCOUNT_TABLE + "(username TEXT PRIMARY KEY, password TEXT, points INTEGER)");
            db.execSQL("CREATE TABLE " + GEOCACHE_TABLE + "(cacheNum INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, points INTEGER, " +
                    "latitude REAL, longitude REAL, cacheName TEXT, description TEXT, Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, image BLOB)");
            db.execSQL("CREATE TABLE " + ACTION_TABLE + "(username TEXT, action TEXT, cacheNum INTEGER, comment TEXT, Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
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

