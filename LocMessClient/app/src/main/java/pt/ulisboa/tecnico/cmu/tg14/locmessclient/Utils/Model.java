package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Profile;

/**
 * Created by brigadinhos on 12/05/2017.
 */

public class Model {

    private Profile profile;
    private boolean selected;
    private boolean isWhite;

    public Model(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }

    @Override
    public String toString() {
        String selectedString = selected ? "selected" : "not selected";
        String value = isWhite ? "CC" : "To";
        return profile.getKey() + " -> " + selectedString + " with value " + value;
    }

}
