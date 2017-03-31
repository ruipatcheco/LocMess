package pt.ulisboa.tecnico.cmov.locmess.Receivers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import pt.ulisboa.tecnico.cmov.locmess.Listeners.OnLocationReceivedListener;

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
            mActivity.onBleReceived(device.getAddress());
        }else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
            mActivity.cleanBluetoothList();
        }
    }
}
