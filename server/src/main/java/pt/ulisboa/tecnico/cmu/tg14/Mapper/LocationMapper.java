package pt.ulisboa.tecnico.cmu.tg14.Mapper;

import org.springframework.jdbc.core.RowMapper;
import pt.ulisboa.tecnico.cmu.tg14.Model.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by trosado on 20/03/17.
 */
public class LocationMapper implements RowMapper<Location>{
    @Override
    public Location mapRow(ResultSet resultSet, int i) throws SQLException {
        Location location = new Location();
        location.setSsid(resultSet.getString("SSID"));
        location.setBle(resultSet.getString("BLE"));
        String uuid = resultSet.getString("COORDID");
        if(uuid != null)
            location.setCoordinates(UUID.fromString(uuid));
        location.setName(resultSet.getString("NAME"));
        return location;
    }
}
