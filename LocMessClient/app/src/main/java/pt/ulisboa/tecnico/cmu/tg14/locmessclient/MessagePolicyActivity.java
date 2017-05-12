package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.OperationStatus;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Message;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Profile;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnResponseListener;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderDbHelper;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.Model;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Adapters.ProfileChoiceAdapter;

public class MessagePolicyActivity extends AppCompatActivity implements OnResponseListener<OperationStatus>{

    // === PREV ACTIVITY ===
    private String mMessageContent;
    private String mStartTime;
    private String mEndTime;
    private String mID;
    private Boolean mIsDecentralized;
    // ======================

    private EditText mKey;
    private EditText mValue;
    private Button mAdd;
    private Button mFinish;
    Activity activity;


    ListView mListView;
    ArrayAdapter<Model> adapter;
    List<Profile> whiteList = new ArrayList<>();
    List<Profile> blackList = new ArrayList<>();
    List<Model> list = new ArrayList<Model>();

    private RadioGroup radioCcToGroup;
    private RadioButton radioTypeButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_policy);

        getExtrasIntent(getIntent());

        activity = this;

        mKey = (EditText) findViewById(R.id.message_profile_key);
        mValue = (EditText) findViewById(R.id.message_profile_value);
        mAdd = (Button) findViewById(R.id.message_profile_button_add);
        mFinish = (Button) findViewById(R.id.message_profile_button_finish);
        mListView = (ListView) findViewById(R.id.message_profiles_list);


        adapter = new ProfileChoiceAdapter(this, getModel());
        mListView.setAdapter(adapter);


        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","Click");

                hideKeyboard();

                if (!isValidInput()) {
                    return;
                }

                String message = mKey.getText().toString() + " -> " + mValue.getText().toString();

                Profile profile = new Profile(mKey.getText().toString(), mValue.getText().toString(), "tiago");

                FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(activity);
                dbHelper.insertProfile(profile);

                list.add(0, new Model(profile));

                adapter.notifyDataSetChanged();

                Log.d("TAG", message);

                mKey.setText("");
                mValue.setText("");
            }
        });

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fillBlackAndWhiteLists();


                Message message = new Message();
                message.setContent(mMessageContent);
                Calendar c = Calendar.getInstance();
                String TAG ="";
                message.setCreationTime(Calendar.getInstance().getTime().getTime());
                Log.d(TAG, "onClick: "+mStartTime);
                message.setUUID(UUID.randomUUID());
                message.setStartTime(Long.valueOf(mStartTime));
                message.setEndTime(Long.valueOf(mEndTime));
                message.setPublisher("tiago"); //TODO to remove
                message.setLocation(mID);
                message.setCentralized(!mIsDecentralized);
                message.setNearby(false);
                message.setWhiteList(whiteList);
                message.setblackList(blackList);

                FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(activity);
                dbHelper.insertMessage(message);


                finish();
            }
        });
    }

    private void fillBlackAndWhiteLists() {
        whiteList.clear();
        blackList.clear();

        for (int i = 0; i < list.size(); i++) {
            if (adapter.getItem(i).isSelected()) {
                if (list.get(i).isWhite()) {
                    whiteList.add(list.get(i).getProfile());
                } else {
                    blackList.add(list.get(i).getProfile());
                }
            }
        }

        Log.i("IMPRIMIR", "IMPRIMIR WHITE");
        for (Profile s : whiteList) {
            Log.i("IMPRIMIR", s.getKey());
        }

        Log.i("IMPRIMIR", "IMPRIMIR BLACK");
        for (Profile s : blackList) {
            Log.i("IMPRIMIR", s.getKey());
        }
    }

    private List<Model> getModel() {
        list.add(new Model(new Profile("Linux", "value", "tiago")));
        list.add(new Model(new Profile("Windows7", "value", "tiago")));
        list.add(new Model(new Profile("Suse", "value", "tiago")));
        list.add(new Model(new Profile("Eclipse", "value", "tiago")));
        list.add(new Model(new Profile("Ubuntu", "value", "tiago")));
        list.add(new Model(new Profile("Solaris", "value", "tiago")));
        list.add(new Model(new Profile("Android", "value", "tiago")));
        list.add(new Model(new Profile("iPhone", "value", "tiago")));
        list.add(new Model(new Profile("Java", "value", "tiago")));
        list.add(new Model(new Profile(".Net", "value", "tiago")));
        list.add(new Model(new Profile("PHP", "value", "tiago")));
        return list;
    }


    private boolean isValidInput() {
        boolean b = mKey.getText().toString().length() > 0 && mValue.getText().toString().length() > 0;
        if (!b) {
            Toast.makeText(activity, "Invalid Key Value Pair", Toast.LENGTH_LONG).show();
        }
        return b;
    }

    private void getExtrasIntent(Intent i) {
        mMessageContent = i.getExtras().getString("mMessageContent");
        mStartTime = i.getExtras().getString("mStartTime");
        mEndTime = i.getExtras().getString("mEndTime");
        mID = i.getExtras().getString("mID");
        mIsDecentralized = i.getExtras().getBoolean("mIsDecentralized");
    }

    private void hideKeyboard() {
        try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    private Calendar getCalendar(String time) {
        String pattern = "(\\d{4})-(\\d{2}|\\d{1})-(\\d{2}|\\d{1}) (\\d{2}|\\d{1}):(\\d{2}|\\d{1})";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(time);

        int year = Integer.parseInt(m.group(1));
        int month = Integer.parseInt(m.group(2));
        int date = Integer.parseInt(m.group(3));
        int hour = Integer.parseInt(m.group(4));
        int minute = Integer.parseInt(m.group(5));

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hour, minute);

        return calendar;
    }

    @Override
    public void onHTTPResponse(OperationStatus response) {
 //TODO
    }
}
