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

    public ItemReader()
    {
        resources = null;
    }

    //TODO: refactor these two methods to reuse common code
    public Bundle getAllDocumentsBundle(android.content.Context context, String databaseName)
    {
        Manager manager = null;
        Database database = null;

        try {
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase(databaseName);
            //addDefaultActivities(database);

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
                Log.d(TAG, "retrievedDocument=" + String.valueOf(document.getProperties()));

                String activity = (String) document.getProperty("activity");
                String details = (String) document.getProperty("details");
                String tagString = (String) document.getProperty("tags"); //TODO: to be replaced with attributes
                String attrString = (String) document.getProperty("attributes");

                Map<String,String> attributes = new HashMap<>();
                if (attrString!=null) {
                    JSONObject map = new JSONObject();

                    try {
                        Type type = new TypeToken<HashMap<String, String>>() {
                        }.getType();
                        attributes = new Gson().fromJson(attrString, type);
                    }
                    catch (Exception ex)
                    {
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
                try {
                    Gson gson = new GsonBuilder().create();
                    TaskListDocument activityItem =
                            new TaskListDocument(document.getId(), activity, details, false, tags, attributes);
                    activitySerialised = gson.toJson(activityItem);
                } catch (Exception e) {
                    Log.e(TAG, "Error: Parsing Activity JSON", e);
                }

                //VALIDATION OF JSON ONLY
                try
                {
                    Gson gson = new GsonBuilder().create();
                    TaskListDocument activityItem = gson.fromJson(activitySerialised, TaskListDocument.class);
                    String activityfromjson = activityItem.getActivity();
                }
                catch (Exception e)
                {
                    Log.e(TAG, "Error: Parsing Activity JSON", e);
                }

                bundle.putString(Integer.toString(rowCount), activitySerialised);
                rowCount++;

                if (row.getConflictingRevisions().size() > 0) {
                    Log.w(TAG, "Conflict in document: %s", row.getDocumentId());
                }
            }
            return bundle;

        } catch (Exception e) {
            Log.e(TAG, "Error returning all queries", e);
            return null;
        }
    }

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

    public void setResources(Resources resources)
    {
        this.resources = resources;
    }



}

