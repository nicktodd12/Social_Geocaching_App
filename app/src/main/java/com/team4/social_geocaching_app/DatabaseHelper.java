package com.team4.social_geocaching_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * A class which assists all activities in retrieving and submitting SQL queries to the database
 */
public class DatabaseHelper {
    //All variables used throughout the helper
    private static final String DATABASE_NAME = "Geocache.db";
    private static final int DATABASE_VERSION = 1;
    private static final String ACCOUNT_TABLE = "Accounts";
    private static final String GEOCACHE_TABLE = "Geocaches";
    private static final String ACTION_TABLE = "Actions";
    private Context context;
    private SQLiteDatabase db;
    private SQLiteStatement currentStmt;
    private static final String NEW_USER_QUERY = "insert into " + ACCOUNT_TABLE + "(username, password, image) values (?, ?, ?)";
    private static final String NEW_GEOCACHE_QUERY = "insert into " + GEOCACHE_TABLE + "(username, points, latitude, longitude, cacheName, description, image) " +
            "values (?, ?, ?, ?, ?, ?, ?)";
    private static final String NEW_ACTION_QUERY = "insert into " + ACTION_TABLE + "(username, action, cacheNum, comment, image) values (?, ?, ?, ?, ?)";

    //Creates a new database helper
    public DatabaseHelper(Context context) {
        this.context = context;
        GeocacheOpenHelper openHelper = new GeocacheOpenHelper(this.context);
        this.db = openHelper.getWritableDatabase();
    }

    //fills tables with initialized data for testing purposes
    public void fillAppWithData(){
        byte[] pic = new byte[0];
        this.insertAccount("A", "aaaaaa", pic);
        this.insertAccount("B", "bbbbbb", pic);
        this.insertAccount("C", "cccccc", pic);
        this.insertAccount("D", "dddddd", pic);
        this.insertGeocache("A", 12, 41.40338, 2.17403, "A1", "This is a cache", pic);
        this.insertGeocache("A", 2, 41.40338, 2.17403, "A2", "This is a cache", pic);
        this.insertGeocache("B",4,4.40338,21.17403,"B","This is a cache", pic);
        this.insertGeocache("C", 2, 2.41, 1.7541, "C1", "This is a cache", pic);
        this.insertGeocache("C", 1, 4.0338, 2.7403, "C2", "This is a cache", pic);
        this.insertGeocache("C", 2, 23.338, 23.34, "C3", "This is a cache", pic);
        this.insertGeocache("D", 1, 1.4238, 5.67, "D", "This is a cache", pic);
        this.insertAction("A", "created", 1, "comment", pic);
        this.insertAction("A", "created", 2, "comment2", pic);
        this.insertAction("B", "created", 3, "comment3", pic);
        this.insertAction("C", "created", 4, "comment4", pic);
        this.insertAction("C", "created", 5, "comment", pic);
        this.insertAction("C", "created", 6, "comment2", pic);
        this.insertAction("D", "created", 7, "comment3", pic);
        this.insertAction("D", "found", 1, "comment3", pic);
        this.insertAction("D", "found", 3, "comment3", pic);
        this.insertAction("A", "found", 5, "comment3", pic);
        this.insertAction("B", "found", 1, "comment3", pic);
    }

    //inserts a new account into the database
    public long insertAccount(String username, String password, byte[] image) {
        this.currentStmt = this.db.compileStatement(NEW_USER_QUERY);
        this.currentStmt.bindString(1, username);
        this.currentStmt.bindString(2, password);
        this.currentStmt.bindBlob(3, image);
        return this.currentStmt.executeInsert();
    }

    //runs an update on the user's account picture
    public int modifyAccountImage(String username, byte[] newImage){
        ContentValues cv = new ContentValues();
        String where = "username=?";
        cv.put("image", newImage);
        return db.update(ACCOUNT_TABLE, cv, where, new String[]{username});
    }

    //inserts a new geocache into the database
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

    //inserts a new action into the database
    public long insertAction(String username, String action, int cacheNum, String comment, byte[] image) {
        this.currentStmt = this.db.compileStatement(NEW_ACTION_QUERY);
        this.currentStmt.bindString(1, username);
        this.currentStmt.bindString(2, action);
        this.currentStmt.bindLong(3, cacheNum);
        this.currentStmt.bindString(4, comment);
        this.currentStmt.bindBlob(5, image);
        return this.currentStmt.executeInsert();
    }

    //returns a list of users matching the input username and password
    public List<String> selectUsernames(String username, String password) {
        //create a list of strings and run the query
        List<String> list = new ArrayList<>();
        Cursor cursor = this.db.query(ACCOUNT_TABLE, new String[] { "username", "password" }, "username = '"+ username +"' " +
                "AND password= '"+ password+"'", null, null, null, "username desc");
        if (cursor.moveToFirst()) {
            do {
                //add the results to the list
                list.add(cursor.getString(0));
                list.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    //Returns a boolean whether or not a username has already been taken
    public boolean usernameTaken(String username) {
        //create a list of strings and run the query
        List<String> list = new ArrayList<>();
        Cursor cursor = this.db.query(ACCOUNT_TABLE, new String[] {"username"}, "username = '"+ username +"'", null, null, null, "username desc");
        if (cursor.moveToFirst()) {
            do {
                //add the results to the list
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        //return true if the list is not empty
        return list.size() != 0;
    }

    //Returns a boolean whether or not any accounts have been created
    public boolean firstTime() {
        //create a list of strings and run the query
        List<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Accounts", null);
        if (cursor.moveToFirst()) {
            do {
                //add the results to the list
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        //return true if the list is empty
        return list.size() == 0;
    }

    public List<Geocache> selectGeocaches(int cacheNum) {
        //create a list of geocaches
        List<Geocache> list = new ArrayList<>();
        //build the query
        String query = "SELECT * FROM Geocaches";
        if(cacheNum>0){
            //do not include a search on the cacheNum if it is 0
            query += " WHERE cacheNum="+cacheNum;
        }
        query+=" ORDER BY cacheNum DESC";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                //add a new geocache to the list for each item returned
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

    //Returns a list of actions based on username, password, or neither
    public List<Action> selectActions(String username, int cacheNum) {
        //create a list of actions and dynamically build the query
        List<Action> list = new ArrayList<>();
        String query = "SELECT * FROM Actions LEFT OUTER JOIN Geocaches ON Actions.cacheNum=Geocaches.cacheNum";
        //check to see if the username/cachenum was specified and add to the query string
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
                //add a new action to the list for each item returned
                Action a = new Action();
                a.setUsername(cursor.getString(0));
                a.setAction(cursor.getString(1));
                a.setCacheNum(Integer.parseInt(cursor.getString(2)));
                a.setComment(cursor.getString(3));
                a.setDate(cursor.getString(4));
                a.setPoints(Integer.parseInt(cursor.getString(8))); //Changed this to 8
                a.setImage(cursor.getBlob(5));
                list.add(a);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    //Returns the accounts sorted by total point value accumulated
    public List<Account> getLeaderboard() {
        //create a list of accounts and run the query
        List<Account> list = new ArrayList<>();
        String query = "SELECT * FROM (SELECT Actions.username AS username, SUM(Geocaches.points) AS points " +
                "FROM Actions LEFT OUTER JOIN Geocaches ON Actions.cacheNum=Geocaches.cacheNum" +
                " WHERE action='found' GROUP BY Actions.username) ORDER BY points DESC";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                //create a new account for each item returned and add to the list
                Account a = new Account();
                a.setUsername(cursor.getString(0));
                a.setPoints(Integer.parseInt(cursor.getString(1)));
                list.add(a);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    //Returns all accounts in the database
    public List<Account> getAllAccounts() {
        //create an account list and run the query
        List<Account> list = new ArrayList<>();
        String query = "SELECT * FROM Accounts";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                //add a new account to the list for each item returned
                Account a = new Account();
                a.setUsername(cursor.getString(0));
                a.setPoints(0);
                a.setImage(cursor.getBlob(2));
                list.add(a);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    //Returns the image associated with the user's account
    public byte[] getAccountImage(String username){
        //run the query on the username
        Cursor cursor = db.query(ACCOUNT_TABLE, new String[]{"image"}, "username=?", new String[]{username}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                //get the image from the database and return it
                return cursor.getBlob(0);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return null;
    }

    private static class GeocacheOpenHelper extends SQLiteOpenHelper {
        GeocacheOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //creates all the tables needed for app functionality
            db.execSQL("CREATE TABLE " + ACCOUNT_TABLE + "(username TEXT PRIMARY KEY, password TEXT, image BLOB)");
            db.execSQL("CREATE TABLE " + GEOCACHE_TABLE + "(cacheNum INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, points INTEGER, " +
                    "latitude REAL, longitude REAL, cacheName TEXT, description TEXT, Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, image BLOB)");
            db.execSQL("CREATE TABLE " + ACTION_TABLE + "(username TEXT, action TEXT, cacheNum INTEGER, comment TEXT, Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, image BLOB," +
                    "PRIMARY KEY(username, action, cacheNUM))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //drop and recreate tables on upgrade
            Log.w("Example", "Upgrading database; this will drop and recreate the tables.");
            db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + GEOCACHE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ACTION_TABLE);
            onCreate(db);
        }
    }
}

