package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnLocationReceivedListener;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.MainActivity;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.ServiceManager;

import static android.content.ContentValues.TAG;

/**
 * Created by trosado on 4/23/17.
 */

public class MasterService extends Service  implements OnLocationReceivedListener{

    private ServicesDataHolder dataHolder;
    private ServiceManager serviceManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: Started Master Service");
        serviceManager = ServiceManager.getInstance();
        dataHolder = ServicesDataHolder.getInstance();
        serviceManager.initReceivers(this);
    }

    @Override
    public void onGPSReceived(double lat, double lon) {
        dataHolder.setLatitude(new Float(lat));
        dataHolder.setLongitude(new Float(lon));
    }

    @Override
    public void onWifiReceived(String name, String ssid) {
        AbstractMap<String,String> map = dataHolder.getSsidContent();
        map.put(name,ssid);
        dataHolder.setSsidContent(map);
    }

    @Override
    public void onBleReceived(String name, String ble) {
        AbstractMap<String,String> map = dataHolder.getBleContent();
        map.put(name,ble);
        dataHolder.setBleContent(map);
    }

    @Override
    public void clearGPSList() {
        dataHolder.setLatitude(new Float(0));
        dataHolder.setLongitude(new Float(0));
    }

    @Override
    public void clearWifiList() {
        dataHolder.setSsidContent(new HashMap<String, String>());
    }

    @Override
    public void clearBluetoothList() {
        dataHolder.setBleContent(new HashMap<String, String>());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MasterService: ", "onDestroy -> unregistering receivers");
        serviceManager.unRegisterReceivers();
        Log.d("MasterService: ", "onDestroy -> stopping services");
        serviceManager.stopServices();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {

        return START_NOT_STICKY;
    }

}
