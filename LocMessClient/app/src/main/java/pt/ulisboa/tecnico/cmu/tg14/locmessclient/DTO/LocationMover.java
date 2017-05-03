package pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO;

/**
 * Created by trosado on 4/23/17.
 */
public class LocationMover {

    String name;
    String ssid;
    String ble;
    double latitude;
    double longitude;
    int radius;

    public LocationMover(){
        this.name = "";
        this.ble = "";
        this.ssid = "";
        this.latitude = 0;
        this.longitude = 0;
        this.radius = 0;
    }

    public LocationMover(String name, String ssid, String ble, double latitude, double longitude, int radius) {
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
