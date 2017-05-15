package pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects;

import java.util.List;
import java.util.UUID;

/**
 * Created by brigadinhos on 26/04/2017.
 */

public class Message {

    private UUID id;
    private long creationTime;
    private long startTime;
    private long endTime;
    private String content;
    private String publisher;
    private String location;
    private boolean isCentralized;
    private boolean isNearby;
    List<Profile> whiteList;
    List<Profile> blackList;


    public Message(){}

    public Message(UUID id, long creationTime, long startTime, long endTime, String content, String publisher, String location, boolean isCentralized, boolean isNearby, List<Profile> whiteList, List<Profile> blackList) {
        this.id = id;
        this.creationTime = creationTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.content = content;
        this.publisher = publisher;
        this.location = location;
        this.isCentralized = isCentralized;
        this.isNearby = isNearby;
        this.whiteList = whiteList;
        this.blackList = blackList;
    }

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

    public boolean isNearby() {
        return isNearby;
    }

    public void setNearby(boolean nearby) {
        isNearby = nearby;
    }

    public boolean isCentralized() {
        return isCentralized;
    }

    public void setCentralized(boolean centralized) {
        isCentralized = centralized;
    }

    public UUID getUUID() {
        return id;
    }

    public void setUUID(UUID uuid) {
        this.id = uuid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
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
