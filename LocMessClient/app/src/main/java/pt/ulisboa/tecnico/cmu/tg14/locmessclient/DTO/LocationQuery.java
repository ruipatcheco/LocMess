package pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO;

/**
 * Created by tiago on 16/04/2017.
 */

import android.support.annotation.Nullable;
import android.util.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiago on 16/04/2017.
 */
public class LocationQuery {
    private Float latitude;
    private Float longitude;
    private List<String> ssidList;
    private List<String> bleList;


    private LocationQuery() {
        latitude = new Float(0);
        longitude = new Float(0);

    }

    public LocationQuery(Float latitude, Float longitude, @Nullable List<String> ssidList, @Nullable List<String> bleList) {
        this.latitude = latitude;
        this.longitude = longitude;

        if(ssidList == null)
            this.ssidList = new ArrayList<>();
        else
            this.ssidList = ssidList;

        if(bleList == null)
            this.bleList = new ArrayList<>();
        else
            this.bleList = bleList;

    }




    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public List<String> getSsidList() {
        return ssidList;
    }

    public List<String> getBleList() {
        return bleList;
    }

    public JSONObject toJSON(){
        StringWriter sw = new StringWriter();
        JsonWriter writer = new JsonWriter(sw);
        try {
            writer.beginObject();
            writer.name("latitude").value(this.latitude);
            writer.name("longitude").value(this.longitude);
            writer.name("ssidList")
                    .beginArray();
                for(String ssid: ssidList){
                    writer.value(ssid);
                }
                writer.endArray();
            writer.name("bleList")
                    .beginArray();
                for(String ble: bleList){
                    writer.value(ble);
                }
                writer.endArray();

            writer.endObject();
            writer.flush();

            return new JSONObject(sw.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
}
