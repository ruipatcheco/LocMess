package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by trosado on 5/12/17.
 */

public class JsonObjectAuthenticatedRequest extends JsonObjectRequest {
    String username;
    String password;

    public JsonObjectAuthenticatedRequest(int method, String url, String username, String password,JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
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
