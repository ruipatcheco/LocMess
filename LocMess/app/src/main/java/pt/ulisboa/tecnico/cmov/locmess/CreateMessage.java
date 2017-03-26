package pt.ulisboa.tecnico.cmov.locmess;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Timestamp;

public class CreateMessage extends AppCompatActivity {
    Calendar c = Calendar.getInstance();
    int sday = -1, smonth = -1, syear = -1;
    int shour = -1, sminute = -1;
    int eday = -1, emonth = -1, eyear = -1;
    int ehour = -1, eminute = -1;
    TextView display,display2;
    EditText messageContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        messageContent = (EditText) findViewById(R.id.messageContent);
        Button startDate = (Button) findViewById(R.id.startDate);
        Button startTime = (Button) findViewById(R.id.startTime);
        Button endDate = (Button) findViewById(R.id.endDate);
        Button endTime = (Button) findViewById(R.id.endTime);
        Button submit = (Button) findViewById(R.id.submit);


        display = (TextView) findViewById(R.id.displaytext);
        display2 = (TextView) findViewById(R.id.displaytext2);








        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageContent.getText().toString().matches("")){
                    Toast.makeText(CreateMessage.this, "You did not enter any content", Toast.LENGTH_SHORT).show();
                }

                if (sday == -1 || shour == -1){
                    Toast.makeText(CreateMessage.this, "You did not enter any start restrictions", Toast.LENGTH_SHORT).show();
                }

                if (eday == -1 || ehour == -1){
                    Toast.makeText(CreateMessage.this, "You did not enter any end restrictions", Toast.LENGTH_SHORT).show();
                }
            }
        });




        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(CreateMessage.this, startDateListner,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(CreateMessage.this, endDateListner,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(CreateMessage.this, startTimeListner, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(CreateMessage.this, endTimeListner, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
            }
        });
    }


    DatePickerDialog.OnDateSetListener startDateListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            sday = dayOfMonth;
            smonth = monthOfYear + 1;
            syear = year;

            display.setText("Choosen date is :" + sday + "/" + smonth + "/"
                    + syear);
        }
    };

    DatePickerDialog.OnDateSetListener endDateListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            eday = dayOfMonth;
            emonth = monthOfYear + 1;
            eyear = year;

            display.setText("Choosen date is :" + eday + "/" + emonth + "/"
                    + syear);
        }
    };

    TimePickerDialog.OnTimeSetListener startTimeListner = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            shour = hourOfDay;
            sminute = minute;

            display2.setText("Choosen time is:" + shour + ":" + sminute);
        }
    };

    TimePickerDialog.OnTimeSetListener endTimeListner = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            ehour = hourOfDay;
            eminute = minute;

            display2.setText("Choosen time is:" + ehour + ":" + eminute);
        }
    };


    private void sendMessage(){

        Timestamp startDate = Timestamp.valueOf(String.format("%04d-%02d-%02d %02d:%02d:00",
                syear, smonth, sday, shour, sminute));
        Long startDateLong = startDate.getTime();

        Timestamp endDate = Timestamp.valueOf(String.format("%04d-%02d-%02d %02d:%02d:00",
                eyear, emonth, eday, ehour, eminute));
        Long endDateLong = endDate.getTime();

        Calendar c = Calendar.getInstance();

        Timestamp creationDate = Timestamp.valueOf(String.format("%04d-%02d-%02d %02d:%02d:00",
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)));
        Long creationDateLong = creationDate.getTime();

        //TODO obtain publisher name
        String publisher = "Quim DA SIRENE!!!!";

        //TODO obtain location name
        String location = "BAR DA SIRENE!!!";

        String createMessageURL = "http://194.210.220.190:8080/message/create?startTime="+startDateLong+"&endTime="+endDateLong+"&creationTime="+creationDateLong+"&content="+messageContent.getText().toString()+"&publisher="+publisher+"&location="+location;
    }
}



