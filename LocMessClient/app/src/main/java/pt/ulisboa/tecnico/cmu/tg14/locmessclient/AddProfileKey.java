package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Profile;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderDbHelper;

public class AddProfileKey extends AppCompatActivity {

    private EditText mKey;
    private EditText mValue;
    private Button mAdd;
    private Button mFinish;

    private ListView mProfileList;
    private Activity activity;

    private HashMap<String,String> keyValueMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_key);
        setTitle(R.string.title_activity_add_key_pair);
        activity = this;

        mKey = (EditText) findViewById(R.id.add_profile_key);
        mValue = (EditText) findViewById(R.id.add_profile_value);
        mAdd = (Button) findViewById(R.id.add_profile_button_add);
        mFinish = (Button) findViewById(R.id.add_profile_button_finish);
        mProfileList = (ListView) findViewById(R.id.add_profile_list);

        final List<String> profileList = new ArrayList<>();
        keyValueMap = new HashMap<>();

        final ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(activity,android.R.layout.simple_dropdown_item_1line, profileList);

        mProfileList.setAdapter(adapterLocation);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("TAG","Click");

                hideKeyboard();

                if (!isValidInput()) {
                    return;
                }

                String key = mKey.getText().toString().toLowerCase();
                String val = mValue.getText().toString().toLowerCase();


                keyValueMap.put(key,val);

                String message = key + " -> " + val;
                profileList.add(message);
                adapterLocation.notifyDataSetChanged();

                mKey.setText("");
                mValue.setText("");

                //Log.d("AddProfile :", "map size-> " + keyValueMap.size());

                addNotification();
            }
        });

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!keyValueMap.isEmpty()){
                    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(activity);
                    dbHelper.insertAllProfiles(keyValueMap);


                    HashMap<String,String> map = dbHelper.getAllProfiles();
                    for (String k: map.keySet()){
                        //Log.d("AddProfile :", "All keysets key -> " + k + " value -> " +map.get(k));
                    }
                }
                finish();
            }
        });
    }

    private boolean isValidInput() {
        if (mKey.length() <= 0) {
            Toast.makeText(activity, "You need to write a key", Toast.LENGTH_LONG).show();
        } else if (mValue.length() <= 0) {
            Toast.makeText(activity, "You need to write a ", Toast.LENGTH_LONG).show();
        }
        return mKey.length() > 0 && mValue.length() > 0;
    }

    private void hideKeyboard() {
        try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    private void addNotification() {
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_add)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification")
                        .setAutoCancel(true)
                        .setVibrate(new long[] { 1000, 1000 })
                        .setLights(Color.BLUE, 3000, 3000)
                        .setSound(uri);

        Intent notificationIntent = new Intent(this, AddMessage.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
