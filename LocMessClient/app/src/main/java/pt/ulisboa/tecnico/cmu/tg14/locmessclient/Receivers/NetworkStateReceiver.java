package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;

/**
 * Created by basilio on 06-05-2017.
 */

public class NetworkStateReceiver extends BroadcastReceiver{

    public void onReceive(Context context, Intent intent) {
        //Log.d("app", "Network connectivity change");
        if (intent.getExtras() != null) {
            ServicesDataHolder sdh = ServicesDataHolder.getInstance();
            NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                Log.i("app", "Network " + ni.getTypeName() + " connected");
                sdh.setCentralizedMode(true);

            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                //Log.d("app", "There's no network connectivity");
                sdh.setCentralizedMode(false);
            }
        }
    }
}
