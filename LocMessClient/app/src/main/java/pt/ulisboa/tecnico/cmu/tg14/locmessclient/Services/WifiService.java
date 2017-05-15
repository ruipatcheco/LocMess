package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trosado on 16/04/17.
 */

public class WifiService extends Service {
    public final static String WIFI = "pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services.WIFI";
    public final static String SERVICE_RESULT = "Result";

    private Handler handler = null;
    private static Runnable runnable = null;
    private WifiManager wifiManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        //Log.d("WifiService","Wifi Started");
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {

                //Log.d("WifiService","Wifi still running");

                List<ScanResult> results = wifiManager.getScanResults();
                Intent i = new Intent(WIFI);
                i.putParcelableArrayListExtra(SERVICE_RESULT,new ArrayList<ScanResult>(results));
                sendBroadcast(i);

                handler.postDelayed(runnable, 60000);
            }
        };

        handler.postDelayed(runnable, 2000);

    }

}
