package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.OperationStatus;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnResponseListener;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderDbHelper;

public class AddLocationActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,OnResponseListener<OperationStatus>{



    private static final String TAG = "AddLocationActivity";
    private static final String GPS="GPS";
    private static final String WIFI="WIFI";
    private static final String BLE="BLE";


    private RadioGroup mLocationRadio;
    private Spinner mLocationList;
    private Button mNext;
    private EditText mLocationName;
    private EditText mLocationRadius;


    private AbstractMap<String, String> nameBLEMAP;
    private List<String> namesBLE;

    private ServicesDataHolder dataHolder;

    private AbstractMap<String, String> nameWifiMap;
    private List<String> namesWifi;

    Activity activity;
    private boolean someOptionChecked; // to check if user selected an item
    private boolean validLocation;
    private String mType;
    private String mID;
    private ArrayAdapter<String> mAdapterWIFI;
    private ArrayAdapter<String> mAdapterBLE;
    private Location mLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        dataHolder = ServicesDataHolder.getInstance();

        someOptionChecked = false;
        validLocation = false;
        activity = this;
        dataHolder =  ServicesDataHolder.getInstance();

        mLocation = new Location();
        mLocationRadio = (RadioGroup) findViewById(R.id.add_location_radio);
        mLocationList = (Spinner) findViewById(R.id.add_location_spinner);
        mNext = (Button) findViewById(R.id.add_location_button);
        mLocationName = (EditText) findViewById(R.id.add_location_name);
        mLocationRadius = (EditText) findViewById(R.id.add_location_radius);

        //Set visibility for radius
        mLocationRadius.setVisibility(View.INVISIBLE);

        nameBLEMAP = dataHolder.getBleContent();
        namesBLE = new ArrayList<>(nameBLEMAP.keySet());

        nameWifiMap = dataHolder.getSsidContent();
        namesWifi = new ArrayList<>(nameWifiMap.keySet());


        mAdapterWIFI = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, namesWifi);
        mAdapterBLE = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, namesBLE);

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


        //UPDATE all lists and values
        addBLE();
        addGPS();
        addWIFI();

        namesBLE.add("BLE2");
        namesWifi.add("WIFI14");

        //Setting default location type to GPS
        mType=GPS;
        mLocationRadio.check(0); // default position for GPS



        mLocationList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                //FIXME set first element of list if nothing is set
                switch (mType){
                    case BLE:
                        //Log.d(TAG, "onItemSelected: BT Name : "+name);
                        //Log.d(TAG, "onItemSelected: BT Selected : "+nameBLEMAP.get(name));
                        mLocation.setBle(name);
                        break;
                    case WIFI:
                        //Log.d(TAG, "onItemSelected: WIFI Name : "+name);
                        //Log.d(TAG, "onItemSelected: WIFI Selected : "+nameWifiMap.get(name));
                        mLocation.setSsid(name);
                        break;

                }

                //Log.d(TAG, "onItemSelected: "+name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (!isValidInput()) {
                    return;
                }

                //mID = mLocationList.getSelectedItem().toString();

                mLocation.setName(mLocationName.getText().toString());

                if(mType.equals(GPS)){
                    if(!mLocationRadius.getText().toString().equals(""))
                        mLocation.setRadius(Integer.parseInt(mLocationRadius.getText().toString()));

                    addGPS();
                }

                FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
                dbHelper.insertLocation(mLocation.getName(),mLocation.getSsid(),mLocation.getBle(),mLocation.getLatitude(),mLocation.getLongitude(),mLocation.getRadius(),"false");

                finish();

            }
        });
    }



    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        someOptionChecked = true;
        if (b) {
            // if b is true, radio button is selected, if false, radio is not selected
            // do not delete this if because onCheckedChanged is called twice if we select another option
            switch (compoundButton.getId()) {
                case 0:
                    // GPS
                    mType = GPS;

                    mLocationList.setVisibility(View.INVISIBLE);
                    mLocationRadius.setVisibility(View.VISIBLE);

                    break;
                case 1:
                    // WIFI
                    mLocationList.setAdapter(mAdapterWIFI);

                    mType = WIFI;

                    mLocationList.setVisibility(View.VISIBLE);
                    mLocationRadius.setVisibility(View.INVISIBLE);

                    break;
                case 2:
                    // BTL
                    mLocationList.setAdapter(mAdapterBLE);

                    mType = BLE;

                    mLocationList.setVisibility(View.VISIBLE);
                    mLocationRadius.setVisibility(View.INVISIBLE);

                    break;
            }
        }
    }

    private boolean isValidInput() {
        boolean emptyName = mLocationName.getText().toString().matches("");

        if (emptyName) {
            Toast.makeText(activity, "Name your location", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!someOptionChecked) {
            Toast.makeText(activity, "Select an option", Toast.LENGTH_LONG).show();
            return false;
        }

        if ( (mType == GPS) && !validLocation) {
            Toast.makeText(activity, "Acquiring a valid GPS signal, try again in 1 minute ", Toast.LENGTH_LONG).show();
            addGPS();
            return false;
        }

        return true;
    }

    public void addGPS(){
        float lat = dataHolder.getLatitude();
        float lon = dataHolder.getLongitude();
        float delta = new Float(0.01);

        if(! ((Math.abs(lat-0) < delta) && (Math.abs(lon-0) < delta)) ){
            //Checks if lat = 0 and lon = 0
            validLocation = true;
            //Log.d("Addlocationactivity: ", " lat-> " + lat + " lon-> " + lon);

            mLocation.setLatitude(lat);
            mLocation.setLongitude(lon);
        }
    }

    public void addWIFI(){
        nameWifiMap = dataHolder.getSsidContent();
        for (String name: nameWifiMap.keySet()){
            namesWifi.add(name);
            //Log.d("AddLocationActivity","Wifi: "+name);
        }
        mAdapterWIFI.notifyDataSetChanged();

    }

    public void addBLE(){
        nameBLEMAP = dataHolder.getBleContent();
        for (String name: nameBLEMAP.keySet()){
            namesBLE.add(name);
            //Log.d("AddLocationActivity","BLE: "+name);
        }
        mAdapterBLE.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onHTTPResponse(OperationStatus response) {
       //TODO

    }
}
