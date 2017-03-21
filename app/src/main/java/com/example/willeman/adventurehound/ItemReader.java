package com.example.willeman.adventurehound;

/**
 * Created by WILLEMAN on 8/1/2016.
 */

import android.content.res.Resources;
import android.os.Bundle;

import com.couchbase.lite.*;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.simple.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ItemReader {

    public static final String TAG = "TTD.ItemRetriever";
    private Resources resources;
    private static int TabBatchSize = 20;
    private Object startKey = null;
    private Boolean isQueryPending = false;

    public ItemReader()
    {
        resources = null;
    }

    /*
    public Bundle getMatchingDocuments(
            android.content.Context context,
            String databaseName,
            String filterKey,
            String filterValue) {
        Database database = null;

        try {
            Manager manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase(databaseName);
        } catch (Exception e) {
            Log.e(TAG, "Error getting database", e);
            return null;
        }

        Bundle bundle = new Bundle();

        View categoryView = database.getView("categories");
        categoryView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                List<String> phones = (List) document.get("phones");
                for (String phone : phones) {
                    emitter.emit(phone, document.get("name"));
                }
            }
        }, "2");

        return bundle;
    }
    */



    //TODO: refactor these two methods to reuse common code
    public Bundle getDocumentsBundle(
            android.content.Context context,
            String databaseName,
            FilterCriteria filter)
    {
        if (databaseName == null)
        {
            Log.e(TAG, "Error: Database name was null");

            return null;
        }

        Manager manager = null;
        Database database = null;

        try {
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase(databaseName);

            //Bundle test = getDocumentsBundle(context,databaseName, filter);
            //addDefaultActivities(database);

        } catch (Exception e) {
            Log.e(TAG, "Error getting database", e);
            return null;
        }

        Bundle bundle = new Bundle();

        try {

            Query query = database.createAllDocumentsQuery();
            query.setDescending(true);

            if (this.startKey == null)
            {
                this.startKey = query.getStartKey();
            }

            query.setStartKey(this.startKey);
            query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS);
            QueryEnumerator result = query.run();
            isQueryPending = true;

            int rowCount = 0;
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {

                //performance
                if (rowCount >= TabBatchSize) {
                    startKey = query.getEndKey();
                    return bundle;
                }
                QueryRow row = it.next();
                Document document = row.getDocument();
                String docStr = String.valueOf(document.getProperties());
                //Log.d(TAG, "retrievedDocument=" + String.valueOf(document.getProperties()));

                //if (!filter.isIncluded(document))
                //{
                 //   continue;
                //}
                /*
                if (!filterKey.isEmpty() && (!filterValue.isEmpty())) {
                    String filterKeyValue = (String) document.getProperty(filterKey);
                    if ((filterKeyValue == null) || (!filterKeyValue.equalsIgnoreCase(filterValue))) {
                        continue;
                    }
                }
                */

                String activity = (String) document.getProperty("activity");
                String details = (String) document.getProperty("details");
                String tagString = (String) document.getProperty("tags"); //TODO: to be replaced with attributes
                String attrString = (String) document.getProperty("attributes");

                Map<String, String> attributes = new HashMap<>();
                if (attrString != null) {
                    JSONObject map = new JSONObject();

                    try {
                        Type type = new TypeToken<HashMap<String, String>>() {
                        }.getType();
                        attributes = new Gson().fromJson(attrString, type);
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getStackTrace().toString());
                    }
                }

                List<String> tags = new ArrayList<String>();

                if (tagString != null) {
                    org.json.JSONArray tagsJson = new org.json.JSONArray(tagString);
                    int len = tagsJson.length();
                    for (int i = 0; i < len; i++) {
                        tags.add(tagsJson.get(i).toString());
                    }
                }

                Log.d(TAG, "retrievedDocument.activity=" + activity);
                String activitySerialised = "";
                TaskListDocument activityItem = null;
                try {
                    Gson gson = new GsonBuilder().create();
                    activityItem =
                            new TaskListDocument(document.getId(), activity, details, false, tags, attributes);
                    activitySerialised = gson.toJson(activityItem);
                } catch (Exception e) {
                    Log.e(TAG, "Error: Parsing Activity JSON", e);
                }

                if (!filter.isIncluded(activityItem)) {
                    continue;
                }


                //VALIDATION OF JSON ONLY
                try {
                    Gson gson = new GsonBuilder().create();
                    activityItem = gson.fromJson(activitySerialised, TaskListDocument.class);
                    String activityfromjson = activityItem.getActivity();
                } catch (Exception e) {
                    Log.e(TAG, "Error: Parsing Activity JSON", e);
                }

                bundle.putString(Integer.toString(rowCount), activitySerialised);
                rowCount++;

                if (row.getConflictingRevisions().size() > 0) {
                    Log.w(TAG, "Conflict in document: %s", row.getDocumentId());
                }
            }

            //for next query
            startKey = query.getEndKey();

            //assuming we have all the docs now..
            isQueryPending = false;
            return bundle;

        } catch (Exception e) {
            Log.e(TAG, "Error returning all queries", e);
            return null;
        }
    }

    /*
    public List<TaskListDocument> getAllDocuments(android.content.Context context, String databaseName)
    {
        Manager manager = null;
        Database database = null;

        List<TaskListDocument> documents = new ArrayList<>();

        try {
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase(databaseName);

        } catch (Exception e) {
            Log.e(TAG, "Error getting database", e);
            return null;
        }

        Bundle bundle = new Bundle();

        try {

            Query query = database.createAllDocumentsQuery();
            query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS);
            QueryEnumerator result = query.run();

            int rowCount = 0;
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                Document document = row.getDocument();
                String docStr = String.valueOf(document.getProperties());

                String activity = (String) document.getProperty("activity");
                String details = (String) document.getProperty("details");
                String tagString = (String) document.getProperty("tags");

                List<String> tags = new ArrayList<String>();
                if (tagString != null) {
                    org.json.JSONArray tagsJson = new org.json.JSONArray(tagString);
                    int len = tagsJson.length();
                    for (int i = 0; i < len; i++) {
                        tags.add(tagsJson.get(i).toString());
                    }
                }

                String attrString = (String) document.getProperty("attributes");

                Map<String,String> attributes = new HashMap<>();
                if (attrString!=null) {
                    JSONObject map = new JSONObject();

                    String jsonString = "Your JSON string";
                    Type type = new TypeToken<HashMap<String, String>>(){}.getType();
                    attributes = new Gson().fromJson(jsonString, type);
                }

                documents.add( new TaskListDocument(document.getId(), activity, details, false, tags, attributes ));
            }
            return documents;

        } catch (Exception e) {
            Log.e(TAG, "Error returning all queries", e);
            return null;
        }
    }
    */

    public void setResources(Resources resources)
    {
        this.resources = resources;
    }



}

