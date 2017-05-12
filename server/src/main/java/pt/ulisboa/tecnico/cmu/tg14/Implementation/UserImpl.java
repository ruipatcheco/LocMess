package pt.ulisboa.tecnico.cmu.tg14.Implementation;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import pt.ulisboa.tecnico.cmu.tg14.dao.UserDao;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.UserMapper;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by trosado on 20/03/17.
 */
public class UserImpl implements UserDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;


    @Override
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);

    }

    @Override
    public void create(String username, String password) {
        String SQL = "insert into Users (username, password) values (?, ?)";
        jdbcTemplateObject.update( SQL, username, password);

        return;
    }

    @Override
    public User getUser(String username) {
        String SQL = "select * from Users where username=?";
        try {
            User user = jdbcTemplateObject.queryForObject(SQL,
                    new Object[]{username}, new UserMapper());
            return user;
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<User> listUser() {
        String SQL = "select * from Users";
        List <User> users = jdbcTemplateObject.query(SQL,
                new UserMapper());
        return users;
    }

    @Override
    public void delete(String username) {
        String SQL = "delete from Users where username = ?";
        jdbcTemplateObject.update(SQL, username);
        System.out.println("Deleted user = " + username );
        return;
    }

    @Override
    public void update(String username, String password) {
        String SQL = "update Users set password = ? where username = ?";
        jdbcTemplateObject.update(SQL, password, username);
        System.out.println("Updated Record with username = " + username);
        return;
    }
}
