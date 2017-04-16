package pt.ulisboa.tecnico.cmu.tg14.DTO;

import java.util.List;

/**
 * Created by tiago on 16/04/2017.
 */
public class LocationQuery {
    private Float latitude;
    private Float longitude;
    private List<String> ssidList;
    private List<String> bleList;


    public LocationQuery() {
        latitude = new Float(0);
        longitude = new Float(0);

    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public List<String> getSsidList() {
        return ssidList;
    }

    public List<String> getBleList() {
        return bleList;
    }
}
