package pt.ulisboa.tecnico.cmov.locmess;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.locmess.Listeners.CustomOnItemSelectedListener;
import pt.ulisboa.tecnico.cmov.locmess.Listeners.OnLocationReceivedListener;
import pt.ulisboa.tecnico.cmov.locmess.Listeners.OnLocationTypeListener;
import pt.ulisboa.tecnico.cmov.locmess.Receivers.GPSReceiver;
import pt.ulisboa.tecnico.cmov.locmess.Services.GPSService;

public class AddLocationActivity extends AppCompatActivity implements OnLocationReceivedListener,OnLocationTypeListener {
    EditText mLocationName;
    Spinner mLocationType;
    EditText mLocationRadius;
    TextView mLocationInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        startServices();

        GPSReceiver GPSReceiver = new GPSReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GPSService.GPS);
        registerReceiver(GPSReceiver, intentFilter);



        mLocationName = (EditText) findViewById(R.id.location_name_text);
        mLocationType = (Spinner) findViewById(R.id.location_type);
        mLocationRadius = (EditText) findViewById(R.id.location_radius_text);
        mLocationInfo = (TextView) findViewById(R.id.location_type_info);



        List<String> locationTypes = new ArrayList<String>();
        locationTypes.add(getString(R.string.location_type_GPS));
        locationTypes.add(getString(R.string.location_type_WIFI));
        locationTypes.add(getString(R.string.location_type_BLE));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,locationTypes);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mLocationType.setAdapter(adapter);
        mLocationType.setOnItemSelectedListener(new CustomOnItemSelectedListener(this));


    }


    private void startServices(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getApplicationContext(),"No Permissions",Toast.LENGTH_LONG).show();
            int code=0;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},code);

        }else{
            System.out.print("Started Services");
            startService(new Intent(getApplicationContext(),GPSService.class));
        }
    }

    private void stopServices(){
        stopService(new Intent(getApplicationContext(),GPSService.class));
    }


    @Override
    public void onGPSReceived(double lat, double lon) {
        mLocationInfo.setText("Lat: "+lat+"\nLon: "+lon);
    }

    @Override
    public void onWifiReceived(String ssid) {
        mLocationInfo.setText("SSID: "+ssid);
    }

    @Override
    public void onBleReceived(String ble) {
        mLocationInfo.setText("BLE: "+ble);
    }


    @Override
    public void onGPSSelected() {
        mLocationRadius.setVisibility(View.VISIBLE);
    }

    @Override
    public void onWifiSelected() {
        mLocationRadius.setVisibility(View.GONE);
    }

    @Override
    public void onBleSelected() {
        mLocationRadius.setVisibility(View.GONE);
    }
}
