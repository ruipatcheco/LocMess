package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.UUID;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Message;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;

public class MessageView extends AppCompatActivity {

    private TextView mPublisher;
    private TextView mMessageContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view);

        Intent receiveIntent = getIntent();

        mPublisher = (TextView) findViewById(R.id.message_view_publisher_name);
        mMessageContent = (TextView) findViewById(R.id.message_view_message_content);

        UUID messageID = (UUID) receiveIntent.getSerializableExtra("MessageID");

        ServicesDataHolder dataHolder = ServicesDataHolder.getInstance();
        Message message  = dataHolder.getMessageMap().get(messageID);

        mPublisher.setText(message.getPublisher());
        mMessageContent.setText(message.getContent());

    }
}
