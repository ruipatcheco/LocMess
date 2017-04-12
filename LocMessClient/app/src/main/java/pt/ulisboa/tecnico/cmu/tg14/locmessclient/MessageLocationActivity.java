package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MessageLocationActivity extends AppCompatActivity {

    private RadioGroup mLocationRadio;
    private Spinner mLocationList;
    private Button mNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_location);

        final Activity activity = this;

        mLocationRadio = (RadioGroup) findViewById(R.id.message_location_radio);
        mLocationList = (Spinner) findViewById(R.id.message_location_spinner);
        mNext = (Button) findViewById(R.id.message_location_next);

        List<String> options = new ArrayList<>();
        options.add("GPS");
        options.add("Wifi");
        options.add("Bluetooth");

        for(String option : options){
            RadioButton button  = new RadioButton(this);
            button.setText(option);
            mLocationRadio.addView(button);
        }

        List<String> locations = new ArrayList<>();
        locations.add("1");
        locations.add("2");
        locations.add("3");
        locations.add("14");
        locations.add("5");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,locations);
        mLocationList.setAdapter(adapter);

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
}
