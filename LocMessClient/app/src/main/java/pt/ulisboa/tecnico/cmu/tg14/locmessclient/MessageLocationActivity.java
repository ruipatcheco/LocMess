package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

    private List<String> mAllLocations;
    private ArrayAdapter<String> mAdapterLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_location);

        getExtrasIntent(getIntent());

        activity = this;

        mNext = (Button) findViewById(R.id.message_location_next);
        mLocationList = (Spinner) findViewById(R.id.message_location_spinner);
        mSwitch = (Switch) findViewById(R.id.message_location_activity_switch);

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(activity);
        mAllLocations = dbHelper.getAllLocationsNames();

        //TODO Add network communication

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, mAllLocations);
        mLocationList.setAdapter(adapter);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i = new Intent(activity, MessagePolicyActivity.class);
                //TODO add message arguments to activity or save to disk
                i.putExtra("mMessageContent", mMessageContent);
                i.putExtra("mStartTime", mStartTime);
                i.putExtra("mEndTime", mEndTime);
                i.putExtra("mSwitch", mSwitch.isChecked());

                mID = mLocationList.getSelectedItem().toString();
                i.putExtra("mID", mID);

                startActivity(i);
                finish();
            }
        });

    }

    private void getExtrasIntent(Intent i) {
        mMessageContent = i.getExtras().getString("mMessageContent");
        mStartTime = i.getExtras().getString("mStartTime");
        mEndTime = i.getExtras().getString("mEndTime");
    }

}
