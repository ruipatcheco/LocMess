package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.annotation.BoolRes;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.LocationQuery;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnResponseListener;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderDbHelper;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.ServerActions;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListMessages.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListMessages#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListMessages extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private ListView mMessageList;

    public ListMessages() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListMessages.
     */
    // TODO: Rename and change types and number of parameters
    public static ListMessages newInstance(String param1, String param2) {
        ListMessages fragment = new ListMessages();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.fragment_list_messages_title);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddMessage.class);
                startActivity(intent);
            }});

        fillDatabase(this.getActivity());

    }

    private void fillDatabase(Activity activity) {
        createDatabase(activity);
        //new FillDatabaseTask(activity).execute();

    }

    private void createDatabase(Context context){
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
        if (doesDatabaseExist(context, dbHelper.getDatabaseName())){
            dbHelper.onDrop(dbHelper.getWritableDatabase());
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle(R.string.fragment_list_messages_title);

        View view = inflater.inflate(R.layout.fragment_list_messages, container, false);
        List<String> list = new ArrayList<>();
        list.add("Mess1");
        list.add("Mess2");
        list.add("Mess3");
        list.add("Mess4");

        ListView listView = (ListView) view.findViewById(R.id.list_messages_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,list);

        listView.setAdapter(arrayAdapter);



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


    /*
    private class FillDatabaseTask extends AsyncTask<Void, Void, Void> implements OnResponseListener<List<Location>>{

        ProgressDialog progDailog;
        Activity activity;

        public FillDatabaseTask(Activity act) {
            this.activity = act;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progDailog = new ProgressDialog(getActivity());
            progDailog.setMessage("Fetching Locations...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progDailog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {

            ServerActions serverActions = new ServerActions(getActivity());
            ArrayList <Location> locations = (ArrayList <Location>) serverActions.getAllLocations(this);

            for(Location l: locations){
                Log.d("FillDatabaseTask", "location received from server -> " + l.getName());
            }

            FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(activity);

            dbHelper.insertAllLocations(locations);


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
    }*/


}
