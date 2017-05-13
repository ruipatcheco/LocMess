package pt.ulisboa.tecnico.cmu.tg14.Model;

import java.util.UUID;

/**
 * Created by trosado on 20/03/17.
 */
public class Location {
    private String name;
    private String ssid;
    private String ble;

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
        this.ssid = ssid;
    }

    public String getBle() {
        return ble;
    }

    public void setBle(String ble) {
        this.ble = ble;
    }


    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
