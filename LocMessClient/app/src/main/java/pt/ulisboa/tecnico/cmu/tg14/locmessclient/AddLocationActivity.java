package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnLocationReceivedListener;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Receivers.BluetoothReceiver;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Receivers.GPSReceiver;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services.BluetoothService;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services.GPSService;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.ServerActions;

public class AddLocationActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,OnLocationReceivedListener {


    private static final String TAG = "AddLocationActivity";
    private static final String GPS="GPS";
    private static final String WIFI="WIFI";
    private static final String BLE="BLE";
    private RadioGroup mLocationRadio;
    private Spinner mLocationList;
    private Button mNext;
    private EditText mLocationName;
    private EditText mLocationRadius;

    List<String> mLocationsGPS;
    List<String> mLocationsWIFI;

    private HashMap<String, String> nameBLEMAP;
    private List<String> namesBLE;

    Activity activity;
    private boolean someOptionChecked; // to check if user selected an item
    private String mType;
    private String mID;
    private ArrayAdapter<String> mAdapterGPS;
    private ArrayAdapter<String> mAdapterWIFI;
    private ArrayAdapter<String> mAdapterBLE;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        startServices();
        initReceivers();

        someOptionChecked = false;
        activity = this;
        mLocation = new Location();
        mLocationRadio = (RadioGroup) findViewById(R.id.add_location_radio);
        mLocationList = (Spinner) findViewById(R.id.add_location_spinner);
        mNext = (Button) findViewById(R.id.add_location_button);
        mLocationName = (EditText) findViewById(R.id.add_location_name);
        mLocationRadius = (EditText) findViewById(R.id.add_location_radius);

        //Set visibility for radius
        mLocationRadius.setVisibility(View.INVISIBLE);

        nameBLEMAP = new HashMap<>();
        namesBLE = new ArrayList<>();

        mLocationsGPS = new ArrayList<>();
        mLocationsWIFI = new ArrayList<>();

        mAdapterGPS = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, mLocationsGPS);
        mAdapterWIFI = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, mLocationsWIFI);
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




        namesBLE.add("BLE2");
        mLocationsGPS.add("GPS3");
        mLocationsWIFI.add("WIFI14");
        mLocationsGPS.add("GPS5");

        //TODO Add network communication



        mLocationList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String address = parent.getItemAtPosition(position).toString();

                switch (mType){
                    case BLE:
                        mLocation.setBle(nameBLEMAP.get(address));
                        break;
                    case WIFI:
                        mLocation.setSsid(address);
                        break;

                }

                Log.d(TAG, "onItemSelected: "+address);
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

                mID = mLocationList.getSelectedItem().toString();

                mLocation.setName(mLocationName.getText().toString());

                ServerActions serverActions = new ServerActions(getApplicationContext());
                serverActions.createLocation(mLocation);

                finish();

            }
        });
    }

    private void initReceivers() {
        GPSReceiver GPSReceiver = new GPSReceiver(this);
        IntentFilter gpsIntentFilter = new IntentFilter();
        gpsIntentFilter.addAction(GPSService.GPS);
        registerReceiver(GPSReceiver, gpsIntentFilter);

        BluetoothReceiver BTReceiver = new BluetoothReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(BTReceiver,filter);


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
                    mLocationList.setAdapter(mAdapterGPS);

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

        return true;
    }

    private void startServices(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getApplicationContext(),"No Permissions",Toast.LENGTH_LONG).show();
            int code=0;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},code);

        }else{
            Log.d("AddLocationActivity","Started Services");
            startService(new Intent(getApplicationContext(), BluetoothService.class));
            startService(new Intent(getApplicationContext(),GPSService.class));
        }
    }

    private void stopServices(){
        stopService(new Intent(getApplicationContext(),GPSService.class));
        stopService(new Intent(getApplicationContext(),BluetoothService.class));
    }

    @Override
    public void onGPSReceived(double lat, double lon) {
        Log.d(TAG, "onGPSReceived: gps");
        if(mType.equals(GPS)){
            mLocation.setLatitude(lat);
            mLocation.setLongitude(lon);
        }
        mLocationsGPS.add("Lat: "+lat);
        mLocationsGPS.add("Lon: "+lon);

        //mLocationInfo.setText("Lat: "+lat+"\nLon: "+lon);
        mAdapterGPS.notifyDataSetChanged();
    }

    @Override
    public void clearGPSList() {
        mLocationsGPS.clear();
    }

    @Override
    public void onWifiReceived(String ssid) {
        if(!mLocationsWIFI.contains(ssid)){
            mLocationsWIFI.add(ssid);
            mAdapterWIFI.notifyDataSetChanged();
        }
        Log.d("AddLocationActivity","Wifi: "+ssid);

    }

    /*
    @Override
    public void clearWifiList() {
        mLocationsWIFI.clear();
    }
    */

    @Override
    public void onBleReceived(String name, String ble) {
        Log.d(TAG, "onBleReceived: ble");
        if(!nameBLEMAP.containsKey(name)){
            nameBLEMAP.put(name, ble);
            namesBLE.add(name);
            mAdapterBLE.notifyDataSetChanged();
        }

        Log.d("AddLocationActivity","Bluetooth: "+ble);
    }

    @Override
    public void clearBluetoothList() {
        nameBLEMAP.clear();
        namesBLE.clear();
    }

}
