package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Message;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.LocationNotFoundException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.MessageMuleNotFoundException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.MessageNotFoundException;
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
                    FeedEntry.LOCATION_COLUMN_LON +" "+ FeedEntry.FLOAT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.LOCATION_COLUMN_RAD +" "+ FeedEntry.INT_TYPE +
                    " )";

    private static final String SQL_CREATE_MULE =

            "CREATE TABLE IF NOT EXISTS " + FeedEntry.MULE_TABLE_NAME + " ( " +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.MULE_COLUMN_ID +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MULE_COLUMN_UUID +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
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
                    FeedEntry.MESSAGE_COLUMN_UUID +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_CREATIONTIME +" "+ FeedEntry.LONG_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_STARTTIME +" "+ FeedEntry.LONG_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_ENDTIME +" "+ FeedEntry.LONG_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_CONTENT +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_PUBLISHER +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_LOCATION +" "+ FeedEntry.TEXT_TYPE +
                " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        createLocationTable(db);
        createMessageTable(db);
        createMuleTable(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        onDrop(db);
    }

    public void onDrop(SQLiteDatabase db) {
        dropLocation(db);
        dropMessage(db);
        dropMule(db);

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // LOCATION

    public void createLocationTable(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LOCATION);
    }

    public void dropLocation(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES + FeedEntry.LOCATION_TABLE_NAME);
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
        Log.d("insertAllLocations: ","added to DB location " + name + ssid + ble + lat + lon);
    }

    public void insertAllLocations(List<Location> locations){
        for (Location location : locations) {
            insertLocation(location.getName(), location.getSsid(), location.getBle(),location.getLatitude(), location.getLongitude());
        }
    }

    public Location getLocation(String name) throws LocationNotFoundException {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.LOCATION_COLUMN_NAME,
                FeedEntry.LOCATION_COLUMN_SSID,
                FeedEntry.LOCATION_COLUMN_BLE,
                FeedEntry.LOCATION_COLUMN_LAT,
                FeedEntry.LOCATION_COLUMN_LON
        };

        Cursor cursor = db.query(
                FeedEntry.LOCATION_TABLE_NAME,             // The table to query
                projection,                               // The columns to return
                FeedEntry.LOCATION_COLUMN_NAME,            // The columns for the WHERE clause
                new String[]{FeedEntry.LOCATION_COLUMN_NAME + "=" + name},                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                   // The sort order
        );

        if (cursor.getCount() == 0) {
            throw new LocationNotFoundException();
        }

        cursor.moveToFirst();

        return associateLocation(cursor);
    }

    public ArrayList<Location> getAllLocations() {
        ArrayList<Location> locations = new ArrayList<Location>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.LOCATION_COLUMN_NAME,
                FeedEntry.LOCATION_COLUMN_SSID,
                FeedEntry.LOCATION_COLUMN_BLE,
                FeedEntry.LOCATION_COLUMN_LAT,
                FeedEntry.LOCATION_COLUMN_LON
        };

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
            locations.add(associateLocation(cursor));
            cursor.moveToNext();
        }
        return locations;
    }

    public ArrayList<String> getAllLocationsNames() {
        ArrayList<String> locations = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.LOCATION_COLUMN_NAME,
        };
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

    private Location associateLocation(Cursor cursor) {
        return new Location(
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.LOCATION_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.LOCATION_COLUMN_SSID)),
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.LOCATION_COLUMN_BLE)),
                cursor.getFloat(cursor.getColumnIndexOrThrow(FeedEntry.LOCATION_COLUMN_LAT)),
                cursor.getFloat(cursor.getColumnIndexOrThrow(FeedEntry.LOCATION_COLUMN_LON)),
                cursor.getInt(cursor.getColumnIndexOrThrow(FeedEntry.LOCATION_COLUMN_RAD))
        );
    }

    // MULE

    public void createMuleTable(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MULE);
    }

    public void dropMule(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES + FeedEntry.MULE_TABLE_NAME);
    }

    public void insertMessageMule (String uuid, long creationTime, long startTime, long endTime, String content, String publisher, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.MULE_COLUMN_ID, "");
        contentValues.put(FeedEntry.MULE_COLUMN_UUID, uuid);
        contentValues.put(FeedEntry.MULE_COLUMN_CREATIONTIME, creationTime);
        contentValues.put(FeedEntry.MULE_COLUMN_STARTTIME, startTime);
        contentValues.put(FeedEntry.MULE_COLUMN_ENDTIME, endTime);
        contentValues.put(FeedEntry.MULE_COLUMN_CONTENT, content);
        contentValues.put(FeedEntry.MULE_COLUMN_PUBLISHER, publisher);
        contentValues.put(FeedEntry.MULE_COLUMN_LOCATION, location);

        db.insert(FeedEntry.MULE_TABLE_NAME, null, contentValues);
    }

    public void insertAllMessageMule (List<Message> muleMessages) {
        for (Message message : muleMessages) {
            insertMessageMule(message.getUUID(), message.getCreationTime(), message.getStartTime(), message.getEndTime(), message.getContent(), message.getPublisher(), message.getLocation());
        }
    }

    public Message getMessageMule(String uuid) throws MessageMuleNotFoundException {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.MULE_COLUMN_UUID,
                FeedEntry.MULE_COLUMN_CREATIONTIME,
                FeedEntry.MULE_COLUMN_STARTTIME,
                FeedEntry.MULE_COLUMN_ENDTIME,
                FeedEntry.MULE_COLUMN_CONTENT,
                FeedEntry.MULE_COLUMN_PUBLISHER,
                FeedEntry.MULE_COLUMN_LOCATION
        };

        Cursor cursor = db.query(
                FeedEntry.MULE_TABLE_NAME,             // The table to query
                projection,                               // The columns to return
                FeedEntry.MULE_COLUMN_UUID,            // The columns for the WHERE clause
                new String[]{FeedEntry.MULE_COLUMN_UUID + "=" + uuid},                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                   // The sort order
        );

        if (cursor.getCount() == 0) {
            throw new MessageMuleNotFoundException();
        }

        cursor.moveToFirst();

        return associateMessageMule(cursor);
    }

    public ArrayList<Message> getAllMuleMessages() {
        ArrayList<Message> messages = new ArrayList<Message>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.MULE_COLUMN_CREATIONTIME,
                FeedEntry.MULE_COLUMN_STARTTIME,
                FeedEntry.MULE_COLUMN_ENDTIME,
                FeedEntry.MULE_COLUMN_CONTENT,
                FeedEntry.MULE_COLUMN_PUBLISHER,
                FeedEntry.MULE_COLUMN_LOCATION
        };

        Cursor cursor = db.query(
                FeedEntry.MULE_TABLE_NAME,            // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                   // The sort order
        );

        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){
            messages.add(associateMessageMule(cursor));
            cursor.moveToNext();
        }
        return messages;
    }

    public boolean deleteMessageMule(SQLiteDatabase db, String uuid) {
        return db.delete(FeedEntry.MULE_TABLE_NAME, FeedEntry.MULE_COLUMN_UUID + "=" + uuid, null) > 0;
    }

    public boolean deleteListMessageMules(SQLiteDatabase db, List<String> messagesMules) {
        boolean result = true;
        for (String messageMule : messagesMules) {
            if (!deleteMessageMule(db, messageMule)) {
                result = false;
            }
        }
        return result;
    }

    public void deleteAllMessageMules(SQLiteDatabase db) {
        db.execSQL("delete from "+ FeedEntry.MULE_TABLE_NAME);
    }

    private Message associateMessageMule(Cursor cursor) {
        return new Message(
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MULE_COLUMN_UUID)),
                cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry.MULE_COLUMN_CREATIONTIME)),
                cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry.MULE_COLUMN_STARTTIME)),
                cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry.MULE_COLUMN_ENDTIME)),
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MULE_COLUMN_CONTENT)),
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MULE_COLUMN_PUBLISHER)),
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MULE_COLUMN_LOCATION))
        );
    }

    // MESSAGE

    public void createMessageTable(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MESSAGE);
    }

    public void dropMessage(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES + FeedEntry.MESSAGE_TABLE_NAME);
    }

    public void insertMessage (String uuid, long creationTime, long startTime, long endTime, String content, String publisher, String location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.MESSAGE_COLUMN_ID, "");
        contentValues.put(FeedEntry.MESSAGE_COLUMN_UUID, uuid);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_CREATIONTIME, creationTime);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_STARTTIME, startTime);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_ENDTIME, endTime);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_CONTENT, content);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_PUBLISHER, publisher);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_LOCATION, location);

        db.insert(FeedEntry.MESSAGE_TABLE_NAME, null, contentValues);
    }

    public void insertAllMessages(List<Message> messages){
        for (Message message : messages) {
            insertMessage(message.getUUID(), message.getCreationTime(), message.getStartTime(), message.getEndTime(), message.getContent(), message.getPublisher(), message.getLocation());
        }
    }

    public Message getMessage(String uuid) throws MessageNotFoundException {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.MESSAGE_COLUMN_UUID,
                FeedEntry.MESSAGE_COLUMN_CREATIONTIME,
                FeedEntry.MESSAGE_COLUMN_STARTTIME,
                FeedEntry.MESSAGE_COLUMN_ENDTIME,
                FeedEntry.MESSAGE_COLUMN_CONTENT,
                FeedEntry.MESSAGE_COLUMN_PUBLISHER,
                FeedEntry.MESSAGE_COLUMN_LOCATION
        };

        Cursor cursor = db.query(
                FeedEntry.MESSAGE_TABLE_NAME,             // The table to query
                projection,                               // The columns to return
                FeedEntry.MESSAGE_COLUMN_UUID,            // The columns for the WHERE clause
                new String[]{FeedEntry.MESSAGE_COLUMN_UUID + "=" + uuid},                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                   // The sort order
        );

        if (cursor.getCount() == 0) {
            throw new MessageNotFoundException();
        }

        cursor.moveToFirst();

        return associateMessage(cursor);
    }

    public ArrayList<Message> getAllMessages() {
        ArrayList<Message> messages = new ArrayList<Message>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.MESSAGE_COLUMN_UUID,
                FeedEntry.MESSAGE_COLUMN_CREATIONTIME,
                FeedEntry.MESSAGE_COLUMN_STARTTIME,
                FeedEntry.MESSAGE_COLUMN_ENDTIME,
                FeedEntry.MESSAGE_COLUMN_CONTENT,
                FeedEntry.MESSAGE_COLUMN_PUBLISHER,
                FeedEntry.MESSAGE_COLUMN_LOCATION
        };

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
            messages.add(associateMessage(cursor));
            cursor.moveToNext();
        }
        return messages;
    }

    public boolean deleteMessage(SQLiteDatabase db, String uuid) {
        return db.delete(FeedEntry.MESSAGE_TABLE_NAME, FeedEntry.MESSAGE_COLUMN_UUID + "=" + uuid, null) > 0;
    }

    public boolean deleteListMessage(SQLiteDatabase db, List<String> messages) {
        boolean result = true;
        for (String message : messages) {
            if (!deleteMessage(db, message)) {
                result = false;
            }
        }
        return result;
    }

    public void deleteAllMessages(SQLiteDatabase db) {
        db.execSQL("delete from "+ FeedEntry.MESSAGE_TABLE_NAME);
    }

    private Message associateMessage(Cursor cursor) {
        return new Message(
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_UUID)),
                cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_CREATIONTIME)),
                cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_STARTTIME)),
                cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_ENDTIME)),
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_CONTENT)),
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_PUBLISHER)),
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_LOCATION))
        );
    }
}
