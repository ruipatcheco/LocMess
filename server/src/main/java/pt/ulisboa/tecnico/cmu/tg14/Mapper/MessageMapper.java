package pt.ulisboa.tecnico.cmu.tg14.Mapper;

import org.springframework.jdbc.core.RowMapper;
import pt.ulisboa.tecnico.cmu.tg14.Model.Coordinates;
import pt.ulisboa.tecnico.cmu.tg14.Model.Message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by trosado on 20/03/17.
 */
public class MessageMapper implements RowMapper<Message>{
    @Override
    public Message mapRow(ResultSet resultSet, int i) throws SQLException {
        Message message = new Message();
        message.setId(UUID.fromString(resultSet.getString("ID")));
        message.setContent(resultSet.getString("CONTENT"));
        message.setCreationTime(resultSet.getTimestamp("CREATIONTIME"));
        message.setStartTime(resultSet.getTimestamp("STARTTIME"));
        message.setEndTime(resultSet.getTimestamp("ENDTIME"));
        message.setPublisher(resultSet.getString("PUBLISHER"));
        message.setLocation(resultSet.getString("LOCATION"));

        return message;
    }

/*
    private String id;
    private Timestamp creationTime;
    private Timestamp startTime;
    private Timestamp endTime;
    private String content;
    private String publisher;

    ID VARCHAR(36) NOT NULL,
    LOCATION VARCHAR(100) NOT NULL,
    CONTENT VARCHAR(100) NOT NULL,
    CREATIONTIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    STARTTIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ENDTIME TIMESTAMP NULL,
    PUBLISHER VARCHAR(100) NOT NULL, */
}
