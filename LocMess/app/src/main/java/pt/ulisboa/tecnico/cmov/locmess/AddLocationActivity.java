package pt.ulisboa.tecnico.cmov.locmess;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.locmess.DataObjects.Location;
import pt.ulisboa.tecnico.cmov.locmess.Listeners.CustomOnItemSelectedListener;
import pt.ulisboa.tecnico.cmov.locmess.Listeners.OnLocationReceivedListener;
import pt.ulisboa.tecnico.cmov.locmess.Listeners.OnLocationTypeListener;
import pt.ulisboa.tecnico.cmov.locmess.Receivers.BluetoothReceiver;
import pt.ulisboa.tecnico.cmov.locmess.Receivers.GPSReceiver;
import pt.ulisboa.tecnico.cmov.locmess.Services.BluetoothService;
import pt.ulisboa.tecnico.cmov.locmess.Services.GPSService;

public class AddLocationActivity extends AppCompatActivity implements OnLocationReceivedListener,OnLocationTypeListener {
    EditText mLocationName;
    Spinner mLocationType;
    Spinner mLocationList; // Location list shows wifi networks or ble devices available
    EditText mLocationRadius;
    TextView mLocationInfo;
    Button mLocationSubmition;

    Location location;
    WifiManager wifiManager;

    List<String> locationResults;
    ArrayAdapter<String> mLocationResultsAdapter;

    List<String> bluetoothResults;
    ArrayAdapter<String> mBLEListAdapter;

    LocationRegisterTask mRegisterTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        startServices();

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

        location = new Location();

        locationResults = new ArrayList<>();
        bluetoothResults = new ArrayList<>();


        mLocationName = (EditText) findViewById(R.id.location_name_text);
        mLocationRadius = (EditText) findViewById(R.id.location_radius_text);
        mLocationInfo = (TextView) findViewById(R.id.location_type_info);


        mLocationType = (Spinner) findViewById(R.id.location_type);

        List<String> locationTypes = new ArrayList<String>();
        locationTypes.add(getString(R.string.location_type_GPS));
        locationTypes.add(getString(R.string.location_type_WIFI));
        locationTypes.add(getString(R.string.location_type_BLE));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,locationTypes);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mLocationType.setAdapter(adapter);
        mLocationType.setOnItemSelectedListener(new CustomOnItemSelectedListener(this));

        mLocationList = (Spinner) findViewById(R.id.devices_list);


        mLocationResultsAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,locationResults);
        mLocationResultsAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mLocationList.setAdapter(mLocationResultsAdapter);


        mBLEListAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,bluetoothResults);
        mBLEListAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mLocationList.setAdapter(mBLEListAdapter);



        mLocationSubmition = (Button) findViewById(R.id.location_submit_button);
        mLocationSubmition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitLocation();
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopServices();
    }


    private void startServices(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getApplicationContext(),"No Permissions",Toast.LENGTH_LONG).show();
            int code=0;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},code);

        }else{
            Log.d("AddLocationActivity","Started Services");

            startService(new Intent(getApplicationContext(), BluetoothService.class));
            startService(new Intent(getApplicationContext(),GPSService.class));
            wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
        }
    }

    private void stopServices(){
        stopService(new Intent(getApplicationContext(),GPSService.class));
        stopService(new Intent(getApplicationContext(),BluetoothService.class));
    }

    @Override
    public void onGPSReceived(double lat, double lon) {
        location.setLatitutde(lat);
        location.setLongitude(lat);

        mLocationInfo.setText("Lat: "+lat+"\nLon: "+lon);
    }

    @Override
    public void onWifiReceived(String ssid) {
        if(!locationResults.contains(ssid)){
            locationResults.add(ssid);
            mLocationResultsAdapter.notifyDataSetChanged();
        }
        Log.d("AddLocationActivity","Wifi: "+ssid);

    }

    @Override
    public void onBleReceived(String ble) {
        if(!bluetoothResults.contains(ble)){
            bluetoothResults.add(ble);
            mBLEListAdapter.notifyDataSetChanged();
        }

        Log.d("AddLocationActivity","Bluetooth: "+ble);
    }

    @Override
    public void cleanBluetoothList() {
        bluetoothResults.clear();
    }


    @Override
    public void onGPSSelected() {
        mLocationRadius.setVisibility(View.VISIBLE);
        mLocationList.setVisibility(View.GONE);
    }

    @Override
    public void onWifiSelected() {
        mLocationRadius.setVisibility(View.GONE);
        mLocationList.setVisibility(View.VISIBLE);

        locationResults.clear();
        mLocationList.setAdapter(mLocationResultsAdapter);

        List<ScanResult> results = wifiManager.getScanResults();
        for(ScanResult result: results)
            onWifiReceived(result.SSID);


    }

    @Override
    public void onBleSelected() {
        mLocationRadius.setVisibility(View.GONE);
        mLocationList.setVisibility(View.VISIBLE);
        mLocationList.setAdapter(mBLEListAdapter);

    }


    private void submitLocation(){

        /// FIXME Verify also if radius is null

        String selectedItem = mLocationType.getSelectedItem().toString();

        location.setName(mLocationName.getText().toString());

        if(selectedItem == getString(R.string.location_type_GPS)){
            location.setRadius(Integer.valueOf(mLocationRadius.getText().toString()));
        }else if(selectedItem == getString(R.string.location_type_WIFI)){
            location.setSsid(mLocationList.getSelectedItem().toString());
        }else if(selectedItem == getString(R.string.location_type_BLE)){
            location.setBle(mLocationList.getSelectedItem().toString());
        }


        mRegisterTask = new LocationRegisterTask(location.getName(),location.getSsid(),location.getBle(),location.getLatitutde(),location.getLongitude(),location.getRadius());
        mRegisterTask.execute((Void) null);
    }





    public class LocationRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mName;
        private final String mSsid;
        private final String mBle;
        private final double mLat;
        private final double mLon;
        private final int mRadius;

        public LocationRegisterTask(String mName, String mSsid, String mBle, double mLat, double mLon, int mRadius) {
            this.mName = mName;
            this.mSsid = mSsid;
            this.mBle = mBle;
            this.mLat = mLat;
            this.mLon = mLon;
            this.mRadius = mRadius;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            System.out.println("Name: "+mName);
            System.out.println("SSID: "+mSsid);
            System.out.println("BLE: "+mBle);
            System.out.println("Lat: "+mLat);
            System.out.println("Lon: "+mLon);
            System.out.println("Radius: "+mRadius);

/*
            RequestQueue queue = Volley.newRequestQueue(getBaseContext());
            //FIXME meter urls num ficheiro de config
            String url = "http://194.210.220.190:8080/user/create?username="+mUsername+"&password="+mPassword;

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //mTextView.setText("Response is: "+ response.substring(0,500));
                            System.out.print("response:"+response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //mTextView.setText("That didn't work!");
                    System.out.print("error:"+error);

                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);



            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mUsername)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }
*/
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegisterTask = null;
           /* showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }*/
        }

        @Override
        protected void onCancelled() {
           /* mRegisterTask = null;
            showProgress(false);*/
        }
    }



}
