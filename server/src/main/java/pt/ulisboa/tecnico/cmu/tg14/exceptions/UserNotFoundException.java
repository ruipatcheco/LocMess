package pt.ulisboa.tecnico.cmu.tg14.exceptions;

/**
 * Created by tiago on 22/04/2017.
 */
public class UserNotFoundException extends Exception {
    String username;

    public UserNotFoundException(String username) {
        this.username = username;
    }

    @Override
    public String getMessage() {
        return "User "+username+" not found.";
    }
}
