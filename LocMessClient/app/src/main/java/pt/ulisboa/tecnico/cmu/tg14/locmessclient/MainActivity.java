package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;

import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import android.widget.TextView;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderDbHelper;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.ServiceManager;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ListMessages.OnFragmentInteractionListener,
        MyMessagesFragment.OnFragmentInteractionListener,ListLocations.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener {

    private static final String TAG = "Main Activity" ;
    private ListView mDrawerList;
    private FloatingActionButton mFab;
    Activity activity;
    NavigationView navigationView;


    private ServiceManager mServiceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            Log.d("EXIT", "LOGOUT GO TO LOGIN ACTIVITY");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mServiceManager = ServiceManager.getInstance(this); //This starts all the needed Services

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;

        mDrawerList = (ListView) findViewById(R.id.nav_options);
        //mDrawerList.setItemChecked(0,true);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity,AddMessage.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, new ListMessages());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.nav_bar_username);
        name.setText(ServicesDataHolder.getInstance().getUsername());


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm = getFragmentManager();

        if (id == R.id.nav_messages) {
            // Handle the camera action
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new ListMessages());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            //Log.d("Menu","msg");

        } else if (id == R.id.nav_my_messages) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new MyMessagesFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            //Log.d("Menu","Mymsg");

        } else if (id == R.id.nav_location) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new ListLocations());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            //Log.d("Menu","location");
        } else if (id == R.id.nav_profile) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new ProfileFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            //Log.d("Menu","profile");

        } else if (id == R.id.nav_log_out) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

            String aux = getResources().getString(R.string.prompt_logout_action);

            alertDialogBuilder.setTitle(aux);

            // set dialog message
            alertDialogBuilder
                    //.setMessage("Click yes to exit!")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("EXIT", true);
                            logout();
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            navigationView.getMenu().getItem(0).setChecked(true);
                            item.setChecked(false);
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
            //Log.d("Menu","Log");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //Log.d("URI",uri.toString());
    }

    private void logout() {
        // delete all sensitive data from database
        // TODO : do I need to stop services?
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        dbHelper.dropDatabase(activity);
    }

}
