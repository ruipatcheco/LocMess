package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by tiago on 30/03/2017.
 */

public class BluetoothService extends Service {

    private BluetoothAdapter mBTAdapter;
    private Handler handler = null;
    private static Runnable runnable = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        Log.d("BluetoothReceiver","Bluetooth Started");

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                Log.d("BluetoothReceiver","Bluetooth still running");
                if(mBTAdapter == null){
                    //FIXME Bluetooth not supported
                    return;
                }

                if(!mBTAdapter.isEnabled()){
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(enableBtIntent);
                }

                if(mBTAdapter.isDiscovering())
                    mBTAdapter.cancelDiscovery();

                mBTAdapter.startDiscovery();



                handler.postDelayed(runnable, 60000);
            }
        };

        handler.postDelayed(runnable, 2000);

    }
}
