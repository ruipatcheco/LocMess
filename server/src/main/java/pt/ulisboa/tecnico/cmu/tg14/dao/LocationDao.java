package pt.ulisboa.tecnico.cmu.tg14.dao;

import pt.ulisboa.tecnico.cmu.tg14.Model.Coordinates;
import pt.ulisboa.tecnico.cmu.tg14.Model.Location;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

/**
 * Created by trosado on 20/03/17.
 */
public interface LocationDao {

    void setDataSource(DataSource ds);

    void create(String name, String ssid,String ble);
    Location getLocationBySSID(String ssid);
    List<Location> getLocationBySSID(List<String> ssids);
    Location getLocationByBle(String ble);
    List<Location> getLocationByBle(List<String> bles);
    List<Location> getLocationByCoord(float lat,float lon);
    List<Location> getLocationList();
    void delete(String name);
}
