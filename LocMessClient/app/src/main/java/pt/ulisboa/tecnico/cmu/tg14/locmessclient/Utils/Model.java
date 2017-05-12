package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils;

/**
 * Created by brigadinhos on 12/05/2017.
 */

public class Model {

    private String name;
    private boolean selected;
    private boolean isCcOrIsTo;

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

    public boolean isCcOrIsTo() {
        return isCcOrIsTo;
    }

    public void setCcOrIsTo(boolean isCcOrIsTo) {
        this.isCcOrIsTo = isCcOrIsTo;
    }

    @Override
    public String toString() {
        String selectedString = selected ? "selected" : "not selected";
        String value = isCcOrIsTo ? "CC" : "To";
        return name+" -> "+selectedString+ " with value "+value;
    }

}
