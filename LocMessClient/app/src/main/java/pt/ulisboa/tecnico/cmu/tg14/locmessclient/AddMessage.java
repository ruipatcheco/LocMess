package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.DateTimeListener;

public class AddMessage extends AppCompatActivity {

    private final String TAG = "AddMessage";
    private EditText mMessageContent;
    private EditText mStartTime;
    private EditText mEndTime;
    private Calendar mCalendar;
    private Button mNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);
        setTitle(R.string.title_activity_add_message);
        final Activity activity = this;

        mCalendar = Calendar.getInstance();

        mMessageContent = (EditText) findViewById(R.id.add_message_content);
        mStartTime = (EditText) findViewById(R.id.add_message_start_time);
        mEndTime = (EditText) findViewById(R.id.add_message_end_time);
        mNext = (Button) findViewById(R.id.button_next);

        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateTimeListener dateTimeListener = new DateTimeListener(getFragmentManager(),mStartTime);
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(dateTimeListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show(getFragmentManager(),"DateTimePickerDialog");

            }
        });

        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimeListener dateTimeListener = new DateTimeListener(getFragmentManager(),mEndTime);
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(dateTimeListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show(getFragmentManager(),"DateTimePickerDialog");

            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(activity,MessageLocationActivity.class);
                //TODO add message arguments to activity or save to disk
                startActivity(intent);
            }
        });



    }
}
