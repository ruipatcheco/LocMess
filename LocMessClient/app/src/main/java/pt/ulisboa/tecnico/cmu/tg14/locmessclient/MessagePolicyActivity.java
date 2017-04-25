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
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MessagePolicyActivity extends AppCompatActivity {

    // === PREV ACTIVITY ===
    private String mMessageContent;
    private String mStartTime;
    private String mEndTime;
    private String mID;
    // ======================

    private EditText mKey;
    private EditText mValue;
    private Switch mSwitch;
    private Button mAdd;
    private ListView mWhite;
    private ListView mBlack;
    private Button mFinish;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_policy);

        getExtrasIntent(getIntent());

        activity = this;

        mKey = (EditText) findViewById(R.id.message_profile_key);
        mValue = (EditText) findViewById(R.id.message_profile_value);
        mSwitch = (Switch) findViewById(R.id.message_profile_toggle);
        mAdd = (Button) findViewById(R.id.message_profile_button_add);
        mWhite = (ListView) findViewById(R.id.message_profile_white_list);
        mBlack = (ListView) findViewById(R.id.message_profile_black_list);
        mFinish = (Button) findViewById(R.id.message_profile_button_finish);

        final List<String> whiteList = new ArrayList<>();
        final List<String> blackList = new ArrayList<>();

        final ArrayAdapter<String> adapterWhite = new ArrayAdapter<String>(activity,android.R.layout.simple_dropdown_item_1line,whiteList);
        final ArrayAdapter<String> adapterBlack = new ArrayAdapter<String>(activity,android.R.layout.simple_dropdown_item_1line,blackList);

        mBlack.setAdapter(adapterBlack);
        mWhite.setAdapter(adapterWhite);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","Click");

                hideKeyboard();

                if (!isValidInput()) {
                    return;
                }

                String message = mKey.getText().toString() + " -> " + mValue.getText().toString();

                if(mSwitch.isChecked()){
                    blackList.add(message);
                    adapterBlack.notifyDataSetChanged();
                }else {
                    whiteList.add(message);
                    adapterWhite.notifyDataSetChanged();
                }

                Log.d("TAG", message);

                mKey.setText("");
                mValue.setText("");
            }
        });

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
    }

    private void hideKeyboard() {
        try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }
}
