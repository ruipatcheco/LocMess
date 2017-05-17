package pt.ulisboa.tecnico.cmu.tg14.Model;

import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * Created by trosado on 5/15/17.
 */
public class Session {

    private String username;
    private String sessionId;
    private Timestamp creationTime;
    private Boolean isValid;

    public Session(String username, String sessionId, Timestamp creationTime, Boolean isValid) {
        this.username = username;
        this.sessionId = sessionId;
        this.creationTime = creationTime;
        this.isValid = isValid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }
}
