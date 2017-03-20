package pt.ulisboa.tecnico.cmu.tg14.Model;

import java.io.Serializable;

/**
 * Created by trosado on 20/03/17.
 */
public class User  implements Serializable{

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
