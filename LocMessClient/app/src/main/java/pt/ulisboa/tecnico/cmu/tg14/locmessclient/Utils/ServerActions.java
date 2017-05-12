package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.HashResult;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.LocationQuery;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.OperationStatus;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Message;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Profile;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnResponseListener;

import static android.content.ContentValues.TAG;

/**
 * Created by trosado on 31/03/17.
 */
public class ServerActions {
    private final static  String addr = "193.136.167.169";
    private final static String port = "8080";
    private final static String endpoint = "http://"+addr+":"+port+"/api";
    private static RequestQueue queue;
    //FIXME to remove
    private final static String username = "test";
    private final static String password = "test";
    //FIXME to remove

    public ServerActions(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    private void makeAuthenticatedRequest(int method, String url, JSONObject jsonObject, final OnResponseListener listener){

        JsonObjectAuthenticatedRequest request = new JsonObjectAuthenticatedRequest(method,url,username,password,jsonObject,new Response.Listener<JSONObject>() {            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                Log.d(TAG, "onResponse: " + response.toString());
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
        String url = endpoint+"/profile/create";
        try{
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(gson.toJson(p));
            Log.d(TAG, "createProfile:"+jsonObject.toString());
            makeAuthenticatedRequest(Request.Method.PUT,url,jsonObject,listener);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }



    public void removeProfile( Profile p,OnResponseListener listener){
        String url = endpoint+"/profile/delete";
        try{
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(gson.toJson(p));
            Log.d(TAG, "removeProfile:"+jsonObject.toString());
            makeAuthenticatedRequest(Request.Method.PUT,url,jsonObject,listener);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public List<Profile> getProfileKeys(final OnResponseListener listener) {
        String url = endpoint + "/profile/listAll";

        final List<Profile> profiles = new ArrayList<>();
        JsonArrayAuthenticatedRequest stringRequest = new JsonArrayAuthenticatedRequest(url,username,password, new Response.Listener<JSONArray>() {
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

                Log.d(TAG, "onResponse: "+response);
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
        JsonObjectRequest request = new JsonObjectRequest(method,url,jsonObject,new Response.Listener<JSONObject>() {            @Override
        public void onResponse(JSONObject response) {
            Gson gson = new Gson();
            Log.d(TAG, "onResponse: " + response.toString());
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
        String url = endpoint +"/user/create";

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
        String url = endpoint+"/user/updatePassword";

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
        String url = endpoint+"/location/create";

        try{
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(gson.toJson(location));

            Log.d(TAG, "createLocation:"+jsonObject.toString());

            makeAuthenticatedRequest(Request.Method.PUT,url,jsonObject,listener);


        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static List<Message> getMessagesFromLocation(Location location, final OnResponseListener listener){
        String url = endpoint+"/message/getMessagesByLocation";

        final List<Message> messages = new ArrayList<>();
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(gson.toJson(location));

            JsonArrayFromJsonObjectAuthenticatedRequest request = new JsonArrayFromJsonObjectAuthenticatedRequest(Request.Method.POST,url,username,password,jsonObject,null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    for(int i = 0;i<response.length();i++){
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Gson gson = new Gson();
                            Log.d(TAG, "onResponse: "+obj.toString());
                            Message msg = gson.fromJson(obj.toString(),Message.class);
                            messages.add(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    listener.onHTTPResponse(messages);


                    Log.d(TAG, "onResponse: "+response);
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
        String url = endpoint + "/location/list";

        final List<Location> locations = new ArrayList<>();
        JsonArrayAuthenticatedRequest stringRequest = new JsonArrayAuthenticatedRequest(url,username,password, new Response.Listener<JSONArray>() {
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

                Log.d(TAG, "onResponse: "+response);
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
        String url = endpoint + "/location/list/hash";
        JsonObjectAuthenticatedRequest request = new JsonObjectAuthenticatedRequest(Request.Method.GET,url,username,password,null,new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                Log.d(TAG, "onResponse: " + response.toString());
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


    public static List<Location> getNearLocations(LocationQuery query, final OnResponseListener listener){
        String url = endpoint+"/location/nearbyLocations";

        final List<Location> locations = new ArrayList<>();
        Log.d(TAG, "request: "+query.toJSON());
        JsonArrayFromJsonObjectAuthenticatedRequest request = new JsonArrayFromJsonObjectAuthenticatedRequest(Request.Method.POST,url,username,password,query.toJSON(),null, new Response.Listener<JSONArray>() {
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


                Log.d(TAG, "onResponse: "+response);
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


    public void createMessage(Message message,final OnResponseListener listener) {
        String url = endpoint+"/message/create";
        try {
            Gson  gson = new Gson();
            JSONObject jsonObject = new JSONObject(gson.toJson(message));
            Log.d(TAG, "createMessage: "+jsonObject);
            makeAuthenticatedRequest(Request.Method.PUT,url,jsonObject,listener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void removeLocation(String name,OnResponseListener listener){
        try {
            String url = endpoint+"/location/delete";
            url+="?name="+URLEncoder.encode(name,"UTF-8");
            makeAuthenticatedRequest(Request.Method.DELETE, url, null, listener);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}


