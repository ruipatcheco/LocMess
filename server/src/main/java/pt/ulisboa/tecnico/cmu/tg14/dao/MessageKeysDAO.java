package pt.ulisboa.tecnico.cmu.tg14.dao;

import pt.ulisboa.tecnico.cmu.tg14.Model.Message;
import pt.ulisboa.tecnico.cmu.tg14.Model.MessageKeys;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Created by basilio on 12-05-2017.
 */
public interface MessageKeysDAO {
    void setDataSource(DataSource ds);


    void create(UUID messageID, String key, String value,Boolean isWhite);

    void delete(UUID id);

    List<MessageKeys> getMessageKeys(UUID id);

}
