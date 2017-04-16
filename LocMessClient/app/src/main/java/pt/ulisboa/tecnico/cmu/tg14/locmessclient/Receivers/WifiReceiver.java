package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.util.Log;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnLocationReceivedListener;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services.WifiService;

import static android.content.ContentValues.TAG;

/**
 * Created by trosado on 16/04/17.
 */

public class WifiReceiver extends BroadcastReceiver {
    OnLocationReceivedListener mActivity;

    public WifiReceiver(OnLocationReceivedListener mActivity) {
        this.mActivity = mActivity;
    }

    @Override

    public void onReceive(Context context, Intent intent) {

        ArrayList<ScanResult> results = intent.getParcelableArrayListExtra(WifiService.SERVICE_RESULT);
        Log.d(TAG, "onReceive: called");
        mActivity.clearWifiList();
        for(ScanResult scanResult: results)
            mActivity.onWifiReceived(scanResult.SSID,scanResult.BSSID);

    }
}
