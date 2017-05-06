package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Profile;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderDbHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    private List<String> list;
    private HashMap<String,String> keyHotfix;
    private ArrayAdapter<String> arrayAdapter;

    public ProfileFragment() {
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
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.fragment_profile_title);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddProfileKey.class);
                startActivity(intent);
            }});

        list = new ArrayList<>();
        keyHotfix = new HashMap<>();
    }

    @Override
    public void onResume() {
        super.onResume();

        new GetProfilesDatabaseTask(this.getView()).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ListView listView = (ListView) view.findViewById(R.id.profile_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int position, long l) {

                String content = (String) adapterView.getItemAtPosition(position);
                final String key = keyHotfix.get(content);

                Toast.makeText(getActivity(),"key -> " + key, Toast.LENGTH_LONG).show();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());

                String aux = getResources().getString(R.string.prompt_delete_key);

                alertDialogBuilder.setTitle(aux +' ' + key + '?');

                // set dialog message
                alertDialogBuilder
                        //.setMessage("Click yes to exit!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Toast.makeText(getActivity(),"DELETE MODAFOKAAAAA!!",Toast.LENGTH_LONG).show();

                                new DeleteProfileDatabaseTask(view,key).execute();

                                list.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });

        return view;
    }

    private class GetProfilesDatabaseTask extends AsyncTask<Void, Void, Void> {

        View v;
        List<String> list2update;

        public GetProfilesDatabaseTask(View view) {
            v = view;
            list2update = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ListView listView = (ListView) v.findViewById(R.id.profile_list);
            arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,list);
            listView.setAdapter(arrayAdapter);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            for(String s : list2update){
                list.add(s);
            }
            arrayAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getActivity());
            List<Profile> profilesList = dbHelper.getListProfiles();


            for(Profile p: profilesList){
                String s = p.getKey() + " -> " + p.getValue();
                list2update.add(s);
                keyHotfix.put(s, p.getKey());
            }

            return null;
        }
    }

    private class DeleteProfileDatabaseTask extends AsyncTask<Void, Void, Void> {

        View v;
        String k;

        public DeleteProfileDatabaseTask(View view, String key) {
            v = view;
            k = key;
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
            FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getActivity());
            dbHelper.deleteProfile(dbHelper.getReadableDatabase(),k);

            return null;
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
