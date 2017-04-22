package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by tiago on 22/04/2017.
 */
public interface JsonArrayFromJsonObjectInterface extends Comparable<Request<JSONArray>> {
    Map<String, String> getHeaders() throws AuthFailureError;

    byte[] getBody() throws AuthFailureError;

    void deliverResponse(JSONArray response);
}
