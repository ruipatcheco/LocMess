package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Message;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderContract.FeedEntry;

/**
 * Created by brigadinhos on 25/04/2017.
 */

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "locmessClient.db";

    private static final String SQL_CREATE_LOCATION =

            "CREATE TABLE IF NOT EXISTS " + FeedEntry.LOCATION_TABLE_NAME + " ( " +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.LOCATION_COLUMN_NAME +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.LOCATION_COLUMN_SSID +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.LOCATION_COLUMN_BLE +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.LOCATION_COLUMN_LAT +" "+ FeedEntry.FLOAT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.LOCATION_COLUMN_LON +" "+ FeedEntry.FLOAT_TYPE +
                " )";

    private static final String SQL_CREATE_MULE =

            "CREATE TABLE IF NOT EXISTS " + FeedEntry.MULE_TABLE_NAME + " ( " +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.MULE_COLUMN_ID +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MULE_COLUMN_CREATIONTIME +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MULE_COLUMN_STARTTIME +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MULE_COLUMN_ENDTIME +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MULE_COLUMN_CONTENT +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MULE_COLUMN_PUBLISHER +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MULE_COLUMN_LOCATION +" "+ FeedEntry.TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_MESSAGE=

            "CREATE TABLE IF NOT EXISTS " + FeedEntry.MESSAGE_TABLE_NAME + " ( " +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.MESSAGE_COLUMN_ID +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_CREATIONTIME +" "+ FeedEntry.LONG_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_STARTTIME +" "+ FeedEntry.LONG_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_ENDTIME +" "+ FeedEntry.LONG_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_CONTENT +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_PUBLISHER +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_LOCATION +" "+ FeedEntry.TEXT_TYPE +
                " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.LOCATION_TABLE_NAME;

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_LOCATION);
        db.execSQL(SQL_CREATE_MESSAGE);
        db.execSQL(SQL_CREATE_MULE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDrop(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



    public void insertMessage (long creationTime, long startTime, long endTime, String content, String publisher, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.MESSAGE_COLUMN_ID, "");

        contentValues.put(FeedEntry.MESSAGE_COLUMN_CREATIONTIME, creationTime);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_STARTTIME, startTime);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_ENDTIME, endTime);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_CONTENT, content);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_PUBLISHER, publisher);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_LOCATION, location);

        db.insert(FeedEntry.MESSAGE_TABLE_NAME, null, contentValues);
    }

    public ArrayList<Message> getAllMessages() {
        ArrayList<Message> messages = new ArrayList<Message>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.MESSAGE_COLUMN_CREATIONTIME,
                FeedEntry.MESSAGE_COLUMN_STARTTIME,
                FeedEntry.MESSAGE_COLUMN_ENDTIME,
                FeedEntry.MESSAGE_COLUMN_CONTENT,
                FeedEntry.MESSAGE_COLUMN_PUBLISHER,
                FeedEntry.MESSAGE_COLUMN_LOCATION
        };

        // Filter results WHERE "title" = 'My Title'
        //String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
        //String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.MESSAGE_COLUMN_LOCATION + " DESC";

        Cursor cursor = db.query(
                FeedEntry.MESSAGE_TABLE_NAME,            // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                   // The sort order
        );

        cursor.moveToFirst();


        while(cursor.isAfterLast() == false){
            Message m = new Message(
                    null,
                    cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_CREATIONTIME)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_STARTTIME)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_ENDTIME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_CONTENT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_PUBLISHER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_LOCATION))
            );
            messages.add(m);
            cursor.moveToNext();
        }
        return messages;
    }

    public void insertLocation (String name, String ssid, String ble, float lat, float lon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.LOCATION_COLUMN_NAME, name);
        contentValues.put(FeedEntry.LOCATION_COLUMN_SSID, ssid);
        contentValues.put(FeedEntry.LOCATION_COLUMN_BLE, ble);
        contentValues.put(FeedEntry.LOCATION_COLUMN_LAT, lat);
        contentValues.put(FeedEntry.LOCATION_COLUMN_LON, lon);
        db.insert(FeedEntry.LOCATION_TABLE_NAME, null, contentValues);
    }

    public void insertAllLocations(ArrayList<Location> locations){
        for (Location l:locations ) {
            insertLocation(l.getName(), l.getSsid(), l.getBle(),l.getLatitude(), l.getLongitude());
            Log.d("insertAllLocations: ","added to DB location" + l.getName());
        }
    }

    public ArrayList<Location> getAllLocations() {
        ArrayList<Location> locations = new ArrayList<Location>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
            FeedEntry._ID,
            FeedEntry.LOCATION_COLUMN_NAME,
            FeedEntry.LOCATION_COLUMN_SSID,
            FeedEntry.LOCATION_COLUMN_BLE,
            FeedEntry.LOCATION_COLUMN_LAT,
            FeedEntry.LOCATION_COLUMN_LON
        };

        // Filter results WHERE "title" = 'My Title'
        //String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
        //String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = FeedEntry.LOCATION_COLUMN_NAME + " DESC";

        Cursor cursor = db.query(
                FeedEntry.LOCATION_TABLE_NAME,            // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){
            Location location = new Location(
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.LOCATION_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.LOCATION_COLUMN_SSID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.LOCATION_COLUMN_BLE)),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(FeedEntry.LOCATION_COLUMN_LAT)),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(FeedEntry.LOCATION_COLUMN_LON))
            );
            locations.add(location);
            cursor.moveToNext();
        }
        return locations;
    }

    public ArrayList<String> getAllLocationsNames() {
        ArrayList<String> locations = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.LOCATION_COLUMN_NAME,
        };

        // Filter results WHERE "title" = 'My Title'
        //String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
        //String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = FeedEntry.LOCATION_COLUMN_NAME + " ASC";

        Cursor cursor = db.query(
                FeedEntry.LOCATION_TABLE_NAME,            // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){

            locations.add(cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.LOCATION_COLUMN_NAME)));
            cursor.moveToNext();
        }
        return locations;
    }
}
