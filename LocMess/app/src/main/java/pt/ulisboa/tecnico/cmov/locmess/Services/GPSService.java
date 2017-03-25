package pt.ulisboa.tecnico.cmov.locmess.Services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.locmess.AddLocationActivity;

import static pt.ulisboa.tecnico.cmov.locmess.R.id.textView;

/**
 * Created by trosado on 24/03/17.
 */

public class GPSService extends Service {

    public final static String GPS = "GPS";


    private LocationListener listener;
    private LocationManager locationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        System.out.println("THis is a test");

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("Location change");
                Toast.makeText(getApplicationContext(),"GPS: "+location.getLatitude(),Toast.LENGTH_LONG);
                System.out.println("GPS Lat:"+location.getLatitude()+" Lon:"+location.getLongitude());
                Intent i = new Intent(GPS);
                i.putExtra("Lon",location.getLongitude());
                i.putExtra("Lat",location.getLatitude());
                sendBroadcast(i);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                System.out.println("Status change");

            }

            @Override
            public void onProviderEnabled(String s) {
                System.out.println("provider enabled");

            }

            @Override
            public void onProviderDisabled(String s) {
                System.out.println("provider disabled");
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,0,listener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }

}
