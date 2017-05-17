package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.Network;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tiago on 22/04/2017.
 */

public class JsonArrayFromJsonObjectAuthenticatedRequest extends JsonArrayFromJsonObjectRequest{

    private String username;
    private String password;

    public JsonArrayFromJsonObjectAuthenticatedRequest(int method, String url,String username, String password, JSONObject requestParam, Map<String, String> headers, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, requestParam, headers, listener, errorListener);
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
