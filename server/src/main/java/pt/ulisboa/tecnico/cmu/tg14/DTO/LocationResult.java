package pt.ulisboa.tecnico.cmu.tg14.DTO;

/**
 * Created by trosado on 4/23/17.
 */
public class LocationResult {

    String name;
    String ssid;
    String ble;
    double latitude;
    double longitude;
    int radius;

    public LocationResult(String name, String ssid, String ble, double latitude, double longitude, int radius) {
        this.name = name;
        this.ssid = ssid;
        this.ble = ble;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public String getSsid() {
        return ssid;
    }

    public String getBle() {
        return ble;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getRadius() {
        return radius;
    }
}
