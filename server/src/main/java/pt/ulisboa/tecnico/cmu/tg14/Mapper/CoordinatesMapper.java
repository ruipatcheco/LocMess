package pt.ulisboa.tecnico.cmu.tg14.Mapper;

import pt.ulisboa.tecnico.cmu.tg14.Model.Coordinates;

import javax.swing.tree.RowMapper;
import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by trosado on 20/03/17.
 */
public class CoordinatesMapper implements RowMapper<Coordinates>{
    @Override
    public Coordinates mapRow(ResultSet resultSet, int i) throws SQLException {
        Coordinates coord = new Coordinates();
        coord.getId();
        coord.get
        User user = new User();
        user.setPassword(resultSet.getString("password"));
        user.setUsername(resultSet.getString("username"));
        return user;
    }
}
