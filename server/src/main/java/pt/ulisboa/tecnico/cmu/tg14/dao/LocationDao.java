package pt.ulisboa.tecnico.cmu.tg14.dao;

import pt.ulisboa.tecnico.cmu.tg14.Model.Coordinates;
import pt.ulisboa.tecnico.cmu.tg14.Model.Location;

import javax.sql.DataSource;
import java.util.UUID;

/**
 * Created by trosado on 20/03/17.
 */
public interface LocationDao {

    void setDataSource(DataSource ds);

    void create(String name, String ssid,String ble,UUID coord);
    Location getLocationBySSID(String ssid);
    Location getLocationByBle(String ble);
    Location getLocationByCoord(float lat,float lon);
    void delete(String name);
}
