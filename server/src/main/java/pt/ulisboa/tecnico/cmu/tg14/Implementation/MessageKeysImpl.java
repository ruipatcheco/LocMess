package pt.ulisboa.tecnico.cmu.tg14.Implementation;

import org.springframework.jdbc.core.JdbcTemplate;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.MessageKeysMapper;
import pt.ulisboa.tecnico.cmu.tg14.Model.MessageKeys;
import pt.ulisboa.tecnico.cmu.tg14.dao.MessageKeysDAO;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

/**
 * Created by basilio on 12-05-2017.
 */
public class MessageKeysImpl implements MessageKeysDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    @Override
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);

    }

    @Override
    public void create(UUID messageID, String key, String value, Boolean isWhite) {
        String SQL = "insert into MessageKeys (id, name,value,iswhite) values (?,?,?,?)";
        jdbcTemplateObject.update( SQL,messageID.toString(),key,value,isWhite);

    }

    @Override
    public void delete(UUID id) {
        String SQL = "delete from MessageKeys where id = ?";
        jdbcTemplateObject.update(SQL, id.toString());
        System.out.println("Deleted MessageKeys id = " + id );

    }

    @Override
    public List<MessageKeys> getMessageKeys(UUID id) {
        String SQL = "" +
                "select * from MessageKeys where id=?";

        List<MessageKeys> messageKeysList = jdbcTemplateObject.query(SQL,
                new Object[]{id.toString()}, new MessageKeysMapper());

        return messageKeysList;
    }
}
