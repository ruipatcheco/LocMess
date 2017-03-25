package pt.ulisboa.tecnico.cmov.locmess.Listeners;

import android.app.Activity;
import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.locmess.R;

/**
 * Created by trosado on 24/03/17.
 */

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    Context mContext;
    OnLocationTypeListener mActivity;

    public CustomOnItemSelectedListener(Context context){
        mContext = context.getApplicationContext();
        mActivity = (OnLocationTypeListener) context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String str = "";

        final String option = parent.getItemAtPosition(position).toString();


        if(option.equals(mContext.getString(R.string.location_type_GPS)))
            mActivity.onGPSSelected();
        else if(option.equals(mContext.getString(R.string.location_type_WIFI)))
            mActivity.onWifiSelected();
        else if(option.equals(mContext.getString(R.string.location_type_BLE)))
            mActivity.onBleSelected();



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
