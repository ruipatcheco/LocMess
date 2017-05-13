package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.Network;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by trosado on 5/12/17.
 */

public class JsonArrayAuthenticatedRequest extends JsonArrayRequest {
    private String username;
    private String password;


    public JsonArrayAuthenticatedRequest( String url, String username,String password, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        this.username = username;
        this.password = password;
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        String credentials = username+":"+password;
        String auth = "Basic "
                + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", auth);
        return headers;
    }
}
