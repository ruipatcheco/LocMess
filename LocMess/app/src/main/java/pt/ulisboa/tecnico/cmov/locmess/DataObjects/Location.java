package pt.ulisboa.tecnico.cmov.locmess.DataObjects;

/**
 * Created by tiago on 26/03/2017.
 */

public class Location {
    String name;
    String ssid;
    String ble;
    double latitutde;
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

    public double getLatitutde() {
        return latitutde;
    }

    public void setLatitutde(double latitutde) {
        setDataNull(false);
        this.latitutde = latitutde;
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
            this.latitutde = 0;
            this.longitude = 0;
            this.radius = 0;
        }else {
            this.ble = null;
            this.ssid = null;
        }
    }
}
