package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Message;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.MessageNotFoundException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderDbHelper;

public class MessageView extends AppCompatActivity {

    private TextView mPublisher;
    private TextView mMessageContent;
    private String messageID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view);

        Intent receiveIntent = getIntent();

        mPublisher = (TextView) findViewById(R.id.message_view_publisher_name);
        mMessageContent = (TextView) findViewById(R.id.message_view_message_content);

        messageID = receiveIntent.getExtras().getString("MessageID");

        //Log.d("MessageView: ", "message id-> " + messageID);

        new getMessageFromDBTask().execute();



    }

    private class getMessageFromDBTask extends AsyncTask<Void, Void, Void> {

        private Message auxMessage;



        public getMessageFromDBTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mPublisher.setText(auxMessage.getPublisher());
            mMessageContent.setText(auxMessage.getContent());

        }

        @Override
        protected Void doInBackground(Void... params) {
            FeedReaderDbHelper dbHelper = FeedReaderDbHelper.getInstance(getApplicationContext());
            Message dbMessage = null;
            try {
                dbMessage = dbHelper.getMessage(messageID.toString());
            } catch (MessageNotFoundException e) {
                e.printStackTrace();
            }

            auxMessage = dbMessage;
            return null;
        }
    }
}
