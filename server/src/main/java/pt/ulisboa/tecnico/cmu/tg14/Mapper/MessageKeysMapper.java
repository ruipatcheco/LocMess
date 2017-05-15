package pt.ulisboa.tecnico.cmu.tg14.Mapper;

import org.springframework.jdbc.core.RowMapper;
import pt.ulisboa.tecnico.cmu.tg14.Model.Location;
import pt.ulisboa.tecnico.cmu.tg14.Model.Message;
import pt.ulisboa.tecnico.cmu.tg14.Model.MessageKeys;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by basilio on 12-05-2017.
 */
public class MessageKeysMapper implements RowMapper<MessageKeys> {
    @Override
    public MessageKeys mapRow(ResultSet resultSet, int i) throws SQLException {
        MessageKeys ms = new MessageKeys(
                UUID.fromString(resultSet.getString("ID")),
                resultSet.getString("NAME"),
                resultSet.getString("VALUE"),
                resultSet.getBoolean("ISWHITE")
                );
        return ms;
    }
}
