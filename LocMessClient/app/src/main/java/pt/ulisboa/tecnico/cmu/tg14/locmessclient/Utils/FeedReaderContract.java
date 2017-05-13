package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils;

import android.provider.BaseColumns;

import java.security.Timestamp;

/**
 * Created by brigadinhos on 25/04/2017.
 */

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TEXT_TYPE = "text";
        public static final String FLOAT_TYPE = "float";
        public static final String LONG_TYPE = "long";
        public static final String INT_TYPE = "int";
        public static final String COMMA_SEP = ",";

        public static final String CENTRALIZED = "true";
        public static final String DECENTRALIZED = "false";


        public static final String LOCATION_TABLE_NAME = "LOCATION";
        public static final String LOCATION_COLUMN_NAME = "name";
        public static final String LOCATION_COLUMN_SSID = "ssid";
        public static final String LOCATION_COLUMN_BLE = "ble";
        public static final String LOCATION_COLUMN_LAT = "lat";
        public static final String LOCATION_COLUMN_LON = "lon";
        public static final String LOCATION_COLUMN_RAD = "radius";
        public static final String LOCATION_COLUMN_CENTRALIZED = "centralized";


        public static final String MULE_MESSAGE_TABLE_NAME = "MULE";
        public static final String MULE_MESSAGE_COLUMN_UUID = "uuid";
        public static final String MULE_MESSAGE_COLUMN_CREATIONTIME = "creationTime";
        public static final String MULE_MESSAGE_COLUMN_STARTTIME = "startTime";
        public static final String MULE_MESSAGE_COLUMN_ENDTIME = "endTime";
        public static final String MULE_MESSAGE_COLUMN_CONTENT = "content";
        public static final String MULE_MESSAGE_COLUMN_PUBLISHER = "publisher";
        public static final String MULE_MESSAGE_COLUMN_LOCATION = "location";



        public static final String MESSAGE_TABLE_NAME = "MESSAGE";
        public static final String MESSAGE_COLUMN_UUID = "uuid";
        public static final String MESSAGE_COLUMN_CREATIONTIME = "creationTime";
        public static final String MESSAGE_COLUMN_STARTTIME = "startTime";
        public static final String MESSAGE_COLUMN_ENDTIME = "endTime";
        public static final String MESSAGE_COLUMN_CONTENT = "content";
        public static final String MESSAGE_COLUMN_PUBLISHER = "publisher";
        public static final String MESSAGE_COLUMN_LOCATION = "location";
        public static final String MESSAGE_COLUMN_CENTRALIZED = "centralized";
        public static final String MESSAGE_COLUMN_ADDEDDECENTRALIZED = "addedDecentralized";
        public static final String MESSAGE_COLUMN_DELETEDDECENTRALIZED= "deletedDecentralized";
        public static final String MESSAGE_COLUMN_NEARBY= "nearby";



        public static final String PROFILE_TABLE_NAME = "PROFILE";
        public static final String PROFILE_COLUMN_KEY = "key";
        public static final String PROFILE_COLUMN_VALUE = "value";
        public static final String PROFILE_COLUMN_ADDEDDECENTRALIZED = "addedDecentralized";
        public static final String PROFILE_COLUMN_DELETEDDECENTRALIZED = "deletedDecentralized";



        public static final String SERVER_PROFILES_TABLE_NAME = "SERVERPROFILE";
        public static final String SERVER_PROFILES_COLUMN_KEY = "key";
        public static final String SERVER_PROFILES_COLUMN_VALUE = "value";



        public static final String MULE_PROFILE_TABLE_NAME = "MULEPROFILE";
        public static final String MULE_PROFILE_COLUMN_UUID = "uuid";
        public static final String MULE_PROFILE_COLUMN_KEY = "key";
        public static final String MULE_PROFILE_COLUMN_VALUE = "value";
        public static final String MULE_PROFILE_COLUMN_ISWHITE = "isWhite";
    }


}
