package com.example.willeman.adventurehound;

import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.couchbase.lite.*;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.simple.JSONObject;


public class MainActivity extends AppCompatActivity {

    private Manager manager;
    private static android.content.Context mContext;
    //private static Context mContext;

    public static final String TAG = "TTD.MainActivity";
    public String DEFAULT_DB_NAME;
    public static final String DB_NAME_OLD = "list_test01";
    public static final String JSON_STRING = "bundleKey"; //todo: rename with better key

    public HashMap<String, String> EXTRA_MESSAGE;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.enableLogging(TAG, Log.DEBUG);
        Log.d(TAG, "Begin Couchbase Events App");

        mContext = getApplicationContext();

        DEFAULT_DB_NAME = getResources().getString(R.string.default_db);

        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        final Spinner activityListView = (Spinner) findViewById(R.id.spinner);

        final ArrayList<String> activityLists = new ArrayList<String>();
        activityLists.add(DEFAULT_DB_NAME);

        activityLists.add(DB_NAME_OLD);
        activityLists.add("list_test03");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, activityLists);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityListView.setAdapter(adapter);


        Manager manager = null;
        Database database = null;

        try {
            manager = new Manager(new AndroidContext(getApplicationContext()), Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase(DEFAULT_DB_NAME);
            //addDefaultActivities(database);

            DefaultListPopulator pop = new DefaultListPopulator(DEFAULT_DB_NAME,getApplicationContext());


        } catch (Exception e) {
            Log.e(TAG, "Error getting database", e);
        }


        ImageView img = (ImageView) findViewById(R.id.activity_go_icon);

        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String dataBaseName = DEFAULT_DB_NAME;
                if (activityListView.getChildCount()>0)
                {
                    try {
                        dataBaseName  = activityListView.getSelectedItem().toString();
                    }
                    catch (Exception ex)
                    {
                        Snackbar.make(view, "No activity list was selected", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }

                SharedPreferences sharedPref = mContext.getSharedPreferences(
                        getString(R.string.preference_file_key), mContext.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.activity_list_id), dataBaseName);
                editor.commit();

                Bundle bundle = new Bundle();

                //TODO: replaced with sharedprefs...remove in time..
                bundle.putString(getResources().getString(R.string.activity_list_id),dataBaseName);
                bundle.putString(getResources().getString(R.string.activity_bundle_type),
                        getResources().getString(R.string.list_all_bundle));

                Intent intent = new Intent(mContext, ActivityListView.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        activityListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String activityList = parent.getItemAtPosition(position).toString();

                Toast.makeText(mContext, activityList, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "ActivityList add functionality to go here", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.willeman.adventurehound/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.willeman.adventurehound/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    private void addDefaultActivities(Database database) {

        Map<String,String> attributes = new HashMap<>();

        createActivityDocWithAttributes(database, "Fairview Winefarm", "Big selection of tastes", Arrays.asList("category:" + getResources().getString(R.string.cat_food_label)),null);
        createActivityDocWithAttributes(database, "Surfing", "Strand beach on a calm day", Arrays.asList("category:" + getResources().getString(R.string.cat_sport_label)),null);
        createActivityDocWithAttributes(database, "SUP", "Where to rent?", Arrays.asList("category:"  + getResources().getString(R.string.cat_sport_label)),null);
        createActivityDocWithAttributes(database, "Koeelbaai camping", "", Arrays.asList("category" +  getResources().getString(R.string.cat_adventure_label)),null);
        createActivityDocWithAttributes(database, "Sutherland", "Stars best in Winter", Arrays.asList("category:" + getResources().getString(R.string.cat_adventure_label)),null);

        attributes = new HashMap<>();
        attributes.put("category", "entertainment");
        createActivityDocWithAttributes(database, "Boardgames evening", "Invite Phlip & Jaco", Arrays.asList("category:" + getResources().getString(R.string.cat_entertainment_label)), null);

        attributes.put("category", "entertainment,adventure");
        createActivityDocWithAttributes(database, "Fanaticus boardgames", "Saturday mornings Plattekloof", Arrays.asList("category:" + getResources().getString(R.string.cat_entertainment_label)), attributes);

        attributes = new HashMap<>();
        attributes.put("category", "food");
        createActivityDocWithAttributes(database, "Sushi Balugas", "", Arrays.asList("category:" + getResources().getString(R.string.cat_food_label)), attributes);

        createActivityDocWithAttributes(database, "Weskus blomme", "September. Bel flower-hotline", Arrays.asList("category:adventure"), null);
        createActivityDocWithAttributes(database, "Colour run", "", Arrays.asList("category:sport"),null);
        createActivityDocWithAttributes(database, "Botmaskop", "Sleepover. Will need ideally 4 people", Arrays.asList("category:adventure", "weather:sunny", "env:outdoors", "energy_level:mediumhigh", "cost:1"),null);
        createActivityDocWithAttributes(database, "Llandudno beachday", "", Arrays.asList("category:adventure"),null);
        createActivityDocWithAttributes(database, "Kreef duik / braai", "", Arrays.asList("category:food"),null);
        createActivityDocWithAttributes(database, "Two Oceans Marathon", "Registration date?", Arrays.asList("category:sport"),null);
        createActivityDocWithAttributes(database, "Parkruns", "Explore different runs", Arrays.asList("category:sport"),null);
        createActivityDocWithAttributes(database, "KKNK", "", Arrays.asList("category:art"),null);
        createActivityDocWithAttributes(database, "Potluck Club", "Book long in advance", Arrays.asList("category:food"),null);
        createActivityDocWithAttributes(database, "Planetarium", "Combine with BBC Wildlife Photography Exhibit", Arrays.asList("category:entertainment"),null);

        createActivityDocWithAttributes(database, "Stellenbosch guide tour", "", Arrays.asList("category:art"), null);
        createActivityDocWithAttributes(database, "Jungle Book DVD (2015)", "", Arrays.asList("category:entertainment"),null);

        createActivityDocWithAttributes(database, "5fm Live concert", "", Arrays.asList("category:entertainment"),null);

    }

    private String createActivityDoc(
            Database database,
            String activity,
            String details) {
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activity", activity);
        map.put("details", details);
        try {
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }

    private String createActivityDocWithAttributes(
            Database database,
            String activity,
            String details,
            List<String> tags,
            Map<String,String> attributes) {
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<String, Object>();

        //TODO: make these string resources
        map.put("activity", activity);
        map.put("details", details);
        map.put("tags", org.json.simple.JSONArray.toJSONString(tags));

        if (attributes!=null) {
            JSONObject propertiesJSON = new JSONObject();
            propertiesJSON.putAll(attributes);
            map.put("attributes", propertiesJSON.toJSONString());
        }


        try {
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }
}
