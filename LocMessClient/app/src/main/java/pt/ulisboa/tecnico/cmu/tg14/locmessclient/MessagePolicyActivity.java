package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class MessagePolicyActivity extends AppCompatActivity {

    private EditText mKey;
    private EditText mValue;
    private Switch mSwitch;
    private Button mAdd;
    private ListView mWhite;
    private ListView mBlack;
    private Button mFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_policy);

        final Activity activity = this;

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

                String key = mKey.getText().toString();
                String value = mValue.getText().toString();

                if(mSwitch.isChecked()){
                    blackList.add(key+"="+value);
                    adapterBlack.notifyDataSetChanged();
                }else {
                    whiteList.add(key+"="+value);
                    adapterWhite.notifyDataSetChanged();
                }

                Log.d("TAG",key+"="+value);
            }
        });



    }
}
