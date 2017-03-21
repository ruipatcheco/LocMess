package pt.ulisboa.tecnico.cmu.tg14.Implementation;

import org.springframework.jdbc.core.JdbcTemplate;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.CoordinatesMapper;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.ProfileMapper;
import pt.ulisboa.tecnico.cmu.tg14.Model.Coordinates;
import pt.ulisboa.tecnico.cmu.tg14.Model.Profile;
import pt.ulisboa.tecnico.cmu.tg14.dao.CoordinatesDao;
import pt.ulisboa.tecnico.cmu.tg14.dao.ProfileDao;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

/**
 * Created by trosado on 20/03/17.
 */
public class ProfileImpl implements ProfileDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    @Override
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);

    }

    @Override
    public String create(String username, String key, String value) {
        String SQL = "insert into Profile (username, key, value) values (?,?,?)";
        jdbcTemplateObject.update( SQL,username,key,value);
        return "OK";
    }

    @Override
    public List<Profile> listAll() {
        String SQL = "select * from Profile";

        List<Profile> profileList = jdbcTemplateObject.query(SQL,new ProfileMapper());

        return profileList;
    }

    @Override
    public String delete(String username, String key) {
        String SQL = "delete from Profile where id = ? and key = ?";
        jdbcTemplateObject.update(SQL,username,key);
        System.out.println("Deleted key = " + key + " from username = " + username);
        return "OK";
    }
}
