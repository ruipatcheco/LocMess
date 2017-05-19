package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services.BluetoothOperations;

public class BluetoothPairingActivity extends AppCompatActivity {
    private static final String TAG = "Pairing BT";
    private ListView deviceList;
    private Button refreshButton;
    private ArrayAdapter<String> mDeviceListAdapter;
    private List<String>  names;
    private BluetoothAdapter mBluetoothAdapter;

    private ServicesDataHolder dataHolder;

    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                dataHolder.addBleContent(device);
                names = dataHolder.getBleNames();
                mDeviceListAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_pairing);

        dataHolder = ServicesDataHolder.getInstance();

        refreshButton = (Button) findViewById(R.id.button_refresh);
        deviceList = (ListView) findViewById(R.id.list_bt_devices);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Enable discoverable
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);


        final ServicesDataHolder dataHolder = ServicesDataHolder.getInstance();
        BluetoothOperations bluetoothOperations = BluetoothOperations.getInstance(this);
        bluetoothOperations.stop();
        names = dataHolder.getBleNames();
        mDeviceListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        deviceList.setAdapter(mDeviceListAdapter);

    }



    public void btnDiscover(View view) {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");
        }

        mBluetoothAdapter.startDiscovery();
        IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
    }
}
