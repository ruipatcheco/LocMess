package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;


import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Message;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Profile;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.LocationNotFoundException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.MessageMuleNotFoundException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.MessageNotFoundException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.MultipleRowsAfectedException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.ProfileNotFoundException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.PublisherNotFoundException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderContract.FeedEntry;

import static android.R.attr.key;
import static android.content.ContentValues.TAG;

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
                    FeedEntry.LOCATION_COLUMN_RAD +" "+ FeedEntry.INT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.LOCATION_COLUMN_CENTRALIZED +" "+ FeedEntry.TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_MULE =

            "CREATE TABLE IF NOT EXISTS " + FeedEntry.MULE_MESSAGE_TABLE_NAME + " ( " +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.MULE_MESSAGE_COLUMN_UUID +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MULE_MESSAGE_COLUMN_CREATIONTIME +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MULE_MESSAGE_COLUMN_STARTTIME +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MULE_MESSAGE_COLUMN_ENDTIME +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MULE_MESSAGE_COLUMN_CONTENT +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MULE_MESSAGE_COLUMN_PUBLISHER +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MULE_MESSAGE_COLUMN_LOCATION +" "+ FeedEntry.TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_MESSAGE =

            "CREATE TABLE IF NOT EXISTS " + FeedEntry.MESSAGE_TABLE_NAME + " ( " +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.MESSAGE_COLUMN_UUID +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_CREATIONTIME +" "+ FeedEntry.LONG_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_STARTTIME +" "+ FeedEntry.LONG_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_ENDTIME +" "+ FeedEntry.LONG_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_CONTENT +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_PUBLISHER +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_LOCATION +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +

                    FeedEntry.MESSAGE_COLUMN_CENTRALIZED +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_ADDEDDECENTRALIZED +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_DELETEDDECENTRALIZED +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGE_COLUMN_NEARBY +" "+ FeedEntry.TEXT_TYPE +

                " )";

    private static final String SQL_CREATE_MESSAGEKEYS =

            "CREATE TABLE IF NOT EXISTS " + FeedEntry.MESSAGEKEYS_TABLE_NAME + " ( " +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.MESSAGEKEYS_COLUMN_UUID +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGEKEYS_COLUMN_KEY +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGEKEYS_COLUMN_VALUE +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MESSAGEKEYS_COLUMN_ISWHITE +" "+ FeedEntry.TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_PROFILE =

            "CREATE TABLE IF NOT EXISTS " + FeedEntry.PROFILE_TABLE_NAME + " ( " +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.PROFILE_COLUMN_KEY +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.PROFILE_COLUMN_VALUE +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.PROFILE_COLUMN_ADDEDDECENTRALIZED +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.PROFILE_COLUMN_DELETEDDECENTRALIZED +" "+ FeedEntry.TEXT_TYPE +
                " )";

    private static final String SQL_CREATE_SERVER_PROFILES =

            "CREATE TABLE IF NOT EXISTS " + FeedEntry.SERVER_PROFILES_TABLE_NAME + " ( " +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.SERVER_PROFILES_COLUMN_KEY +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.SERVER_PROFILES_COLUMN_VALUE +" "+ FeedEntry.TEXT_TYPE +
                " )";


    private static final String SQL_CREATE_MULE_PROFILE =

            "CREATE TABLE IF NOT EXISTS " + FeedEntry.MULE_PROFILE_TABLE_NAME + " ( " +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.MULE_PROFILE_COLUMN_UUID+" " + FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MULE_PROFILE_COLUMN_KEY +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MULE_PROFILE_COLUMN_VALUE +" "+ FeedEntry.TEXT_TYPE + FeedEntry.COMMA_SEP +
                    FeedEntry.MULE_PROFILE_COLUMN_ISWHITE+" "+ FeedEntry.TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ";

    private static FeedReaderDbHelper mInstance = null;

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static FeedReaderDbHelper getInstance(Context context){
        if (mInstance == null) {
            mInstance = new FeedReaderDbHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public void onCreate(SQLiteDatabase db) {
        /*db.execSQL(SQL_CREATE_LOCATION);
        db.execSQL(SQL_CREATE_MESSAGE);
        db.execSQL(SQL_CREATE_MULE);*/

        createLocationTable(db);
        createMessageTable(db);

        createMuleTable(db);
        createProfileTable(db);
        createMuleProfileTable(db);
        createServerProfilesTable(db);
        createMessageKeysTable(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over

        onDrop();
    }

    public void onDrop() {
        SQLiteDatabase db = this.getWritableDatabase();

        dropLocation();
        dropMessage();
        dropMule();
        dropServerProfiles();

        onCreate(db);
    }

    public void dropDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    public void onDowngrade(int oldVersion, int newVersion) {
        SQLiteDatabase db = this.getWritableDatabase();

        onUpgrade(db, oldVersion, newVersion);
    }

    // LOCATION

    public void createLocationTable(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_LOCATION);
    }

    public void dropLocation() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(db.isReadOnly())
            db.execSQL(SQL_DELETE_ENTRIES + FeedEntry.LOCATION_TABLE_NAME);
    }

    public void deleteAllLocations() {
        dropLocation();
        createLocationTable(this.getReadableDatabase());
    }

    public void insertLocation (String name, String ssid, String ble, float lat, float lon,int radius, String centralized) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.LOCATION_COLUMN_NAME, name);
        contentValues.put(FeedEntry.LOCATION_COLUMN_SSID, ssid);
        contentValues.put(FeedEntry.LOCATION_COLUMN_BLE, ble);
        contentValues.put(FeedEntry.LOCATION_COLUMN_LAT, lat);
        contentValues.put(FeedEntry.LOCATION_COLUMN_LON, lon);
        contentValues.put(FeedEntry.LOCATION_COLUMN_RAD, radius);
        contentValues.put(FeedEntry.LOCATION_COLUMN_CENTRALIZED, centralized);
        if(!db.isReadOnly())
            db.insert(FeedEntry.LOCATION_TABLE_NAME, null, contentValues);
        //Log.d("insertAllLocations: ","added to DB location " + name + ssid + ble + lat + lon + radius);
    }

    public void insertAllLocations(List<Location> locations){
        for (Location location : locations) {
            insertLocation(location.getName(), location.getSsid(), location.getBle(),location.getLatitude(), location.getLongitude(),location.getRadius(), FeedEntry.CENTRALIZED);
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
                FeedEntry.LOCATION_COLUMN_LON,
                FeedEntry.LOCATION_COLUMN_RAD
        };

        Cursor cursor = db.query(
                FeedEntry.LOCATION_TABLE_NAME,             // The table to query
                projection,                               // The columns to return
                FeedEntry.LOCATION_COLUMN_NAME + " = ?",            // The columns for the WHERE clause
                new String[]{name},                                     // The values for the WHERE clause
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
                FeedEntry.LOCATION_COLUMN_LON,
                FeedEntry.LOCATION_COLUMN_RAD
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

    public ArrayList<Location> getAllDescentralizedLocations() {
        ArrayList<Location> locations = new ArrayList<Location>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.LOCATION_COLUMN_NAME,
                FeedEntry.LOCATION_COLUMN_SSID,
                FeedEntry.LOCATION_COLUMN_BLE,
                FeedEntry.LOCATION_COLUMN_LAT,
                FeedEntry.LOCATION_COLUMN_LON,
                FeedEntry.LOCATION_COLUMN_RAD
        };

        String sortOrder = FeedEntry.LOCATION_COLUMN_NAME + " ASC";

        Cursor cursor = db.query(
                FeedEntry.LOCATION_TABLE_NAME,                      // The table to query
                projection,                                        // The columns to return
                FeedEntry.LOCATION_COLUMN_CENTRALIZED + " = ?",   // The columns for the WHERE clause
                new String[]{FeedEntry.DECENTRALIZED},           // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                          // don't filter by row groups
                sortOrder                                     // The sort order
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

    public boolean deleteLocation(String locationName) {
        SQLiteDatabase db = this.getWritableDatabase();

        String table = FeedEntry.LOCATION_TABLE_NAME;
        String whereClause = FeedEntry.LOCATION_COLUMN_NAME + " = ?";
        String[] whereArgs = new String[] { locationName };

        return db.delete(table, whereClause, whereArgs) > 0;
    }

    public String getLocationsNameHash() throws NoSuchAlgorithmException, IOException {

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

        ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        while(cursor.isAfterLast() == false){
            String name = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.LOCATION_COLUMN_NAME));
            //Log.d("Hash loc name -> ", name);
            bOutput.write(digest.digest(name.getBytes("UTF-8")));
            cursor.moveToNext();
        }

        byte[] locationsNameHash = digest.digest(bOutput.toByteArray());

        //Log.d("Hash ->" , new String(Base64.encode(locationsNameHash, Base64.DEFAULT)));
        return new String(Base64.encode(locationsNameHash, Base64.DEFAULT));
    }

    public void setCentralized(String locationName) throws LocationNotFoundException, MultipleRowsAfectedException {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(FeedEntry.LOCATION_COLUMN_CENTRALIZED, FeedEntry.CENTRALIZED);

        int result = db.update(FeedEntry.LOCATION_TABLE_NAME, cv, FeedEntry.LOCATION_COLUMN_NAME + " = ?", new String[] {locationName});

        if (result == 0) {
            throw new LocationNotFoundException();
        } else if (result > 1) {
            throw new MultipleRowsAfectedException();
        }
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

    public void dropMule() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(SQL_DELETE_ENTRIES + FeedEntry.MULE_MESSAGE_TABLE_NAME);
    }

    public void insertMessageMule (String uuid, long creationTime, long startTime, long endTime, String content, String publisher, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.MULE_MESSAGE_COLUMN_UUID, uuid);
        contentValues.put(FeedEntry.MULE_MESSAGE_COLUMN_CREATIONTIME, creationTime);
        contentValues.put(FeedEntry.MULE_MESSAGE_COLUMN_STARTTIME, startTime);
        contentValues.put(FeedEntry.MULE_MESSAGE_COLUMN_ENDTIME, endTime);
        contentValues.put(FeedEntry.MULE_MESSAGE_COLUMN_CONTENT, content);
        contentValues.put(FeedEntry.MULE_MESSAGE_COLUMN_PUBLISHER, publisher);
        contentValues.put(FeedEntry.MULE_MESSAGE_COLUMN_LOCATION, location);

        db.insert(FeedEntry.MULE_MESSAGE_TABLE_NAME, null, contentValues);
    }

    public void insertAllMessageMule (List<Message> muleMessages) {
        for (Message message : muleMessages) {
            insertMessageMule(message.getUUID().toString(), message.getCreationTime(), message.getStartTime(), message.getEndTime(), message.getContent(), message.getPublisher(), message.getLocation());
        }
    }

    public Message getMessageMule(String uuid) throws MessageMuleNotFoundException {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.MULE_MESSAGE_COLUMN_UUID,
                FeedEntry.MULE_MESSAGE_COLUMN_CREATIONTIME,
                FeedEntry.MULE_MESSAGE_COLUMN_STARTTIME,
                FeedEntry.MULE_MESSAGE_COLUMN_ENDTIME,
                FeedEntry.MULE_MESSAGE_COLUMN_CONTENT,
                FeedEntry.MULE_MESSAGE_COLUMN_PUBLISHER,
                FeedEntry.MULE_MESSAGE_COLUMN_LOCATION
        };

        Cursor cursor = db.query(
                FeedEntry.MULE_MESSAGE_TABLE_NAME,             // The table to query
                projection,                               // The columns to return
                FeedEntry.MULE_MESSAGE_COLUMN_UUID + " = ?",            // The columns for the WHERE clause
                new String[]{uuid},                                     // The values for the WHERE clause
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
                FeedEntry.MULE_MESSAGE_COLUMN_UUID,
                FeedEntry.MULE_MESSAGE_COLUMN_CREATIONTIME,
                FeedEntry.MULE_MESSAGE_COLUMN_STARTTIME,
                FeedEntry.MULE_MESSAGE_COLUMN_ENDTIME,
                FeedEntry.MULE_MESSAGE_COLUMN_CONTENT,
                FeedEntry.MULE_MESSAGE_COLUMN_PUBLISHER,
                FeedEntry.MULE_MESSAGE_COLUMN_LOCATION
        };

        Cursor cursor = db.query(
                FeedEntry.MULE_MESSAGE_TABLE_NAME,            // The table to query
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

    public boolean deleteMessageMule(String uuid) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(FeedEntry.MULE_MESSAGE_TABLE_NAME, FeedEntry.MULE_MESSAGE_COLUMN_UUID + "=" + uuid, null) > 0;
    }

    public boolean deleteListMessageMules(List<String> messagesMules) {
        SQLiteDatabase db = this.getWritableDatabase();

        boolean result = true;
        for (String messageMule : messagesMules) {
            if (!deleteMessageMule(messageMule)) {
                result = false;
            }
        }
        return result;
    }

    public void deleteAllMessageMules() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from "+ FeedEntry.MULE_MESSAGE_TABLE_NAME);
    }

    private Message associateMessageMule(Cursor cursor) {
        Boolean isCentralized = true;
        String isCentralizedAux = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_CENTRALIZED));
        if(isCentralizedAux.equals("false")){
            isCentralized = false;
        }


        return new Message(
                UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MULE_MESSAGE_COLUMN_UUID))),
                cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry.MULE_MESSAGE_COLUMN_CREATIONTIME)),
                cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry.MULE_MESSAGE_COLUMN_STARTTIME)),
                cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry.MULE_MESSAGE_COLUMN_ENDTIME)),
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MULE_MESSAGE_COLUMN_CONTENT)),
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MULE_MESSAGE_COLUMN_PUBLISHER)),
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MULE_MESSAGE_COLUMN_LOCATION)),
                isCentralized,
                false,
                null,
                null
        );
    }

    // MESSAGE

    public void createMessageTable(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_MESSAGE);
    }

    public void createMessageKeysTable(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_MESSAGEKEYS);
    }


    public void dropMessage() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(SQL_DELETE_ENTRIES + FeedEntry.MESSAGE_TABLE_NAME);
    }

    public void insertMessage(Message message) {
        insertMessage(
                message.getUUID().toString(),
                message.getCreationTime(),
                message.getStartTime(),
                message.getEndTime(),
                message.getContent(),
                message.getPublisher(),
                message.getLocation(),
                message.isCentralized(),
                message.isNearby(),
                message.getWhiteList(),
                message.getBlackList()
        );
    }

    public void insertMessage (String uuid, long creationTime, long startTime, long endTime, String content, String publisher, String location, boolean isCentralized, boolean isNearby, List<Profile> whiteList, List<Profile> blackList) {
        SQLiteDatabase db = this.getWritableDatabase();

        String centralized = "false";
        if(isCentralized){
            centralized = "true";
        }


        String nearby = "false";
        if(isNearby){
            nearby = "true";
        }


        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.MESSAGE_COLUMN_UUID, uuid);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_CREATIONTIME, creationTime);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_STARTTIME, startTime);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_ENDTIME, endTime);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_CONTENT, content);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_PUBLISHER, publisher);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_LOCATION, location);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_CENTRALIZED, centralized);

        contentValues.put(FeedEntry.MESSAGE_COLUMN_ADDEDDECENTRALIZED, "true");
        contentValues.put(FeedEntry.MESSAGE_COLUMN_DELETEDDECENTRALIZED, "false");
        contentValues.put(FeedEntry.MESSAGE_COLUMN_NEARBY, nearby);

        db.insert(FeedEntry.MESSAGE_TABLE_NAME, null, contentValues);

        insertMessageKeys(whiteList,uuid,"true");
        insertMessageKeys(blackList,uuid,"false");
    }

    private void insertMessageKeys(List<Profile> profileList, String uuid ,String isWhite) {

        //Log.d("DBHelper: ","insertMessageKeys, isWhite = " +isWhite);

        SQLiteDatabase db = this.getWritableDatabase();

        if(profileList != null){
            for(Profile p : profileList){
                ContentValues contentValues = new ContentValues();

                contentValues.put(FeedEntry.MESSAGEKEYS_COLUMN_UUID, uuid);
                contentValues.put(FeedEntry.MESSAGEKEYS_COLUMN_KEY, p.getKey());
                contentValues.put(FeedEntry.MESSAGEKEYS_COLUMN_VALUE, p.getValue());
                contentValues.put(FeedEntry.MESSAGEKEYS_COLUMN_ISWHITE, isWhite);

                //Log.d("DBHelper: ","insertMessageKeys inserting key/value/uuid = " +p.getKey() + p.getValue()+uuid);

                db.insert(FeedEntry.MESSAGEKEYS_TABLE_NAME, null, contentValues);
            }
        }

    }

    private ArrayList<Profile> getMessageKeys(String uuid, Boolean getWhitelist) throws MessageKeysNotFoundException {
        SQLiteDatabase db = this.getReadableDatabase();
        String isWhite = "false";
        if(getWhitelist){
            isWhite = "true";
        }

        Log.d("DBHelper: ", "getMessageKeys obtaining keys for message id -> " + uuid);
        Log.d("DBHelper: ", "getMessageKeys obtaining isWhite?" + isWhite);

        String[] projection = makeDefaultMessageKeysProjection();

        ArrayList<Profile> keys = new ArrayList<>();

        Cursor cursor = db.query(
                FeedEntry.MESSAGEKEYS_TABLE_NAME,             // The table to query
                projection,                               // The columns to return
                FeedEntry.MESSAGEKEYS_COLUMN_UUID + "= ? and " + FeedEntry.MESSAGEKEYS_COLUMN_ISWHITE + " = ?",    // The columns for the WHERE clause
                new String[]{uuid,isWhite},                       // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        if (cursor.getCount() == 0) {
            throw new MessageKeysNotFoundException();
        }

        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){
            keys.add(associateMessageKeys(cursor));
            cursor.moveToNext();
        }

        return keys;

    }

    private Profile associateMessageKeys(Cursor cursor) {

        String username = ServicesDataHolder.getInstance().getUsername();

        return new Profile(
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGEKEYS_COLUMN_KEY)),
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGEKEYS_COLUMN_VALUE)),
                username
        );
    }

    public void insertMessageFromServer(Message message) {

        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(message));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d("DBHelper: ", "inserting message from server -> "+jsonObject.toString());



        insertMessageFromServer(
                message.getUUID().toString(),
                message.getCreationTime(),
                message.getStartTime(),
                message.getEndTime(),
                message.getContent(),
                message.getPublisher(),
                message.getLocation(),
                message.isCentralized(),
                message.isNearby()
        );
    }

    public void insertMessageFromServer (String uuid, long creationTime, long startTime, long endTime, String content, String publisher, String location, boolean isCentralized, boolean isNearby) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.MESSAGE_COLUMN_UUID, uuid);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_CREATIONTIME, creationTime);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_STARTTIME, startTime);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_ENDTIME, endTime);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_CONTENT, content);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_PUBLISHER, publisher);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_LOCATION, location);
        contentValues.put(FeedEntry.MESSAGE_COLUMN_CENTRALIZED, "true");

        contentValues.put(FeedEntry.MESSAGE_COLUMN_ADDEDDECENTRALIZED, "false");
        contentValues.put(FeedEntry.MESSAGE_COLUMN_DELETEDDECENTRALIZED, "false");
        contentValues.put(FeedEntry.MESSAGE_COLUMN_NEARBY, "true");
        if(!db.isReadOnly())
            db.insert(FeedEntry.MESSAGE_TABLE_NAME, null, contentValues);
    }

    public void updateMessageInsertedToServer(String uuid) throws MultipleRowsAfectedException, MessageNotFoundException {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(FeedEntry.MESSAGE_COLUMN_ADDEDDECENTRALIZED, "false");

        int result = db.update(FeedEntry.MESSAGE_TABLE_NAME, cv, FeedEntry.MESSAGE_COLUMN_UUID+ " = ?", new String[] {uuid});

        if (result == 0) {
            throw new MessageNotFoundException();
        } else if (result > 1) {
            throw new MultipleRowsAfectedException();
        }
    }

    public void insertAllMessages(List<Message> messages){
        for (Message message : messages) {
            Gson gson = new Gson();
            //Log.d(TAG, "insertAllMessages: "+gson.toJson(message));
            insertMessage(message);
        }
    }

    public Message getMessage(String uuid) throws MessageNotFoundException {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = makeDefaultMessageProjection();

        Cursor cursor = db.query(
                FeedEntry.MESSAGE_TABLE_NAME,             // The table to query
                projection,                               // The columns to return
                FeedEntry.MESSAGE_COLUMN_UUID + "= ?",    // The columns for the WHERE clause
                new String[]{uuid},                       // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        if (cursor.getCount() == 0) {
            throw new MessageNotFoundException();
        }

        cursor.moveToFirst();

        return associateMessage(cursor);
    }

    public List<Message> getMessagesFromUser(String publisher) throws PublisherNotFoundException {
        //Log.d("DBHelper: ", "obtaining messages from username = " +publisher);

        ArrayList<Message> messages = new ArrayList<Message>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = makeDefaultMessageProjection();

        Cursor cursor = db.query(
                FeedEntry.MESSAGE_TABLE_NAME,                        // The table to query
                projection,                                         // The columns to return
                FeedEntry.MESSAGE_COLUMN_PUBLISHER + " = ? and "+FeedEntry.MESSAGE_COLUMN_NEARBY +" =?",       // The columns for the WHERE clause
                new String[]{publisher, "false"},                          // The values for the WHERE clause
                null,                                            // don't group the rows
                null,                                           // don't filter by row groups
                null                                           // The sort order
        );

        if (cursor.getCount() == 0) {
            throw new PublisherNotFoundException();
        }

        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){
            messages.add(associateMessage(cursor));
            cursor.moveToNext();
        }
        return messages;
    }

    public ArrayList<Message> getAllMessages() {
        ArrayList<Message> messages = new ArrayList<Message>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = makeDefaultMessageProjection();

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

    public ArrayList<Message> getAllNearbyMessages() {
        ArrayList<Message> messages = new ArrayList<Message>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = makeDefaultMessageProjection();

        Cursor cursor = db.query(
                FeedEntry.MESSAGE_TABLE_NAME,            // The table to query
                projection,                               // The columns to return
                FeedEntry.MESSAGE_COLUMN_NEARBY+ " = ? ",                                     // The columns for the WHERE clause
                new String[]{"true"},                                     // The values for the WHERE clause
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

    public ArrayList<Message> getAllMessagesDeletedWhileDecentralized() {
        ArrayList<Message> messages = new ArrayList<Message>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = makeDefaultMessageProjection();

        Cursor cursor = db.query(
                FeedEntry.MESSAGE_TABLE_NAME,            // The table to query
                projection,                               // The columns to return
                FeedEntry.MESSAGE_COLUMN_DELETEDDECENTRALIZED + " = ? ",                                     // The columns for the WHERE clause
                new String[]{"true"},                                     // The values for the WHERE clause
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

    public ArrayList<Message> getAllMessagesAddedWhileDecentralized() {
        ArrayList<Message> messages = new ArrayList<Message>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = makeDefaultMessageProjection();

        Cursor cursor = db.query(
                FeedEntry.MESSAGE_TABLE_NAME,            // The table to query
                projection,                               // The columns to return
                FeedEntry.MESSAGE_COLUMN_ADDEDDECENTRALIZED + " = ? ",                                     // The columns for the WHERE clause
                new String[]{"true"},                                     // The values for the WHERE clause
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

    public void deleteMessageInTheFuture(String uuid) throws MessageNotFoundException, MultipleRowsAfectedException {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(FeedEntry.MESSAGE_COLUMN_DELETEDDECENTRALIZED, "true");

        int result = db.update(FeedEntry.MESSAGE_TABLE_NAME, cv, FeedEntry.MESSAGE_COLUMN_UUID + " = ?", new String[] {uuid});

        if (result == 0) {
            throw new MessageNotFoundException();
        } else if (result > 1) {
            throw new MultipleRowsAfectedException();
        }
    }

    public boolean deleteAllMessagesExceptMyOwnAndDecentralized() {
        SQLiteDatabase db = this.getWritableDatabase();
        String username = ServicesDataHolder.getInstance().getUsername();

        String table = FeedEntry.MESSAGE_TABLE_NAME;
        String whereClause = FeedEntry.MESSAGE_COLUMN_PUBLISHER + " != ?" +" and "+ FeedEntry.MESSAGE_COLUMN_CENTRALIZED +" = ?";
        String[] whereArgs = new String[] { username, "false" };

        return db.delete(table, whereClause, whereArgs) > 0;
    }

    public boolean deleteAllNearbyMessages() {
        SQLiteDatabase db = this.getWritableDatabase();

        String table = FeedEntry.MESSAGE_TABLE_NAME;
        String whereClause = FeedEntry.MESSAGE_COLUMN_NEARBY + " = ?";
        String[] whereArgs = new String[] { "true" };

        int removed = 0;
        if(!db.isReadOnly())
            removed = db.delete(table, whereClause, whereArgs);

        //Log.d("DBService", "deleteAllNearbyMessages -> " + removed);

        return removed > 0;
    }

    public boolean deleteMessage(String uuid) {
        SQLiteDatabase db = this.getWritableDatabase();
            db.delete(FeedEntry.MESSAGEKEYS_TABLE_NAME, FeedEntry.MESSAGEKEYS_COLUMN_UUID + "= '" + uuid + "'", null);

        return db.delete(FeedEntry.MESSAGE_TABLE_NAME, FeedEntry.MESSAGE_COLUMN_UUID + "= '" + uuid + "'", null) > 0;
    }

    public boolean deleteAllMyMessagesNotNearby(String publisher) {
        SQLiteDatabase db = this.getWritableDatabase();

        String table = FeedEntry.MESSAGE_TABLE_NAME;
        String whereClause = FeedEntry.MESSAGE_COLUMN_PUBLISHER+ "=? and " + FeedEntry.MESSAGE_COLUMN_NEARBY+ " =? ";
        String[] whereArgs = new String[] { publisher, "false" };

        return db.delete(table, whereClause, whereArgs) > 0;
    }

    public boolean deleteListMessage(List<String> messages) {
        SQLiteDatabase db = this.getWritableDatabase();

        boolean result = true;
        for (String message : messages) {
            if (!deleteMessage(message)) {
                result = false;
            }
        }
        return result;
    }

    public void deleteAllMessages() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from "+ FeedEntry.MESSAGE_TABLE_NAME);
    }

    private Message associateMessage(Cursor cursor) {
        Boolean isCentralized = true;
        String isCentralizedAux = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_CENTRALIZED));
        if(isCentralizedAux.equals("false")){
            isCentralized = false;
        }

        Boolean isNearby = false;
        String isNearbyAux = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_NEARBY));
        if(isNearbyAux.equals("true")){
            isNearby = true;
        }

        String uuid = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_UUID));

        ArrayList<Profile> whiteList = new ArrayList<>();
        ArrayList<Profile> blackList = new ArrayList<>();
        try {
            whiteList = getMessageKeys(uuid, true);
        } catch (MessageKeysNotFoundException e) {
            e.printStackTrace();
        }

        try {
            blackList = getMessageKeys(uuid, false);
        } catch (MessageKeysNotFoundException e) {
            e.printStackTrace();
        }

        for(Profile p : whiteList){
            Log.d("AssociateMessage: ","whitelist -> " + p.getKey());
        }

        for(Profile p : blackList){
            Log.d("AssociateMessage: ","blacklist -> " + p.getKey());
        }


        return new Message(
                UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_UUID))),
                cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_CREATIONTIME)),
                cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_STARTTIME)),
                cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_ENDTIME)),
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_CONTENT)),
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_PUBLISHER)),
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.MESSAGE_COLUMN_LOCATION)),
                isCentralized,
                isNearby,
                whiteList,
                blackList
        );
    }

    private String[] makeDefaultMessageProjection() {
        return new String[] {
                FeedEntry._ID,
                FeedEntry.MESSAGE_COLUMN_UUID,
                FeedEntry.MESSAGE_COLUMN_CREATIONTIME,
                FeedEntry.MESSAGE_COLUMN_STARTTIME,
                FeedEntry.MESSAGE_COLUMN_ENDTIME,
                FeedEntry.MESSAGE_COLUMN_CONTENT,
                FeedEntry.MESSAGE_COLUMN_PUBLISHER,
                FeedEntry.MESSAGE_COLUMN_LOCATION,
                FeedEntry.MESSAGE_COLUMN_CENTRALIZED,
                FeedEntry.MESSAGE_COLUMN_ADDEDDECENTRALIZED,
                FeedEntry.MESSAGE_COLUMN_DELETEDDECENTRALIZED,
                FeedEntry.MESSAGE_COLUMN_NEARBY
        };
    }

    private String[] makeDefaultMessageKeysProjection() {
        return new String[] {
                FeedEntry._ID,
                FeedEntry.MESSAGEKEYS_COLUMN_UUID,
                FeedEntry.MESSAGEKEYS_COLUMN_KEY,
                FeedEntry.MESSAGEKEYS_COLUMN_VALUE,
                FeedEntry.MESSAGEKEYS_COLUMN_ISWHITE,
        };
    }

    // PROFILE

    public void createProfileTable(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_PROFILE);
    }

    public void dropProfile() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(SQL_DELETE_ENTRIES + FeedEntry.PROFILE_TABLE_NAME);
    }

    public void insertProfile (String key, String value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.PROFILE_COLUMN_KEY, key);
        contentValues.put(FeedEntry.PROFILE_COLUMN_VALUE, value);
        contentValues.put(FeedEntry.PROFILE_COLUMN_ADDEDDECENTRALIZED, "true");
        contentValues.put(FeedEntry.PROFILE_COLUMN_DELETEDDECENTRALIZED, "false");


        db.insert(FeedEntry.PROFILE_TABLE_NAME, null, contentValues);
    }

    public void insertProfile(Profile profile) {
        insertProfile(profile.getKey(), profile.getValue());
    }

    public void insertProfileFromServer (String key, String value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.PROFILE_COLUMN_KEY, key);
        contentValues.put(FeedEntry.PROFILE_COLUMN_VALUE, value);
        contentValues.put(FeedEntry.PROFILE_COLUMN_ADDEDDECENTRALIZED, "false");
        contentValues.put(FeedEntry.PROFILE_COLUMN_DELETEDDECENTRALIZED, "false");


        db.insert(FeedEntry.PROFILE_TABLE_NAME, null, contentValues);
    }

    public void insertAllProfiles(HashMap<String, String> profiles){
        for (String key : profiles.keySet()) {
            insertProfile(key, profiles.get(key));
        }
    }

    public void insertAllProfilesFromServer(List<Profile> profiles){
        for (Profile p : profiles) {
            insertProfileFromServer(p.getKey(), p.getValue());
        }
    }

    public String getProfileValue(String key) throws ProfileNotFoundException {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.PROFILE_COLUMN_KEY,
                FeedEntry.PROFILE_COLUMN_VALUE,
                FeedEntry.PROFILE_COLUMN_ADDEDDECENTRALIZED,
                FeedEntry.PROFILE_COLUMN_DELETEDDECENTRALIZED
        };



        Cursor cursor = db.query(
                FeedEntry.PROFILE_TABLE_NAME,             // The table to query
                projection,                               // The columns to return
                FeedEntry.PROFILE_COLUMN_KEY + " = ? " + FeedEntry.PROFILE_COLUMN_DELETEDDECENTRALIZED + " = ? ",            // The columns for the WHERE clause
                new String[]{key, "false"},                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                   // The sort order
        );

        if (cursor.getCount() == 0) {
            throw new ProfileNotFoundException();
        }

        cursor.moveToFirst();

        return associateProfile(cursor).getValue();
    }

    public HashMap<String, String> getAllProfiles() {
        HashMap<String, String> profiles = new HashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.PROFILE_COLUMN_KEY,
                FeedEntry.PROFILE_COLUMN_VALUE,
                FeedEntry.PROFILE_COLUMN_ADDEDDECENTRALIZED,
                FeedEntry.PROFILE_COLUMN_DELETEDDECENTRALIZED
        };

        Cursor cursor = db.query(
                FeedEntry.PROFILE_TABLE_NAME,            // The table to query
                projection,                               // The columns to return
                FeedEntry.PROFILE_COLUMN_DELETEDDECENTRALIZED + " = ? ",                                     // The columns for the WHERE clause
                new String[]{"false"},                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                   // The sort order
        );

        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){
            Profile profile = associateProfile(cursor);
            profiles.put(profile.getKey(), profile.getValue());
            cursor.moveToNext();
        }
        return profiles;
    }

    public List<Profile> getMyProfiles() {
        ArrayList<Profile> profiles = new ArrayList<Profile>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.PROFILE_COLUMN_KEY,
                FeedEntry.PROFILE_COLUMN_VALUE,
                FeedEntry.PROFILE_COLUMN_ADDEDDECENTRALIZED,
                FeedEntry.PROFILE_COLUMN_DELETEDDECENTRALIZED
        };

        String sortOrder = FeedEntry.PROFILE_COLUMN_KEY + " ASC";

        Cursor cursor = db.query(
                FeedEntry.PROFILE_TABLE_NAME,            // The table to query
                projection,                               // The columns to return
                FeedEntry.PROFILE_COLUMN_DELETEDDECENTRALIZED + " = ? ",                                     // The columns for the WHERE clause
                new String[]{"false"},                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                   // The sort order
        );

        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){
            profiles.add(associateProfile(cursor));
            cursor.moveToNext();
        }
        return profiles;
    }

    public boolean deleteProfile(String key) {
        SQLiteDatabase db = this.getWritableDatabase();

        String table = FeedEntry.PROFILE_TABLE_NAME;
        String whereClause = FeedEntry.PROFILE_COLUMN_KEY + "=?";
        String[] whereArgs = new String[] { key };

        return db.delete(table, whereClause, whereArgs) > 0;
    }

    public void deleteProfileInTheFuture(String key) throws ProfileNotFoundException, MultipleRowsAfectedException {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(FeedEntry.PROFILE_COLUMN_DELETEDDECENTRALIZED, "true");

        int result = db.update(FeedEntry.PROFILE_TABLE_NAME, cv, FeedEntry.PROFILE_COLUMN_KEY + " = ?", new String[] {key});

        if (result == 0) {
            throw new ProfileNotFoundException();
        } else if (result > 1) {
            throw new MultipleRowsAfectedException();
        }
    }

    public void updateProfileInsertedToServer(String key, String value) throws ProfileNotFoundException, MultipleRowsAfectedException {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(FeedEntry.PROFILE_COLUMN_ADDEDDECENTRALIZED, "false");

        String table = FeedEntry.PROFILE_TABLE_NAME;
        String whereClause = FeedEntry.PROFILE_COLUMN_KEY + " = ?" +" and "+ FeedEntry.PROFILE_COLUMN_VALUE +" = ?";
        String[] whereArgs = new String[] { key, value };

        int result = db.update(table,cv, whereClause, whereArgs);

        if (result == 0) {
            throw new ProfileNotFoundException();
        } else if (result > 1) {
            throw new MultipleRowsAfectedException();
        }
    }

    public ArrayList<Profile> getAllProfilesToRemoveFromServer() {
        ArrayList<Profile> profiles = new ArrayList<Profile>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.PROFILE_COLUMN_KEY,
                FeedEntry.PROFILE_COLUMN_VALUE,
                FeedEntry.PROFILE_COLUMN_ADDEDDECENTRALIZED,
                FeedEntry.PROFILE_COLUMN_DELETEDDECENTRALIZED
        };

        String sortOrder = FeedEntry.PROFILE_COLUMN_KEY + " ASC";

        Cursor cursor = db.query(
                FeedEntry.PROFILE_TABLE_NAME,            // The table to query
                projection,                               // The columns to return
                FeedEntry.PROFILE_COLUMN_DELETEDDECENTRALIZED + " = ? ",                                     // The columns for the WHERE clause
                new String[]{"true"},                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                   // The sort order
        );

        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){
            profiles.add(associateProfile(cursor));
            cursor.moveToNext();
        }
        return profiles;
    }

    public ArrayList<Profile> getAllProfilesAddedWhileDecentralized() {
        ArrayList<Profile> profiles = new ArrayList<Profile>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.PROFILE_COLUMN_KEY,
                FeedEntry.PROFILE_COLUMN_VALUE,
                FeedEntry.PROFILE_COLUMN_ADDEDDECENTRALIZED,
                FeedEntry.PROFILE_COLUMN_DELETEDDECENTRALIZED
        };

        String sortOrder = FeedEntry.PROFILE_COLUMN_KEY + " ASC";

        Cursor cursor = db.query(
                FeedEntry.PROFILE_TABLE_NAME,            // The table to query
                projection,                               // The columns to return
                FeedEntry.PROFILE_COLUMN_ADDEDDECENTRALIZED + " = ? ",                                     // The columns for the WHERE clause
                new String[]{"true"},                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                   // The sort order
        );

        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){
            profiles.add(associateProfile(cursor));
            cursor.moveToNext();
        }
        return profiles;
    }

    public boolean deleteProfiles(HashMap<String, String> profiles) {
        SQLiteDatabase db = this.getWritableDatabase();

        boolean result = true;
        for (String key : profiles.keySet()) {
            if (!deleteMessage(key)) {
                result = false;
            }
        }
        return result;
    }

    public void deleteAllProfiles() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from "+ FeedEntry.PROFILE_TABLE_NAME);
    }

    private Profile associateProfile(Cursor cursor) {
        String username = ServicesDataHolder.getInstance().getUsername();
        return new Profile(
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.PROFILE_COLUMN_KEY)),
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.PROFILE_COLUMN_VALUE)),
                username
        );
    }


    // SERVERPROFILES

    public void createServerProfilesTable(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_SERVER_PROFILES);
    }

    public void dropServerProfiles() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(SQL_DELETE_ENTRIES + FeedEntry.SERVER_PROFILES_TABLE_NAME);
    }

    public void insertServerProfiles (String key, String value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.SERVER_PROFILES_COLUMN_KEY, key);
        contentValues.put(FeedEntry.SERVER_PROFILES_COLUMN_VALUE, value);


        db.insert(FeedEntry.SERVER_PROFILES_TABLE_NAME, null, contentValues);
    }

    public void insertAllServerProfiles(List<Profile> profiles){
        for (Profile profile : profiles) {
            insertServerProfiles(profile.getKey(), profile.getValue());
        }
    }

    public List<Profile> getListAllServerProfiles() {
        ArrayList<Profile> profiles = new ArrayList<Profile>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.PROFILE_COLUMN_KEY,
                FeedEntry.PROFILE_COLUMN_VALUE
        };

        String sortOrder = FeedEntry.SERVER_PROFILES_COLUMN_KEY + " ASC";

        Cursor cursor = db.query(
                FeedEntry.SERVER_PROFILES_TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){
            profiles.add(associateServerProfiles(cursor));
            cursor.moveToNext();
        }
        return profiles;
    }

    public void deleteAllServerProfiles() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(!db.isReadOnly())
            db.execSQL("delete from "+ FeedEntry.SERVER_PROFILES_TABLE_NAME);
    }

    private Profile associateServerProfiles(Cursor cursor) {
        return new Profile(
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.PROFILE_COLUMN_KEY)),
                cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.PROFILE_COLUMN_VALUE))
        );
    }


    // PROFILEMULE

    public void createMuleProfileTable(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_MULE_PROFILE);
    }

    public void dropMULEProfile() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(SQL_DELETE_ENTRIES + FeedEntry.MULE_PROFILE_TABLE_NAME);
    }

    public void insertMuleProfile (String uuid, String key, String value, boolean isWhite) {
        SQLiteDatabase db = this.getWritableDatabase();
        String isWhiteHotfix = "false";
        if(isWhite){
            isWhiteHotfix = "true";
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.MULE_PROFILE_COLUMN_UUID, uuid);
        contentValues.put(FeedEntry.MULE_PROFILE_COLUMN_KEY, key);
        contentValues.put(FeedEntry.MULE_PROFILE_COLUMN_VALUE, value);
        contentValues.put(FeedEntry.MULE_PROFILE_COLUMN_ISWHITE, isWhiteHotfix);

        db.insert(FeedEntry.MULE_PROFILE_TABLE_NAME, null, contentValues);
    }

    public List<Profile> getMuleProfileWhiteList(String uuid) throws ProfileNotFoundException {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.MULE_PROFILE_COLUMN_UUID,
                FeedEntry.MULE_PROFILE_COLUMN_KEY,
                FeedEntry.MULE_PROFILE_COLUMN_VALUE,
                FeedEntry.MULE_PROFILE_COLUMN_ISWHITE,
        };

        Cursor cursor = db.query(
                FeedEntry.MULE_PROFILE_TABLE_NAME,             // The table to query
                projection,                               // The columns to return
                FeedEntry.MULE_PROFILE_COLUMN_UUID+ " = ?" + FeedEntry.MULE_PROFILE_COLUMN_ISWHITE + " = ?",            // The columns for the WHERE clause
                new String[]{uuid, "true"},                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                   // The sort order
        );

        if (cursor.getCount() == 0) {
            throw new ProfileNotFoundException();
        }

        cursor.moveToFirst();
        ArrayList<Profile> whiteList = new ArrayList<>();

        while(cursor.isAfterLast() == false){
            whiteList.add(associateProfile(cursor));
            cursor.moveToNext();
        }

        return whiteList;
    }


    public List<Profile> getMuleProfileBlackList(String uuid) throws ProfileNotFoundException {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.MULE_PROFILE_COLUMN_UUID,
                FeedEntry.MULE_PROFILE_COLUMN_KEY,
                FeedEntry.MULE_PROFILE_COLUMN_VALUE,
                FeedEntry.MULE_PROFILE_COLUMN_ISWHITE,
        };

        Cursor cursor = db.query(
                FeedEntry.MULE_PROFILE_TABLE_NAME,             // The table to query
                projection,                               // The columns to return
                FeedEntry.MULE_PROFILE_COLUMN_UUID+ " = ?" + FeedEntry.MULE_PROFILE_COLUMN_ISWHITE + " = ?",            // The columns for the WHERE clause
                new String[]{uuid, "false"},                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                   // The sort order
        );

        if (cursor.getCount() == 0) {
            throw new ProfileNotFoundException();
        }

        cursor.moveToFirst();
        ArrayList<Profile> whiteList = new ArrayList<>();

        while(cursor.isAfterLast() == false){
            whiteList.add(associateProfile(cursor));
            cursor.moveToNext();
        }

        return whiteList;
    }

    public boolean deleteAllMuleProfilesOfMessageID(String uuid) {
        SQLiteDatabase db = this.getWritableDatabase();

        String table = FeedEntry.MULE_PROFILE_TABLE_NAME;
        String whereClause = FeedEntry.MULE_PROFILE_COLUMN_UUID+ "=?";
        String[] whereArgs = new String[] { uuid };

        return db.delete(table, whereClause, whereArgs) > 0;
    }


    public void deleteAllMuleProfiles() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from "+ FeedEntry.MULE_PROFILE_TABLE_NAME);
    }

    public void deleteAll() {
        this.deleteAllMessages();
        this.deleteAllLocations();
        this.deleteAllMessageMules();
        this.deleteAllProfiles();
        this.deleteAllMuleProfiles();
        this.deleteAllServerProfiles();
        this.deleteAllNearbyMessages();
    }
}
