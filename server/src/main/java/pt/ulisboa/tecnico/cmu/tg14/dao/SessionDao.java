package pt.ulisboa.tecnico.cmu.tg14.dao;

import pt.ulisboa.tecnico.cmu.tg14.Model.Session;

import javax.sql.DataSource;

/**
 * Created by trosado on 5/15/17.
 */
public interface SessionDao {
    void setDataSource(DataSource ds);

    void create(String username,String sessionID,Boolean isValid);
    Session getSessionIDByUsername(String username);
    void disableSession(String username,String sessionID);
    void disableAllSessions(String username);

}
