package pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects;

/**
 * Created by trosado on 15/04/17.
 */


public class Location {
    String name;
    String ssid;
    String ble;
    double latitude;
    double longitude;
    int radius;

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
        setDataNull(true);
        this.ble = ble;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        setDataNull(false);
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
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
            this.radius = 0;
        }else {
            this.ble = null;
            this.ssid = null;
        }
    }
}
