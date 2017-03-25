package pt.ulisboa.tecnico.cmov.locmess;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.locmess.Listeners.CustomOnItemSelectedListener;
import pt.ulisboa.tecnico.cmov.locmess.Receivers.LocationReceiver;
import pt.ulisboa.tecnico.cmov.locmess.Services.GPSService;

public class AddLocationActivity extends AppCompatActivity {
    EditText mLocationName;
    Spinner mLocationType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        startServices();

        LocationReceiver locationReceiver = new LocationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GPSService.GPS);
        registerReceiver(locationReceiver, intentFilter);



        mLocationName = (EditText) findViewById(R.id.location_name_text);

        mLocationType = (Spinner) findViewById(R.id.location_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.location_types_list,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mLocationType.setAdapter(adapter);
        mLocationType.setOnItemSelectedListener(new CustomOnItemSelectedListener());


    }




    private void startServices(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getApplicationContext(),"No Permissions",Toast.LENGTH_LONG);
            int code=0;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},code);
            // return;
        }else{
            System.out.print("Started Services");
            startService(new Intent(getApplicationContext(),GPSService.class));
        }
    }

    private void stopServices(){
        stopService(new Intent(getApplicationContext(),GPSService.class));
    }




}
