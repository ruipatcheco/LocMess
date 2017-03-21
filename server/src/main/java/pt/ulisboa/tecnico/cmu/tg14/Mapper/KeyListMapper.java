package pt.ulisboa.tecnico.cmu.tg14.Mapper;


import org.springframework.jdbc.core.RowMapper;
import pt.ulisboa.tecnico.cmu.tg14.Model.KeyList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by trosado on 21/03/17.
 */
public class KeyListMapper implements RowMapper<KeyList> {

    @Override
    public KeyList mapRow(ResultSet resultSet, int i) throws SQLException {
        KeyList keyList = new KeyList();
        keyList.setKey(resultSet.getString("Name"));
        keyList.setMsgid(UUID.fromString(resultSet.getString("MSGID")));
        keyList.setValue(resultSet.getString("Value"));
        keyList.setType(resultSet.getString("Type"));

        return keyList;
    }
}
