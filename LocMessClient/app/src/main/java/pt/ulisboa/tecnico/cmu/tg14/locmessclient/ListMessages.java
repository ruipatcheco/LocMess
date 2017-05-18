package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Message;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderDbHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListMessages.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListMessages#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListMessages extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ListView mMessageListView;
    private List<Message> mMessageList;
    private ArrayAdapter<Message> arrayAdapter;
    private Handler handler;
    private List<String> messageContentList;


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

        handler = new Handler();
        handler.postDelayed(new GetMessageFromDBThread(),500);


        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddMessage.class);
                startActivity(intent);
            }});

        checkInternetConnection();
        fillDatabase(this.getActivity());

    }

    private void checkInternetConnection(){
        //fixme meter no login
        ServicesDataHolder dataHolder = ServicesDataHolder.getInstance().getInstance();
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        dataHolder.setCentralizedMode(isConnected);

       // Toast.makeText(getActivity(), "has internet connection = " + isConnected, Toast.LENGTH_LONG).show();
    }

    private void fillDatabase(Activity activity) {

        //FIXME -> move to login activity

        createDatabase(activity,true);

    }

    private void createDatabase(Context context, boolean deleteDatabase){
        FeedReaderDbHelper dbHelper = FeedReaderDbHelper.getInstance(context);
        if (doesDatabaseExist(context, dbHelper.getDatabaseName()) && deleteDatabase){
            //context.deleteDatabase(FeedReaderDbHelper.DATABASE_NAME);
            //dbHelper.onDrop(dbHelper.getWritableDatabase());
            //Log.d("createDatabase","old database detected and deleted");
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
        //Log.d("createDatabase","database created");

/*
        //FIXME TESTING
        dbHelper.insertLocation("Tecnico","testSSID","testBLE", (float) 0.1, (float) 0.2, "true");

        Calendar c = Calendar.getInstance();
        dbHelper.insertMessage(UUID.randomUUID().toString(), c.getTimeInMillis(),c.getTimeInMillis(),c.getTimeInMillis(),"olateste","publisher","tenicno");
        dbHelper.insertMessage(UUID.randomUUID().toString(), c.getTimeInMillis(),c.getTimeInMillis(),c.getTimeInMillis(),"o rosado é noob","publisher","tenicno");
        ArrayList<Message> messages = dbHelper.getAllMessages();

        for(Message m:messages){
            //Log.d("message", m.getUUID().toString());
            //Log.d("createDatabase", "creationtime: "+m.getCreationTime());
            //Log.d("createDatabase", "content: "+m.getContent());
        }

        //Log.d("message0", "uuid: " + messages.get(0).getUUID().toString());
        //Log.d("message0", "creationtime: "+messages.get(0).getCreationTime());
        //Log.d("message0", "content: "+messages.get(0).getContent());
        //Log.d("message0", "publisher: "+messages.get(0).getPublisher());

        try {
            Message m0 = dbHelper.getMessage(messages.get(0).getUUID().toString());
            //Log.d("message0BD", "uuid: " + m0.getUUID().toString());
            //Log.d("message0DB", "creationtime: "+m0.getCreationTime());
            //Log.d("message0DB", "content: "+m0.getContent());
            //Log.d("message0DB", "publisher: "+m0.getPublisher());
        } catch (MessageNotFoundException e) {
            //Log.d("message0BD", "MESSAGE NOT FOUND");
            e.printStackTrace();
        }

        try {
            for (Message m0 : dbHelper.getMessagesFromUser("publisher")) {
                //Log.d("message0BD", "uuid: " + m0.getUUID().toString());
                //Log.d("message0DB", "creationtime: "+m0.getCreationTime());
                //Log.d("message0DB", "content: "+m0.getContent());
                //Log.d("message0DB", "publisher: "+m0.getPublisher());
            }
        } catch (PublisherNotFoundException e) {
            //Log.d("messagePUBLISHER", "publisher not found");
        }

        dbHelper.insertMessageMule(UUID.randomUUID().toString(), c.getTimeInMillis(),c.getTimeInMillis(),c.getTimeInMillis(),"olateste","publisher","tenicno");
        ArrayList<Message> messagesMule = dbHelper.getAllMuleMessages();
        for(Message m:messagesMule){
            //Log.d("createDatabase", "mulecreationtime: "+m.getCreationTime());
            //Log.d("createDatabase", "mulecontent: "+m.getContent());
        }

        ArrayList<Location> locations = dbHelper.getAllLocations();
        for(Location l:locations){
            //Log.d("createDatabase", "name of location: "+l.getName());
            //Log.d("createDatabase", "lat of location: "+l.getLatitude());
        }
        //Log.d("createDatabase","location size: "+locations.size());

*/

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.fragment_list_messages_title);

        View view = this.getView();


        mMessageListView = (ListView)  view.findViewById(R.id.list_messages_list);

        mMessageList = new ArrayList<>();
        messageContentList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, messageContentList);


        mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(),MessageView.class);
                //assuming the message is in the same list position as the arrayadapter position
                String content = (String) adapterView.getItemAtPosition(position);
                Message message = mMessageList.get(position);
                intent.putExtra("MessageID",message.getUUID().toString());
                startActivity(intent);
            }
        });

    }


    private class GetMessageFromDBThread implements Runnable{
        private List<Message> auxList;

        @Override
        public void run() {
            auxList = new ArrayList<>();

            FeedReaderDbHelper dbHelper = FeedReaderDbHelper.getInstance(getActivity());
            List<Message> dbMessages = dbHelper.getAllNearbyMessages();

            mMessageListView.setAdapter(arrayAdapter);
            mMessageList.clear();
            messageContentList.clear();
            for(Message m: dbMessages){
                mMessageList.add(m);
                messageContentList.add(m.getContent());
            }

            arrayAdapter.notifyDataSetChanged();
            handler.postDelayed(this,5000);
        }
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



}
