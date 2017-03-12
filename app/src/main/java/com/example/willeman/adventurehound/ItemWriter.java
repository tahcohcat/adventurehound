package com.example.willeman.adventurehound;

/**
 * Created by WILLEMAN on 8/1/2016.
 */

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.UnsavedRevision;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.util.Log;

import org.json.simple.JSONObject;

import java.util.Map;

public class ItemWriter {

    public static final String TAG = "TTD.ItemWriter";
    //private Resources resources;

    public ItemWriter()
    {
        //TODO: remove
        //resources = null;
    }

    public boolean writeItem(
            android.content.Context context,
            String databaseName,
            final TaskListDocument item)
    {
        Manager manager = null;
        Database database = null;

        try {
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase(databaseName);

        } catch (Exception e) {
            Log.e(TAG, "Error getting database", e);
            return false;
        }

        if ((database == null) || (manager == null))
        {
            Log.e(TAG, "Error could not get database or manager");
            return false;
        }

        try {

            if (item.isNew())
            {
                Document newDocument = database.createDocument();
                item.setDocumentId(newDocument.getId());
            }

            Document document = database.getDocument(item.getDocId());

            document.update(new Document.DocumentUpdater() {
                @Override
                public boolean update(UnsavedRevision newRevision) {
                    Map<String, Object> updatedProperties = newRevision.getUserProperties();
                    updatedProperties.put("activity", item.getActivity());
                    updatedProperties.put("details", item.getDetails());
                    updatedProperties.put("tags", org.json.simple.JSONArray.toJSONString(item.getTags()));

                    JSONObject propertiesJSON = new JSONObject();
                    propertiesJSON.putAll(item.getAttributes());
                    updatedProperties.put("attributes", propertiesJSON.toJSONString());
                    newRevision.setUserProperties(updatedProperties);
                    return true;
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error returning all queries", e);
            return false;
        }
        return true;
    }

    public boolean deleteItem(android.content.Context context,
                              String databaseName,
                              TaskListDocument item) {

        Manager manager = null;
        Database database = null;

        try {
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase(databaseName);

            // delete the document
            try {
                Document doc = database.getDocument(item.getDocId());
                doc.delete();
                Log.d (TAG, "Deleted document, deletion status = " + doc.isDeleted());
                return doc.isDeleted();
            } catch (CouchbaseLiteException e) {
                Log.e (TAG, "Cannot delete document", e);
                return false;
            }

        } catch (Exception e) {
            Log.e(TAG, "Error getting database", e);
            return false;
        }
    }

    //TODO: remove
    //public void setResources(Resources resources)
    //{
    //    this.resources = resources;
    //}



}

