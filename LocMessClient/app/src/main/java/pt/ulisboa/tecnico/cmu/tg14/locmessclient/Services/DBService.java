package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.HashResult;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.LocationQuery;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Message;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnResponseListener;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderDbHelper;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.ServerActions;

import static android.content.ContentValues.TAG;

/**
 * Created by tiago on 30/03/2017.
 */

public class DBService extends Service {

    private ServerActions  serverActions;
    private Handler handler = null;
    private static Runnable runnable = null;
    private ServicesDataHolder dataHolder = ServicesDataHolder.getInstance();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        Log.d("DBUpdater","DB Updater Started");
        serverActions = new ServerActions(this);


        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                LocationQuery query = new LocationQuery(dataHolder.getLatitude(),dataHolder.getLongitude(),
                        dataHolder.getSsidAddresses(),dataHolder.getBleAddresses());
                Gson gson = new Gson();
                Log.d(TAG,"DB service query "+gson.toJson(query));
                serverActions.getNearLocations(query, new OnResponseListener<List<Location>>() {
                    @Override
                    public void onHTTPResponse(List<Location> response) {
                        dataHolder.setNearLocations(response);

                        Log.d(TAG, "onHTTPResponse: nearLocations Set");
                    }
                });
                //FIXME example to
                serverActions.getListLocationHash(new OnResponseListener<HashResult>() {
                    @Override
                    public void onHTTPResponse(HashResult response) {
                        Log.d(TAG, "onHTTPResponse: "+ response.getHash());
                    }
                });
                //FIXME to remove example of base64 encode
                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    Log.d(TAG,"Hash: "+new String(Base64.encode(digest.digest("CMUCenas".getBytes("UTF-8")),Base64.NO_WRAP)));

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                List<Location>  locations = dataHolder.getNearLocations();
                final FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());

                //dbHelper.deleteAllMessages();

                for(Location location: locations){
                    serverActions.getMessagesFromLocation(location, new OnResponseListener<List<Message>>() {
                        @Override
                        public void onHTTPResponse(List<Message> response) {
                            ArrayList<Message> messages = dbHelper.getAllMessages();
                            messages.addAll(response);
                            dbHelper.insertAllMessages(messages);
                            Log.d(TAG, "onHTTPResponse: added Messages to DB");
                        }
                    });
                }

                serverActions.getAllLocations(new OnResponseListener<List<Location>>() {
                    @Override
                    public void onHTTPResponse(List<Location> response) {
                        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
                        dbHelper.insertAllLocations(response);

                        Log.d(TAG, "onHTTPResponse: Got all locations");
                    }
                });

                handler.postDelayed(runnable, 4000);
            }
        };

        handler.postDelayed(runnable, 2000);

    }
}
