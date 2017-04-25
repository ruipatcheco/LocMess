package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.LocationQuery;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnResponseListener;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.ServerActions;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListLocations.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListLocations#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListLocations extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ServicesDataHolder mDataHolder;
    private OnFragmentInteractionListener mListener;

    public ListLocations() {
        // Required empty public constructor
        mDataHolder = ServicesDataHolder.getInstance();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListLocations.
     */
    // TODO: Rename and change types and number of parameters
    public static ListLocations newInstance(String param1, String param2) {
        ListLocations fragment = new ListLocations();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    private class ListLocationsTask extends AsyncTask<Void, Void, Void> implements OnResponseListener<List<Location>> {

        ProgressDialog progDailog;
        private ArrayAdapter<String> arrayAdapter;
        private List<String> locationListNames;
        private View view;
        public ListLocationsTask(View view) {
            this.view = view;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progDailog = new ProgressDialog(getActivity());
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();

            locationListNames = new ArrayList<>();
            ListView listView = (ListView) view.findViewById(R.id.list_locations_list);
            arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, locationListNames);
            listView.setAdapter(arrayAdapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progDailog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {

            ServerActions serverActions = new ServerActions(getActivity());
            LocationQuery query = new LocationQuery(mDataHolder.getLatitude(),mDataHolder.getLongitude()
                                                    ,mDataHolder.getSsidAddresses(),mDataHolder.getBleAddresses());
            serverActions.getNearLocations(query,this);
            return null;
        }

        @Override
        public void onHTTPResponse(List<Location> response) {
            for(Location l : response){
                locationListNames.add(l.getName());
                Log.d(TAG, "doInBackground: "+l.getName());
            }
            arrayAdapter.notifyDataSetChanged();
        }
    }










    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.fragment_list_locations_title);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddLocationActivity.class);
                startActivity(intent);
            }});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_locations, container, false);

        new ListLocationsTask(view).execute();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onResume() {
        super.onResume();
        new ListLocationsTask(getView()).execute();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
