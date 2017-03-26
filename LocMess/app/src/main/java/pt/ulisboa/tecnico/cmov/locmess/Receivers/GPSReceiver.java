package pt.ulisboa.tecnico.cmov.locmess.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pt.ulisboa.tecnico.cmov.locmess.Listeners.OnLocationReceivedListener;

/**
 * Created by trosado on 24/03/17.
 */

public class GPSReceiver extends BroadcastReceiver {

    OnLocationReceivedListener mActivity;

    public GPSReceiver(Context context){
        mActivity = (OnLocationReceivedListener) context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        double latitude = intent.getDoubleExtra("Lat",0);
        double longitude = intent.getDoubleExtra("Lon",0);
        mActivity.onGPSReceived(latitude,longitude);
    }
}
