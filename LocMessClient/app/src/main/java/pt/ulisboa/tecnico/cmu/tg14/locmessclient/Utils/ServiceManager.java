package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Receivers.BluetoothReceiver;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Receivers.GPSReceiver;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Receivers.WifiReceiver;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services.BluetoothService;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services.GPSService;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services.WifiService;

/**
 * Created by trosado on 4/23/17.
 */

public class ServiceManager {
    private WifiReceiver mWifiReceiver;
    private BluetoothReceiver mBTReceiver;
    private GPSReceiver mGPSReceiver;

    private Context mActivity;
    private Service mService;

    public ServiceManager(Service service,Activity activity) {
        this.mService = service;
        this.mActivity = activity;
    }

    public void initReceivers() {
        mGPSReceiver = new GPSReceiver(mService);
        IntentFilter gpsIntentFilter = new IntentFilter();
        gpsIntentFilter.addAction(GPSService.GPS);
        mService.registerReceiver(mGPSReceiver, gpsIntentFilter);

        mBTReceiver = new BluetoothReceiver(mService);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mService.registerReceiver(mBTReceiver,filter);

        mWifiReceiver = new WifiReceiver(mService);
        IntentFilter wifiIntentFilter = new IntentFilter();
        wifiIntentFilter.addAction(WifiService.WIFI);
        mService.registerReceiver(mWifiReceiver, wifiIntentFilter);

    }



    public void startServices(){
        if (ActivityCompat.checkSelfPermission(mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(mActivity,"No Permissions",Toast.LENGTH_LONG).show();
            int code=0;
            ActivityCompat.requestPermissions((Activity) mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},code);

        }else{
            Log.d("MainActivity","Started Services");
            mService.startService(new Intent(mActivity,WifiService.class));
            mService.startService(new Intent(mActivity, BluetoothService.class));
            mService.startService(new Intent(mActivity,GPSService.class));
        }
    }


    public void stopServices(){
        mService.stopService(new Intent(mActivity,GPSService.class));
        mService.stopService(new Intent(mActivity,WifiService.class));
        mActivity.stopService(new Intent(mActivity,BluetoothService.class));
    }

    public void unRegisterReceivers(){
        mService.unregisterReceiver(mBTReceiver);
        mService.unregisterReceiver(mGPSReceiver);
        mService.unregisterReceiver(mWifiReceiver);
    }
}
