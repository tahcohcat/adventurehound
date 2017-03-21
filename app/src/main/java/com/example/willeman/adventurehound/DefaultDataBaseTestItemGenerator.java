package com.example.willeman.adventurehound;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.util.Log;

import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultDataBaseTestItemGenerator {

    public static final String TAG = "TTD.DefaultDBTestItemGenerator";
    //private Context context = null;

    public DefaultDataBaseTestItemGenerator()
    {
    }

/*
    public DefaultDataBaseTestItemGenerator(Context context)
    {
        this.context = context;
    }
*/

    public void addDefaultActivities(Database database) {

        Map<String,String> attributes = new HashMap<>();

        createActivityDocWithAttributes(database, "Fairview Winefarm", "Big selection of tastes", Arrays.asList("category:" + "food"), null);
        createActivityDocWithAttributes(database, "Surfing", "Strand beach on a calm day", Arrays.asList("category:" + "sport"),null);
        createActivityDocWithAttributes(database, "SUP", "Where to rent?", Arrays.asList("category:"  + "sport"),null);
        createActivityDocWithAttributes(database, "Koeelbaai camping", "", Arrays.asList("category" +  "adventure"),null);
        createActivityDocWithAttributes(database, "Sutherland", "Stars best in Winter", Arrays.asList("category:" + "adventure"),null);

        attributes = new HashMap<>();
        attributes.put("category", "entertainment");
        createActivityDocWithAttributes(database, "Boardgames evening", "Invite Phlip & Jaco", Arrays.asList("category:" + "entertainment"), null);
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
