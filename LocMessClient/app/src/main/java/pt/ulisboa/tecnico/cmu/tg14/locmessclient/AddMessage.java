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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import net.danlew.android.joda.JodaTimeAndroid;

import org.w3c.dom.Text;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.DateTimeListener;

public class AddMessage extends AppCompatActivity {

    private final String TAG = "AddMessage";
    private EditText mMessageContent;
    private TextView mStartTime;
    private TextView mEndTime;
    private Calendar mCalendar;
    private Button mNext;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);
        setTitle(R.string.title_activity_add_message);
        activity = this;

        mCalendar = Calendar.getInstance();

        mMessageContent = (EditText) findViewById(R.id.add_message_content);
        mStartTime = (TextView) findViewById(R.id.add_message_start_time);
        mEndTime = (TextView) findViewById(R.id.add_message_end_time);
        mNext = (Button) findViewById(R.id.button_next);

        mStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasChange) {
                timeListenerAux(mStartTime);
            }
        });

        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListenerAux(mStartTime);
            }
        });

        mEndTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasChange) {
                timeListenerAux(mEndTime);
            }
        });

        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListenerAux(mEndTime);
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                hideKeyboard();

                if (!isValidInput()) {
                    return;
                }
                Intent i = new Intent(activity,MessageLocationActivity.class);
                i.putExtra("mMessageContent",mMessageContent.getText().toString());
                i.putExtra("mStartTime",mStartTime.getText().toString());
                i.putExtra("mEndTime",mEndTime.getText().toString());

                //TODO add message arguments to activity or save to disk
                startActivity(i);
                finish();
            }
        });
    }

    private boolean isValidInput() {

        if (mMessageContent.length() <= 0) {
            Toast.makeText(activity, "You need to write a message", Toast.LENGTH_LONG).show();
            return false;
        } else if (mStartTime.length() <= 0) {
            Toast.makeText(activity, "You need to set the Start Time", Toast.LENGTH_LONG).show();
            return false;
        } else if (mStartTime.length() > 0) {

            Log.d(TAG, mStartTime.getText().toString());
            //Calendar cStart = getCalendar(mStartTime.getText().toString());
            //Calendar cEnd = getCalendar(mEndTime.getText().toString());

            //if (cEnd.before(cStart)) {
            //    Toast.makeText(activity, "End time must be after than Start time", Toast.LENGTH_LONG).show();
            //    return false;
            //}
        }
        return true;
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

    private void hideKeyboard() {
        try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    private void timeListenerAux(TextView time) {
        DateTimeListener dateTimeListener = new DateTimeListener(getFragmentManager(), time);
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(dateTimeListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show(getFragmentManager(),"DateTimePickerDialog");
    }

    public boolean checkTime(String startTime, String endTime) {
        return convertTime(startTime) >= convertTime(endTime);
    }

    public long convertTime(String time) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date date = (Date)formatter.parse(time);
            return date.getTime();
        } catch (ParseException p) {
            return 0;
        }
    }
}
