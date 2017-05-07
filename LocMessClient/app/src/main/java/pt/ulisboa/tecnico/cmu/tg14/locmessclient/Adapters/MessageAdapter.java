package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Message;

/**
 * Created by trosado on 5/6/17.
 */

public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

}
