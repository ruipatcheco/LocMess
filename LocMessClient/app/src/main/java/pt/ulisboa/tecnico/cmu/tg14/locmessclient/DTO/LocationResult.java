package pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO;

import java.util.UUID;

/**
 * Created by trosado on 4/19/17.
 */

public class LocationResult {
    private String name;
    private String ssid;
    private String ble;
    private UUID coordinates;

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

    public UUID getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(UUID coordinates) {
        this.coordinates = coordinates;
    }
}
