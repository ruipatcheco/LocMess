package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.DataObject;

/**
 * Created by brigadinhos on 17/05/2017.
 */

public class BluetoothConnection {

    private Context context;

    private BluetoothAdapter mBTAdapter;
    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;

    private BluetoothDevice mDevice;
    private UUID deviceUUID;

    private static final String appName = "LocMess";
    private static final UUID LOCMESS_UUID = UUID.fromString("c70f6eff-bef2-44c8-a6d4-82b0e2f0913d");

    private List<DataObject> objs;

    public BluetoothConnection(Context context) {
        this.context = context;
        startSession();
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket bsSocket;

        public AcceptThread() {
            BluetoothServerSocket socketTemp = null;

            try {
                socketTemp = mBTAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, LOCMESS_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bsSocket = socketTemp;
        }

        public void run() {
            BluetoothSocket bSocket = null;

            try {
                bSocket = bsSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (bSocket != null) {
                connected(bSocket, mDevice);
            }
        }

        public void cancel() {
            try {
                bsSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket socket;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            mDevice = device;
            deviceUUID = uuid;
        }

        public void run() {
            BluetoothSocket temp = null;

            try {
                temp = mDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }

            socket = temp;

            mBTAdapter.cancelDiscovery();

            try {
                socket.connect();
            } catch (IOException e) {
                e.printStackTrace();
                // can not connect so close the socket
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            connected(socket, mDevice);
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectedThread extends Thread {
        InputStream input = null;
        OutputStream output = null;
        BluetoothSocket socket;

        public ConnectedThread(BluetoothSocket socket) {
            this.socket = socket;

            try {
                input = socket.getInputStream();
                output = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {
                try {
                    bytes = input.read(buffer);
                    ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(buffer));
                    objs = (ArrayList<DataObject>) inputStream.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                    // end the connection
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void write(byte[] bytes) {
            try {
                output.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void startSession() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        if (acceptThread == null) {
            acceptThread = new AcceptThread();
            acceptThread.start();
        }
    }

    public void startClient(BluetoothDevice device, UUID uuid) {
        connectThread = new ConnectThread(device, uuid);
        connectThread.start();
    }

    private void connected(BluetoothSocket socket, BluetoothDevice device) {
        connectedThread = new ConnectedThread(socket);
        connectedThread.start();
    }

    public void write(byte[] out) {
        connectedThread.write(out);
    }

}
