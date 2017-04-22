package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.LocationQuery;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;

import static android.content.ContentValues.TAG;

/**
 * Created by trosado on 31/03/17.
 */
public class ServerActions {
    private final static  String addr = "192.168.1.112";
    private final static String port = "8080";
    private final static String endpoint = "http://"+addr+":"+port;
    private static RequestQueue queue;

    public ServerActions(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    private void makeRequest(String url){
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        //FIXME
                        System.out.print("response:"+response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                System.out.print("error:"+error);

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void createUser(String username,String password){
        String url = endpoint +"/user/create?username="+username+"&password="+password;
        makeRequest(url);
    }

    public void updatePassword(String username,String password){
        String url = endpoint+"/user/updatePassword?username="+username+"&password="+password;
        makeRequest(url);
    }

    public void createLocation(Location location){
        Log.d(TAG, "createLocation: SSID: " + location.getSsid());
        String radius = location.getRadius()>0 ? "&radius="+location.getRadius() : "";
        //FIXME add stuff the miners names with spaces
        String url = endpoint+"/location/create?name="+location.getName()+"&ssid="+location.getSsid()+"&ble="
                +location.getBle()+"&lat="+location.getLatitude()+"&lon="+location.getLongitude()+radius;
        Log.d(TAG, "createLocation URL: " +  url);

        makeRequest(url);
    }

    public static List<Location> getNearLocations(LocationQuery query){
        String url = endpoint+"/location/nearByLocations";

        List<Location> locations = new ArrayList<>();
            /*TODO
            * - requestt receiveing a JsonObject and reading a JsonArray (Currently gives exception)
            * */

        JSONArray a = new JSONArray();
        JsonArrayFromJsonObjectRequest request = new JsonArrayFromJsonObjectRequest(Request.Method.POST,url,query.toJSON(),null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
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



    public void makeJSONRequest(String url){

       // JsonObjectRequest request = new JsonObjectRequest(url,new JSONObject())
    }


}


