package pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects;

import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by trosado on 15/04/17.
 */


public class Location {
    String name;
    String ssid;
    String ble;
    float latitude;
    float longitude;
    int radius;

    public Location() {  /* empty */  }

    public Location(String name, String ssid, String ble, float latitude, float longitude) {
        this.name = name;
        this.ssid = ssid;
        this.ble = ble;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        setDataNull(true);
        this.ssid = ssid;
    }

    public String getBle() {
        return ble;
    }

    public void setBle(String ble) {
        Log.d(TAG, "setBle: was called");
        setDataNull(true);
        this.ble = ble;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        setDataNull(false);
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        setDataNull(false);
        this.longitude = longitude;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        setDataNull(false);
        this.radius = radius;
    }

    private void setDataNull(boolean gps){
        if(gps){
            this.latitude = 0;
            this.longitude = 0;
        }else {
            this.ble = null;
            this.ssid = null;
        }
    }
}
