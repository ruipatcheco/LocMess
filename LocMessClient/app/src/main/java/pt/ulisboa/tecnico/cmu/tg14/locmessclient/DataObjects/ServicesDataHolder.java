package pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by trosado on 4/23/17.
 */

public class ServicesDataHolder {

    private AbstractMap<String,String> bleContent;
    private AbstractMap<String,String> ssidContent;
    private Float longitude;
    private Float latitude;
    private String username;
    private AbstractMap<UUID,Message> messageMap;
    private boolean centralizedMode;

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

    public AbstractMap<String, String> getBleContent() {
        return bleContent;
    }

    public void setBleContent(AbstractMap<String, String> bleContent) {
        this.bleContent = bleContent;
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

    public List<String> getBleAddresses(){
        return new ArrayList(bleContent.values());
    }


    public List<String> getSsidAddresses(){
        return new ArrayList(ssidContent.values());
    }

    public static ServicesDataHolder getOurInstance() {
        return ourInstance;
    }





}
