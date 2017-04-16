package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Receivers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnLocationReceivedListener;

import static android.content.ContentValues.TAG;


/**
 * Created by tiago on 30/03/2017.
 */

public class BluetoothReceiver extends BroadcastReceiver {

    OnLocationReceivedListener mActivity;

    public BluetoothReceiver(Context context){
        mActivity = (OnLocationReceivedListener) context;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if(BluetoothDevice.ACTION_FOUND.equals(action)){
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.d(TAG, "onReceive: addr:"+device.getAddress());
            mActivity.onBleReceived(device.getName() ,device.getAddress());
        }else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
            mActivity.clearBluetoothList();
        }
    }
}
