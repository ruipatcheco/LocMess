package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

<<<<<<< HEAD
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
=======
import java.io.IOException;
>>>>>>> a68b79a30b6ea2d2175e691da47f7d24d68bc08c
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.HashResult;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.LocationQuery;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.OperationStatus;
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

public class DBService extends Service implements OnResponseListener<String> {

    private ServerActions  serverActions;
    private Handler handler = null;
    private static Runnable runnable = null;
    private ServicesDataHolder dataHolder = ServicesDataHolder.getInstance();
    private FeedReaderDbHelper dbHelper;
    private String latestServerHash;

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

                dbHelper = new FeedReaderDbHelper(getApplicationContext());


                if(dataHolder.isCentralizedMode()){

                    //UPDATE NEAR LOCATIONS
                    updateNearLocations();

                    //CHECK AND DELETE OFFLINE DELETED LOCATIONS
                    ArrayList<String> offlineDeletedLocationNames = dataHolder.getRemovedLocations();
                    if(offlineDeletedLocationNames.size()!=0){
                        removeLocationsFromServer(offlineDeletedLocationNames);
                    }

                    //CHECK AND INSERT OFFLINE INSERTED LOCATIONS
                    ArrayList<Location> offlineAddedLocations = checkOfflineAddedLocations();
                    if(offlineAddedLocations.size()!=0){
                        //SEND LOCATIONS TO SERVER
                        sendLocationsToServer(offlineAddedLocations);
                    }
    
                    boolean isUpdated = checkDBEqualToServerDB();

                    if(!isUpdated){
                        dbHelper.deleteAllLocations();
                        getAndInsertAllLocations();
                    }

                    updateMessages();
                }

                handler.postDelayed(runnable, 4000);
            }
        };

        handler.postDelayed(runnable, 2000);

    }

    private void removeLocationsFromServer(ArrayList<String> offlineDeletedLocationNames) {
        for (String name : offlineDeletedLocationNames){
            serverActions.removeLocation(name, new OnResponseListener<OperationStatus>() {
                @Override
                public void onHTTPResponse(OperationStatus response) {
                    if(response.isERROR()){
                        //FIXME -> deu asneira
                    }
                }
            });
        }
        dataHolder.clearRemovedLocations();
    }

    private void updateMessages() {
        List<Location>  locations = dataHolder.getNearLocations();

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
    }

    private void getAndInsertAllLocations() {
        List<Location> locations = null;
        serverActions.getAllLocations(new OnResponseListener<List<Location>>() {
            @Override
            public void onHTTPResponse(List<Location> response) {
                dbHelper.insertAllLocations(response);
            }
        });
    }

    private boolean checkDBEqualToServerDB() {
        String localHash;

        serverActions.getLocationsNameHash(this);

        try {
            localHash = dbHelper.getLocationsNameHash();

            return (latestServerHash.equals(localHash) && (latestServerHash!= null) );

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendLocationsToServer(ArrayList<Location> offlineLocations) {
        for(Location l: offlineLocations){
            serverActions.createLocation(l, new OnResponseListener<OperationStatus>() {
                @Override
                public void onHTTPResponse(OperationStatus response) {
                    if(response.isOK()){
                        //SET BOOLEAN ISOFFLINE TO FALSE
                        dbHelper.setCentralized(l.getName());
                    }
                }
            });
        }
    }

    private ArrayList<Location> checkOfflineAddedLocations() {

        return null;
    }

    private void updateNearLocations(){
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
    }

    @Override
    public void onHTTPResponse(String response) {
        latestServerHash = response;
    }
}
