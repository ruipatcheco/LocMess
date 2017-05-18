package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.LocationQuery;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.MessageServer;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.OperationStatus;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Message;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Profile;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.LocationNotFoundException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.MessageNotFoundException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.MultipleRowsAfectedException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.ProfileNotFoundException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnResponseListener;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderDbHelper;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.Network.ServerActions;

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
    private boolean updateProfiles = true;
    private boolean updateOldProfileKeys = true;
    private boolean updateOldMessages = true;
    private boolean stop = false;
    private Future runnableManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.stop = true;
        Log.d(TAG, "onDestroy: GOT DESTROiED");
        runnableManager.cancel(true);
    }

    @Override
    public void onCreate() {

        //Log.d("DBUpdater","DB Updater Started");
        serverActions = new ServerActions(this);
        dbHelper = FeedReaderDbHelper.getInstance(getApplicationContext());

        //Do once
        if(dataHolder.isCentralizedMode()) {
            //FIXME -> quando o login estiver a bombar

        }


        handler = new Handler();
        runnable = new Runnable() {
            public void run() {

                if(dataHolder.isCentralizedMode()||!stop){

                    //Log.d("DBService", "entered DBService");


                    ////////////////---------------PROFILES----------------------/////////////

                    //OBTAIN OLDER PROFILES
                    if(updateOldProfileKeys){
                        updateOldProfileKeys = false;
                        getAndInsertOldProfileKeys();
                    }


                    //CHECK AND DELETE OFFLINE DELETED PROFILES
                    ArrayList<Profile> offlineDeletedProfiles = dbHelper.getAllProfilesToRemoveFromServer();
                    //Log.d("DBService", "offline deleted profiles -> "+offlineDeletedProfiles.size());
                    if(offlineDeletedProfiles.size()!=0){
                        removeProfilesFromServer(offlineDeletedProfiles);
                        updateProfiles =true;
                    }


                    //CHECK AND INSERT OFFLINE INSERTED PROFILES
                    ArrayList<Profile> offlineInsertedProfiles = dbHelper.getAllProfilesAddedWhileDecentralized();
                    //Log.d("DBService", "offline inserted profiles -> "+offlineInsertedProfiles.size());
                    if(offlineInsertedProfiles.size()!=0){
                        insertProfilesToServer(offlineInsertedProfiles);
                        updateProfiles = true;
                    }

                    if(updateProfiles){
                        //CLEAR SERVER PROFILE LIST AND OBTAIN ALL PROFILES FROM SERVER
                        getAndInsertAllServerProfiles();
                    }




                    ////////////////---------------LOCATIONS----------------------/////////////

                    //CHECK AND DELETE OFFLINE DELETED LOCATIONS
                    ArrayList<String> offlineDeletedLocationNames = dataHolder.getRemovedLocations();
                    //Log.d("DBService", "offline deleted locations -> "+offlineDeletedLocationNames.size());
                    if(offlineDeletedLocationNames.size()!=0){
                        removeLocationsFromServer(offlineDeletedLocationNames);
                    }

                    //CHECK AND INSERT OFFLINE INSERTED LOCATIONS
                    ArrayList<Location> offlineAddedLocations = checkOfflineAddedLocations();
                    //Log.d("DBService", "offline added locations -> "+offlineAddedLocations.size());
                    if(offlineAddedLocations.size()!=0){
                        //SEND LOCATIONS TO SERVER
                        sendLocationsToServer(offlineAddedLocations);
                    }

                    //UPDATE NEAR LOCATIONS
                    updateNearLocations();

                    boolean isUpdated = checkDBEqualToServerDB();

                    if(!isUpdated){
                        //Log.d("DBService", "Local Locations DB differs from server's, clearing local DB");

                        dbHelper.deleteAllLocations();
                        getAndInsertAllLocations();
                    }

                    ////////////////---------------LOCATIONS----------------------/////////////

                    if(updateOldMessages){
                        //OBTAIN OLDER MESSAGES
                        getAndInsertOldMessages();
                    }

                    ////////////////---------------MESSAGES----------------------/////////////

                    updateMessages();
                }

                handler.postDelayed(runnable, 5000);
            }
        };

        runnableManager = Executors.newSingleThreadExecutor().submit(runnable);

        handler.postDelayed(runnable, 2000);

    }

    private void getAndInsertOldMessages() {
        ArrayList<Message> result = new ArrayList<>();

        dbHelper.deleteAllMyMessagesNotNearby(dataHolder.getUsername());
        serverActions.getMyMessages(new OnResponseListener<List<Message>>() {

            @Override
            public void onHTTPResponse(List<Message> response) {
                updateOldMessages = false;
                //Log.d("DBService", "Adding my own old messages -> "+response.size());


                //boolean isCentralized = true, boolean isNearby = false
                for(Message m : response){
                    dbHelper.insertMessage(m);
                }
            }
        });
    }


    private void insertProfilesToServer(ArrayList<Profile> offlineInsertedProfiles) {
        for(Profile p : offlineInsertedProfiles){

            //Log.d("DBService", "inserting to server profile key -> "+p.getKey());

            serverActions.insertProfile(p, new OnResponseListener<OperationStatus>() {
                @Override
                public void onHTTPResponse(OperationStatus response) {
                    if(response.isERROR()){
                        //FIXME -> deu asneira
                    }
                }
            });
            try {
                dbHelper.updateProfileInsertedToServer(p.getKey(),p.getValue());
            } catch (ProfileNotFoundException e) {
                e.printStackTrace();
            } catch (MultipleRowsAfectedException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeProfilesFromServer(ArrayList<Profile> offlineDeletedProfiles) {
        for(Profile p : offlineDeletedProfiles){

            //Log.d("DBService", "removing from server profile key -> "+p.getKey());

            serverActions.removeProfile(p, new OnResponseListener<OperationStatus>() {
                @Override
                public void onHTTPResponse(OperationStatus response) {
                    if(response.isERROR()){
                        //FIXME -> deu asneira
                    }
                }
            });
            dbHelper.deleteProfile(p.getKey());
        }
    }


    private void getAndInsertAllServerProfiles() {
        serverActions.getProfileKeys(new OnResponseListener<List<Profile>>() {

            @Override
            public void onHTTPResponse(List<Profile> response) {
                updateProfiles = false;
                List<Profile> profiles = new ArrayList<Profile>();
                for(Profile p : response){
                    profiles.add(p);
                }
                //Log.d("DBService", "Adding server profile keys -> "+profiles.size());

                dbHelper.deleteAllServerProfiles();
                dbHelper.insertAllServerProfiles(profiles);
            }
        });
    }

    private void getAndInsertOldProfileKeys() {
        serverActions.getMyProfileKeys(new OnResponseListener<List<Profile>>() {

            @Override
            public void onHTTPResponse(List<Profile> profiles) {
                //Log.d("DBService", "Adding my own old profile keys -> "+profiles.size());
                dbHelper.deleteAllProfiles();
                dbHelper.insertAllProfilesFromServer(profiles);
            }
        });
    }

    private void removeLocationsFromServer(ArrayList<String> offlineDeletedLocationNames) {
        for (String name : offlineDeletedLocationNames){
            //Log.d("DBService", "Removing offline deleted location from server -> "+name);

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

        ArrayList<Message> offlineInsertedMessages = dbHelper.getAllMessagesAddedWhileDecentralized();
        Log.d("DBService", "offline inserted messages -> " + offlineInsertedMessages.size());


        if(offlineInsertedMessages.size()!=0){
            insertMessagesToServer(offlineInsertedMessages);
        }

        ArrayList<Message> offlineDeletedMessages = dbHelper.getAllMessagesDeletedWhileDecentralized();

        if(offlineDeletedMessages.size()!=0){
            deleteMessagesFromServer(offlineDeletedMessages);
        }
        //Log.d("DBService", "offline deleted messages -> " + offlineDeletedMessages.size());


        dbHelper.deleteAllNearbyMessages();

        for(Location location: locations){
            Log.d("DBService", "searching for messages on location -> " + location.getName());

            serverActions.getMessagesFromLocation(location, new OnResponseListener<List<Message>>() {
                @Override
                public void onHTTPResponse(List<Message> response) {

                    Gson gson = new Gson();
                    for(Message m : response){
                        Log.d("DBService", "added nearby message " + gson.toJson(m));
                        dbHelper.insertMessageFromServer(m);
                    }
                }
            });
        }



    }


    private void deleteMessagesFromServer(ArrayList<Message> offlineDeletedMessages) {
        for(Message m : offlineDeletedMessages){

            MessageServer ms = new MessageServer(m.getUUID(),m.getCreationTime(),m.getStartTime(),m.getEndTime(),m.getContent(),m.getPublisher(),m.getLocation(),m.getWhiteList(),m.getBlackList());

            //Log.d("DBService", "deleting message from server -> "+m.getUUID());

            serverActions.removeMessage(ms, new OnResponseListener<OperationStatus>() {
                @Override
                public void onHTTPResponse(OperationStatus response) {
                    if(response.isERROR()){
                        //FIXME -> deu asneira
                    }
                }
            });

            dbHelper.deleteMessage(m.getUUID().toString());
        }
    }

    private void insertMessagesToServer(ArrayList<Message> offlineInsertedMessages) {
        for(Message m : offlineInsertedMessages){

            Log.d("DBService", "inserting to server message -> "+m.getUUID());
            MessageServer ms = new MessageServer(m.getUUID(),m.getCreationTime(),m.getStartTime(),m.getEndTime(),m.getContent(),m.getPublisher(),m.getLocation(),m.getWhiteList(),m.getBlackList());

            serverActions.createMessage(ms, new OnResponseListener<OperationStatus>() {
                @Override
                public void onHTTPResponse(OperationStatus response) {
                    if(response.isERROR()){
                        //FIXME -> deu asneira
                    }
                }
            });

            try {
                dbHelper.updateMessageInsertedToServer(m.getUUID().toString());
            } catch (MultipleRowsAfectedException e) {
                e.printStackTrace();
            } catch (MessageNotFoundException e) {
                e.printStackTrace();
            }

        }
    }



    private void getAndInsertAllLocations() {
        List<Location> locations = null;
        serverActions.getAllLocations(new OnResponseListener<List<Location>>() {
            @Override
            public void onHTTPResponse(List<Location> response) {
                for(Location l : response){
                    //Log.d("DBService", "Re-Filling DB with location -> "+l.getName());
                }
                dbHelper.insertAllLocations(response);
            }
        });
    }

    private boolean checkDBEqualToServerDB() {
        String localHash = null;

        serverActions.getListLocationHash(this);

        try {
            localHash = dbHelper.getLocationsNameHash();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Log.d("DBService", "CheckDBEquals local -> " + localHash + " server -> "+latestServerHash);

        if(latestServerHash == null || localHash == null){
            return false;
        }

        return localHash.contains(latestServerHash);

    }

    private void sendLocationsToServer(ArrayList<Location> offlineLocations) {

        for(Location l: offlineLocations){

            //Log.d("DBService", "Sending offline added locations to server ->"+l.getName());

            try {
                dbHelper.setCentralized(l.getName());
            } catch (LocationNotFoundException e) {
                e.printStackTrace();
            } catch (MultipleRowsAfectedException e) {
                e.printStackTrace();
            }

            serverActions.createLocation(l, new OnResponseListener<OperationStatus>() {
                @Override
                public void onHTTPResponse(OperationStatus response) {
                }
            });
        }
    }

    private ArrayList<Location> checkOfflineAddedLocations() {
        return dbHelper.getAllDescentralizedLocations();
    }

    private void updateNearLocations(){
        LocationQuery query = new LocationQuery(dataHolder.getLatitude(),dataHolder.getLongitude(),
                dataHolder.getSsidNames(),dataHolder.getBleNames());

        serverActions.getNearLocations(query, new OnResponseListener<List<Location>>() {
            @Override
            public void onHTTPResponse(List<Location> response) {

                for(Location l : response){
                    dataHolder.setNearLocations(response);
                    //Log.d("DBService", "received near location ->"+l.getName());
                }

            }
        });
    }

    @Override
    public void onHTTPResponse(String response) {
        latestServerHash = response;
    }
}
