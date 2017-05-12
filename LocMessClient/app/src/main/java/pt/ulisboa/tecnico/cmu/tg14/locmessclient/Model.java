package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

/**
 * Created by Duarte on 12/05/2017.
 */

public class Model {

    private String name;
    private boolean selected;
    private boolean blacklist;

    public Model(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isBlacklist() {
        return blacklist;
    }

    public void setBlacklist(boolean blacklist) {
        this.blacklist = blacklist;
    }

    @Override
    public String toString() {
        String selectedString = selected ? "selected" : "not selected";
        String value = blacklist ? "CC" : "To";
        return name+" -> "+selectedString+ " with value "+value;
    }

}
