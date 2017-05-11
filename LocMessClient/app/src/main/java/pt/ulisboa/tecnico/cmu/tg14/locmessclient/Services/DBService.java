package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.LocationQuery;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.OperationStatus;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Message;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Profile;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.LocationNotFoundException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.MultipleRowsAfectedException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.ProfileNotFoundException;
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
        dbHelper = new FeedReaderDbHelper(getApplicationContext());

        //Do once
        if(dataHolder.isCentralizedMode()) {
            //FIXME -> quando o login estiver a bombar

            //CHECK AND DELETE OFFLINE DELETED PROFILES
            ArrayList<Profile> offlineDeletedProfiles = dbHelper.getAllProfilesToRemoveFromServer();
            Log.d("DBService", "offline deleted profiles -> "+offlineDeletedProfiles.size());
            if(offlineDeletedProfiles.size()!=0){
                removeProfilesFromServer(offlineDeletedProfiles);
            }

            //CHECK AND INSERT OFFLINE INSERTED PROFILES
            ArrayList<Profile> offlineInsertedProfiles = dbHelper.getAllProfilesAddedWhileDecentralized();
            Log.d("DBService", "offline inserted profiles -> "+offlineInsertedProfiles.size());
            if(offlineInsertedProfiles.size()!=0){
                insertProfilesToServer(offlineInsertedProfiles);
            }

            dbHelper.deleteAllProfiles();

            //OBTAIN OLDER PROFILES
            getAndInsertAllProfileKeys();

        }


        handler = new Handler();
        runnable = new Runnable() {
            public void run() {


                if(dataHolder.isCentralizedMode()){

                    Log.d("DBService", "entered DBService");


                    ////////////////---------------PROFILES----------------------/////////////

                    //CHECK AND DELETE OFFLINE DELETED PROFILES
                    ArrayList<Profile> offlineDeletedProfiles = dbHelper.getAllProfilesToRemoveFromServer();
                    Log.d("DBService", "offline deleted profiles -> "+offlineDeletedProfiles.size());
                    if(offlineDeletedProfiles.size()!=0){
                        removeProfilesFromServer(offlineDeletedProfiles);
                    }


                    //CHECK AND INSERT OFFLINE INSERTED PROFILES
                    ArrayList<Profile> offlineInsertedProfiles = dbHelper.getAllProfilesAddedWhileDecentralized();
                    Log.d("DBService", "offline inserted profiles -> "+offlineInsertedProfiles.size());
                    if(offlineInsertedProfiles.size()!=0){
                        insertProfilesToServer(offlineInsertedProfiles);
                    }


                    ////////////////---------------LOCATIONS----------------------/////////////

                    //CHECK AND DELETE OFFLINE DELETED LOCATIONS
                    ArrayList<String> offlineDeletedLocationNames = dataHolder.getRemovedLocations();
                    Log.d("DBService", "offline deleted locations -> "+offlineDeletedLocationNames.size());
                    if(offlineDeletedLocationNames.size()!=0){
                        removeLocationsFromServer(offlineDeletedLocationNames);
                    }

                    //CHECK AND INSERT OFFLINE INSERTED LOCATIONS
                    ArrayList<Location> offlineAddedLocations = checkOfflineAddedLocations();
                    Log.d("DBService", "offline added locations -> "+offlineAddedLocations.size());
                    if(offlineAddedLocations.size()!=0){
                        //SEND LOCATIONS TO SERVER
                        sendLocationsToServer(offlineAddedLocations);
                    }

                    //UPDATE NEAR LOCATIONS
                    updateNearLocations();

                    boolean isUpdated = checkDBEqualToServerDB();

                    if(!isUpdated){
                        Log.d("DBService", "Localmessages DB differs from server's, clearing local DB");

                        dbHelper.deleteAllLocations();
                        getAndInsertAllLocations();
                    }

                    updateMessages();
                }

                handler.postDelayed(runnable, 5000);
            }
        };

        handler.postDelayed(runnable, 2000);

    }


    private void insertProfilesToServer(ArrayList<Profile> offlineInsertedProfiles) {
        for(Profile p : offlineInsertedProfiles){

            Log.d("DBService", "inserting to server profile key -> "+p.getKey());

            serverActions.insertProfile(p, new OnResponseListener<OperationStatus>() {
                @Override
                public void onHTTPResponse(OperationStatus response) {
                    if(response.isERROR()){
                        //FIXME -> deu asneira
                    }
                }
            });
            try {
                dbHelper.updateProfileInsertedToServer(p.getKey());
            } catch (ProfileNotFoundException e) {
                e.printStackTrace();
            } catch (MultipleRowsAfectedException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeProfilesFromServer(ArrayList<Profile> offlineDeletedProfiles) {
        for(Profile p : offlineDeletedProfiles){

            Log.d("DBService", "removing from server profile key -> "+p.getKey());

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


    private void getAndInsertAllProfileKeys() {
        serverActions.getProfileKeys(new OnResponseListener<List<Profile>>() {

            @Override
            public void onHTTPResponse(List<Profile> response) {
                HashMap<String,String> profiles = new HashMap<String, String>();
                for(Profile p : response){
                    profiles.put(p.getKey(),p.getValue());
                }
                Log.d("DBService", "Adding old profile keys -> "+profiles.size());

                dbHelper.insertAllProfilesFromServer(profiles);
            }
        });
    }

    private void removeLocationsFromServer(ArrayList<String> offlineDeletedLocationNames) {
        for (String name : offlineDeletedLocationNames){
            Log.d("DBService", "Removing offline deleted location from server -> "+name);

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

                    dbHelper.deleteAllMessages();
                    dbHelper.insertAllMessages(response);

                    for(Message m : response){
                        Log.d("DBService", "added nearby ");
                    }

                }
            });
        }
    }

    private void getAndInsertAllLocations() {
        List<Location> locations = null;
        serverActions.getAllLocations(new OnResponseListener<List<Location>>() {
            @Override
            public void onHTTPResponse(List<Location> response) {
                for(Location l : response){
                    Log.d("DBService", "Re-Filling DB with location -> "+l.getName());
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

        Log.d("DBService", "CheckDBEquals local -> " + localHash + " server -> "+latestServerHash);

        if(latestServerHash == null || localHash == null){
            return false;
        }

        return localHash.contains(latestServerHash);

    }

    private void sendLocationsToServer(ArrayList<Location> offlineLocations) {

        for(Location l: offlineLocations){

            Log.d("DBService", "Sending offline added locations to server ->"+l.getName());

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
                dataHolder.getSsidAddresses(),dataHolder.getBleAddresses());
        Gson gson = new Gson();
        Log.d(TAG,"DB service query "+gson.toJson(query));
        serverActions.getNearLocations(query, new OnResponseListener<List<Location>>() {
            @Override
            public void onHTTPResponse(List<Location> response) {
                dataHolder.setNearLocations(response);

                for(Location l : response){
                    Log.d("DBService", "updated near location with name ->"+l.getName());
                }

            }
        });
    }

    @Override
    public void onHTTPResponse(String response) {
        latestServerHash = response;
    }
}
