package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Receivers;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnLocationReceivedListener;

/**
 * Created by trosado on 24/03/17.
 */

public class GPSReceiver extends BroadcastReceiver {

    OnLocationReceivedListener mService;

    public GPSReceiver(Service service){
        mService = (OnLocationReceivedListener) service;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //mService.clearGPSList(); FIXME clear the list somehow
        double latitude = intent.getDoubleExtra("Lat",0);
        double longitude = intent.getDoubleExtra("Lon",0);
        mService.onGPSReceived(latitude,longitude);

    }
}
