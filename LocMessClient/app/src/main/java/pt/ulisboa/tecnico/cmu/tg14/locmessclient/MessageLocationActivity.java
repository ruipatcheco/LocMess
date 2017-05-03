package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.LocationQuery;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Message;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnResponseListener;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderDbHelper;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.ServerActions;

import static android.content.ContentValues.TAG;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_location);

        getExtrasIntent(getIntent());
        mLocationList = (Spinner) findViewById(R.id.message_location_spinner);

        activity = this;

        new ListLocationsTask(activity).execute();

        mNext = (Button) findViewById(R.id.message_location_next);
        mSwitch = (Switch) findViewById(R.id.message_location_activity_switch);

        //FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(activity);
        //mAllLocations = dbHelper.getAllLocationsNames();

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i = new Intent(activity, MessagePolicyActivity.class);
                //TODO add message arguments to activity or save to disk

                fixIDNull();
                putExtras(i);
                startActivity(i);
                finish();
            }
        });

    }

    private void fixIDNull() {
        if (locationListNames.size() < 1) {
            mID = "";
        } else {
            mID = mLocationList.getSelectedItem().toString();
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
        i.putExtra("mSwitch", mSwitch.isChecked());
        i.putExtra("mID", mID);
    }

    private class ListLocationsTask extends AsyncTask<Void, Void, Void> implements OnResponseListener<List<Location>> {

        ProgressDialog progDailog;
        private ArrayAdapter<String> arrayAdapter;
        private Activity activity;
        List<Location> l;


        public ListLocationsTask(Activity activity) {
            this.activity = activity;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progDailog = new ProgressDialog(activity);
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();

            locationListNames = new ArrayList<>();
            arrayAdapter = new ArrayAdapter(activity,android.R.layout.simple_dropdown_item_1line, locationListNames);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mLocationList.setAdapter(arrayAdapter);

            progDailog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ServerActions serverActions = new ServerActions(activity);
            l = serverActions.getAllLocations(this);

            return null;
        }


        @Override
        public void onHTTPResponse(List<Location> response) {
            for(Location l : response){
                locationListNames.add(l.getName());
                Log.d(TAG, "doInBackground: "+l.getName());
            }
            arrayAdapter.notifyDataSetChanged();
        }
    }

}
