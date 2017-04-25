package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils;

import android.provider.BaseColumns;

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


        public static final String LOCATION_TABLE_NAME = "location";
        public static final String LOCATION_COLUMN_NAME = "name";
        public static final String LOCATION_COLUMN_SSID = "ssid";
        public static final String LOCATION_COLUMN_BLE = "ble";
        public static final String LOCATION_COLUMN_LAT = "lat";
        public static final String LOCATION_COLUMN_LON = "lon";
    }
}
