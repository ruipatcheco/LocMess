package pt.ulisboa.tecnico.cmu.tg14.dao;

import pt.ulisboa.tecnico.cmu.tg14.Model.KeyList;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

/**
 * Created by trosado on 21/03/17.
 */
public interface KeyListDao {
    void setDataSource(DataSource ds);

    void create(UUID messageID,String type,String key,String value);
    List<KeyList> getListFromMessage(UUID messageID, String type);

    void delete(UUID messageID,String type);

}
