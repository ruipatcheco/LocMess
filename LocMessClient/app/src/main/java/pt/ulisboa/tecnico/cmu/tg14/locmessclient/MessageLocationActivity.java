package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MessageLocationActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private RadioGroup mLocationRadio;
    private Spinner mLocationList;
    private Button mNext;
    List<String> locationsGPS;
    List<String> locationsWIFI;
    List<String> locationsBLE;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_location);

        mLocationRadio = (RadioGroup) findViewById(R.id.message_location_radio);
        mLocationList = (Spinner) findViewById(R.id.message_location_spinner);
        mNext = (Button) findViewById(R.id.message_location_next);

        List<String> options = new ArrayList<>();
        options.add("GPS");
        options.add("Wifi");
        options.add("Bluetooth");

        int i = 0;
        for(String option : options){
            RadioButton button  = new RadioButton(this);
            button.setText(option);
            button.setId(i);
            button.setOnCheckedChangeListener(this);
            mLocationRadio.addView(button);
            i++;
        }

        locationsGPS = new ArrayList<>();
        locationsWIFI = new ArrayList<>();
        locationsBLE = new ArrayList<>();

        locationsBLE.add("BLE1");
        locationsBLE.add("BLE2");
        locationsGPS.add("GPS3");
        locationsWIFI.add("WIFI14");
        locationsGPS.add("GPS5");

        //TODO Add network communication

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(activity,MessagePolicyActivity.class);
                //TODO add message arguments to activity or save to disk
                startActivity(intent);
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            // if b is true, radio button is selected, if false, radio is not selected
            // do not delete this if because onCheckedChanged is called twice if we select another option
            switch (compoundButton.getId()) {
                case 0:
                    // GPS
                    ArrayAdapter<String> adapterGPS = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, locationsGPS);
                    mLocationList.setAdapter(adapterGPS);
                    break;
                case 1:
                    ArrayAdapter<String> adapterWIFI = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, locationsWIFI);
                    mLocationList.setAdapter(adapterWIFI);
                    break;
                case 2:
                    ArrayAdapter<String> adapterBLE = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, locationsBLE);
                    mLocationList.setAdapter(adapterBLE);
                    break;
            }
        }
    }
}
