package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.Network;

import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by trosado on 4/19/17.
 */

public class GsonRequest<T> extends Request {
    private final Gson gson = new Gson();
    private final Class<T> obj;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;

    public GsonRequest(int method, String url, Response.ErrorListener listener, Class<T> obj, Map<String, String> headers, Response.Listener<T> listener1) {
        super(method, url, listener);
        this.obj = obj;
        this.headers = headers;
        this.listener = listener1;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, obj),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }

    }

    @Override
    protected void deliverResponse(Object response) {
        listener.onResponse((T) response);
    }

}
