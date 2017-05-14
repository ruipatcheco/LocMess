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
    private long start;
    private long end;
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
                i.putExtra("mStartTime",""+start);
                i.putExtra("mEndTime",""+end);

                //Log.d(TAG, "onClick: "+mStartTime);
                startActivity(i);
                finish();
            }
        });
    }

    private boolean isValidInput() {

        if (mMessageContent.length() <= 0) {
            Toast.makeText(activity, "You need to write a message", Toast.LENGTH_LONG).show();
            return false;
        }
        if (mStartTime.length() <= 0) {
            Toast.makeText(activity, "You need to set the Start Time", Toast.LENGTH_LONG).show();
            return false;
        }
        if (mEndTime.length() <= 0) {
            start = convertTime(mStartTime.getText().toString());
            end = 0;
            return true;
        } else {
            start = convertTime(mStartTime.getText().toString());
            end = convertTime(mEndTime.getText().toString());
        }
        if(checkTime(start, end)) {
            return true;
        }
        Toast.makeText(activity, "You need to set the Start Time before the End Time", Toast.LENGTH_LONG).show();
        return false;
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
                calendar.get(Calendar.MONTH) + 1,   // TODO : FIX BUG OF GETTING MONTH 0
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show(getFragmentManager(),"DateTimePickerDialog");
    }

    public boolean checkTime(long startTime, long endTime) {
        return startTime <= endTime;
    }

    private void fixTime(TextView time) {
        String[] parts = time.getText().toString().split("-");
        if (parts.length == 3) {
            time.setText(parts[0] + "-" + String.valueOf(Integer.parseInt(parts[1]) + 1) + "-" + parts[2]);
        } else {
            time.setText("");
        }
    }

    public long convertTime(String time) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = (Date) formatter.parse(time);

            //Log.d("time -> ", String.valueOf(date.getTime()));

            return date.getTime();


        } catch (ParseException p1) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = (Date) formatter.parse(time);
                return date.getTime();
            } catch (ParseException p2) {
                return 0;
            }
        }
    }
}
