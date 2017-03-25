package pt.ulisboa.tecnico.cmov.locmess.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by trosado on 24/03/17.
 */

public class LocationReceiver extends BroadcastReceiver {
    double latitude;
    double longitude;


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.latitude = intent.getDoubleExtra("Lat",0);
        this.longitude = intent.getDoubleExtra("Lon",0);
    }
}
