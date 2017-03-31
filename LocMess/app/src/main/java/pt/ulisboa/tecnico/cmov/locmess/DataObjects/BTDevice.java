package pt.ulisboa.tecnico.cmov.locmess.DataObjects;

/**
 * Created by tiago on 30/03/2017.
 */

public class BTDevice {
    private String name;
    private String address;
    private boolean paired;
    public BTDevice(String name, String address,boolean paired) {
        this.name = name;
        this.address = address;
        this.paired = paired;
    }

    public boolean isPaired() {
        return paired;
    }

    public void setPaired(boolean paired) {
        this.paired = paired;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
