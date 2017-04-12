package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AddLocationActivity extends AppCompatActivity {

    private EditText locationName;
    private ListView possibleLocationsList;
    private RadioGroup locationRadio;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        locationName = (EditText) findViewById(R.id.add_location_name);
        possibleLocationsList = (ListView) findViewById(R.id.possible_locations_list);
        locationRadio = (RadioGroup) findViewById(R.id.add_location_radio);
        addButton = (Button)  findViewById(R.id.add_location);


        List<String> options = new ArrayList<>();
        options.add("GPS");
        options.add("Wifi");
        options.add("Bluetooth");

        int i = 0;
        for(String option : options){
            RadioButton button  = new RadioButton(this);
            button.setText(option);
            button.setId(i);
            //button.setOnCheckedChangeListener(this);
            locationRadio.addView(button);
            i++;
        }
    }
}
