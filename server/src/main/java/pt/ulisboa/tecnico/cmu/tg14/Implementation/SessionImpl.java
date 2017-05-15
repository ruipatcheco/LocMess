package pt.ulisboa.tecnico.cmu.tg14.Implementation;

import org.springframework.jdbc.core.JdbcTemplate;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.SessionMapper;
import pt.ulisboa.tecnico.cmu.tg14.Model.Session;
import pt.ulisboa.tecnico.cmu.tg14.dao.SessionDao;

import javax.sql.DataSource;

/**
 * Created by trosado on 5/15/17.
 */
public class SessionImpl implements SessionDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    @Override
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);

    }

    @Override
    public void create(String username, String sessionID, Boolean isValid) {
        String SQL = "insert into Sessions (username, SESSIONID,ISVALID) values (?,?,?)";
        jdbcTemplateObject.update( SQL,username,sessionID,isValid);
    }

    @Override
    public Session getSessionIDByUsername(String username) {
        String SQL = "SELECT * FROM Sessions WHERE username =? AND isValid= true";
        Session session = jdbcTemplateObject.queryForObject(SQL,new Object[]{username},new SessionMapper());
        return session;
    }

    @Override
    public void disableSession(String username,String sessionID) {
        String SQL = "UPDATE Sessions set isvalid = ? where username = ? AND SESSIONID=?";
        jdbcTemplateObject.update(SQL, false,username,sessionID);
        System.out.println("Disabled Session for username = " + username);
    }

    @Override
    public void disableAllSessions(String username) {
        String SQL = "UPDATE Sessions set isvalid = ? where username = ?";
        jdbcTemplateObject.update(SQL, false,username);
        System.out.println("Disabled all sessions for username = " + username);
    }
}
