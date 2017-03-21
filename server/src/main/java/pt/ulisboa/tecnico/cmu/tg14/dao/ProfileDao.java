package pt.ulisboa.tecnico.cmu.tg14.dao;

import pt.ulisboa.tecnico.cmu.tg14.Model.Profile;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by trosado on 20/03/17.
 */
public interface ProfileDao {
   /**
    * This is the method to be used to initialize
    * database resources ie. connection.
    */
   void setDataSource(DataSource ds);
   /**
    * This is the method to be used to create
    * a record in the Student table.
    */
   String create(String username, String key, String value);
   String delete(String username, String key);
   List<Profile> listAll();
}
