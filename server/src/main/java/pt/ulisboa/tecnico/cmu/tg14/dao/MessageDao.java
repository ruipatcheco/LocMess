package pt.ulisboa.tecnico.cmu.tg14.dao;

import pt.ulisboa.tecnico.cmu.tg14.Model.Message;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Created by trosado on 20/03/17.
 */
public interface MessageDao {
   /**
    * This is the method to be used to initialize
    * database resources ie. connection.
    */
   void setDataSource(DataSource ds);

   void create(UUID id, Timestamp startTime, Timestamp endTime, Timestamp creationTime, String content, String publisher, String location);

   void create(UUID id, Timestamp startTime, Timestamp creationTime, String content, String publisher, String location);

   void delete(UUID id);

   Message getMessage(UUID id);

   List<Message> getMessagesByLocation(String location);

    List<Message> getMessagesByUsername(String username);
}
