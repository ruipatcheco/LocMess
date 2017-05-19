package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.DataObject;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Message;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderDbHelper;

import static android.content.ContentValues.TAG;

/**
 * Created by tiago on 30/03/2017.
 */

public class BluetoothService extends Service {

    private static BluetoothAdapter mBTAdapter;

    private List<BluetoothDevice> alreadyPaired = new ArrayList<>();


    private BluetoothDevice mDevice;


    private static Handler mHandler = null;
    private static Runnable runnable = null;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public  void onCreate() {

        //Log.d("BluetoothReceiver","Bluetooth Started");

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();

        mHandler = new Handler();
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


                // Bluetooth discovery

                if(mBTAdapter.isDiscovering())
                    mBTAdapter.cancelDiscovery();

                mBTAdapter.startDiscovery();



                mHandler.postDelayed(runnable, 60000);
            }
        };

        mHandler.postDelayed(runnable, 2000);


        // Setup Server
        BluetoothOperations bluetoothOperations = BluetoothOperations.getInstance(getApplicationContext());
        bluetoothOperations.start();


    }
}
