package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.Network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by tiago on 22/04/2017.
 */

public class JsonArrayFromJsonObjectRequest extends Request<JSONArray> implements JsonArrayFromJsonObjectInterface {
    private final Gson gson = new Gson();
    private final JSONObject requestParam;
    private final Map<String,String> headers;
    private final Response.Listener<JSONArray> listener;

    public JsonArrayFromJsonObjectRequest(int method, String url, JSONObject requestParam, Map<String, String> headers, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.requestParam = requestParam;
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    new JSONArray(json),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }

    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf8";
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return requestParam.toString().getBytes();
    }

    @Override
    public void deliverResponse(JSONArray response) {
        listener.onResponse(response);
    }


}
