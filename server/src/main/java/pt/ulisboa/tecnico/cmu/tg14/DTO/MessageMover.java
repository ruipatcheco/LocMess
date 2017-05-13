package pt.ulisboa.tecnico.cmu.tg14.DTO;

import pt.ulisboa.tecnico.cmu.tg14.Model.MessageKeys;
import pt.ulisboa.tecnico.cmu.tg14.Model.Profile;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Created by trosado on 20/03/17.
 */
public class MessageMover {


    private UUID id;
    private Timestamp creationTime;
    private Timestamp startTime;
    private Timestamp endTime;
    private String content;
    private String publisher;
    private String location;
    private List<Profile> whiteList;
    private List<Profile> blackList;


    public List<Profile> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(List<Profile> whiteList) {
        this.whiteList = whiteList;
    }

    public List<Profile> getBlackList() {
        return blackList;
    }

    public void setBlackList(List<Profile> blackList) {
        this.blackList = blackList;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

}
