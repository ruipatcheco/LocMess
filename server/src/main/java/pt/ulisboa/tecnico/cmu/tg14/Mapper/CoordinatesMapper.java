package pt.ulisboa.tecnico.cmu.tg14.Mapper;

import pt.ulisboa.tecnico.cmu.tg14.Model.Coordinates;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by trosado on 20/03/17.
 */
public class CoordinatesMapper implements RowMapper<Coordinates>{
    @Override
    public Coordinates mapRow(ResultSet resultSet, int i) throws SQLException {
        Coordinates coord = new Coordinates();
        coord.setId(UUID.fromString(resultSet.getString("ID")));
        coord.setLatitude(resultSet.getFloat("Lat"));
        coord.setLongitude(resultSet.getFloat("Lon"));
        coord.setRadius(resultSet.getInt("Radius"));
        return coord;
    }
}
