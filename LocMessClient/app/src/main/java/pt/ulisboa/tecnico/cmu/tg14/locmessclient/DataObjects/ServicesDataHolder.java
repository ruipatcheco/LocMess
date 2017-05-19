package pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * Created by trosado on 4/23/17.
 */

public class ServicesDataHolder {

    private AbstractMap<String,BluetoothDevice> bleContent;
    private AbstractMap<String,String> ssidContent;
    private Float longitude;
    private Float latitude;

    private String username;
    private String password;


    private AbstractMap<UUID,Message> messageMap; //FIXME may not be needed
    private List<Location> nearLocations;
    private ArrayList<String> removedLocations;

    private boolean centralizedMode;
    private boolean threadHasBeenStarted = false;
    private boolean killThread = false;

    private static final ServicesDataHolder ourInstance = new ServicesDataHolder();

    public static ServicesDataHolder getInstance() {
        return ourInstance;
    }

    private ServicesDataHolder() {

        bleContent = new HashMap<>();
        ssidContent = new HashMap<>();
        latitude = new Float(0);
        longitude = new Float(0);
        messageMap = new HashMap<>();
        nearLocations = new ArrayList<>();
        removedLocations = new ArrayList<>();
        username = "";
        password = "";

    }

    public ArrayList<String> getRemovedLocations() {
        return removedLocations;
    }

    public void addRemovedLocation(String name){
        removedLocations.add(name);
    }

    public void clearRemovedLocations(){
        removedLocations.clear();
    }

    public List<Location> getNearLocations() {
        return nearLocations;
    }

    public void addNearLocation(Location l){nearLocations.add(l);}

    public void setNearLocations(List<Location> nearLocations) {
        this.nearLocations = nearLocations;
    }

    public boolean isCentralizedMode() {
        return centralizedMode;
    }

    public void setCentralizedMode(boolean centralizedMode) {
        this.centralizedMode = centralizedMode;
    }

    public AbstractMap<UUID,Message> getMessageMap() {
        return messageMap;
    }

    public void setMessageHashMap(AbstractMap<UUID,Message> messageList) {
        this.messageMap = messageList;
    }

    public void setMessageMapFromList(List<Message> messages){
        messageMap = new HashMap<>();
        for (Message message: messages) {
            messageMap.put(message.getUUID(),message);
        }
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) {
        this.password = password;
    }
    public AbstractMap<String, BluetoothDevice> getBleContent() {
        return bleContent;
    }

    public void setBleContent(AbstractMap<String,BluetoothDevice> bleContent) {
        this.bleContent = bleContent;
    }
    public void addBleContent(BluetoothDevice device){
        String name = (device.getName() == null) ? device.getAddress() : device.getName();
        this.bleContent.put(name,device);
    }

    public AbstractMap<String, String> getSsidContent() {
        return ssidContent;
    }

    public void setSsidContent(AbstractMap<String, String> ssidContent) {
        this.ssidContent = ssidContent;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public List<String> getBleNames(){
        return new ArrayList<>(bleContent.keySet());
    }


    public List<String> getSsidNames(){
        return new ArrayList(ssidContent.keySet());
    }

    public static ServicesDataHolder getOurInstance() {
        return ourInstance;
    }

    public boolean isThreadHasBeenStarted() {
        return threadHasBeenStarted;
    }

    public void setThreadHasBeenStarted(boolean threadHasBeenStarted) {
        this.threadHasBeenStarted = threadHasBeenStarted;
    }

    public boolean isKillThread() {
        return killThread;
    }

    public void setKillThread(boolean killThread) {
        this.killThread = killThread;
    }

}
