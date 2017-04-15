package pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects;

/**
 * Created by trosado on 15/04/17.
 */

public class Location {
    private int radius;
    private String name;
    private float Latitude;
    private float Longitude;
    private String ssid;
    private String ble;

    public Location(String name, int radius, float latitude, float longitude) {
        this.radius = radius;
        this.name = name;
        Latitude = latitude;
        Longitude = longitude;
    }

    public Location(String name, String val, boolean isBle) {
        this.name = name;
        if(isBle)
            this.ble=val;
        else
            this.ssid = val;

    }

    public int getRadius() {
        return radius;
    }

    public String getName() {
        return name;
    }

    public float getLatitude() {
        return Latitude;
    }

    public float getLongitude() {
        return Longitude;
    }

    public String getSsid() {
        return ssid;
    }

    public String getBle() {
        return ble;
    }
}
