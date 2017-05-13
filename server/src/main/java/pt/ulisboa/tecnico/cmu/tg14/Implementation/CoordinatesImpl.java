package pt.ulisboa.tecnico.cmu.tg14.Implementation;

import org.springframework.jdbc.core.JdbcTemplate;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.CoordinatesMapper;
import pt.ulisboa.tecnico.cmu.tg14.Model.Coordinates;
import pt.ulisboa.tecnico.cmu.tg14.dao.CoordinatesDao;

import javax.sql.DataSource;
import java.util.UUID;

/**
 * Created by trosado on 20/03/17.
 */
public class CoordinatesImpl implements CoordinatesDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    @Override
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);

    }

    @Override
    public void create(String name, double latitude, double longitude, int radius) {
        String SQL = "insert into Coordinates (name, lat, lon,radius) values (?, ?,?, ?)";
        jdbcTemplateObject.update( SQL,name, latitude, longitude,radius);
    }

    @Override
    public Coordinates getCoordinates(String name) {
        String SQL = "select * from Coordinates where name=?";
        Coordinates coord = jdbcTemplateObject.queryForObject(SQL,
                new Object[]{name}, new CoordinatesMapper());
        return coord;
    }

    @Override
    public void updateCoordinates(String name, double latitude, double longitude, int radius) {
        String SQL = "update Coordinates set lat = ? and lon = ? and radius = ? where name = ?";
        jdbcTemplateObject.update(SQL, latitude, longitude,radius, name);
        System.out.println("Updated Location with name = " + name);
    }

    @Override
    public void delete(String name) {
        String SQL = "delete from Coordinates where name = ?";
        jdbcTemplateObject.update(SQL, name);
        System.out.println("Deleted Coordinates name = " + name );
    }
}
