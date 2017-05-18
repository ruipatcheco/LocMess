package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.Network;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.HashResult;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.LocationQuery;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.MessageServer;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.OperationStatus;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Message;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Profile;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnResponseListener;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.Network.Ssl.HttpsTrustManager;

import static android.content.ContentValues.TAG;

/**
 * Created by trosado on 31/03/17.
 */
public class ServerActions {
    private final static  String addr = "193.136.167.169";
    private final static String port = "8443";
    private final static String endpoint = "https://"+addr+":"+port+"/api";
    private static RequestQueue queue;
    private Context mContext;

    private static String sessionID = "";
    private static String sessionIdURL = "";
    private static ServicesDataHolder dataHolder;


    public ServerActions(Context context) {
        queue = Volley.newRequestQueue(context);
        mContext = context;
        dataHolder = ServicesDataHolder.getInstance();
    }

    private void makeAuthenticatedRequest(int method, String url, JSONObject jsonObject, final OnResponseListener listener){
        new HttpsTrustManager(mContext).allowServerCertificate();
        JsonObjectAuthenticatedRequest request = new JsonObjectAuthenticatedRequest(method,url,dataHolder.getUsername(),dataHolder.getPassword(),jsonObject,new Response.Listener<JSONObject>() {            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                //Log.d(TAG, "onResponse: " + response.toString());
                OperationStatus statusResponse = gson.fromJson(response.toString(), OperationStatus.class);
                listener.onHTTPResponse(statusResponse);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ",error);
            }
        });

        queue.add(request);
    }



    public void insertProfile( Profile p,final OnResponseListener listener){

        String url = generateURL("/profile/create");
        try{
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(gson.toJson(p));
            //Log.d(TAG, "createProfile:"+jsonObject.toString());
            makeAuthenticatedRequest(Request.Method.PUT,url,jsonObject,listener);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }



    public void removeProfile( Profile p,OnResponseListener listener){
        String url = generateURL("/profile/delete");
        try{
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(gson.toJson(p));
            //Log.d(TAG, "removeProfile:"+jsonObject.toString());
            makeAuthenticatedRequest(Request.Method.PUT,url,jsonObject,listener);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public List<Profile> getMyProfileKeys(final OnResponseListener listener) {
        String url = generateURL("/profile/myList");

        final List<Profile> profiles = new ArrayList<>();
        JsonArrayAuthenticatedRequest stringRequest = new JsonArrayAuthenticatedRequest(url,dataHolder.getUsername(),dataHolder.getPassword(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0;i<response.length();i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Gson gson = new Gson();
                        Profile p = gson.fromJson(obj.toString(),Profile.class);
                        profiles.add(p);
                        //Log.d("ServerActions: ", "received my own profile ->"+ p.getKey());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //Log.d("ServerActions: ", "received my own profile size ->"+ profiles.size());

                listener.onHTTPResponse(profiles);

                //Log.d(TAG, "onResponse: "+response);
            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                System.out.print("error: " + error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        return profiles;
    }

    public List<Profile> getProfileKeys(final OnResponseListener listener) {
        String url = generateURL("/profile/listAll");

        final List<Profile> profiles = new ArrayList<>();
        new HttpsTrustManager(mContext).allowServerCertificate();
        JsonArrayAuthenticatedRequest stringRequest = new JsonArrayAuthenticatedRequest(url,dataHolder.getUsername(),dataHolder.getPassword(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0;i<response.length();i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Gson gson = new Gson();
                        Profile p = gson.fromJson(obj.toString(),Profile.class);
                        profiles.add(p);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                listener.onHTTPResponse(profiles);

                //Log.d(TAG, "onResponse: "+response);
            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                System.out.print("error: " + error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        return profiles;
    }

    private void makeSimpleRequest(int method, String url, JSONObject jsonObject, final OnResponseListener listener){
        new HttpsTrustManager(mContext).allowServerCertificate();
        JsonObjectRequest request = new JsonObjectRequest(method,url,jsonObject,new Response.Listener<JSONObject>() {            @Override
        public void onResponse(JSONObject response) {
            Gson gson = new Gson();
            //Log.d(TAG, "onResponse: " + response.toString());
            OperationStatus statusResponse = gson.fromJson(response.toString(), OperationStatus.class);
            listener.onHTTPResponse(statusResponse);
        }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ",error);
            }
        });

        queue.add(request);

    }

    public void createUser(String username,String password,final OnResponseListener listener){
        String url = generateURL("/user/create");

        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("username",username);
            jsonObject.accumulate("password",password);

            makeSimpleRequest(Request.Method.PUT,url,jsonObject,listener);

        }catch (JSONException e){
            e.printStackTrace();
        }


    }

    public void updatePassword(String username,String password,final OnResponseListener listener) throws Exception {
        String url = generateURL("/user/updatePassword");

        Log.e(TAG, "updatePassword: Still need update" );

        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("username",username);
            jsonObject.accumulate("password",password);

            makeAuthenticatedRequest(Request.Method.PUT,url,jsonObject,listener);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void createLocation(Location location,final OnResponseListener listener){
        String url = generateURL("/location/create");

        try{
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(gson.toJson(location));

            //Log.d(TAG, "createLocation:"+jsonObject.toString());

            makeAuthenticatedRequest(Request.Method.PUT,url,jsonObject,listener);


        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void getMyMessages(final OnResponseListener listener){
        String url = generateURL("/message/myMessages");

        final List<Message> messages = new ArrayList<>();
        final Gson gson = new Gson();

        JsonArrayAuthenticatedRequest request = new JsonArrayAuthenticatedRequest(url, dataHolder.getUsername(), dataHolder.getPassword()
                , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0;i<response.length();i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        MessageServer ms = gson.fromJson(obj.toString(),MessageServer.class);
                            Message m = new Message(ms.getId(),ms.getCreationTime(),ms.getStartTime(),ms.getEndTime(),ms.getContent(),ms.getPublisher(),ms.getLocation(),true,false,ms.getWhiteList(),ms.getBlackList());
                        messages.add(m);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listener.onHTTPResponse(messages);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ", error);
            }
        });

        queue.add(request);
    }

    public static List<Message> getMessagesFromLocation(Location location, final OnResponseListener listener){
        String url = generateURL("/message/getMessagesByLocation");

        final List<Message> messages = new ArrayList<>();
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(gson.toJson(location));
            HttpsTrustManager.allowServerCertificate();
            JsonArrayFromJsonObjectAuthenticatedRequest request = new JsonArrayFromJsonObjectAuthenticatedRequest(Request.Method.POST,url,dataHolder.getUsername(),dataHolder.getPassword(),jsonObject,null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    for(int i = 0;i<response.length();i++){
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Gson gson = new Gson();
                            //Log.d(TAG, "onResponse: "+obj.toString());
                            MessageServer msg = gson.fromJson(obj.toString(),MessageServer.class);

                            Message m = new Message(msg.getId(),msg.getCreationTime(), msg.getStartTime(),msg.getEndTime(),msg.getContent(),msg.getPublisher(),msg.getLocation(),true,true,null,null);
                            messages.add(m);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    listener.onHTTPResponse(messages);


                    //Log.d(TAG, "onResponse: "+response);
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "onErrorResponse: ",error);
                }
            });



            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public List<Location> getAllLocations(final OnResponseListener listener) {
        String url = generateURL("/location/list");

        final List<Location> locations = new ArrayList<>();
        new HttpsTrustManager(mContext).allowServerCertificate();
        JsonArrayAuthenticatedRequest stringRequest = new JsonArrayAuthenticatedRequest(url,dataHolder.getUsername(),dataHolder.getPassword(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0;i<response.length();i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Gson gson = new Gson();
                        Location l = gson.fromJson(obj.toString(),Location.class);
                        locations.add(l);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                listener.onHTTPResponse(locations);

                //Log.d(TAG, "onResponse: "+response);
            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                System.out.print("error: " + error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        return locations;
    }

    public void getListLocationHash(final OnResponseListener<String> listener){
        String url = generateURL("/location/list/hash");
        new HttpsTrustManager(mContext).allowServerCertificate();
        JsonObjectAuthenticatedRequest request = new JsonObjectAuthenticatedRequest(Request.Method.GET,url,dataHolder.getUsername(),dataHolder.getPassword(),null,new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                //Log.d(TAG, "onResponse: " + response.toString());
                HashResult hashResult= gson.fromJson(response.toString(), HashResult.class);
                listener.onHTTPResponse(hashResult.getHash());
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ",error);
            }
        });


        queue.add(request);

    }


    public List<Location> getNearLocations(LocationQuery query, final OnResponseListener listener){
        String url = generateURL("/location/nearbyLocations");

        final List<Location> locations = new ArrayList<>();
        //Log.d(TAG, "request: "+query.toJSON());
        new HttpsTrustManager(mContext).allowServerCertificate();
        JsonArrayFromJsonObjectAuthenticatedRequest request = new JsonArrayFromJsonObjectAuthenticatedRequest(Request.Method.POST,url,dataHolder.getUsername(),dataHolder.getPassword(),query.toJSON(),null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0;i<response.length();i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Gson gson = new Gson();
                        Location l = gson.fromJson(obj.toString(),Location.class);
                        locations.add(l);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                listener.onHTTPResponse(locations);


                //Log.d(TAG, "onResponse: "+response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ",error);
            }
        });

        queue.add(request);

        return locations;
    }


    public void createMessage(MessageServer message,final OnResponseListener listener) {
        String url = generateURL("/message/create");
        try {
            Gson  gson = new Gson();
            JSONObject jsonObject = new JSONObject(gson.toJson(message));
            Log.d(TAG, "createMessage: "+jsonObject);
            makeAuthenticatedRequest(Request.Method.PUT,url,jsonObject,listener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void removeMessage( MessageServer m,OnResponseListener listener){
        String url = generateURL("/message/delete");
        try {
            url += "?id="+URLEncoder.encode(m.getId().toString(),"UTF-8");
            //Log.d(TAG, "removeMessage:"+m.getId());
            makeAuthenticatedRequest(Request.Method.PUT,url,null,listener);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    public void removeLocation(String name,OnResponseListener listener){
        try {
            String url = generateURL("/location/delete");
            url+="&name="+URLEncoder.encode(name,"UTF-8");
            makeAuthenticatedRequest(Request.Method.DELETE, url, null, listener);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void login(final String username, final String password, final OnResponseListener<Boolean> listener){
        String url = endpoint+"/user/login";
        final boolean[] loggedin = {false};
        new HttpsTrustManager(mContext).allowServerCertificate();
        StringRequest strReq = new StringRequest(Request.Method.GET,
               url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"String session id: "+response.toString());
                        try {
                            sessionID = URLEncoder.encode(response,"UTF-8");
                            sessionIdURL = "?sessionID="+sessionID;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        listener.onHTTPResponse(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error.getMessage());
                        listener.onHTTPResponse(false);
                    }
                }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
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
            }};

        queue.add(strReq);
    }

    public void logout() {
        String url = generateURL("/user/logout");
        final boolean[] loggedin = {false};
        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"String session id: "+response.toString());
                        try {
                            sessionID = URLEncoder.encode(response,"UTF-8");
                            sessionIdURL = "?sessionID="+sessionID;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error.getMessage());
                    }
                }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
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
            }};

        queue.add(strReq);
    }

    private static String generateURL(String path){
        return endpoint+path+sessionIdURL;
    }

}

