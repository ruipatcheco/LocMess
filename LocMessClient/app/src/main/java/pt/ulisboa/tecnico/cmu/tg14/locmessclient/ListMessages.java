package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.LocationMover;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Message;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnResponseListener;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderDbHelper;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.ServerActions;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListMessages.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListMessages#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListMessages extends Fragment {

    private List<Message> mMessageList;

    private OnFragmentInteractionListener mListener;

    private ListView mMessageListView;

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
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.fragment_list_messages_title);



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

        //FIXME -> move to login activity and check server connection to infer boolean value

        createDatabase(activity,true);
        //new FillDatabaseTask(activity).execute();

    }

    private void createDatabase(Context context, boolean deleteDatabase){
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
        if (doesDatabaseExist(context, dbHelper.getDatabaseName()) && deleteDatabase){
            dbHelper.onDrop(dbHelper.getWritableDatabase());
            Log.d("createDatabase","old database detected and deleted");
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
        Log.d("createDatabase","database created");

        dbHelper.insertLocation("Tecnico","testSSID","testBLE", (float) 0.1, (float) 0.2);


        //TESTING
        Calendar c = Calendar.getInstance();
        dbHelper.insertMessage("0", c.getTimeInMillis(),c.getTimeInMillis(),c.getTimeInMillis(),"olateste","publisher","tenicno");
        ArrayList<Message> messages = dbHelper.getAllMessages();

        for(Message m:messages){
            Log.d("createDatabase", "creationtime: "+m.getCreationTime());
            Log.d("createDatabase", "content: "+m.getContent());
        }

        dbHelper.insertMessageMule("0", c.getTimeInMillis(),c.getTimeInMillis(),c.getTimeInMillis(),"olateste","publisher","tenicno");
        ArrayList<Message> messagesMule = dbHelper.getAllMuleMessages();
        for(Message m:messagesMule){
            Log.d("createDatabase", "mulecreationtime: "+m.getCreationTime());
            Log.d("createDatabase", "mulecontent: "+m.getContent());
        }

        ArrayList<Location> locations = dbHelper.getAllLocations();
        for(Location l:locations){
            Log.d("createDatabase", "name of location: "+l.getName());
            Log.d("createDatabase", "lat of location: "+l.getLatitude());
        }
        Log.d("createDatabase","location size: "+locations.size());

        return;
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


        try {
            mMessageList = new ListMessagesTask(view).execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        mMessageListView = (ListView)  view.findViewById(R.id.list_messages_list);

        mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(),MessageView.class);
                //assuming the message is in the same list position as the arrayadapter position
                String content = (String) adapterView.getItemAtPosition(position);
                Message message = mMessageList.get(position);
                intent.putExtra("MessageID",message.getUUID());
                startActivity(intent);
            }
        });


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


    private class ListMessagesTask extends AsyncTask<Void, Void, List<Message>> implements OnResponseListener<List<Message>> {

        ProgressDialog progDailog;
        private ArrayAdapter<Message> arrayAdapter;
        private List<Message> messageList;
        private List<String> messageContentList;
        private View view;
        public ListMessagesTask(View view) {
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

            messageList = new ArrayList<>();
            messageContentList = new ArrayList<>();

            ListView listView = (ListView) view.findViewById(R.id.list_messages_list);
            arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, messageContentList);
            listView.setAdapter(arrayAdapter);
        }

        @Override
        public void onPostExecute(List< Message> messages) {
            super.onPostExecute(messages);
            progDailog.dismiss();
        }

        @Override
        protected List<Message> doInBackground(Void... params) {

            ServerActions serverActions = new ServerActions(getActivity());
            //TODO Change to get locations from local DB
            LocationMover locationMover = new LocationMover("Tagus","","",0,0,0);
            return serverActions.getMessagesFromLocation(locationMover,this);
        }

        @Override
        public void onHTTPResponse(List<Message> response) {
            for(Message m : response){
                messageList.add(m);
                messageContentList.add(m.getContent());
            }
            ServicesDataHolder servicesDataHolder = ServicesDataHolder.getInstance();
            servicesDataHolder.setMessageMapFromList(mMessageList);

            arrayAdapter.notifyDataSetChanged();
        }
    }


    /*
    private class FillDatabaseTask extends AsyncTask<Void, Void, Void> {

        Context context;
        ArrayList<Location> locations;

        public FillDatabaseTask(Context context, ArrayList<Location> locations) {
            this.context = context;
            this.locations = locations;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
            dbHelper.insertAllLocations(locations);

        dbHelper.insertLocation("Tecnico","testSSID","testBLE", (float) 0.1, (float) 0    return null;
        }
    }*/

    private class GetLocationsTask extends AsyncTask<Void, Void, Void> implements OnResponseListener<List<Location>>{

        ProgressDialog progDailog;
        Context context;
        List<Location> locations;

        public GetLocationsTask(Context context) {
            this.context = context;
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

            //new FillDatabaseTask(context, locations).execute();

            progDailog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {

            ServerActions serverActions = new ServerActions(getActivity());
            locations = serverActions.getAllLocations(this);

            return null;
        }

        @Override
        public void onHTTPResponse(List<Location> response) {
            FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
            dbHelper.insertAllLocations(locations);
        }
    }


}
