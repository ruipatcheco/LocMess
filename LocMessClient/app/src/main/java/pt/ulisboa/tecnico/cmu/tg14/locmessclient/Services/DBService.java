package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.LocationQuery;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnResponseListener;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.ServerActions;

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

        Log.d("DBUpdater","Bluetooth Started");
        serverActions = new ServerActions(this);


        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                LocationQuery query = new LocationQuery(dataHolder.getLatitude(),dataHolder.getLongitude(),
                        dataHolder.getSsidAddresses(),dataHolder.getBleAddresses());
                serverActions.getNearLocations(query, new OnResponseListener() {
                    @Override
                    public void onHTTPResponse(Object response) {
                        //TODO add data to db

                    }
                });

                List<Location>  locations = new ArrayList<>(); //FIXME get locations from db
                for(Location location: locations){
                    serverActions.getMessagesFromLocation(location, new OnResponseListener() {
                        @Override
                        public void onHTTPResponse(Object response) {
                            //TODO add data to db
                        }
                    });

                }

                serverActions.getAllLocations(new OnResponseListener() {
                    @Override
                    public void onHTTPResponse(Object response) {
                        //TODO add data to db
                    }
                });

                handler.postDelayed(runnable, 60000);
            }
        };

        handler.postDelayed(runnable, 2000);

    }
}
