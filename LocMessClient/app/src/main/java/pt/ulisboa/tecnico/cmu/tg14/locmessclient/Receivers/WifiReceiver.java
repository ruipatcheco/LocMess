package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnLocationReceivedListener;

/**
 * Created by trosado on 16/04/17.
 */

public class WifiReceiver extends BroadcastReceiver {
    OnLocationReceivedListener mActivity;

    public WifiReceiver(OnLocationReceivedListener mActivity) {
        this.mActivity = mActivity;
    }

    @Override

    public void onReceive(Context context, Intent intent) {

    }
}
