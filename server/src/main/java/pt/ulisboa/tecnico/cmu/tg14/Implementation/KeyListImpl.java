package pt.ulisboa.tecnico.cmu.tg14.Implementation;

import org.springframework.jdbc.core.JdbcTemplate;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.KeyListMapper;
import pt.ulisboa.tecnico.cmu.tg14.Model.KeyList;
import pt.ulisboa.tecnico.cmu.tg14.dao.KeyListDao;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

/**
 * Created by trosado on 21/03/17.
 */
public class KeyListImpl implements KeyListDao {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    @Override
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }


    @Override
    public void create(UUID messageID, String type, String key, String value) {
        String SQL = "insert into List (msgid, type, name, value) values (?, ?, ?,?)";
        jdbcTemplateObject.update( SQL, messageID, type, key, value);
        return;
    }

    @Override
    public List<KeyList> getListFromMessage(UUID messageID, String type) {
        String SQL = "SELECT * FROM List WHERE msgid=? AND type=?";
        return jdbcTemplateObject.query(SQL,new Object[]{messageID.toString(),type},new KeyListMapper());
    }

    @Override
    public void delete(UUID messageID, String type) {
        String SQL = "delete from List where msgid=? AND type=?";
        jdbcTemplateObject.update(SQL, messageID,type);
    }
}
