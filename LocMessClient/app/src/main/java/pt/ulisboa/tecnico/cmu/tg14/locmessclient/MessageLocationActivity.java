package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderDbHelper;

public class MessageLocationActivity extends AppCompatActivity {

    // === PREV ACTIVITY ===
    private String mMessageContent;
    private String mStartTime;
    private String mEndTime;
    // ======================

    private Button mNext;
    private Activity activity;
    private Spinner mLocationList;
    private Switch mSwitch;
    private String mID;

    private List<String> locationListNames;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_location);

        getExtrasIntent(getIntent());
        mLocationList = (Spinner) findViewById(R.id.message_location_spinner);

        activity = this;
        locationListNames = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(activity,android.R.layout.simple_dropdown_item_1line, locationListNames);


        new ListLocationsTask().execute();

        mNext = (Button) findViewById(R.id.message_location_next);
        mSwitch = (Switch) findViewById(R.id.message_location_activity_switch);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i = new Intent(activity, MessagePolicyActivity.class);

                if (!fixIDNull()) {
                    return;
                }
                putExtras(i);
                startActivity(i);
                finish();
            }
        });

    }

    private boolean fixIDNull() {
        if (locationListNames.size() < 1) {
            Toast.makeText(activity, "Please provide location", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            mID = mLocationList.getSelectedItem().toString();
            return true;
        }
    }

    private void getExtrasIntent(Intent i) {
        mMessageContent = i.getExtras().getString("mMessageContent");
        mStartTime = i.getExtras().getString("mStartTime");
        mEndTime = i.getExtras().getString("mEndTime");
    }

    private void putExtras(Intent i) {
        i.putExtra("mMessageContent", mMessageContent);
        i.putExtra("mStartTime", mStartTime);
        i.putExtra("mEndTime", mEndTime);
        i.putExtra("mIsDecentralized", mSwitch.isChecked());
        i.putExtra("mID", mID);
    }

    private class ListLocationsTask extends AsyncTask<Void, Void, Void> {

        List<String> auxList;

        public ListLocationsTask() {
            auxList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mLocationList.setAdapter(arrayAdapter);

            for(String s: auxList){
                locationListNames.add(s);
            }
            arrayAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(activity);
            List<String> dbLocationNames = dbHelper.getAllLocationsNames();

            for(String s: dbLocationNames){
                auxList.add(s);
                //Log.d("MessageLocationActivity","added location from db-> " + s);
            }
            return null;
        }
    }

}
