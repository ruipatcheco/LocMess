package pt.ulisboa.tecnico.cmu.tg14.Model;

/**
 * Created by trosado on 20/03/17.
 */
public class Profile {

    private String username;
    private String key;
    private String value;

    public Profile(){}

    public Profile(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Profile(String username, String key, String value) {
        this.username = username;
        this.key = key;
        this.value = value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
