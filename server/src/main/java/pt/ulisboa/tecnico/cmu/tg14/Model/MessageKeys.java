package pt.ulisboa.tecnico.cmu.tg14.Model;

import java.util.UUID;

/**
 * Created by basilio on 12-05-2017.
 */
public class MessageKeys {
    private UUID messageID;
    private Boolean isWhite;
    private String key;
    private String value;

    public MessageKeys(UUID messageID, String key, String value,Boolean isWhite) {
        this.messageID = messageID;
        this.isWhite = isWhite;
        this.key = key;
        this.value = value;
    }

    public UUID getMessageID() {
        return messageID;
    }

    public void setMessageID(UUID messageID) {
        this.messageID = messageID;
    }

    public Boolean getWhite() {
        return isWhite;
    }

    public void setWhite(Boolean white) {
        isWhite = white;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
