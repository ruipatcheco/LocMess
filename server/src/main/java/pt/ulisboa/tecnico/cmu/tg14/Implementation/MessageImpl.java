package pt.ulisboa.tecnico.cmu.tg14.Implementation;


import org.springframework.jdbc.core.JdbcTemplate;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.CoordinatesMapper;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.MessageMapper;
import pt.ulisboa.tecnico.cmu.tg14.Model.Coordinates;
import pt.ulisboa.tecnico.cmu.tg14.Model.Message;
import pt.ulisboa.tecnico.cmu.tg14.dao.CoordinatesDao;
import pt.ulisboa.tecnico.cmu.tg14.dao.MessageDao;

import javax.sql.DataSource;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Created by trosado on 20/03/17.
 */
public class MessageImpl implements MessageDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    @Override
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);

    }


    /*
    private UUID id;
    private Timestamp creationTime;
    private Timestamp startTime;
    private Timestamp endTime;
    private String content;
    private String publisher;
    private UUID coordinatesID;

    */

    @Override
    public void create(UUID id,Timestamp startTime, Timestamp endTime, Timestamp creationTime, String content, String publisher, String location) {
        String SQL = "insert into Message (id, location, content, creationtime, starttime, endtime, publisher) values (?,?,?,?,?,?,?)";
        jdbcTemplateObject.update( SQL,id.toString(),location,content,creationTime,startTime,endTime,publisher);

        return;

    }

    @Override
    public void create(UUID id,Timestamp startTime, Timestamp creationTime, String content, String publisher, String location) {
        String SQL = "insert into Message (id, location, content, creationtime, endtime, publisher) values (?,?,?,?,?,?)";
        jdbcTemplateObject.update( SQL,id.toString(),location,content,creationTime,startTime,publisher);
        return;

    }


    @Override
    public void delete(UUID id) {
        String SQL = "delete from Message where id = ?";
        jdbcTemplateObject.update(SQL, id.toString());
        System.out.println("Deleted Message id = " + id );
        return;
    }

    @Override
    public Message getMessage(UUID id) {
        String SQL = "" +
                "select * from Message where id=?";
        Message m = jdbcTemplateObject.queryForObject(SQL,
                new Object[]{id.toString()}, new MessageMapper());
        return m;
    }

    @Override
    public List<Message> getMessagesByLocation(String location) {
        //TODO a faltar consideracao sobre a timeframe util das mensagens

        String SQL = "" +
                "select * from Message where location=?";

        List<Message> messageList = jdbcTemplateObject.query(SQL,
                new Object[]{location}, new MessageMapper());

        return messageList;
    }

    @Override
    public List<Message> getMessagesByUsername(String username) {

        String SQL = "" +
                "select * from Message where PUBLISHER=?";

        List<Message> messageList = jdbcTemplateObject.query(SQL,new Object[]{username}, new MessageMapper());
        return messageList;
    }

}
