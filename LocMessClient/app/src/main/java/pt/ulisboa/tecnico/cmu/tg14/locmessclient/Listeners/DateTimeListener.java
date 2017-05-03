package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners;

import android.app.FragmentManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.w3c.dom.Text;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

/**
 * Created by trosado on 07/04/17.
 */

public class DateTimeListener implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    private TextView mEditText;
    private FragmentManager mfragmentManager;
    public DateTimeListener(FragmentManager fragmentManager,TextView mEditText) {
        this.mEditText = mEditText;
        this.mfragmentManager=fragmentManager;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        String str = year+"-"+monthOfYear+"-"+dayOfMonth;
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(DateTimeListener.this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),true);
        timePickerDialog.show(mfragmentManager,"TimePickerDialog");
        mEditText.setText(str);
        Log.d(TAG,str);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String str = hourOfDay+":"+minute;
        String date = mEditText.getText().toString();
        mEditText.setText(date+" "+str);
        Log.d(TAG,str);

    }
}
