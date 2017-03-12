package com.example.willeman.adventurehound;

/**
 * Created by WILLEMAN on 7/21/2016.
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ActivityListView extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListViewPagerAdapter mListViewPagerAdapter;
    ViewPager mViewPager;

    ListView lv;
    ListView upcoming_lv;
    //Context context;
    public static final String TAG = "TTD.ArrayListView";

    //public String DEFAULT_DB_NAME = "list_test06";

    public String dataBaseName;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    protected Intent intent;

    public void removeItem(TaskListDocument item)
    {
        ItemWriter writer = new ItemWriter();

        if (writer.deleteItem(this, dataBaseName, item))
        {
            Toast.makeText(this,
                    "Activity: " + item.getActivity() + " deleted", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this,
                    "Error: Could not delete: " + item.getActivity(), Toast.LENGTH_LONG).show();
        }

        refreshActivityList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tabbed_parent_layout);

        //TODO: assumption true for now but will change...
        SearchTypes intentType = SearchTypes.FilteredList;

        this.intent = getIntent();

        mListViewPagerAdapter = new ListViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mListViewPagerAdapter);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mListViewPagerAdapter.refreshPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onItemClick(AdapterView<?> l, View view, int position, long id) {

        String value = l.getAdapter().getItem(position).toString();

        try {

            if (getResources().getString(R.string.alphabetic_sort_keyword).equalsIgnoreCase(value)) {
                lv = (ListView) findViewById(R.id.activities_list_view);
                CustomSwipeAdapter adapter = (CustomSwipeAdapter) lv.getAdapter();
                adapter.sortByAtoZ();
            }

            if (getResources().getString(R.string.rating_sort_keyword).equalsIgnoreCase(value)) {
                lv = (ListView) findViewById(R.id.activities_list_view);
                CustomSwipeAdapter adapter = (CustomSwipeAdapter) lv.getAdapter();
                adapter.sortByRating();
            }

            if (getResources().getString(R.string.filter_keyword).equalsIgnoreCase(value))
            {
                startActivity(new Intent(this, FilterActivity.class));
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG,"",ex);
        }

    }

    private void handleBundle(Bundle bundle) {

        if (bundle == null) {
            //back button pressed
            refreshActivityList();
            return;
        }

        //Filter Bundle
        if (bundle.containsKey(getResources().getString(R.string.filter_bundle))) {

            String bundleContent = bundle.getString(getResources().getString(R.string.filter_bundle));
            FilterCriteria filter = null;

            try {
                filter = new GsonBuilder().create().fromJson(bundleContent, FilterCriteria.class);

            } catch (Exception e) {
                Log.e(TAG, "Error: ", e);
                Log.e(TAG, "Could deserialize FilterCriteria from string [" +
                        bundle.getString(getResources().getString(R.string.filter_bundle)) + "]");
            }

            Map<Integer, TaskListDocument> itemMap = getAllItemsMap();
            Map<Integer, TaskListDocument> filteredItems = filterItemsOnCriteria(filter, itemMap);
            lv = (ListView) findViewById(R.id.activities_list_view);
            lv.setAdapter(new CustomSwipeAdapterFactory().create(
                    this,
                    filteredItems,
                    lv,
                    this));
            return;
        }

        //Edit or New Activity Returned
        if (bundle.containsKey(getResources().getString(R.string.activity_bundle))) {

            ItemWriter writer = new ItemWriter();
            int position = getResources().getInteger(R.integer.activity_position_unset); //new item
            try {
                position = Integer.valueOf(bundle.getString(getResources().getString(R.string.activity_position)));
            } catch (Exception ex) {
                //may be new item...
                Log.i(TAG, "Activities' position was not sent in bundle...assuming its a new activity item");
            }

            String activityAsString = bundle.getString(getResources().getString(R.string.activity_bundle));
            Gson gson = new GsonBuilder().create();
            TaskListDocument activityItem = gson.fromJson(activityAsString, TaskListDocument.class);

            if (writer.writeItem(getApplicationContext(), dataBaseName, activityItem)) {
                ImageView view = (ImageView) findViewById(R.id.filter_activity_button);
                Snackbar.make(view, "Activity '" + activityItem.getActivity() + "' updated", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                Log.e(TAG, "Activity '" + activityItem.getActivity() + "' could not be updated");
                ImageView view = (ImageView) findViewById(R.id.filter_activity_button);
                Snackbar.make(view, "Error: Activity '" + activityItem.getActivity() + "' could not be updated", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        //First Call / Other
        refreshActivityList();
    }

    //Refresh List - Get all items (unfiltered list)
    private void refreshActivityList() {
        Map<Integer, TaskListDocument> itemMap = getAllItemsMap();
        lv = (ListView) findViewById(R.id.activities_list_view);
        lv.setAdapter(new CustomSwipeAdapterFactory().create(
                this,
                itemMap,
                lv,
                this));

        //refresh upcoming activities here...
        /*
        upcoming_lv = (ListView) findViewById(R.id.upcoming_activities_list_view);
        upcoming_lv.setAdapter(new CustomSwipeAdapterFactory().create(
                this,
                itemMap,
                upcoming_lv,
                this));
        */
    }

    protected Map<Integer, TaskListDocument> getAllItemsMap() {
        return getAllItemsMap(new ItemReader().getAllDocumentsBundle(
                this, this.dataBaseName));
    }

    private Map<Integer, TaskListDocument> getAllItemsMap(Bundle bundle) {
        if (bundle == null) {
            ImageView view = (ImageView) findViewById(R.id.filter_activity_button);
            Snackbar.make(view, "Error: no data found for list " + this.dataBaseName, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return null;
        }
        return extractActivityItems(bundle);
    }

    protected Map<Integer, TaskListDocument> extractActivityItems(Bundle b) {
        Map<Integer, TaskListDocument> items = new HashMap<>();

        Set<String> bundleKeys = b.keySet();
        for (String position : bundleKeys) {
            try {
                int pos = Integer.parseInt(position);
                if (b.containsKey(position)) {
                    String serialisedActivity = b.getString(position);

                    Gson gson = new GsonBuilder().create();
                    TaskListDocument activityItem = gson.fromJson(serialisedActivity, TaskListDocument.class);
                    items.put(Integer.parseInt(position), activityItem);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error: ", e);
                Log.d(TAG, "Could not list activity [" + position + "]");
            }
        }
        return items;
    }

    protected Map<Integer, TaskListDocument> filterItemsOnSearch(
            String query,
            Map<Integer, TaskListDocument> items) {
        Map<Integer, TaskListDocument> filteredItems = new HashMap<>();

        int itemCount = 0;
        for (Map.Entry<Integer, TaskListDocument> entry : items.entrySet()) {
            Integer key = entry.getKey();
            TaskListDocument item = entry.getValue();
            assert(item!=null);

            if (item.getCategory().contains(query)) {
                filteredItems.put(itemCount, item);
                itemCount++;
            }
        }
        return filteredItems;
    }

    protected Map<Integer, TaskListDocument> filterItemsOnCriteria(
            FilterCriteria filter,
            Map<Integer, TaskListDocument> items) {
        Map<Integer, TaskListDocument> filteredItems = new HashMap<>();

        int itemCount = 0;
        for (Map.Entry<Integer, TaskListDocument> entry : items.entrySet()) {
            TaskListDocument item = entry.getValue();
            if (ActivityFilter.isIncluded(item, filter)) {
                filteredItems.put(itemCount, item);
                itemCount++;
            }
        }
        return filteredItems;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        boolean activatedSearch = false;

        switch (item.getItemId()) {
            case R.id.search: {
                onSearchRequested();
                activatedSearch = true;
                break;
            }
            case R.id.show_hide_options_drawer_icon: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
                View drawerPane = (View) findViewById(R.id.drawerPane);
                if (drawer.isDrawerOpen(drawerPane))
                {
                    drawer.closeDrawer(drawerPane);
                }
                else
                {
                    drawer.openDrawer(drawerPane);
                }
                break;
            }
        }
        return activatedSearch;
    }

    @Override
    public boolean onSearchRequested() {
        return super.onSearchRequested();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listview_activity, menu);
        //getMenuInflater().inflate(R.menu.searchable_menu, menu);

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ActivityListView Page", // TODO: Define a title for the content shown.
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
                "ActivityListView Page", // TODO: Define a title for the content shown.
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

}