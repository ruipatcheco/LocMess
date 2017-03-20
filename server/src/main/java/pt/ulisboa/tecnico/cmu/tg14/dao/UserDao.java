package pt.ulisboa.tecnico.cmu.tg14.dao;

import pt.ulisboa.tecnico.cmu.tg14.Model.User;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by trosado on 20/03/17.
 */
public interface UserDao {
   /**
    * This is the method to be used to initialize
    * database resources ie. connection.
    */
   void setDataSource(DataSource ds);
   /**
    * This is the method to be used to create
    * a record in the Student table.
    */
   void create(String username, String password);
   /**
    * This is the method to be used to list down
    * a record from the Student table corresponding
    * to a passed student id.
    */
   User getUser(String username);
   /**
    * This is the method to be used to list down
    * all the records from the Student table.
    */
   List<User> listUser();
   /**
    * This is the method to be used to delete
    * a record from the Student table corresponding
    * to a passed student id.
    */
   void delete(String username);
   /**
    * This is the method to be used to update
    * a record into the Student table.
    */
   void update(String name, String password);

    User findByUsername(String username);
}
