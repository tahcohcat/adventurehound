package com.example.willeman.adventurehound;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Map;

// Instances of this class are fragments representing a single
// object in our collection.
public class ListViewMainFragment extends android.support.v4.app.Fragment {
    public static final String ARG_OBJECT = "object";
    private static final String TAG = "ListViewMainFragment";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // The last two arguments ensure LayoutParams are inflated
        // properly.
        final View rootView = inflater.inflate(
                R.layout.listview_first_tab, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), getActivity().MODE_PRIVATE);

        ActivityListView parent = (ActivityListView) getActivity();

        parent.dataBaseName = sharedPref.getString(getString(R.string.activity_list_id), "NO_ACTIVITY_LIST_ID_FOUND");

        Intent intent = parent.getIntent();

        final DrawerLayout drawer = (DrawerLayout) rootView.findViewById(R.id.drawerLayout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        //TODO: rename the button
        FloatingActionButton activityFilterButton = (FloatingActionButton) rootView.findViewById(R.id.filter_activity_button);
        activityFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: re-add this in drawer pane
                //startActivity(new Intent(getActivity(), FilterActivity.class));

                DrawerLayout drawer = (DrawerLayout) rootView.findViewById(R.id.drawerLayout);
                View drawerPane = (View) rootView.findViewById(R.id.drawerPane);
                if (drawer.isDrawerOpen(drawerPane)) {
                    drawer.closeDrawer(drawerPane);
                } else {
                    drawer.openDrawer(drawerPane);
                }
            }
        });

        FloatingActionButton activityAddButton = (FloatingActionButton) rootView.findViewById(R.id.add_activity_button);
        activityAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                try {
                    Intent intent = new Intent(getContext(), SingleItemActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "Error: Calling create new activity", e);
                    Toast.makeText(getContext(),
                            "Error loading requested activity", Toast.LENGTH_LONG).show();
                }
            }
        });

        ListView navigationSortListView = (ListView) rootView.findViewById(R.id.activitylist_drawer_sort_listview);

        ArrayList<String> sortinglistItems = new ArrayList<String>();
        sortinglistItems.add(0, getResources().getString(R.string.alphabetic_sort_keyword));
        sortinglistItems.add(1, getResources().getString(R.string.rating_sort_keyword));
        sortinglistItems.add(2, getResources().getString(R.string.price_sort_keyword));
        sortinglistItems.add(3, "Unused sort slot");

        //Sorting otpions
        ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(parent, android.R.layout.simple_list_item_1, sortinglistItems);
        navigationSortListView.setAdapter(adapter);
        navigationSortListView.setOnItemClickListener(parent);
        ListView navigationOtherListView =
                (ListView) rootView.findViewById(R.id.activitylist_drawer_other_listview);

        //Other options
        ArrayList<String> otherOptionslistItems = new ArrayList<String>();
        otherOptionslistItems.add(0, getResources().getString(R.string.filter_keyword));

        ArrayAdapter<String> otherListViewAdapter
                = new ArrayAdapter<String>(parent, android.R.layout.simple_list_item_1, otherOptionslistItems);
        navigationOtherListView.setAdapter(otherListViewAdapter);
        navigationOtherListView.setOnItemClickListener(parent);

        SearchTypes searchType = SearchTypes.FilteredList;

        assert(parent!=null);
        assert(parent.intent!=null);
        assert(getResources().getString(R.string.activity_bundle_type)!=null);

        String bundleType = parent.intent.getStringExtra(getResources().getString(R.string.activity_bundle_type));

        assert(bundleType!=null);

        if (bundleType == null)
        {
            assert(false);
            Log.e(TAG,"Bundle type was not set");

            Snackbar.make(rootView, "Bundle type was not set", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return rootView;
        }

        //Intent from MainActivity
        //TODO: for consistency always set bundletype - should never be null
        //currently  is for new or edit
        if (bundleType.equalsIgnoreCase(getResources().getString(R.string.search_bundle)))
        {
            String query = intent.getStringExtra(SearchManager.QUERY);

            //TODO: create ItemReaderFactory class that returns implementation of ItemReader
            //    : interface. That way getting rid of CouchBase would not mean massive
            //    : refactor.

            Bundle bundle = new ItemReader().getDocumentsBundle(parent, parent.dataBaseName, new FilterCriteria());

            if (bundle == null) {
                Log.e(TAG, "ActivityListView: Error: ItemRetriever.getAllDocuments returned null for list '" + parent.dataBaseName + "'");

                ImageView view = (ImageView) rootView.findViewById(R.id.filter_activity_button);
                Snackbar.make(view, "ActivityListView: Error: ItemRetriever.getAllDocuments" +
                        "returned null for list '" + parent.dataBaseName + "'", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //TODO: rather return empty view here
                return null;
            }

            Map<Integer, TaskListDocument> itemMap = parent.extractActivityItems(bundle);

            //no longer needed
            //Map<Integer, TaskListDocument> filteredItems = parent.filterItemsOnSearch(query, itemMap);

            parent.lv = (ListView) rootView.findViewById(R.id.activities_list_view);
            parent.lv.setAdapter(new CustomSwipeAdapterFactory().create(
                    parent,
                    itemMap,
                    parent.lv,
                    parent));

            return rootView;
        }
        //Intent from FilterActivity or SingleItemActivity
        else {
            //All None Searched ActivityList actions
            assert(parent!=null);
            assert(parent.intent!=null);

            handleNonSearchBundle(parent, rootView);
        }


        Bundle args = getArguments();
        return rootView;
    }

    private void handleNonSearchBundle(ActivityListView parent, View rootView) {

        Bundle bundle = parent.intent.getExtras();

        if (bundle == null) {
            //back button pressed
            refreshActivityList(parent, rootView);
            return;
        }

        if (!bundle.containsKey(getResources().getString(R.string.activity_bundle_type)))
        {
            Snackbar.make(rootView, "[E2] Bundle type was not set", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            refreshActivityList(parent, rootView);
            return;
        }

        String bundleType = bundle.getString(getResources().getString(R.string.activity_bundle_type));

        //Filter Bundle
        if (bundleType.equalsIgnoreCase(getResources().getString(R.string.filter_bundle))) {

            String bundleContent = bundle.getString(getResources().getString(R.string.filter_bundle));
            FilterCriteria filter = null;

            try {
                filter = new GsonBuilder().create().fromJson(bundleContent, FilterCriteria.class);

            } catch (Exception e) {
                Log.e(TAG, "Error: ", e);
                Log.e(TAG, "Could deserialize FilterCriteria from string [" +
                        bundle.getString(getResources().getString(R.string.filter_bundle)) + "]");
            }

            Map<Integer, TaskListDocument> itemMap = parent.getItemsMap(filter);
            //Map<Integer, TaskListDocument> filteredItems = parent.filterItemsOnCriteria(filter, itemMap);
            parent.lv = (ListView) rootView.findViewById(R.id.activities_list_view);
            parent.lv.setAdapter(new CustomSwipeAdapterFactory().create(
                    parent,
                    itemMap,
                    parent.lv,
                    parent));
            return;
        }

        //Edit or New Activity Returned
        if (bundleType.equalsIgnoreCase(getResources().getString(R.string.update_single_item_bundle))) {

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

            if (writer.writeItem(getContext(), parent.dataBaseName, activityItem)) {
                ImageView view = (ImageView) rootView.findViewById(R.id.filter_activity_button);

                //TODO: fix this!!
                Snackbar.make(rootView, "Activity '" + activityItem.getActivity() + "' updated", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                Log.e(TAG, "Activity '" + activityItem.getActivity() + "' could not be updated");
                ImageView view = (ImageView) rootView.findViewById(R.id.filter_activity_button);
                Snackbar.make(rootView, "Error: Activity '" + activityItem.getActivity() + "' could not be updated", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        //First Call / Other
        refreshActivityList(parent, rootView);
    }

    //Refresh List - Get all items (unfiltered list)
    private void refreshActivityList(ActivityListView parent, View rootView) {
        Map<Integer, TaskListDocument> itemMap = parent.getItemsMap();

        //TODO: warning! changed from listView - not sure this is right id
        parent.lv = (ListView) rootView.findViewById(R.id.activities_list_view);
        parent.lv.setAdapter(new CustomSwipeAdapterFactory().create(
                parent,
                itemMap,
                parent.lv,
                parent));

    }
}

