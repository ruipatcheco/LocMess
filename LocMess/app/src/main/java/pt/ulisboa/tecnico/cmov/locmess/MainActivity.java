package pt.ulisboa.tecnico.cmov.locmess;

import android.content.Context;
import android.icu.text.MessageFormat;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ListView messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageList = (ListView) findViewById(R.id.messageList);

        ArrayList<Message> messages = getMessagesByLocation();

        ArrayAdapter<Message> adapter = new ArrayAdapter<Message>(this, android.R.layout.simple_expandable_list_item_1, messages);

        messageList.setAdapter(adapter);



    }

    private ArrayList<Message> getMessagesByLocation(){

        String closestLocation ="error";

        double lat=0, lon=0;

        //TODO use gps service to obtain lat and lon

        String locationQueryURL = "http://194.210.220.190:8080/location/listByCoord?lat="+lat+"&lon="+lon;

        try {

            JSONArray locationArray = new JSONArray(locationQueryURL);
            for (int i = 0; i <locationArray.length() ; i++) {
                JSONObject jsonObject = locationArray.optJSONObject(i);

                closestLocation = jsonObject.optString("name");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String messagesQueryURL = "http://194.210.220.190:8080/getMessagesByLocation?location="+closestLocation;

        ArrayList<Message> messages = new ArrayList<>();

        try {

            JSONArray messagesArray = new JSONArray(messagesQueryURL);
            for (int i = 0; i <messagesArray.length() ; i++) {
                JSONObject jsonObject = messagesArray.optJSONObject(i);
                Message m = new Message();

                m.setId(UUID.fromString(jsonObject.getString("id")));
                m.setContent(jsonObject.getString("content"));
                m.setCreationTime(new Timestamp(jsonObject.getLong("creationTime")));
                m.setStartTime(new Timestamp(jsonObject.getLong("startTime")));
                m.setEndTime(new Timestamp(jsonObject.getLong("endTime")));
                m.setLocation(jsonObject.getString("location"));
                m.setPublisher(jsonObject.getString("publisher"));

                messages.add(m);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return messages;
    }



    public class Message{
        private UUID id;
        private Timestamp creationTime;
        private Timestamp startTime;
        private Timestamp endTime;
        private String content;
        private String publisher;
        private String location;

        @Override
        public String toString(){
            return "Publisher -> " + publisher + "\nContent -> " + content;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public Timestamp getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(Timestamp creationTime) {
            this.creationTime = creationTime;
        }

        public Timestamp getStartTime() {
            return startTime;
        }

        public void setStartTime(Timestamp startTime) {
            this.startTime = startTime;
        }

        public Timestamp getEndTime() {
            return endTime;
        }

        public void setEndTime(Timestamp endTime) {
            this.endTime = endTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
