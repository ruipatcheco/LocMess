package pt.ulisboa.tecnico.cmu.tg14.Mapper;

import jdk.internal.util.xml.impl.Input;
import org.springframework.jdbc.core.RowMapper;
import pt.ulisboa.tecnico.cmu.tg14.Model.Session;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by trosado on 5/15/17.
 */
public class SessionMapper implements RowMapper<Session> {

    @Override
    public Session mapRow(ResultSet resultSet, int i) throws SQLException {

        return new Session(
                    resultSet.getString("username"),
                    resultSet.getString("SESSIONID"),
                    resultSet.getTimestamp("CREATIONTIME"),
                    resultSet.getBoolean("ISVALID"));
    }
}
