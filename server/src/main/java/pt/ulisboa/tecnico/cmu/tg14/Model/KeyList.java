package pt.ulisboa.tecnico.cmu.tg14.Model;

import java.util.UUID;

/**
 * Created by trosado on 21/03/17.
 */
public class KeyList {
    private UUID msgid;
    private String type;
    private String key;
    private String value;

    public UUID getMsgid() {
        return msgid;
    }

    public void setMsgid(UUID msgid) {
        this.msgid = msgid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
