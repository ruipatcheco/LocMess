package pt.ulisboa.tecnico.cmu.tg14.Implementation;

import org.springframework.jdbc.core.JdbcTemplate;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.CoordinatesMapper;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.UserMapper;
import pt.ulisboa.tecnico.cmu.tg14.Model.Coordinates;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;
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
    public void create(float latitude, float longitude, int radius) {
        String SQL = "insert into Coordinates (id, lat, lon,radius) values (?, ?,?, ?)";
        String id = UUID.randomUUID().toString();
        jdbcTemplateObject.update( SQL,id, latitude, longitude,radius);
        return;
    }

    @Override
    public Coordinates getCoordinates(UUID id) {
        String SQL = "select * from Coordinates where id=?";
        Coordinates coord = jdbcTemplateObject.queryForObject(SQL,
                new Object[]{id.toString()}, new CoordinatesMapper());
        return coord;
    }

    @Override
    public void updateCoordinates(UUID id, float latitude, float longitude, int radius) {
        String SQL = "update Coordinates set lat = ? and lon = ? and radius = ? where id = ?";
        jdbcTemplateObject.update(SQL, latitude, longitude,radius, id);
        System.out.println("Updated Location with id = " + id);
        return;
    }

    @Override
    public void delete(UUID id) {
        String SQL = "delete from Coordinates where id = ?";
        jdbcTemplateObject.update(SQL, id.toString());
        System.out.println("Deleted Location id = " + id );
        return;
    }
}
