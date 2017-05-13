package pt.ulisboa.tecnico.cmu.tg14.Mapper;

import org.springframework.jdbc.core.RowMapper;
import pt.ulisboa.tecnico.cmu.tg14.Model.Profile;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by trosado on 20/03/17.
 */
public class ProfileMapper implements RowMapper<Profile> {
    @Override
    public Profile mapRow(ResultSet resultSet, int i) throws SQLException {
        Profile p = new Profile(
                resultSet.getString("username"),
                resultSet.getString("name"),
                resultSet.getString("value")
        );
        return p;
    }
}
