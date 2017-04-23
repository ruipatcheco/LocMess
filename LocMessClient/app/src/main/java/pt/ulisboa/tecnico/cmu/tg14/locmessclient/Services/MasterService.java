package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnLocationReceivedListener;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.ServiceManager;

/**
 * Created by trosado on 4/23/17.
 */

public class MasterService extends Service  implements OnLocationReceivedListener{

    private ServicesDataHolder dataHolder;
    private ServiceManager mServiceManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        dataHolder = ServicesDataHolder.getInstance();
        mServiceManager = new ServiceManager(this,(Activity) getApplicationContext()); //FIXME Hackish way
        mServiceManager.startServices();
        mServiceManager.initReceivers();
    }

    @Override
    public void onGPSReceived(double lat, double lon) {
        dataHolder.setLatitude(new Float(lat));
        dataHolder.setLongitude(new Float(lon));
    }

    @Override
    public void onWifiReceived(String name, String ssid) {
        
    }

    @Override
    public void onBleReceived(String name, String ble) {

    }

    @Override
    public void clearGPSList() {

    }

    @Override
    public void clearWifiList() {

    }

    @Override
    public void clearBluetoothList() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceManager.unRegisterReceivers();
        mServiceManager.stopServices();
    }
}
