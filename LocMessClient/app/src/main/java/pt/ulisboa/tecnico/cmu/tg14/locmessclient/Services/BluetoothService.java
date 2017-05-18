package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.DataObject;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by tiago on 30/03/2017.
 */

public class BluetoothService extends Service {

    private BluetoothAdapter mBTAdapter;
    private BluetoothConnection mBluetoothConnection;
    private BluetoothPairing mBluetoothPairing;

    private List<BluetoothDevice> alreadyPaired = new ArrayList<>();

    private BluetoothDevice mDevice;

    private Handler handler = null;
    private static Runnable runnable = null;

    private Context context;

    private static final UUID LOCMESS_UUID = UUID.fromString("c70f6eff-bef2-44c8-a6d4-82b0e2f0913d");


    public void startBluetoothConnection(BluetoothDevice device, UUID uuid) {
        mBluetoothConnection.startClient(device, uuid);
    }

    public void sendData(DataObject dataObject) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(dataObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = bos.toByteArray();

        mBluetoothConnection.write(bytes);
    }

    public void startConnection() {
        startBluetoothConnection(mDevice, LOCMESS_UUID);
    }

    public void assignDeviceToConnect() {
        List<BluetoothDevice> devices = mBluetoothPairing.getListBluetoothDevicesDetected();
        if (devices != null) {
            while (devices.size() > 0) {
                if (alreadyPaired.contains(devices.get(0))) {
                    devices.remove(0);
                    continue;
                }
                // assign the first device
                mDevice = devices.get(0);
                alreadyPaired.add(devices.get(0));
                return;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createBond() {
        mDevice.createBond();
        mBluetoothConnection = new BluetoothConnection(context);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        //Log.d("BluetoothReceiver","Bluetooth Started");

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();

        handler = new Handler();
        runnable =  new Runnable() {
            public void run() {
                //Log.d("BluetoothReceiver","Bluetooth still running");
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
