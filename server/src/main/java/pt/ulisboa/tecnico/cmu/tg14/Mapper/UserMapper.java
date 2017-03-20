package pt.ulisboa.tecnico.cmu.tg14.Mapper;

import org.springframework.jdbc.core.RowMapper;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by trosado on 20/03/17.
 */
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setPassword(resultSet.getString("password"));
        user.setUsername(resultSet.getString("username"));
        return user;
    }
}
