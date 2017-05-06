package pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects;

/**
 * Created by basilio on 06-05-2017.
 */

public class Profile {
    private String key;
    private String value;

    public Profile(String key, String value) {
        this.key = key;
        this.value = value;
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
