package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners;

/**
 * Created by tiago on 25/03/2017.
 */

public interface OnLocationReceivedListener {
     void onGPSReceived(double lat, double lon);
     void onWifiReceived(String name,String ssid);
     void onBleReceived(String name, String ble);
     void clearGPSList();
     void clearWifiList();
     void clearBluetoothList();
}
