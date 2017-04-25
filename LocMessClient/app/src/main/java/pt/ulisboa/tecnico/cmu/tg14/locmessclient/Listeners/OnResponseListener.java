package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners;

import com.google.gson.JsonArray;

/**
 * Created by trosado on 4/25/17.
 */

public interface OnResponseListener<T> {
    public void onHTTPResponse(T response);
}
