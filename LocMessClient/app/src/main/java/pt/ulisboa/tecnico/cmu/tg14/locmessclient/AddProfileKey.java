package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddProfileKey extends AppCompatActivity {

    private EditText mKey;
    private EditText mValue;
    private Button mAdd;
    private Button mFinish;

    private ListView mProfileList;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_key);
        setTitle(R.string.title_activity_add_message);
        activity = this;

        mKey = (EditText) findViewById(R.id.add_profile_key);
        mValue = (EditText) findViewById(R.id.add_profile_value);
        mAdd = (Button) findViewById(R.id.add_profile_button_add);
        mFinish = (Button) findViewById(R.id.add_profile_button_finish);
        mProfileList = (ListView) findViewById(R.id.add_profile_list);

        final List<String> profileList = new ArrayList<>();

        final ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(activity,android.R.layout.simple_dropdown_item_1line, profileList);

        mProfileList.setAdapter(adapterLocation);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","Click");

                hideKeyboard();

                if (!isValidInput()) {
                    return;
                }

                String message = mKey.getText().toString() + " -> " + mValue.getText().toString();
                profileList.add(message);
                adapterLocation.notifyDataSetChanged();

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
}
