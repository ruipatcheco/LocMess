package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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

public class DBService extends Service {

    private ServerActions  serverActions;
    private Handler handler = null;
    private static Runnable runnable = null;
    private ServicesDataHolder dataHolder = ServicesDataHolder.getInstance();
    private FeedReaderDbHelper dbHelper;
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
                dbHelper = new FeedReaderDbHelper(getApplicationContext());

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
        Byte[] serverHash, localHash;
        localHash = dbHelper.getLocationsNameHash();

        serverActions.getLocationsNameHash();

        return (serverHash.equals(localHash) && serverHash != null);
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
}
