package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners;

import android.bluetooth.BluetoothDevice;

/**
 * Created by tiago on 25/03/2017.
 */

public interface OnLocationReceivedListener {
     void onGPSReceived(double lat, double lon);
     void onWifiReceived(String name,String ssid);
     void onBleReceived(BluetoothDevice device);
     void clearGPSList();
     void clearWifiList();
     void clearBluetoothList();
}
