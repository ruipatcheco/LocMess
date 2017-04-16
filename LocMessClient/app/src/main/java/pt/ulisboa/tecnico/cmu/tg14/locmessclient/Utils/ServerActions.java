package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;

import static android.content.ContentValues.TAG;

/**
 * Created by trosado on 31/03/17.
 */
public class ServerActions {
    private final String addr = "194.210.220.229";
    private final String port = "8080";
    private RequestQueue queue;

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
        String url = "http://"+addr+":"+port+"/user/create?username="+username+"&password="+password;
        makeRequest(url);
    }

    public void updatePassword(String username,String password){
        String url = "http://"+addr+":"+port+"/user/updatePassword?username="+username+"&password="+password;
        makeRequest(url);
    }

    public void createLocation(Location location){

        String radius = location.getRadius()>0 ? "&radius="+location.getRadius() : "";
        //FIXME add stuff the miners names with spaces
        String url = "http://"+addr+":"+port+"/location/create?name="+location.getName()+"&ssid="+location.getSsid()+"&ble="
                +location.getBle()+"&lat="+location.getLatitude()+"&lon="+location.getLongitude()+radius;
        Log.d(TAG, "createLocation URL: " +  url);

        makeRequest(url);
    }


}


