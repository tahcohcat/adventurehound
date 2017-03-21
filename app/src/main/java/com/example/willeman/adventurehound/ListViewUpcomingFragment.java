package com.example.willeman.adventurehound;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

// Instances of this class are fragments representing a single
// object in our collection.
public class ListViewUpcomingFragment extends android.support.v4.app.Fragment {
    public static final String ARG_OBJECT = "object";
    private static final String TAG = "ListViewMainFragment";

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(
                R.layout.listview_second_tab, container, false);


        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), getActivity().MODE_PRIVATE);

        ActivityListView parent = (ActivityListView) getActivity();

        parent.dataBaseName = sharedPref.getString(getString(R.string.activity_list_id), "NO_ACTIVITY_LIST_ID_FOUND");

        assert(parent!=null);

        Bundle bundle = new ItemReader().getDocumentsBundle(parent, parent.dataBaseName, new FilterCriteria(true)); //do not filter

        if (bundle == null) {
            Log.e(TAG, "ActivityListView: Error: ItemRetriever.getAllDocuments returned null for list '" + parent.dataBaseName + "'");

            ImageView view = (ImageView) rootView.findViewById(R.id.filter_activity_button);
            Snackbar.make(view, "ActivityListView: Error: ItemRetriever.getAllDocuments" +
                    "returned null for list '" + parent.dataBaseName + "'", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            //TODO: rather return empty view here
            return null;
        }

        handleUpcomingEvents(parent,rootView);
        return rootView;
    }

    private void handleUpcomingEvents(ActivityListView parent, View rootView) {

        refreshUpcomingActivityList(parent, rootView);
    }

    private void refreshUpcomingActivityList(ActivityListView parent, View rootView) {

        Map<Integer, TaskListDocument> itemMap = parent.getItemsMap();

        Map<Integer, TaskListDocument> immenentItemsMap = new HashMap<>();

        int itemCount = 0;
        for (Map.Entry<Integer, TaskListDocument> entry : itemMap.entrySet()) {
            TaskListDocument item = entry.getValue();

            UpcomingDateCriteria criteria = new UpcomingDateCriteria();

            //TODO: replace with resource
            if (criteria.isIncluded(item.getAttribute("date"))) {
                immenentItemsMap.put(itemCount++,item);
            }
        }

        parent.upcoming_lv = (ListView) rootView.findViewById(R.id.upcoming_activities_list_view);
        parent.upcoming_lv.setAdapter(new CustomSwipeAdapterFactory().create(
                parent,
                immenentItemsMap,
                parent.upcoming_lv,
                parent));
    }

    public void refreshItems() {
        ActivityListView parent = (ActivityListView) getActivity();
        refreshUpcomingActivityList(parent,rootView);
    }
}

