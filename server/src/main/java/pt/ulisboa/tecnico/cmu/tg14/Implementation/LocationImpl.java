package pt.ulisboa.tecnico.cmu.tg14.Implementation;

import org.springframework.jdbc.core.JdbcTemplate;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.CoordinatesMapper;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.LocationMapper;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.UserMapper;
import pt.ulisboa.tecnico.cmu.tg14.Model.Coordinates;
import pt.ulisboa.tecnico.cmu.tg14.Model.Location;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;
import pt.ulisboa.tecnico.cmu.tg14.dao.LocationDao;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

/**
 * Created by trosado on 20/03/17.
 */
public class LocationImpl implements LocationDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    @Override
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);

    }



    @Override
    public void create(String name, String ssid, String ble, UUID coord) {
        String SQL = "insert into Location (name, ssid, ble, coordid ) values (?, ?, ?,?)";
        jdbcTemplateObject.update( SQL, name, ssid, ble, coord.toString());
        return;
    }

    @Override
    public Location getLocationBySSID(String ssid) {
        String SQL = "Select * from Location where ssid=?";
        Location loc = jdbcTemplateObject.queryForObject(SQL,new Object[]{ssid},new LocationMapper());
        return loc;
    }

    @Override
    public Location getLocationByBle(String ble) {
        String SQL = "Select * from Location where ble=?";
        Location loc = jdbcTemplateObject.queryForObject(SQL,new Object[]{ble},new LocationMapper());
        return loc;
    }

    @Override
    public Location getLocationByCoord(float lat, float lon) {
        String CoordSQL = "SELECT * ,POW(69.1 * (lat - ?), 2) + POW(69.1 * (? - lon) * COS(lat / 57.3), 2) AS distance FROM Coordinates " +
                "HAVING distance < POW(radius,2) ORDER BY distance;";
        List<Coordinates> coords = jdbcTemplateObject.query(CoordSQL,new Object[]{lat,lon},new CoordinatesMapper());

        for(Coordinates coord: coords){
            String SQL = "Select * from Location where coordid=?";
            List<Location> loclist = jdbcTemplateObject.query(SQL,new Object[]{coord.getId().toString()},new LocationMapper());
            if(!loclist.isEmpty())
                return loclist.get(0);

        }
        return null;
    }


    @Override
    public void delete(String name) {

        String SQL = "delete from Location where name = ?";
        jdbcTemplateObject.update(SQL, name);
    }
}