package pt.ulisboa.tecnico.cmov.locmess.Services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.util.ArraySet;

import java.util.Set;

import pt.ulisboa.tecnico.cmov.locmess.DataObjects.BTDevice;
import pt.ulisboa.tecnico.cmov.locmess.Receivers.BluetoothReceiver;

/**
 * Created by tiago on 30/03/2017.
 */

public class BluetoothService extends Service {

    BluetoothAdapter mBTAdapter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        System.out.println("Added Bluetooth stuff");
        if(mBTAdapter == null){
            //FIXME Bluetooth not supported
        }

        if(!mBTAdapter.isEnabled()){
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBT);
        }

        if(mBTAdapter.isDiscovering())
            mBTAdapter.cancelDiscovery();

        mBTAdapter.startDiscovery();

    }
}
