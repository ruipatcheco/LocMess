package pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by trosado on 4/23/17.
 */

public class ServicesDataHolder {

    private AbstractMap<String,String> bleContent;
    private AbstractMap<String,String> ssidContent;
    private Float longitude;
    private Float latitude;

    private static final ServicesDataHolder ourInstance = new ServicesDataHolder();

    public static ServicesDataHolder getInstance() {
        return ourInstance;
    }

    private ServicesDataHolder() {
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
