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
        public static final String COMMA_SEP = ",";


        public static final String LOCATION_TABLE_NAME = "LOCMESSLOCATION";
        public static final String LOCATION_COLUMN_NAME = "name";
        public static final String LOCATION_COLUMN_SSID = "ssid";
        public static final String LOCATION_COLUMN_BLE = "ble";
        public static final String LOCATION_COLUMN_LAT = "lat";
        public static final String LOCATION_COLUMN_LON = "lon";


        public static final String MULE_TABLE_NAME = "MULE";
        public static final String MULE_COLUMN_UUID = "uuid";
        public static final String MULE_COLUMN_CREATIONTIME = "creationTime";
        public static final String MULE_COLUMN_STARTTIME = "startTime";
        public static final String MULE_COLUMN_ENDTIME = "endTime";
        public static final String MULE_COLUMN_CONTENT = "content";
        public static final String MULE_COLUMN_PUBLISHER = "publisher";
        public static final String MULE_COLUMN_LOCATION = "location";
    }


}
