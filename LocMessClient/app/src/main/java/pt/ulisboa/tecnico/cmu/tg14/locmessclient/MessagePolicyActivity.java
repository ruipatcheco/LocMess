package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
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
import android.widget.Switch;
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
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnResponseListener;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderDbHelper;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.Model;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.MyAdapter;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.ServerActions;

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
    List<String> whiteList;
    List<String> blackList;
    List<Model> list = new ArrayList<Model>();

    private RadioGroup radioCcToGroup;
    private RadioButton radioTypeButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_policy);

        getExtrasIntent(getIntent());
        adapter = new MyAdapter(this, getModel());


        activity = this;

        mKey = (EditText) findViewById(R.id.message_profile_key);
        mValue = (EditText) findViewById(R.id.message_profile_value);
        mAdd = (Button) findViewById(R.id.message_profile_button_add);
        mFinish = (Button) findViewById(R.id.message_profile_button_finish);
        mListView = (ListView) findViewById(R.id.message_profiles_list);

        mListView.setAdapter(adapter);


        final List<String> whiteList = new ArrayList<>();
        final List<String> blackList = new ArrayList<>();


        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","Click");

                hideKeyboard();

                if (!isValidInput()) {
                    return;
                }

                String message = mKey.getText().toString() + " -> " + mValue.getText().toString();

                /*
                if(mSwitch.isChecked()){
                    blackList.add(message);
                    adapterBlack.notifyDataSetChanged();
                }else {
                    whiteList.add(message);
                    adapterWhite.notifyDataSetChanged();
                }
                */

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
                //FIXME tratar disto
                message.setCreationTime(Calendar.getInstance().getTime().getTime());
                Log.d(TAG, "onClick: "+mStartTime);
                message.setUUID(UUID.randomUUID());
                message.setStartTime(Long.valueOf(mStartTime));
                message.setEndTime(Long.valueOf(mEndTime));
                message.setPublisher("tiago"); //TODO to remove
                message.setLocation(mID);
                message.setCentralized(!mIsDecentralized);


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
                if (list.get(i).isCcOrIsTo()) {
                    whiteList.add(list.get(i).getName());
                } else {
                    blackList.add(list.get(i).getName());
                }
            }
        }

        Log.i("o", "WHITE");
        for (String s : whiteList) {
            Log.i("o", s);
        }

        Log.i("o", "BLACK");
        for (String s : blackList) {
            Log.i("o", s);
        }
    }

    private List<Model> getModel() {
        list.add(new Model("Linux"));
        list.add(new Model("Windows7"));
        list.add(new Model("Suse"));
        list.add(new Model("Eclipse"));
        list.add(new Model("Ubuntu"));
        list.add(new Model("Solaris"));
        list.add(new Model("Android"));
        list.add(new Model("iPhone"));
        list.add(new Model("Java"));
        list.add(new Model(".Net"));
        list.add(new Model("PHP"));
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
